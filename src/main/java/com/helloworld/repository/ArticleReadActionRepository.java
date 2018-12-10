package com.helloworld.repository;

import com.helloworld.data.ArticleReadAction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ArticleReadActionRepository extends CrudRepository<ArticleReadAction, String> {

    List<ArticleReadAction> findAll();

}
