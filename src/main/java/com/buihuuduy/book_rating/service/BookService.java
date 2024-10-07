package com.buihuuduy.book_rating.service;

import com.buihuuduy.book_rating.DTO.PageFilterInput;
import com.buihuuduy.book_rating.DTO.request.ExplorePageFilter;
import com.buihuuduy.book_rating.DTO.response.BookResponse;
import com.buihuuduy.book_rating.entity.BookCategoryEntity;
import com.buihuuduy.book_rating.entity.BookEntity;
import com.buihuuduy.book_rating.exception.CustomException;
import com.buihuuduy.book_rating.exception.ErrorCode;
import com.buihuuduy.book_rating.repository.BookCategoryRepository;
import com.buihuuduy.book_rating.repository.BookRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
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
    private final BookCategoryRepository bookCategoryRepository;
    private final BookRepository bookRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public BookService(BookCategoryRepository bookCategoryRepository, BookRepository bookRepository) {
        this.bookCategoryRepository = bookCategoryRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public Page<BookResponse> getBooksInExplorePage(PageFilterInput<ExplorePageFilter> input, Pageable pageable)
    {
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT ")
            .append("    b.book_name, b.book_description, ")
            .append("    b.book_image, b.published_date, ")
            .append("    b.book_format, b.book_sale_link, ")
            .append("    bl.language_name, b.book_author,  ")
            .append("    GROUP_CONCAT(c.cate_name ORDER BY c.cate_name SEPARATOR ', ') AS category ")
            .append("FROM book b ")
            .append("LEFT JOIN book_category bc ON b.id = bc.book_id ")
            .append("LEFT JOIN book_language bl ON b.language_id = bl.id ")
            .append("LEFT JOIN category c ON bc.category_id = c.id ")
            .append("WHERE 1=1 ");

        int paramIndex = 1;

        if (input.getFilter().getBookAuthor() != null && !input.getFilter().getBookAuthor().isEmpty()) {
            sql.append(" AND LOWER(b.book_author) LIKE LOWER(CONCAT('%', ?").append(paramIndex).append(", '%')) ");
            paramIndex++;
        }

        if (input.getFilter().getCategoryId() != null) {
            sql.append("GROUP BY ")
                .append("    b.book_name, b.book_description, ")
                .append("    b.book_image, b.published_date, ")
                .append("    b.book_format, b.book_sale_link, ")
                .append("    bl.language_name, b.book_author ")
                .append(" HAVING SUM(c.id = ?").append(paramIndex).append(") > 0 ");
        } else {
            sql.append("GROUP BY ")
                .append("    b.book_name, b.book_description, ")
                .append("    b.book_image, b.published_date, ")
                .append("    b.book_format, b.book_sale_link, ")
                .append("    bl.language_name, b.book_author");
        }
        Query query = entityManager.createNativeQuery(sql.toString());

        // Set parameters
        int queryParamIndex = 1;

        if (input.getFilter().getCategoryId() != null) {
            query.setParameter(queryParamIndex, + input.getFilter().getCategoryId());
            queryParamIndex++;
        }
        if (input.getFilter().getBookAuthor() != null && !input.getFilter().getBookAuthor().isEmpty()) {
            query.setParameter(queryParamIndex,  input.getFilter().getBookAuthor());
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
            BookResponse bookResponse = getBookResponse(result);
            bookResponseList.add(bookResponse);
        }
        return new PageImpl<>(bookResponseList, pageable, totalRows);
    }

    private static BookResponse getBookResponse(Object[] result) {
        BookResponse bookResponse = new BookResponse();
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
}
