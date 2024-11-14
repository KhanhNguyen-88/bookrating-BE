package com.buihuuduy.book_rating.service.impl;

import com.buihuuduy.book_rating.DTO.PageFilterInput;
import com.buihuuduy.book_rating.DTO.request.BookRequestDTO;
import com.buihuuduy.book_rating.DTO.request.ExplorePageFilter;
import com.buihuuduy.book_rating.DTO.response.BookDetailPageResponse;
import com.buihuuduy.book_rating.DTO.response.BookResponse;
import com.buihuuduy.book_rating.DTO.response.FeedbackResponse;
import com.buihuuduy.book_rating.entity.BookCategoryEntity;
import com.buihuuduy.book_rating.entity.BookEntity;
import com.buihuuduy.book_rating.exception.CustomException;
import com.buihuuduy.book_rating.exception.ErrorCode;
import com.buihuuduy.book_rating.mapper.BookMapper;
import com.buihuuduy.book_rating.repository.BookCategoryRepository;
import com.buihuuduy.book_rating.repository.BookRepository;
import com.buihuuduy.book_rating.service.BookService;
import com.buihuuduy.book_rating.service.utils.CommonFunction;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BookServiceImpl implements BookService
{
    private static final Logger log = LoggerFactory.getLogger(BookServiceImpl.class);
    private final BookCategoryRepository bookCategoryRepository;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @PersistenceContext
    private EntityManager entityManager;

    public BookServiceImpl(BookCategoryRepository bookCategoryRepository, BookRepository bookRepository, BookMapper bookMapper) {
        this.bookCategoryRepository = bookCategoryRepository;
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    @Transactional
    public Page<BookResponse> getBooksInExplorePage(PageFilterInput<ExplorePageFilter> input, Pageable pageable)
    {
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT ")
                .append("   b.id, ")
                .append("   b.book_name, b.book_description, ")
                .append("   b.book_image, b.published_date, ")
                .append("   b.book_format, b.book_sale_link, ")
                .append("   bl.language_name, b.book_author,  ")
                .append("   GROUP_CONCAT(c.cate_name ORDER BY c.cate_name SEPARATOR ', ') AS category, ")
                .append("   CEIL(AVG(fb.rating)) AS average_rating, COUNT(fb.rating) AS rating_count, b.created_at")
                .append(" FROM book b ")
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
                .append("   b.id, ")
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

        List<BookResponse> bookResponseList = new ArrayList<>();
        for(Object[] result : results)
        {
            BookResponse bookResponse = getBookResponseExplorePage(result);
            bookResponseList.add(bookResponse);
        }
        return new PageImpl<>(bookResponseList, pageable, totalRows);
    }

    private static BookResponse getBookResponseExplorePage(Object[] result) {
        BookResponse bookResponse = new BookResponse();
        System.out.println(result[0]);
        bookResponse.setId((Integer) result[0]);
        bookResponse.setBookName((String) result[1]);
        bookResponse.setBookDescription((String) result[2]);
        bookResponse.setBookImage((String) result[3]);
        bookResponse.setPublishedDate((Date) result[4]);
        bookResponse.setBookFormat((String) result[5]);
        bookResponse.setBookSaleLink((String) result[6]);
        bookResponse.setLanguage((String) result[7]);
        bookResponse.setBookAuthor((String) result[8]);
        bookResponse.setCategoryName((String) result[9]);
        bookResponse.setAverageRating((Long) result[10]);
        bookResponse.setTotalRating((Long) result[11]);
        bookResponse.setCreatedAt((Date) result[12]);
        return bookResponse;
    }
    private static BookResponse convertBookResponseDetail(Object[] result) {
        BookResponse bookResponse = new BookResponse();
        Object[] bookData = (Object[]) result[0] ;
        bookResponse.setId((Integer) bookData[0]);
        bookResponse.setBookName((String) bookData[1]);
        bookResponse.setBookDescription((String) bookData[2]);
        bookResponse.setBookImage((String) bookData[3]);
        bookResponse.setPublishedDate((Date) bookData[4]);
        bookResponse.setBookFormat((String) bookData[5]);
        bookResponse.setBookSaleLink((String) bookData[6]);
        bookResponse.setLanguage((String) bookData[7]);
        bookResponse.setBookAuthor((String) bookData[8]);
        bookResponse.setCategoryName((String) bookData[9]);
        bookResponse.setAverageRating((Long) bookData[10]);
        bookResponse.setTotalRating((Long) bookData[11]);
        bookResponse.setCreatedAt((Date) bookData[12]);
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

    @Override
    public BookDetailPageResponse getBookDetailById(Integer bookId)
    {
        BookDetailPageResponse bookDetailPageResponse = new BookDetailPageResponse();

        BookResponse bookResponse = convertBookResponseDetail(bookRepository.getBookResponseByBookId(bookId));

        bookDetailPageResponse.setBookResponse(bookResponse);

        StringBuilder sql = new StringBuilder();

        sql.append("SELECT ")
                .append("   fb.user_id, ")
                .append("   u.username, u.user_image, ")
                .append("   fb.comment, fb.rating, ")
                .append("   fb.updated_at ")
                .append("FROM book_rating_db.feedback fb ")
                .append("LEFT JOIN book_rating_db.user u ON fb.user_id = u.id ")
                .append("WHERE fb.book_id = ").append(bookId);

        Query query = entityManager.createNativeQuery(sql.toString());

        List<Object[]> results = query.getResultList();
        List<FeedbackResponse> feedbackResponseList = new ArrayList<>();

        for(Object[] result : results)
        {
            FeedbackResponse feedbackResponse = new FeedbackResponse();

            feedbackResponse.setUserId((Integer) result[0]);
            feedbackResponse.setUserName((String) result[1]);
            feedbackResponse.setUserImage((String) result[2]);
            feedbackResponse.setComment((String) result[3]);
            feedbackResponse.setRating((Integer) result[4]);
            feedbackResponse.setCreatedAt((LocalDateTime) result[5]);

            feedbackResponseList.add(feedbackResponse);
        }

        bookDetailPageResponse.setFeedbackResponseList(feedbackResponseList);
        return bookDetailPageResponse;
    }

    @Override
    public List<String> getAuthorsRecommendation(String input)
    {
        List<String> authors;
        log.info("Input {}", input);
        authors = bookRepository.getAuthorByTitle(input.trim());
        return authors;
    }

    @Override
    public List<BookResponse> getPostedBookByUsername(String username)
    {
        List<BookEntity> bookEntities = bookRepository.findByCreatedBy(username);
        List<BookResponse> bookResponseList = new ArrayList<>();
        for(BookEntity bookEntity : bookEntities) {
            BookResponse bookResponse = bookMapper.toBookResponse(bookEntity);
            bookResponseList.add(bookResponse);
        }
        return bookResponseList;
    }

    @Override
    public List<BookResponse> searchBookByText(String common)
    {
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT ")
                .append("   b.id, ")
                .append("   b.book_name, b.book_image, b.book_author, ")
                .append("   GROUP_CONCAT(c.cate_name ORDER BY c.cate_name SEPARATOR ', ') ")
                .append("FROM book b ")
                .append("LEFT JOIN book_category bc ON b.id = bc.book_id ")
                .append("LEFT JOIN category c ON bc.category_id = c.id ")
                .append("WHERE ( ")
                .append("   b.book_name LIKE CONCAT('%', '").append(common).append("', '%') OR ")
                .append("   b.book_author LIKE CONCAT('%', '").append(common).append("', '%') OR ")
                .append("   c.cate_name LIKE CONCAT('%', '").append(common).append("', '%') ")
                .append(") ")
                .append("GROUP BY ")
                .append("   b.id, b.book_name, b.book_image, b.book_author");

        Query query = entityManager.createNativeQuery(sql.toString());

        List<Object[]> results = query.getResultList();
        List<BookResponse> bookResponseList = new ArrayList<>();

        for(Object[] result : results)
        {
            BookResponse bookResponse = new BookResponse();
            bookResponse.setId((Integer) result[0]);
            bookResponse.setBookName((String) result[1]);
            bookResponse.setBookImage((String) result[2]);
            bookResponse.setBookAuthor((String) result[3]);
            bookResponse.setCategoryName((String) result[4]);
            bookResponseList.add(bookResponse);
        }
        return bookResponseList;
    }

    @Override
    public List<BookResponse> getFavoriteBookByUserId(Integer userId)
    {
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT ")
                .append("   b.id, b.book_name, b.book_image ")
                .append("FROM book b ")
                .append("JOIN favorite_book fb on b.id = fb.book_id ")
                .append("JOIN user u on u.id = fb.user_id ")
                .append("WHERE fb.user_id = ").append(userId);

        Query query = entityManager.createNativeQuery(sql.toString());
        List<Object[]> results = query.getResultList();
        List<BookResponse> bookResponseList = new ArrayList<>();
        for(Object[] result : results)
        {
            BookResponse bookResponse = new BookResponse();
            bookResponse.setId((Integer) result[0]);
            bookResponse.setBookName((String) result[1]);
            bookResponse.setBookImage((String) result[2]);
            bookResponseList.add(bookResponse);
        }
        return bookResponseList;
    }

    @Override
    public void createBook(String token, BookRequestDTO bookRequestDTO)
    {
        BookEntity bookEntity = new BookEntity();

        bookEntity.setBookName(bookRequestDTO.getBookName());
        bookEntity.setBookDescription(bookRequestDTO.getBookDescription());
        bookEntity.setBookImage(bookRequestDTO.getBookImage());
        bookEntity.setPublishedDate(bookRequestDTO.getPublishedDate());
        bookEntity.setBookSaleLink(bookRequestDTO.getBookSaleLink());
        bookEntity.setBookFormat(bookRequestDTO.getBookFormat());
        bookEntity.setLanguageId(bookRequestDTO.getLanguageId());
        bookEntity.setBookAuthor(bookRequestDTO.getBookAuthor());
        bookEntity.setApprovalStatus(false); // Cho duyet
        String username = CommonFunction.getUsernameFromToken(token);
        bookEntity.setCreatedBy(username);

        bookRepository.save(bookEntity);

        // Luu bang CateBook
        for(Integer categoryId : bookRequestDTO.getCategoryId()) {
            BookCategoryEntity bookCategoryEntity = new BookCategoryEntity();
            bookCategoryEntity.setBookId(bookEntity.getId());
            bookCategoryEntity.setCategoryId(categoryId);
            bookCategoryRepository.save(bookCategoryEntity);
        }
    }
}
