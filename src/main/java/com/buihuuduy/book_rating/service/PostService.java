package com.buihuuduy.book_rating.service;

import com.buihuuduy.book_rating.DTO.CommentRequest;
import com.buihuuduy.book_rating.DTO.PostRequest;
import com.buihuuduy.book_rating.entity.CommentEntity;
import com.buihuuduy.book_rating.entity.PostEntity;
import com.buihuuduy.book_rating.repository.CommentRepository;
import com.buihuuduy.book_rating.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService
{
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    public PostEntity createPost(PostRequest postRequest)
    {
        PostEntity postEntity = new PostEntity();
        postEntity.setTitle(postRequest.getTitle());
        postEntity.setContent(postRequest.getContent());
        postEntity = postRepository.save(postEntity);
        return postEntity;
    }

    public CommentEntity addCommentToPost(CommentRequest commentRequest)
    {
        PostEntity postEntity = postRepository.findById(commentRequest.getPostId())
                .orElseThrow(() -> new RuntimeException("post not found"));

        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setContent(commentRequest.getContent());
        commentEntity.setPost(postEntity);
        return commentRepository.save(commentEntity);
    }
}
