package com.khanhvan.book_rating.DTO.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserInfoRequest
{
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
