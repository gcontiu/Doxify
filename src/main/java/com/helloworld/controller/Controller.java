package com.helloworld.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.helloworld.data.Article;
import com.helloworld.data.Author;
import com.helloworld.data.Comment;
import com.helloworld.data.dto.ArticleDTO;
import com.helloworld.data.dto.AuthorDTO;
import com.helloworld.data.dto.CommentDTO;
import com.helloworld.repository.ArticleRepository;
import com.helloworld.repository.AuthorRepository;

@RestController
public class Controller {

    private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);

    private final AuthorRepository authorRepository;
    private final ArticleRepository articleRepository;

    @Autowired
    public Controller(AuthorRepository authorRepository, ArticleRepository articleRepository) {
        this.authorRepository = authorRepository;
        this.articleRepository = articleRepository;
    }

    @PostMapping(value = "/author")
    ResponseEntity saveAuthor(@RequestBody AuthorDTO authorDTO) {
        LOGGER.info("Saving new author with username '{}'...", authorDTO.userName);
        authorRepository.save(new Author(authorDTO.userName));
        LOGGER.info("Author saved.");

        return ResponseEntity.ok("Author saved.");
    }

    @PostMapping(value = "/article")
    public ResponseEntity saveArticle(@RequestBody ArticleDTO articleDTO) {
        LOGGER.info("Saving new article named '{}' from user '{}'...", articleDTO.articleName, articleDTO.authorUserName);
        Author author = authorRepository.findByUserName(articleDTO.authorUserName);
        author.addArticle(new Article(articleDTO.articleName, author));
        authorRepository.save(author);
        LOGGER.info("Article saved.");

        return ResponseEntity.ok("Article saved.");
    }


    @PostMapping(value = "/comment")
    public ResponseEntity saveComment(@RequestBody CommentDTO commentDTO) {
        LOGGER.info("Saving new comment to article '{}' from user '{}'...", commentDTO.articleName, commentDTO.authorUserName);
        Author author = authorRepository.findByUserName(commentDTO.authorUserName);
        Article article = articleRepository.findByName(commentDTO.articleName);
        Comment comment = new Comment(author, article);
        author.addComment(comment);
        article.addComment(comment);
        authorRepository.save(author);
        articleRepository.save(article);
        LOGGER.info("Comment saved.");

        return ResponseEntity.ok("Comment saved.");
    }
}
