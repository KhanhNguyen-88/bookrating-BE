package com.khanhvan.book_rating.exception;

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
    LOGIN_SUCCESSFULLY("Đăng nhập thành công"),
    EMAIL_SENT_SUCCESSFULLY("Mã xác thực đã được gửi đến email của bạn"),
    UPDATE_SUCCESSFULLY("Cập nhật thông tin thành công"),
    ;

    String message;
}
