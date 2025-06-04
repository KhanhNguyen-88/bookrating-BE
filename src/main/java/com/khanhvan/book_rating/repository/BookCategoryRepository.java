package com.khanhvan.book_rating.repository;

import com.khanhvan.book_rating.entity.BookCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookCategoryRepository extends JpaRepository<BookCategoryEntity, Integer>
{
    List<BookCategoryEntity> findByCategoryId(Integer categoryId);
}
