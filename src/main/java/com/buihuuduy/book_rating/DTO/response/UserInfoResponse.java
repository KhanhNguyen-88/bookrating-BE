package com.buihuuduy.book_rating.DTO.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserInfoResponse
{
    Integer id;

    String username;

    String password;

    String userImage;

    String fullName;

    LocalDate userDOB;

    String userAddress;

    Boolean userGender;

    String userPhone;

    String userEmail;

    String userLink;
}
