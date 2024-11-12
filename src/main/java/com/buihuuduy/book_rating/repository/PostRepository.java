package com.buihuuduy.book_rating.repository;

import com.buihuuduy.book_rating.entity.PostEntityTmp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<PostEntityTmp, Integer> {
}
