package com.buihuuduy.book_rating.controller;

import com.buihuuduy.book_rating.DTO.PageFilterInput;
import com.buihuuduy.book_rating.DTO.PageResponse;
import com.buihuuduy.book_rating.DTO.request.ExplorePageFilter;
import com.buihuuduy.book_rating.DTO.response.BookResponse;
import com.buihuuduy.book_rating.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book")
public class BookController
{
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/get-all-category")
    public PageResponse<List<BookResponse>> getAllPublicDepartment(@RequestBody PageFilterInput<ExplorePageFilter> input) {
        Pageable pageable = input.getPageSize() == 0
                ? PageRequest.of(0, Integer.MAX_VALUE) // Sử dụng PageRequest với size rất lớn khi không phân trang
                : PageRequest.of(input.getPageNumber(), input.getPageSize());
        Page<BookResponse> result = bookService.getBooksInExplorePage(input, pageable);
        return new PageResponse<List<BookResponse>>().result(result.getContent()).dataCount(result.getTotalElements());
    }
}
