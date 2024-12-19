package com.buihuuduy.book_rating.service.impl;

import com.buihuuduy.book_rating.DTO.PageFilterInput;
import com.buihuuduy.book_rating.DTO.request.BookRequestDTO;
import com.buihuuduy.book_rating.DTO.request.CommentRequest;
import com.buihuuduy.book_rating.DTO.request.ExplorePageFilter;
import com.buihuuduy.book_rating.DTO.response.BookDetailResponse;
import com.buihuuduy.book_rating.DTO.response.BookResponse;
import com.buihuuduy.book_rating.DTO.response.CommentResponse;
import com.buihuuduy.book_rating.DTO.response.PercentFeedback;
import com.buihuuduy.book_rating.entity.BookCategoryEntity;
import com.buihuuduy.book_rating.entity.BookEntity;
import com.buihuuduy.book_rating.entity.CommentEntity;
import com.buihuuduy.book_rating.entity.UserEntity;
import com.buihuuduy.book_rating.exception.CustomException;
import com.buihuuduy.book_rating.exception.ErrorCode;
import com.buihuuduy.book_rating.mapper.BookMapper;
import com.buihuuduy.book_rating.repository.BookCategoryRepository;
import com.buihuuduy.book_rating.repository.BookRepository;
import com.buihuuduy.book_rating.repository.CommentRepository;
import com.buihuuduy.book_rating.repository.UserRepository;
import com.buihuuduy.book_rating.service.BookService;
import com.buihuuduy.book_rating.service.utils.CommonFunction;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class BookServiceImpl implements BookService
{
    private static final Logger log = LoggerFactory.getLogger(BookServiceImpl.class);
    private final BookCategoryRepository bookCategoryRepository;
    private final BookRepository bookRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BookMapper bookMapper;

    @PersistenceContext
    private EntityManager entityManager;

    public BookServiceImpl(BookCategoryRepository bookCategoryRepository, BookRepository bookRepository, CommentRepository commentRepository, UserRepository userRepository, BookMapper bookMapper) {
        this.bookCategoryRepository = bookCategoryRepository;
        this.bookRepository = bookRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    @Transactional
    public Page<BookResponse> getBooksInExplorePage(PageFilterInput<ExplorePageFilter> input, Pageable pageable)
    {
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT ")
                .append("   b.id, ")
                .append("   b.book_name, b.book_description, ")
                .append("   b.book_image, b.published_date, ")
                .append("   b.book_format, b.book_sale_link, ")
                .append("   bl.language_name, b.book_author,  ")
                .append("   GROUP_CONCAT(c.cate_name ORDER BY c.cate_name SEPARATOR ', ') AS category, ")
                .append("   CEIL(AVG(fb.rating)) AS average_rating, COUNT(fb.rating) AS rating_count, b.created_at")
                .append(" FROM book b ")
                .append("LEFT JOIN book_category bc ON b.id = bc.book_id ")
                .append("LEFT JOIN book_language bl ON b.language_id = bl.id ")
                .append("LEFT JOIN category c ON bc.category_id = c.id ")
                .append("LEFT JOIN book_rating_db.feedback fb ON fb.book_id = b.id ")
                .append("WHERE 1=1 AND b.approval_status = 1 ");

        int paramIndex = 1;

        if (input.getFilter().getBookAuthor() != null && !input.getFilter().getBookAuthor().trim().isEmpty()) {
            sql.append(" AND LOWER(b.book_author) LIKE LOWER(CONCAT('%', ?").append(paramIndex).append(", '%')) ");
            paramIndex++;
        }

        sql.append("GROUP BY ")
                .append("   b.id, ")
                .append("    b.book_name, b.book_description, ")
                .append("    b.book_image, b.published_date, ")
                .append("    b.book_format, b.book_sale_link, ")
                .append("    bl.language_name, b.book_author ")
                .append("HAVING 1=1 ");

        if (input.getFilter().getCategoryId() != null) {
            sql.append("AND SUM(c.id = ?").append(paramIndex).append(") > 0 ");
            paramIndex++;
        }
        if (input.getFilter().getRating() != null) {
            sql.append("AND average_rating = ?").append(paramIndex).append(" ");
        }

        Query query = entityManager.createNativeQuery(sql.toString());

        // Set parameters
        int queryParamIndex = 1;

        if (input.getFilter().getBookAuthor() != null && !input.getFilter().getBookAuthor().isEmpty()) {
            query.setParameter(queryParamIndex,  input.getFilter().getBookAuthor());
            queryParamIndex++;
        }
        if (input.getFilter().getCategoryId() != null) {
            query.setParameter(queryParamIndex, + input.getFilter().getCategoryId());
            queryParamIndex++;
        }
        if (input.getFilter().getRating() != null) {
            query.setParameter(queryParamIndex, + input.getFilter().getRating());
        }

        // Retrieve and map results
        List<Object[]> results;

        int totalRows = query.getResultList().size(); // Count the total rows
        if (pageable.isPaged()) {
            results = query.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        } else {
            results = query.getResultList(); // No pagination
        }

        List<BookResponse> bookResponseList = new ArrayList<>();
        for(Object[] result : results)
        {
            BookResponse bookResponse = getBookResponseExplorePage(result);
            bookResponseList.add(bookResponse);
        }
        return new PageImpl<>(bookResponseList, pageable, totalRows);
    }

    private static BookResponse getBookResponseExplorePage(Object[] result) {
        BookResponse bookResponse = new BookResponse();
        System.out.println(result[0]);
        bookResponse.setId((Integer) result[0]);
        bookResponse.setBookName((String) result[1]);
        bookResponse.setBookDescription((String) result[2]);
        bookResponse.setBookImage((String) result[3]);
        bookResponse.setPublishedDate((Date) result[4]);
        bookResponse.setBookFormat((String) result[5]);
        bookResponse.setBookSaleLink((String) result[6]);
        bookResponse.setLanguage((String) result[7]);
        bookResponse.setBookAuthor((String) result[8]);
        bookResponse.setCategoryName((String) result[9]);
        bookResponse.setAverageRating((Long) result[10]);
        bookResponse.setTotalRating((Long) result[11]);
        bookResponse.setCreatedAt((Date) result[12]);
        return bookResponse;
    }
    private static BookResponse convertBookResponseDetail(Object[] result) {
        BookResponse bookResponse = new BookResponse();
        Object[] bookData = (Object[]) result[0] ;
        bookResponse.setId((Integer) bookData[0]);
        bookResponse.setBookName((String) bookData[1]);
        bookResponse.setBookDescription((String) bookData[2]);
        bookResponse.setBookImage((String) bookData[3]);
        bookResponse.setPublishedDate((Date) bookData[4]);
        bookResponse.setBookFormat((String) bookData[5]);
        bookResponse.setBookSaleLink((String) bookData[6]);
        bookResponse.setLanguage((String) bookData[7]);
        bookResponse.setBookAuthor((String) bookData[8]);
        bookResponse.setCategoryName((String) bookData[9]);
        bookResponse.setAverageRating((Long) bookData[10]);
        bookResponse.setTotalRating((Long) bookData[11]);
        bookResponse.setCreatedAt((Date) bookData[12]);
        bookResponse.setCreatedBy((String) bookData[13]);
        bookResponse.setUserImage((String) bookData[14]);
        bookResponse.setUserId((Integer) bookData[15]);
        if (bookData[14] != null) {
            bookResponse.setIsFavorite((Integer) bookData[16]);
        } else {
            bookResponse.setIsFavorite(0);
        }
        return bookResponse;
    }

    public List<BookEntity> getBooksInExplorePage1(Integer categoryId)
    {
        List<BookCategoryEntity> bookCategoryEntityList = bookCategoryRepository.findByCategoryId(categoryId);
        if(bookCategoryEntityList.isEmpty()) {
            throw new CustomException(ErrorCode.CATEGORY_NOT_FOUND);
        }
        List<BookEntity> books = new ArrayList<>();
        for(BookCategoryEntity bookCategoryEntity : bookCategoryEntityList) {
            BookEntity bookEntity = bookRepository.findById(bookCategoryEntity.getBookId()).orElseThrow(
                    () -> new CustomException(ErrorCode.BOOK_NOT_FOUND)
            );
            books.add(bookEntity);
        }
        return books;
    }

    @Override
    public BookDetailResponse getBookDetailByIdWithUserId(Integer userId, Integer bookId)
    {
        BookDetailResponse bookDetailPageResponse = new BookDetailResponse();

        BookResponse bookResponse = convertBookResponseDetail(bookRepository.getBookResponseByBookIdWithToken(userId, bookId));

        bookDetailPageResponse.setBookResponse(bookResponse);

        StringBuilder sql = new StringBuilder();

        sql.append("SELECT ")
                .append("   fb.user_id, ")
                .append("   u.username, u.user_image, ")
                .append("   fb.comment, fb.rating, ")
                .append("   fb.updated_at ")
                .append("FROM feedback fb ")
                .append("LEFT JOIN user u ON fb.user_id = u.id ")
                .append("WHERE fb.book_id = ").append(bookId)
                .append(" ORDER BY fb.id DESC");
        ;

        Query query = entityManager.createNativeQuery(sql.toString());

        List<Object[]> results = query.getResultList();
        List<CommentResponse> feedbackResponseList = new ArrayList<>();
        if (results == null || results.isEmpty()) {
            bookDetailPageResponse.setFeedbackResponseList(null);
        } else {
            for (Object[] result : results) {
                CommentResponse feedbackResponse = new CommentResponse();
                feedbackResponse.setUserId((Integer) result[0]);
                feedbackResponse.setUserName((String) result[1]);
                feedbackResponse.setUserImage((String) result[2]);
                feedbackResponse.setComment((String) result[3]);
                feedbackResponse.setRating((Integer) result[4]);
                //feedbackResponse.setCreatedAt((LocalDateTime) result[5]);
                feedbackResponseList.add(feedbackResponse);
            }
            bookDetailPageResponse.setFeedbackResponseList(feedbackResponseList);
        }

        if(bookDetailPageResponse.getFeedbackResponseList() != null)
        {
            List<PercentFeedback> percentFeedbackList = new ArrayList<>();
            Map<Integer, Integer> percentFeedbackMap = new HashMap<>();
            // Count feedback
            for(CommentResponse commentResponse : feedbackResponseList) {
                int star = commentResponse.getRating();
                percentFeedbackMap.put(star, percentFeedbackMap.getOrDefault(star, 0) + 1);
            }
            for (int i = 1; i <= 5; i++) {
                int count = percentFeedbackMap.getOrDefault(i, 0);
                double percent = feedbackResponseList.isEmpty() ? 0 : (count * 100.0) / feedbackResponseList.size(); // Tính phần trăm
                percent = Double.parseDouble(String.format(Locale.US, "%.2f", percent));
                percentFeedbackList.add(new PercentFeedback(i, count, percent));
            }
            bookDetailPageResponse.setPercentFeedbackList(percentFeedbackList);
        }

        return bookDetailPageResponse;
    }

    @Override
    public List<String> getAuthorsRecommendation(String input)
    {
        List<String> authors;
        log.info("Input {}", input);
        authors = bookRepository.getAuthorByTitle(input.trim());
        return authors;
    }

    @Override
    public List<BookResponse> getPostedBookByUsername(String username)
    {
        List<BookEntity> bookEntities = bookRepository.findByCreatedBy(username);
        List<BookResponse> bookResponseList = new ArrayList<>();
        for(BookEntity bookEntity : bookEntities) {
            BookResponse bookResponse = bookMapper.toBookResponse(bookEntity);
            bookResponseList.add(bookResponse);
        }
        return bookResponseList;
    }

    @Override
    public List<BookResponse> searchBookByText(String common)
    {
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT ")
                .append("   b.id, ")
                .append("   b.book_name, b.book_image, b.book_author, ")
                .append("   GROUP_CONCAT(c.cate_name ORDER BY c.cate_name SEPARATOR ', ') ")
                .append("FROM book b ")
                .append("LEFT JOIN book_category bc ON b.id = bc.book_id ")
                .append("LEFT JOIN category c ON bc.category_id = c.id ")
                .append("WHERE ( ")
                .append("   b.book_name LIKE CONCAT('%', '").append(common).append("', '%') OR ")
                .append("   b.book_author LIKE CONCAT('%', '").append(common).append("', '%') OR ")
                .append("   c.cate_name LIKE CONCAT('%', '").append(common).append("', '%') ")
                .append(") AND b.approval_status = 1 ")
                .append("GROUP BY ")
                .append("   b.id, b.book_name, b.book_image, b.book_author");

        Query query = entityManager.createNativeQuery(sql.toString());

        List<Object[]> results = query.getResultList();
        List<BookResponse> bookResponseList = new ArrayList<>();

        for(Object[] result : results)
        {
            BookResponse bookResponse = new BookResponse();
            bookResponse.setId((Integer) result[0]);
            bookResponse.setBookName((String) result[1]);
            bookResponse.setBookImage((String) result[2]);
            bookResponse.setBookAuthor((String) result[3]);
            bookResponse.setCategoryName((String) result[4]);
            bookResponseList.add(bookResponse);
        }
        return bookResponseList;
    }

    @Override
    public List<BookResponse> getFavoriteBookByUserId(Integer userId)
    {
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT ")
                .append("   b.id, b.book_name, b.book_image ")
                .append("FROM book b ")
                .append("JOIN favorite_book fb on b.id = fb.book_id ")
                .append("JOIN user u on u.id = fb.user_id ")
                .append("WHERE fb.user_id = ").append(userId)
                .append(" AND b.approval_status = 1 ")
                .append(" ORDER BY b.id DESC");

        Query query = entityManager.createNativeQuery(sql.toString());
        List<Object[]> results = query.getResultList();
        List<BookResponse> bookResponseList = new ArrayList<>();
        for(Object[] result : results)
        {
            BookResponse bookResponse = new BookResponse();
            bookResponse.setId((Integer) result[0]);
            bookResponse.setBookName((String) result[1]);
            bookResponse.setBookImage((String) result[2]);
            bookResponseList.add(bookResponse);
        }
        return bookResponseList;
    }

    @Override
    public List<BookResponse> getMyBookByUserId(Integer userId)
    {
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT ")
                .append("   b.id, b.book_name, b.book_image ")
                .append("FROM book b ")
                .append("JOIN user u ON b.created_by = u.username ")
                .append("WHERE u.id = ").append(userId)
                .append(" AND b.approval_status = 1 ")
                .append(" ORDER BY b.id DESC");

        Query query = entityManager.createNativeQuery(sql.toString());
        List<Object[]> results = query.getResultList();
        List<BookResponse> bookResponseList = new ArrayList<>();
        for(Object[] result : results)
        {
            BookResponse bookResponse = new BookResponse();
            bookResponse.setId((Integer) result[0]);
            bookResponse.setBookName((String) result[1]);
            bookResponse.setBookImage((String) result[2]);
            bookResponseList.add(bookResponse);
        }
        return bookResponseList;
    }

    @Override
    public void upBook(String token, BookRequestDTO bookRequestDTO)
    {
        BookEntity bookEntity = new BookEntity();

        bookEntity.setBookName(bookRequestDTO.getBookName());
        bookEntity.setBookDescription(bookRequestDTO.getBookDescription());
        bookEntity.setBookImage(bookRequestDTO.getBookImage());
        bookEntity.setPublishedDate(bookRequestDTO.getPublishedDate());
        bookEntity.setBookSaleLink(bookRequestDTO.getBookSaleLink());
        bookEntity.setBookFormat(bookRequestDTO.getBookFormat());
        bookEntity.setLanguageId(bookRequestDTO.getLanguageId());
        bookEntity.setBookAuthor(bookRequestDTO.getBookAuthor());
        bookEntity.setApprovalStatus(0);

        String username = CommonFunction.getUsernameFromToken(token);
        bookEntity.setCreatedBy(username);
        bookEntity.setCreatedAt(LocalDateTime.now());

        bookRepository.save(bookEntity);

        // Luu bang CateBook
        for(Integer categoryId : bookRequestDTO.getCategoryId()) {
            BookCategoryEntity bookCategoryEntity = new BookCategoryEntity();
            bookCategoryEntity.setBookId(bookEntity.getId());
            bookCategoryEntity.setCategoryId(categoryId);
            bookCategoryRepository.save(bookCategoryEntity);
        }
    }

    @Override
    public void commentBook(String token, CommentRequest commentRequest)
    {
        CommentEntity commentEntity = new CommentEntity();

        commentEntity.setBookId(commentRequest.getBookId());
        commentEntity.setComment(commentRequest.getComment());
        commentEntity.setRating(commentRequest.getRating());

        String username = CommonFunction.getUsernameFromToken(token);
        UserEntity userEntity = userRepository.findByUsername(username);
        commentEntity.setCreatedBy(username);
        commentEntity.setCreatedAt(LocalDateTime.now());
        commentEntity.setUserId(userEntity.getId());

        commentRepository.save(commentEntity);
    }

    @Override
    public List<BookDetailResponse> getBookListOnHomePage(Integer userId)
    {
        List<BookEntity> bookEntityList = bookRepository.findAllOrderByIdDesc();

        List<BookDetailResponse> bookDetailResponseList = new ArrayList<>();

        for(BookEntity bookEntity : bookEntityList) {
            BookDetailResponse bookDetailResponse = getBookDetailByIdWithUserId(userId, bookEntity.getId());
            bookDetailResponseList.add(bookDetailResponse);
        }

        return bookDetailResponseList;
    }

    @Override
    public void approveBook(Integer bookId) {
        BookEntity bookEntity = bookRepository.findById(bookId).orElseThrow(
                () -> new CustomException(ErrorCode.BOOK_NOT_FOUND)
        );
        bookEntity.setApprovalStatus(1);
        bookRepository.save(bookEntity);
    }

    @Override
    public List<BookResponse> getBookInFavoriteRanking() {
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT ")
                .append("b.id, b.book_image, COUNT(favorite_book.user_id) AS favorite_number ")
                .append("FROM book b ")
                .append("LEFT JOIN favorite_book ON favorite_book.book_id = b.id ")
                .append("GROUP BY b.id, b.book_image ")
                .append("ORDER BY favorite_number DESC ")
                .append("LIMIT 10");
                ;

        Query query = entityManager.createNativeQuery(sql.toString());
        List<Object[]> results = query.getResultList();
        List<BookResponse> bookResponseList = new ArrayList<>();
        for(Object[] result : results)
        {
            BookResponse bookResponse = new BookResponse();
            bookResponse.setId((Integer) result[0]);
            bookResponse.setBookImage((String) result[1]);
            bookResponseList.add(bookResponse);
        }
        return bookResponseList;
    }

    @Override
    public List<BookResponse> getBookInNewestRanking() {
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT ")
                .append("b.id, b.book_image ")
                .append("FROM book b ")
                .append("ORDER BY b.created_at DESC ")
                .append("LIMIT 10");
        ;

        Query query = entityManager.createNativeQuery(sql.toString());
        List<Object[]> results = query.getResultList();
        List<BookResponse> bookResponseList = new ArrayList<>();
        for(Object[] result : results)
        {
            BookResponse bookResponse = new BookResponse();
            bookResponse.setId((Integer) result[0]);
            bookResponse.setBookImage((String) result[1]);
            bookResponseList.add(bookResponse);
        }
        return bookResponseList;
    }

    @Override
    public Flux<ServerSentEvent<List<BookDetailResponse>>> streamPosts(Integer userId) {
        return Flux.interval(Duration.ofSeconds(2))
                .publishOn(Schedulers.boundedElastic())
                .map(sequence -> ServerSentEvent.<List<BookDetailResponse>>builder().id(String.valueOf(sequence))
                        .event("post-list-event").data(getBookListOnHomePage(userId))
                        .build());
    }

    @Override
    public Flux<ServerSentEvent<List<BookDetailResponse>>> streamPostsOnAdminPage() {
        return Flux.interval(Duration.ofSeconds(2))
                .publishOn(Schedulers.boundedElastic())
                .map(sequence -> ServerSentEvent.<List<BookDetailResponse>>builder().id(String.valueOf(sequence))
                        .event("post-list-event").data(getBookListOnAdminPage())
                        .build());
    }

    private List<BookDetailResponse> getBookListOnAdminPage()
    {
        List<BookEntity> bookEntityList = bookRepository.findAllOnAdminPage();

        List<BookDetailResponse> bookDetailResponseList = new ArrayList<>();

        for(BookEntity bookEntity : bookEntityList) {
            BookDetailResponse bookDetailResponse = getBookDetailByIdAdminPage(bookEntity.getId());
            bookDetailResponseList.add(bookDetailResponse);
        }

        return bookDetailResponseList;
    }

    public BookDetailResponse getBookDetailByIdAdminPage(Integer bookId)
    {
        BookDetailResponse bookDetailPageResponse = new BookDetailResponse();
        BookResponse bookResponse = convertBookResponseDetailAdminPage(bookRepository.getBookResponseByBookIdWithTokenAdminPage( bookId));

        bookDetailPageResponse.setBookResponse(bookResponse);
        bookDetailPageResponse.setFeedbackResponseList(null);

        return bookDetailPageResponse;
    }

    private static BookResponse convertBookResponseDetailAdminPage(Object[] result) {
        BookResponse bookResponse = new BookResponse();
        Object[] bookData = (Object[]) result[0] ;
        bookResponse.setId((Integer) bookData[0]);
        bookResponse.setBookName((String) bookData[1]);
        bookResponse.setBookDescription((String) bookData[2]);
        bookResponse.setBookImage((String) bookData[3]);
        bookResponse.setPublishedDate((Date) bookData[4]);
        bookResponse.setBookFormat((String) bookData[5]);
        bookResponse.setBookSaleLink((String) bookData[6]);
        bookResponse.setLanguage((String) bookData[7]);
        bookResponse.setBookAuthor((String) bookData[8]);
        bookResponse.setCategoryName((String) bookData[9]);
        bookResponse.setCreatedAt((Date) bookData[10]);
        bookResponse.setCreatedBy((String) bookData[11]);
        bookResponse.setUserImage((String) bookData[12]);
        return bookResponse;
    }
}