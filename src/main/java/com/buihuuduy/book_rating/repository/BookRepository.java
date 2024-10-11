package com.buihuuduy.book_rating.repository;

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
}
