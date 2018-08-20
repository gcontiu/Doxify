package com.helloworld.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;

@Service
public class QuoteService {
    private static final Resource QUOTE_FEED = new ClassPathResource("quote.json");
    private final List<WebSocketSession> sessions = new ArrayList<>();

    private final ObjectMapper objectMapper;

    @Autowired
    public QuoteService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Sends a random quote to each session every two seconds
     */
    @Scheduled(fixedDelay = 2000L)
    public void sendMessage() {
        sessions.stream().forEach(this::sendMessage);
    }

    @SneakyThrows
    private String randomQuote() {
        List<Quote> quotes = asList(objectMapper.readValue(QUOTE_FEED.getInputStream(), Quote[].class));
        Collections.shuffle(quotes);
        Quote quote = quotes.parallelStream()
                .findFirst()
                .orElse(Quote.builder().build());
        return objectMapper.writeValueAsString(quote);
    }

    @SneakyThrows
    private void sendMessage(WebSocketSession session) {
        session.sendMessage(new TextMessage(this.randomQuote()));
    }

    public void addSession(WebSocketSession session) {
        sessions.add(session);
    }

    public void removeSession(WebSocketSession session) {
        sessions.remove(session);
    }
}
