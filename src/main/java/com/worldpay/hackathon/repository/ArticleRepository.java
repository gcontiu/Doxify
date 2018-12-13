package com.worldpay.hackathon.repository;

import java.util.List;

import com.worldpay.hackathon.data.Author;
import org.springframework.data.repository.CrudRepository;

import com.worldpay.hackathon.data.Article;

public interface ArticleRepository extends CrudRepository<Article, String> {

    Article findByTitle(String title);

    long count();

    List<Article> findAllByAuthor(Author author);

    List<Article> findAllByCategory(String category);

}
