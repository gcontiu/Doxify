package com.helloworld.controller;

import com.helloworld.data.dto.AuthorStatsDTO;
import com.helloworld.service.DataProcessorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StatsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatsController.class);

    private final DataProcessorService dataProcessorService;

    @Autowired
    public StatsController(DataProcessorService dataProcessorService) {
        this.dataProcessorService = dataProcessorService;
    }

    @GetMapping("/authorStats")
    @ResponseBody
    List<AuthorStatsDTO> getAllAuthorStats() {
        LOGGER.info("Returning author stats for all authors.");
        return dataProcessorService.getAllAuthorStats();
    }
}
