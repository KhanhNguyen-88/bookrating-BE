package com.khanhvan.book_rating.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "following_account")
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FollowingAccountEntity extends AuditingEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    // Duoc follow
    @Column(name = "followed_account_id")
    Integer followedAccountId;

    // Dang đi follow
    @Column(name = "follower_account_id")
    Integer followerAccountId;
}
