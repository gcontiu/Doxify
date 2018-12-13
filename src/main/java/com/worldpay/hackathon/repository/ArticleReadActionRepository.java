package com.worldpay.hackathon.repository;

import com.worldpay.hackathon.data.ArticleReadAction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ArticleReadActionRepository extends CrudRepository<ArticleReadAction, String> {

    List<ArticleReadAction> findAll();

}
