package com.buihuuduy.book_rating.DTO.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountResponse
{
    String userImage;

    String userName;

    Integer userId;

    boolean followBack;
}
