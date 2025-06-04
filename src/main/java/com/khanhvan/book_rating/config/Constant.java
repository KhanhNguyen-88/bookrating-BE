package com.khanhvan.book_rating.config;

public class Constant
{
    public static final String[] PUBLIC_URLS =
    {
        "/api/user/register",
        "/api/user/login",
        "/api/user/following-account/{userId}",
        "/api/user/follower-account/{userId}",
        "/api/user/get-all",
        "/api/user/delete/{userId}",
        "/api/user/update/{userId}",
        "/api/user/info-detail/{userId}",

        "/api/book/get-explore-page",
        "/api/book/{bookId}",
        "/api/book/get-authors/{input}",
        "/api/book/get-posted-by-username/{username}",
        "api/book/get-book-unapprove/{userId}",
        "/api/book/search-common/{text}",
        "/api/book/get-book-by-userId/{userId}",
        "/api/book/get-my-book-by-userId/{userId}",
        "/api/book/get-list-book-detail",
        "/api/book/detail-with-userid/{bookId}",
        "/api/book/ranking-favorite",
        "/api/book/ranking-newest",
        "/api/book/chart",
        "/api/book/get-book-relation",

        "/api/mail/send-code",
        "/api/mail/verify-code",

        "/api/category/get-all",
        "/api/category/get-by-book",
        "/api/category/add",
        "/api/category/chart",
        "/api/category/update",
        "/api/category/info-detail/{categoryId}",

        "/api/feedback/add",
        "/api/feedback/stream",
        "/api/book/detail-with-userid/{bookId}",

        "/api/login-history/chart",

        "/api/file/upload",
        "/api/file/preview/{fileName}",
        "/api/book/stream/*",

        "/api/language/add",
        "/api/language/get-all",

        "/ws/**",
        "/api/book/stream-admin-page",
        "/admin-get-all-book",
    };
}
