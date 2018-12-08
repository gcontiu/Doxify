package com.helloworld.controller;

import java.util.List;

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

import com.helloworld.adapter.UserDataAdapter;
import com.helloworld.data.dto.ArticleDTO;
import com.helloworld.data.dto.UserDashboardDTO;
import com.helloworld.service.DataProcessorService;

@RestController
public class ArticleReadController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleReadController.class);

    @Autowired
    private DataProcessorService dataProcessorService;
    @Autowired
    private UserDataAdapter userDataAdapter;

    @CacheEvict(cacheNames = "authorStats", allEntries = true)
    @PostMapping("/articleReadAction")
    public void articleReadAction(@RequestBody ArticleDTO articleDTO) {
        LOGGER.info("Article '{}' was red for {} seconds.", articleDTO.title, articleDTO.timeSpentInSeconds);
        dataProcessorService.processArticleDetails(articleDTO);
        LOGGER.info("Invalidated AuthorStatsCache.");
    }

    @PostMapping("/blackListedArticle")
    public void readBlackListedArticle(@RequestBody ArticleDTO articleDTO) {
        LOGGER.info("Article '{}' was detected as blacklisted.", articleDTO.title);
        dataProcessorService.processBlackListedArticle(articleDTO);
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
