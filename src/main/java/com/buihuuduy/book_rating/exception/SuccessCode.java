package com.buihuuduy.book_rating.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum SuccessCode
{
    REGISTER_SUCCESSFULLY("Đăng ký thành công"),
    LOGIN_SUCCESSFULLY("Đăng nhập thành công")
    ;

    String message;
}
