package com.buihuuduy.book_rating.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PROTECTED)
public class ApiResponse <T>
{
    int code;

    String message;

    T result;

    public ApiResponse result(T data) {
        this.result = data;
        return this;
    }
}
