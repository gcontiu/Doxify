package com.helloworld.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.helloworld.data.Article;
import com.helloworld.data.Author;

public interface ArticleRepository extends CrudRepository<Article, String> {

    Article findByTitle(String title);

    long count();

    List<Article> findAllByAuthor(Author author);

    List<Article> findAllByCategory(String category);

}
