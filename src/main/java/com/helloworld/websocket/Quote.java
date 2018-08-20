package com.helloworld.websocket;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Quote {
    private String author;
    private String quote;
}
