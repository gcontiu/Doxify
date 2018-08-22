package com.helloworld.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.util.Arrays.asList;

@Service
public class QuoteService {

    private  static final Resource QUOTE_FEED = new ClassPathResource("quote.json");

    private final List<Quote> listOfQuotes;

    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    private final ObjectMapper objectMapper;

    @Inject
    public QuoteService(ObjectMapper objectMapper) throws IOException {
        this.objectMapper = objectMapper;
        // initialize list of quotes
        this.listOfQuotes = asList(objectMapper.readValue(QUOTE_FEED.getInputStream(), Quote[].class));
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
        Collections.shuffle(listOfQuotes);
        Quote quote = listOfQuotes.stream()
                .findAny()
                .orElseGet(() -> Quote.builder().build());
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
