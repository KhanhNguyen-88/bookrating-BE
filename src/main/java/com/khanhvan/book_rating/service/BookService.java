package com.khanhvan.book_rating.service;

import com.khanhvan.book_rating.DTO.PageFilterInput;
import com.khanhvan.book_rating.DTO.request.BookRequestDTO;
import com.khanhvan.book_rating.DTO.request.CommentRequest;
import com.khanhvan.book_rating.DTO.response.BookDetailResponse;
import com.khanhvan.book_rating.DTO.response.BookResponse;
import com.khanhvan.book_rating.DTO.request.ExplorePageFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import java.util.List;
import java.util.Map;

@Service
public interface BookService
{
    Flux<ServerSentEvent<List<BookDetailResponse>>> streamPosts(Integer userId);

    Flux<ServerSentEvent<List<BookDetailResponse>>> streamPostsOnAdminPage();

    Page<BookResponse> getBooksInExplorePage(PageFilterInput<ExplorePageFilter> input, Pageable pageable);

    BookDetailResponse getBookDetailByIdWithUserId(Integer userId, Integer bookId);

    List<String> getAuthorsRecommendation(String input);

    List<BookResponse> getPostedBookByUsername(String username);

    List<BookResponse> searchBookByText(String common);

    List<BookResponse> getFavoriteBookByUserId(Integer userId);

    List<BookResponse> getMyBookByUserId(Integer userId);
    List<BookResponse> getBookUnapproveByUserId(Integer userId);

    void upBook(String token, BookRequestDTO bookRequestDTO);

    void commentBook(String token, CommentRequest commentRequest);

    List<BookDetailResponse> getBookListOnHomePage(Integer userId);

    void approveBook(Integer bookId);
    void cancelBook(Integer bookId);
    List<BookResponse> findBokHasRelationship(String author, List<String> categoryNames);

    List<BookResponse> getBookInFavoriteRanking();

    List<BookResponse> getBookInNewestRanking();

    Map<Integer, Long> displayBookQuantityByMonth();
    List<BookResponse> getAllBook();
}
