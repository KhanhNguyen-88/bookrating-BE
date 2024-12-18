package com.buihuuduy.book_rating.repository;

import com.buihuuduy.book_rating.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer>
{
    boolean existsByUsername(String username);
    boolean existsByUserEmail(String email);
    UserEntity findByUsername(String username);
    @Query("SELECT COUNT(*) FROM FollowingAccountEntity f WHERE  f.followedAccountId = :followingId and f.followerAccountId = :followerId and f.isActive = true ")
    Long introspectFollowBack(@Param("followingId") Integer followingId, @Param("followerId") Integer followerId);

    List<UserEntity> findAllByIsActiveIsTrueAndIsAdminIsFalse();
}
