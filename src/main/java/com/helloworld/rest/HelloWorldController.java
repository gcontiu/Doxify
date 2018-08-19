package com.helloworld.rest;

import com.helloworld.dto.Greeting;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @GetMapping("/")
    public Greeting helloWorld(@RequestParam(value = "name", defaultValue = "World") String name) {
        Greeting greeting = new Greeting();
        greeting.setMessage(String.format("Hello, %s!", name));
        return greeting;
    }

    @GetMapping("/{name}")
    public Greeting greeting(@PathVariable("name") String name) {
        Greeting greeting = new Greeting();
        greeting.setMessage(String.format("Hello, %s!", name));
        return greeting;
    }
}
