package com.buihuuduy.book_rating.repository;

import com.buihuuduy.book_rating.entity.FavoriteBookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteBookRepository extends JpaRepository<FavoriteBookEntity, Integer>
{
    FavoriteBookEntity findByUserIdAndBookId(Integer userId, Integer bookId);

    List<FavoriteBookEntity> findAllByUserId(Integer userId);
}
