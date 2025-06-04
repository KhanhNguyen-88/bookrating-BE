package com.khanhvan.book_rating.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageFilterInput<T>
{
    @NotNull(message = "pageNumber must not be null")
    @JsonProperty("pageNumber")
    private Integer pageNumber;

    @NotNull(message = "pageSize must not be null")
    @JsonProperty("pageSize")
    private Integer pageSize;

    @NotNull(message = "filter must not be null")
    @JsonProperty("filter")
    private T filter;
}
