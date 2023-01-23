package com.example.springreverseproxy;

import lombok.Data;

@Data
public class App {
    private final String host;
    private final int port;
}
