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
public enum ErrorCode
{
    USERNAME_ALREADY_EXIST(409, "Tên người dùng đã tồn tại"),
    EMAIL_ALREADY_EXIST(409, "Email đã tồn tại"),
    USER_NOT_FOUND(404, "Không tìm thấy người dùng"),
    USER_NAME_INVALID(400, "Tên người dùng phải chứa ký tự"),
    PASSWORD_INVALID(400, "Mật khẩu phải có ít nhất 8 ký tự"),
    FULL_NAME_INVALID(400, "Họ tên phải chứa ký tự"),
    LOGIN_UNSUCCESSFULLY(401, "Đăng nhập không thành công"),
    UNAUTHORIZED(403, "Bạn không có quyền truy cập tài nguyên này"),
    EMAIL_INVALID(400, "Email phải có định dạng hợp lệ"),
    VERIFICATION_CODE_INVALID(400, "Mã xác minh không hợp lệ"),
    CATEGORY_NOT_FOUND(404, "Không tìm thấy danh mục"),
    BOOK_NOT_FOUND(404, "Không tìm thấy sách"),
    JWT_INVALID(400, "JWT không hợp lệ"),
    ;

    Integer code;

    String message;
}
