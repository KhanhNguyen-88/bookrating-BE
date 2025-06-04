package com.khanhvan.book_rating.repository;

import com.khanhvan.book_rating.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Integer>
{
    List<CommentEntity> findByUserId(int userId);
}
