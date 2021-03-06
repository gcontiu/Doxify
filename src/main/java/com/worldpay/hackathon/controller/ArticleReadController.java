package com.worldpay.hackathon.controller;

import java.util.List;

import com.worldpay.hackathon.adapter.UserDataAdapter;
import com.worldpay.hackathon.data.dto.ArticleDTO;
import com.worldpay.hackathon.data.dto.UserDashboardDTO;
import com.worldpay.hackathon.service.DataProcessorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArticleReadController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleReadController.class);

    private final DataProcessorService dataProcessorService;
    private final UserDataAdapter userDataAdapter;

    @Autowired
    public ArticleReadController(DataProcessorService dataProcessorService, UserDataAdapter userDataAdapter) {
        this.dataProcessorService = dataProcessorService;
        this.userDataAdapter = userDataAdapter;
    }

    @CacheEvict(cacheNames = { "authorStats", "averageTimeOnArticle", "topAchievedCoinsForArticle", "articleStats" }, allEntries = true)
    @PostMapping("/articleReadAction")
    public void articleReadAction(@RequestBody ArticleDTO articleDTO) {
        LOGGER.info("Article '{}' was red for {} seconds.", articleDTO.title, articleDTO.timeSpentInSeconds);
        dataProcessorService.processArticleDetails(articleDTO);
        LOGGER.info("Invalidated Caches for authorStats, averageTimeOnArticle, topAchievedCoinsForArticle, and articleStats.");
    }

    @CacheEvict(cacheNames = { "authorStats", "averageTimeOnArticle", "topAchievedCoinsForArticle", "articleStats" }, allEntries = true)
    @PostMapping("/blackListedArticle")
    public void readBlackListedArticle(@RequestBody ArticleDTO articleDTO) {
        LOGGER.info("Article '{}' was detected as blacklisted.", articleDTO.title);
        dataProcessorService.processBlackListedArticle(articleDTO);
        LOGGER.info("Invalidated Caches for authorStats, averageTimeOnArticle, topAchievedCoinsForArticle, and articleStats.");
    }

    @GetMapping("/allArticles")
    @ResponseBody
    public Long getAllArticles() {
        return dataProcessorService.countAllArticles();
    }

    @GetMapping("/allArticlesForAuthor")
    @ResponseBody
    public ResponseEntity<List<UserDashboardDTO>> getAllArticlesForAuthor(@RequestParam("username") String username) {
        return new ResponseEntity<>(userDataAdapter.getAllArticles(username), HttpStatus.OK);
    }

}
