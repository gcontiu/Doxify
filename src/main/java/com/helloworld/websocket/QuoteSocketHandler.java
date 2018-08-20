package com.helloworld.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.inject.Inject;
import java.io.IOException;

@Component
public class QuoteSocketHandler extends TextWebSocketHandler {

    @Inject
    private QuoteService quoteService;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws InterruptedException, IOException {
        // do nothing here for now
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        quoteService.addSession(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        quoteService.removeSession(session);
    }

}