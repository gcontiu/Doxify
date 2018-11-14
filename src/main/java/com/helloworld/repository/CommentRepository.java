package com.helloworld.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.helloworld.data.Article;
import com.helloworld.data.Comment;

public interface CommentRepository extends CrudRepository<Comment, Article> {

    List<Comment> findAllByArticle(Article article);

    Comment findByContentHash(String contentHash);
}
