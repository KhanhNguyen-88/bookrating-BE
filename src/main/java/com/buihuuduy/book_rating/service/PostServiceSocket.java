//package com.buihuuduy.book_rating.service;
//
//import com.buihuuduy.book_rating.DTO.CommentRequest;
//import com.buihuuduy.book_rating.DTO.PostRequest;
//import com.buihuuduy.book_rating.entity.CommentEntityTmp;
//import com.buihuuduy.book_rating.entity.PostEntityTmp;
//import com.buihuuduy.book_rating.repository.CommentRepository;
//import com.buihuuduy.book_rating.repository.PostRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class PostService
//{
//    @Autowired
//    private PostRepository postRepository;
//
//    @Autowired
//    private CommentRepository commentRepository;
//
//    public PostEntityTmp createPost(PostRequest postRequest)
//    {
//        PostEntityTmp postEntityTmp = new PostEntityTmp();
//        postEntityTmp.setTitle(postRequest.getTitle());
//        postEntityTmp.setContent(postRequest.getContent());
//        postEntityTmp = postRepository.save(postEntityTmp);
//        return postEntityTmp;
//    }
//
//    public CommentEntityTmp addCommentToPost(CommentRequest commentRequest)
//    {
//        PostEntityTmp postEntityTmp = postRepository.findById(commentRequest.getPostId())
//                .orElseThrow(() -> new RuntimeException("post not found"));
//
//        CommentEntityTmp commentEntity = new CommentEntityTmp();
//        commentEntity.setContent(commentRequest.getContent());
//        commentEntity.setPost(postEntityTmp);
//        return commentRepository.save(commentEntity);
//    }
//}
