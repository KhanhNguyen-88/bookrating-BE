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

    @Query(value = "SELECT b.id, b.book_name as bookName, b.book_description as bookDescription, " +
            "b.book_image as bookImage, b.published_date as publishedDate, b.book_format as bookFormat, " +
            "b.book_sale_link as bookSaleLink, l.language_name as languageName, b.book_author as bookAuthor, " +
            "GROUP_CONCAT(c.cate_name ORDER BY c.cate_name SEPARATOR ', ') AS categoryName, " +
            "CEIL(AVG(fb.rating)) AS averageRating, COUNT(fb.rating) AS totalRating, b.created_at as createdAt " +
            "FROM book b " +
            "JOIN book_language l ON b.language_id = l.id " +
            "JOIN book_category bc ON b.id = bc.book_id " +
            "JOIN category c ON bc.category_id = c.id " +
            "LEFT JOIN feedback fb ON fb.book_id = b.id " +
            "WHERE b.id = :bookId " +
            "GROUP BY b.id", nativeQuery = true)
    Object[] getBookResponseByBookId(@Param("bookId") Integer bookId);
}
