package com.buihuuduy.book_rating.config;

public class Constant
{
    public static final String[] PUBLIC_URLS =
    {
        "/api/user/register",
        "/api/user/login",
        "/api/user/detail/{userId}",
        "/api/user/following-account/{userId}",
        "/api/user/follower-account/{userId}",

        "/api/book/get-explore-page",
        "/api/book/{bookId}",
        "/api/book/get-authors/{input}",
        "/api/book/get-posted-by-username/{username}",
        "/api/book/search-common/{text}",
        "/api/book/get-book-by-userId/{userId}",
        "/api/book/get-book-by-userId/1",
        "/api/book/get-list-book-detail",
        "/api/book/stream/{userId}",
        "/api/book/detail-with-userid/{bookId}",

        "/api/mail/send-code",
        "/api/mail/verify-code",

        "/api/category/get-all",
        "/api/category/get-by-book",
        "/api/category/add",
        "/api/feedback/add",
        "/api/feedback/stream",

        "/ws/**",
        "/api/book/stream/{userId}"
    };
}
