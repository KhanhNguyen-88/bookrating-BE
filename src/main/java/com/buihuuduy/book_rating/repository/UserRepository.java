package com.buihuuduy.book_rating.repository;

import com.buihuuduy.book_rating.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer>
{
    boolean existsByUsername(String username);
    boolean existsByUserEmail(String email);
    UserEntity findByUsername(String username);
}
