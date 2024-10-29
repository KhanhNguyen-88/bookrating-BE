package com.buihuuduy.book_rating.repository;

import com.buihuuduy.book_rating.DTO.response.BookResponse;
import com.buihuuduy.book_rating.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Integer>
{
    @Query("SELECT b.bookAuthor FROM BookEntity b WHERE LOWER(b.bookAuthor) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<String> getAuthorByTitle(@Param("title") String title);

    @Query("SELECT b FROM BookEntity b WHERE b.createdBy = :username")
    List<BookEntity> findByCreatedBy(@Param("username") String username);

    @Query("SELECT new com.buihuuduy.book_rating.DTO.response.BookResponse(" +
            "   b.id, b.bookName, b.bookDescription, " +
            "   b.bookImage, b.publishedDate, b.bookFormat, " +
            "   b.bookSaleLink , l.languageName, " +
            "   b.bookAuthor, c.cateName) " +
            "FROM BookEntity b " +
            "JOIN LanguageEntity l ON b.languageId = l.id " +
            "JOIN BookCategoryEntity bc ON b.id = bc.bookId " +
            "JOIN CategoryEntity c ON bc.categoryId = c.id " +
            "WHERE b.id = :bookId")
    BookResponse getBookResponseByBookId(@Param("bookId") Integer bookId);
}
