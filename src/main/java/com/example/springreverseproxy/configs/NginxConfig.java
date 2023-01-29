package com.example.springreverseproxy.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Data
@Component
@ConfigurationProperties(prefix = "nginx")
public class NginxConfig {
    private HashSet<String> ipBlockList = new HashSet<>();
    private String configPath;
    private String templatePath;
    private String redirectHost;
}
