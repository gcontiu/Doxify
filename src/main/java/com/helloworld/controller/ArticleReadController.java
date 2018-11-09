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
    public void test (@RequestBody DocumentationDetailsDTO documentationDetailsDTO) {
        LOGGER.info("Article '{}' red for {} seconds.", documentationDetailsDTO.articleTitle, documentationDetailsDTO.spentTimeInSeconds);
        dataProcessorService.processDocumentDetails(documentationDetailsDTO);
    }

}
