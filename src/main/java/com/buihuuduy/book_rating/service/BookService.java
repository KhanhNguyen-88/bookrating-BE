package com.buihuuduy.book_rating.service;

import com.buihuuduy.book_rating.DTO.PageFilterInput;
import com.buihuuduy.book_rating.DTO.request.ExplorePageFilter;
import com.buihuuduy.book_rating.DTO.response.BookDetailResponse;
import com.buihuuduy.book_rating.DTO.response.BookExploreResponse;
import com.buihuuduy.book_rating.DTO.response.FeedbackResponse;
import com.buihuuduy.book_rating.entity.BookCategoryEntity;
import com.buihuuduy.book_rating.entity.BookEntity;
import com.buihuuduy.book_rating.exception.CustomException;
import com.buihuuduy.book_rating.exception.ErrorCode;
import com.buihuuduy.book_rating.mapper.BookMapper;
import com.buihuuduy.book_rating.repository.BookCategoryRepository;
import com.buihuuduy.book_rating.repository.BookRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookService
{
    private static final Logger log = LoggerFactory.getLogger(BookService.class);
    private final BookCategoryRepository bookCategoryRepository;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @PersistenceContext
    private EntityManager entityManager;

    public BookService(BookCategoryRepository bookCategoryRepository, BookRepository bookRepository, BookMapper bookMapper) {
        this.bookCategoryRepository = bookCategoryRepository;
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    @Transactional
    public Page<BookExploreResponse> getBooksInExplorePage(PageFilterInput<ExplorePageFilter> input, Pageable pageable)
    {
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT ")
            .append("   b.book_name, b.book_description, ")
            .append("   b.book_image, b.published_date, ")
            .append("   b.book_format, b.book_sale_link, ")
            .append("   bl.language_name, b.book_author,  ")
            .append("   GROUP_CONCAT(c.cate_name ORDER BY c.cate_name SEPARATOR ', ') AS category, ")
            .append("   CEIL(AVG(fb.rating)) AS average_rating ")
            .append("FROM book b ")
            .append("LEFT JOIN book_category bc ON b.id = bc.book_id ")
            .append("LEFT JOIN book_language bl ON b.language_id = bl.id ")
            .append("LEFT JOIN category c ON bc.category_id = c.id ")
            .append("LEFT JOIN book_rating_db.feedback fb ON fb.book_id = b.id ")
            .append("WHERE 1=1 ");

        int paramIndex = 1;

        if (input.getFilter().getBookAuthor() != null && !input.getFilter().getBookAuthor().trim().isEmpty()) {
            sql.append(" AND LOWER(b.book_author) LIKE LOWER(CONCAT('%', ?").append(paramIndex).append(", '%')) ");
            paramIndex++;
        }

        sql.append("GROUP BY ")
            .append("    b.book_name, b.book_description, ")
            .append("    b.book_image, b.published_date, ")
            .append("    b.book_format, b.book_sale_link, ")
            .append("    bl.language_name, b.book_author ")
            .append("HAVING 1=1 ");

        if (input.getFilter().getCategoryId() != null) {
            sql.append("AND SUM(c.id = ?").append(paramIndex).append(") > 0 ");
            paramIndex++;
        }
        if (input.getFilter().getRating() != null) {
            sql.append("AND average_rating = ?").append(paramIndex).append(" ");
        }

        Query query = entityManager.createNativeQuery(sql.toString());

        // Set parameters
        int queryParamIndex = 1;

        if (input.getFilter().getBookAuthor() != null && !input.getFilter().getBookAuthor().isEmpty()) {
            query.setParameter(queryParamIndex,  input.getFilter().getBookAuthor());
            queryParamIndex++;
        }
        if (input.getFilter().getCategoryId() != null) {
            query.setParameter(queryParamIndex, + input.getFilter().getCategoryId());
            queryParamIndex++;
        }
        if (input.getFilter().getRating() != null) {
            query.setParameter(queryParamIndex, + input.getFilter().getRating());
        }

        // Retrieve and map results
        List<Object[]> results;

        int totalRows = query.getResultList().size(); // Count the total rows
        if (pageable.isPaged()) {
            results = query.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        } else {
            results = query.getResultList(); // No pagination
        }

        List<BookExploreResponse> bookResponseList = new ArrayList<>();
        for(Object[] result : results)
        {
            BookExploreResponse bookResponse = getBookResponse(result);
            bookResponseList.add(bookResponse);
        }
        return new PageImpl<>(bookResponseList, pageable, totalRows);
    }

    private static BookExploreResponse getBookResponse(Object[] result) {
        BookExploreResponse bookResponse = new BookExploreResponse();
        bookResponse.setBookName((String) result[0]);
        bookResponse.setBookDescription((String) result[1]);
        bookResponse.setBookImage((String) result[2]);
        bookResponse.setPublishedDate((LocalDate) result[3]);
        bookResponse.setBookFormat((String) result[4]);
        bookResponse.setBookSaleLink((String) result[5]);
        bookResponse.setLanguage((String) result[6]);
        bookResponse.setBookAuthor((String) result[7]);
        bookResponse.setCategoryName((String) result[8]);
        return bookResponse;
    }

    public List<BookEntity> getBooksInExplorePage1(Integer categoryId)
    {
        List<BookCategoryEntity> bookCategoryEntityList = bookCategoryRepository.findByCategoryId(categoryId);
        if(bookCategoryEntityList.isEmpty()) {
            throw new CustomException(ErrorCode.CATEGORY_NOT_FOUND);
        }
        List<BookEntity> books = new ArrayList<>();
        for(BookCategoryEntity bookCategoryEntity : bookCategoryEntityList) {
            BookEntity bookEntity = bookRepository.findById(bookCategoryEntity.getBookId()).orElseThrow(
                    () -> new CustomException(ErrorCode.BOOK_NOT_FOUND)
            );
            books.add(bookEntity);
        }
        return books;
    }

    public BookDetailResponse getBookDetailById(Integer bookId)
    {
        BookEntity bookEntity = bookRepository.findById(bookId).orElseThrow(
                () -> new CustomException(ErrorCode.BOOK_NOT_FOUND)
        );

        BookDetailResponse bookDetailResponse = bookMapper.toBookDetailResponse(bookEntity);

        StringBuilder sql = new StringBuilder();

        sql.append("SELECT ")
                .append("   u.username, u.user_image, ")
                .append("   fb.comment, fb.rating, ")
                .append("   fb.updated_at ")
                .append("FROM book_rating_db.feedback fb ")
                .append("LEFT JOIN book_rating_db.user u ON fb.user_id = u.id ");

        Query query = entityManager.createNativeQuery(sql.toString());

        List<Object[]> results = query.getResultList();
        List<FeedbackResponse> feedbackResponseList = new ArrayList<>();

        for(Object[] result : results)
        {
            FeedbackResponse feedbackResponse = new FeedbackResponse();

            feedbackResponse.setUserName((String) result[0]);
            feedbackResponse.setUserImage((String) result[1]);
            feedbackResponse.setComment((String) result[2]);
            feedbackResponse.setRating((Integer) result[3]);
            feedbackResponse.setCreatedAt((LocalDate) result[4]);

            feedbackResponseList.add(feedbackResponse);
        }

        bookDetailResponse.setFeedbackResponseList(feedbackResponseList);
        return bookDetailResponse;
    }

    public List<String> getAuthorsRecommendation(String input)
    {
        List<String> authors;
        log.info("Input {}", input);
        authors = bookRepository.getAuthorByTitle(input.trim());
        return authors;
    }
}
