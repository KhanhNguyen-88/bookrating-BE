package com.buihuuduy.book_rating.repository;

import com.buihuuduy.book_rating.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Integer> {
}
