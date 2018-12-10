package com.helloworld.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.helloworld.adapter.AuthorStatsAdapter;
import com.helloworld.data.dto.AuthorStatsDTO;
import com.helloworld.service.DataProcessorService;


@RestController
public class DashboardDataController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DashboardDataController.class);

    private final AuthorStatsAdapter authorStatsAdapter;
    private final DataProcessorService service;

    @Autowired
    public DashboardDataController(AuthorStatsAdapter authorStatsAdapter, DataProcessorService service) {
        this.authorStatsAdapter = authorStatsAdapter;
        this.service = service;
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
}
