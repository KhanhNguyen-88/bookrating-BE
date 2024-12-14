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

    @Query("SELECT b FROM BookEntity b WHERE b.createdBy = :username AND b.approvalStatus = 1")
    List<BookEntity> findByCreatedBy(@Param("username") String username);

    @Query("SELECT b FROM BookEntity b WHERE b.approvalStatus = 1 ORDER BY b.id DESC")
    List<BookEntity> findAllOrderByIdDesc();

    @Query("SELECT b FROM BookEntity b WHERE b.approvalStatus = 0 ORDER BY b.id DESC")
    List<BookEntity> findAllOnAdminPage();

    @Query(value = "SELECT b.id, b.book_name, b.book_description, " +
            "b.book_image, b.published_date, b.book_format, " +
            "b.book_sale_link, l.language_name, b.book_author, " +
            "GROUP_CONCAT(c.cate_name ORDER BY c.cate_name SEPARATOR ', '), " +
            "CEIL(AVG(fb.rating)), COUNT(fb.rating), b.created_at, u.full_name, u.user_image, u.id, " +
            "CASE WHEN favorite_book.user_id IS NOT NULL THEN true ELSE false END "+
            "FROM book b " +
            "JOIN book_language l ON b.language_id = l.id " +
            "JOIN book_category bc ON b.id = bc.book_id " +
            "JOIN category c ON bc.category_id = c.id " +
            "LEFT JOIN feedback fb ON fb.book_id = b.id " +
            "LEFT JOIN user u ON b.created_by = u.username " +
            "LEFT JOIN favorite_book ON b.id = favorite_book.book_id AND favorite_book.user_id = :userId " +
            "WHERE b.id = :bookId AND b.approval_status = 1 " +
            "GROUP BY b.id, b.book_name, b.book_description, b.book_image, " +
            "b.published_date, b.book_format, b.book_sale_link, " +
            "l.language_name, b.book_author, b.created_at, u.full_name, u.user_image, u.id; " , nativeQuery = true)
    Object[] getBookResponseByBookIdWithToken(@Param("userId") Integer userId, @Param("bookId") Integer bookId);

    @Query(value = "SELECT b.id, b.book_name, b.book_description, " +
            "b.book_image, b.published_date, b.book_format, " +
            "b.book_sale_link, l.language_name, b.book_author, " +
            "GROUP_CONCAT(c.cate_name ORDER BY c.cate_name SEPARATOR ', '), " +
            "b.created_at, u.full_name, u.user_image " +
            "FROM book b " +
            "JOIN book_language l ON b.language_id = l.id " +
            "JOIN book_category bc ON b.id = bc.book_id " +
            "JOIN category c ON bc.category_id = c.id " +
            "LEFT JOIN user u ON b.created_by = u.username " +
            "WHERE b.id = :bookId AND b.approval_status = 0 " +
            "GROUP BY b.id, b.book_name, b.book_description, b.book_image, " +
            "b.published_date, b.book_format, b.book_sale_link, " +
            "l.language_name, b.book_author, b.created_at, u.full_name, u.user_image; " , nativeQuery = true)
    Object[] getBookResponseByBookIdWithTokenAdminPage(@Param("bookId") Integer bookId);
}
