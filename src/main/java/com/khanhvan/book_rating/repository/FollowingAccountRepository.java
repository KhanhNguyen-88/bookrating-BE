package com.khanhvan.book_rating.repository;

import com.khanhvan.book_rating.entity.FollowingAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowingAccountRepository extends JpaRepository<FollowingAccountEntity, Integer>
{
    @Query("SELECT f.followedAccountId FROM FollowingAccountEntity f WHERE f.followerAccountId = :yourAccountId and f.isActive = true ")
    List<Integer> findAllFollowingAccountIdsByYourAccountId(@Param("yourAccountId") Integer yourAccountId);

    @Query("SELECT f.followerAccountId FROM FollowingAccountEntity f WHERE f.followedAccountId = :yourAccountId and f.isActive = true")
    List<Integer> findAllFollowerAccountIdsByFollowingAccountId(@Param("yourAccountId") Integer yourAccountId);

    FollowingAccountEntity findByFollowerAccountIdAndFollowedAccountId(Integer followerAccountId, Integer followedAccountId);

    @Query("SELECT COUNT(f) FROM FollowingAccountEntity f " +
            "WHERE f.followerAccountId = :followerAccountId " +
            "AND f.followedAccountId = :followedAccountId " +
            "AND f.isActive = true")
    Integer checkFollowStatus(@Param("followerAccountId") Integer followerAccountId, @Param("followedAccountId") Integer followedAccountId);

    @Query("SELECT f FROM FollowingAccountEntity f " +
            "WHERE f.followedAccountId = :userId OR f.followerAccountId = :userId")
    List<FollowingAccountEntity> findByUserId(@Param("userId") Integer userId);
}
