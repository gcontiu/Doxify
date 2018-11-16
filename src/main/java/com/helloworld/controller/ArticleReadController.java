package com.helloworld.controller;

import com.helloworld.data.dto.ArticleDTO;
import com.helloworld.service.DataProcessorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class ArticleReadController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleReadController.class);

    @Autowired
    private DataProcessorService dataProcessorService;

    @PostMapping("/articleReadAction")
    public void articleReadAction (@RequestBody ArticleDTO articleDTO) {
        LOGGER.info("Article '{}' was red for {} seconds.", articleDTO.title, articleDTO.timeSpentInSeconds);
        dataProcessorService.processArticleDetails(articleDTO);
    }

    @PostMapping("/blackListedArticle")
    public void readBlackListedArticle (@RequestBody ArticleDTO articleDTO) {
        LOGGER.info("Article '{}' was detected as blacklisted.", articleDTO.title);
        dataProcessorService.processBlackListedArticle(articleDTO);
    }

    @GetMapping("/allArticles")
    @ResponseBody
    public Long getAllArticles() {
        return dataProcessorService.countAllArticles();
    }

}
