package com.buihuuduy.book_rating.DTO.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import java.util.List;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDetailResponse
{
    Integer id;

    String userImage;

    String userName;

    Integer bookNumberPost;

    Integer followerAccounts;

    Integer followingAccounts;

    // Check this account which you follow
    Boolean isFollowing;
}
