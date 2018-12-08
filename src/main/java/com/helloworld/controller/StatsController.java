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


@RestController
public class StatsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatsController.class);

    private final AuthorStatsAdapter authorStatsAdapter;

    @Autowired
    public StatsController(AuthorStatsAdapter authorStatsAdapter) {
        this.authorStatsAdapter = authorStatsAdapter;
    }

    @GetMapping("/authorStats")
    @ResponseBody
    List<AuthorStatsDTO> getAllAuthorStats() {
        LOGGER.info("Returning author stats for all authors.");
        return authorStatsAdapter.getAllAuthorStats();
    }
}
