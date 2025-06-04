package com.khanhvan.book_rating.repository;

import com.khanhvan.book_rating.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {
    @Query("SELECT c FROM CategoryEntity c JOIN BookCategoryEntity bc ON c.id = bc.categoryId WHERE bc.bookId = :bookId")
    List<CategoryEntity> findCategoriesByBookId(@Param("bookId") Long bookId);
    @Modifying
    @Transactional
    @Query("UPDATE CategoryEntity c SET c.cateName = :cateName, c.cateDescription = :cateDescription WHERE c.id = :id")
    int updateCateById(@Param("cateName") String cateName, @Param("cateDescription") String cateDescription, @Param("id") Long id);
    CategoryEntity findById(int id);
}
