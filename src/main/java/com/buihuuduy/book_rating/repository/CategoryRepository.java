package com.buihuuduy.book_rating.repository;

import com.buihuuduy.book_rating.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {
    @Query("SELECT c FROM CategoryEntity c JOIN BookCategoryEntity bc ON c.id = bc.categoryId WHERE bc.bookId = :bookId")
    List<CategoryEntity> findCategoriesByBookId(@Param("bookId") Long bookId);

}
