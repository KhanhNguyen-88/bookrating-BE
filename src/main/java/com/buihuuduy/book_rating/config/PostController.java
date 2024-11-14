package com.buihuuduy.book_rating.config;

import com.buihuuduy.book_rating.DTO.CommentRequest;
import com.buihuuduy.book_rating.DTO.PostRequest;
import com.buihuuduy.book_rating.entity.CommentEntity;
import com.buihuuduy.book_rating.entity.PostEntityTmp;
import com.buihuuduy.book_rating.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@CrossOrigin
public class PostController
{
    @Autowired
    private PostService postService;

    @MessageMapping("/post.create")
    @SendTo("/topic/posts")
    public PostEntityTmp createPost(PostRequest postRequest) {
        return postService.createPost(postRequest);
    }

    @MessageMapping("/post.comment")
    @SendTo("/topic/comments")
    public CommentEntity commentOnPost(CommentRequest commentRequest) {
        return postService.addCommentToPost(commentRequest);
    }
}
