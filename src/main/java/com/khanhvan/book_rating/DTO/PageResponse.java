package com.khanhvan.book_rating.DTO;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class PageResponse<T> extends ApiResponse<T>
{
    long dataCount;

    public PageResponse(){}

    public PageResponse<T> dataCount(long dataCount) {
        this.dataCount = dataCount;
        return this;
    }

    @Override
    public PageResponse<T> result(T data) {
        super.result(data);
        return this;
    }
}
