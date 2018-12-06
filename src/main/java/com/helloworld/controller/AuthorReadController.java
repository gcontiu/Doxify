package com.helloworld.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.helloworld.service.DataProcessorService;

@RestController
public class AuthorReadController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorReadController.class);

    @Autowired
    private DataProcessorService service;

    @GetMapping("/allAuthors")
    @ResponseBody
    public Long countAllAuthors() {
        return service.countAllAuthors();
    }
}
