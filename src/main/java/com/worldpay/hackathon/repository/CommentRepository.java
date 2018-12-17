package com.worldpay.hackathon.repository;

import org.springframework.data.repository.CrudRepository;

import com.worldpay.hackathon.data.Article;
import com.worldpay.hackathon.data.Comment;

public interface CommentRepository extends CrudRepository<Comment, Article> {

    Comment findByContentHash(String contentHash);
}
