package com.buihuuduy.book_rating.repository;

import com.buihuuduy.book_rating.entity.FavoriteBookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteBookRepository extends JpaRepository<FavoriteBookEntity, Integer> {
}
