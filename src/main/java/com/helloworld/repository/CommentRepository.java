package com.helloworld.repository;

import org.springframework.data.repository.CrudRepository;

import com.helloworld.data.Article;
import com.helloworld.data.Comment;

public interface CommentRepository extends CrudRepository<Comment, Article> {

    Comment findByContentHash(String contentHash);
}
