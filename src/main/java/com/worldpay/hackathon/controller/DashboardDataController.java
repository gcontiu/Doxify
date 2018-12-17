package com.worldpay.hackathon.controller;

import java.util.ArrayList;
import java.util.List;

import com.worldpay.hackathon.data.dto.AuthorStatsDTO;
import com.worldpay.hackathon.service.DataProcessorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.worldpay.hackathon.adapter.ArticleStatsAdapter;
import com.worldpay.hackathon.adapter.AuthorStatsAdapter;
import com.worldpay.hackathon.data.dto.ArticleStatsDTO;

@RestController
public class DashboardDataController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DashboardDataController.class);

    private final AuthorStatsAdapter authorStatsAdapter;
    private final ArticleStatsAdapter articleStatsAdapter;
    private final DataProcessorService service;

    @Autowired
    public DashboardDataController(AuthorStatsAdapter authorStatsAdapter, DataProcessorService service, ArticleStatsAdapter articleStatsAdapter) {
        this.authorStatsAdapter = authorStatsAdapter;
        this.service = service;
        this.articleStatsAdapter = articleStatsAdapter;
    }

    @GetMapping("/authorStats")
    @ResponseBody
    List<AuthorStatsDTO> getAllAuthorStats() {
        LOGGER.info("Returning author stats for all authors.");
        return authorStatsAdapter.getAllAuthorStats();
    }

    @GetMapping("/allAuthors")
    @ResponseBody
    public Long countAllAuthors() {
        LOGGER.info("Returning the number of authors.");
        return service.countAllAuthors();
    }

    @GetMapping("/averageTimeOnArticles")
    @ResponseBody
    Double getAverageTimeSpentOnArticles() {
        LOGGER.info("Returning the average time spent on articles.");
        return authorStatsAdapter.getAverageTimeSpentOnArticles();
    }

    @GetMapping("/maxCoinAchievedArticle")
    @ResponseBody
    Double getTopAchievedCoinForArticle() {
        LOGGER.info("Returning the maximum coin count achieved on an article.");
        return authorStatsAdapter.getTopAchievedCoinForArticle();
    }

    @GetMapping("/articleStats")
    @ResponseBody
    List<ArticleStatsDTO> getAllArticleStatsByUsernameOrCategory(@RequestParam(name = "username", required = false) String username,
            @RequestParam(name = "category", required = false) String category) {
        if (category != null && !category.isEmpty()) {
            LOGGER.info("Returning all article stats for category '{}'.", category);
            return articleStatsAdapter.getAllArticleStatsByCategory(category);
        }
        if (username != null && !username.isEmpty()) {
            LOGGER.info("Returning all article stats for author with username '{}'.", username);
            return articleStatsAdapter.getAllArticleStatsByUsername(username);
        }
        LOGGER.info("Invalid article stats request, returning empty list.");
        return new ArrayList<>();
    }
}
