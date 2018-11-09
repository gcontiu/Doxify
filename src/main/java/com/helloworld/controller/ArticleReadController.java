package com.helloworld.controller;

import com.helloworld.data.dto.DocumentationDetailsDTO;
import com.helloworld.service.DataProcessorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ArticleReadController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleReadController.class);

    @Autowired
    private DataProcessorService dataProcessorService;

    @PostMapping("/articleReadAction")
    public void articleReadAction (@RequestBody DocumentationDetailsDTO documentationDetailsDTO) {
        LOGGER.info("Article '{}' was red for {} seconds.", documentationDetailsDTO.articleTitle, documentationDetailsDTO.spentTimeInSeconds);
        dataProcessorService.processArticleDetails(documentationDetailsDTO);
    }

    @PostMapping("/blackListedArticle")
    public void readBlackListedArticle (@RequestBody DocumentationDetailsDTO documentationDetailsDTO) {
        LOGGER.info("Article '{}' was detected as blacklisted.", documentationDetailsDTO.articleTitle);
        dataProcessorService.processBlackListedArticle(documentationDetailsDTO);
    }

}
