package com.helloworld.helloworld;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Greeting {
    private String message;
}
