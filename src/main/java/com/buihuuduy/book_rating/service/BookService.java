package com.buihuuduy.book_rating.service;

import com.buihuuduy.book_rating.DTO.PageFilterInput;
import com.buihuuduy.book_rating.DTO.request.BookRequestDTO;
import com.buihuuduy.book_rating.DTO.request.CommentRequest;
import com.buihuuduy.book_rating.DTO.request.ExplorePageFilter;
import com.buihuuduy.book_rating.DTO.response.BookDetailResponse;
import com.buihuuduy.book_rating.DTO.response.BookResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public interface BookService
{
    Flux<ServerSentEvent<List<BookDetailResponse>>> streamPosts(String token);

    Page<BookResponse> getBooksInExplorePage(PageFilterInput<ExplorePageFilter> input, Pageable pageable);

    BookDetailResponse getBookDetailByIdWithToken(String token, Integer bookId);

    List<String> getAuthorsRecommendation(String input);

    List<BookResponse> getPostedBookByUsername(String username);

    List<BookResponse> searchBookByText(String common);

    List<BookResponse> getFavoriteBookByUserId(Integer userId);

    void upBook(String token, BookRequestDTO bookRequestDTO);

    void commentBook(String token, CommentRequest commentRequest);

    List<BookDetailResponse> getBookListOnHomePage(String token);
}
