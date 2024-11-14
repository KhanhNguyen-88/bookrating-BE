package com.buihuuduy.book_rating.service;

import com.buihuuduy.book_rating.DTO.PageFilterInput;
import com.buihuuduy.book_rating.DTO.request.BookRequestDTO;
import com.buihuuduy.book_rating.DTO.request.ExplorePageFilter;
import com.buihuuduy.book_rating.DTO.response.BookDetailPageResponse;
import com.buihuuduy.book_rating.DTO.response.BookResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookService
{
    Page<BookResponse> getBooksInExplorePage(PageFilterInput<ExplorePageFilter> input, Pageable pageable);

    BookDetailPageResponse getBookDetailById(Integer bookId);

    List<String> getAuthorsRecommendation(String input);

    List<BookResponse> getPostedBookByUsername(String username);

    List<BookResponse> searchBookByText(String common);

    List<BookResponse> getFavoriteBookByUserId(Integer userId);

    void createBook(String token, BookRequestDTO bookRequestDTO);
}
