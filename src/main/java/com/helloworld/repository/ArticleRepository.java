package com.helloworld.repository;

import org.springframework.data.repository.CrudRepository;

import com.helloworld.data.Article;

public interface ArticleRepository extends CrudRepository<Article, String> {

    Article findByTitle(String title);
}
