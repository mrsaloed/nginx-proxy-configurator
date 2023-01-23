package com.example.springreverseproxy.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Formatter;

@Slf4j
@Controller
@RequestMapping("/proxy-config")
public class ProxyController {
    @Value("${nginx.config.path}")
    private String confPath;
    @Value("${nginx.path}")
    private String nginxPath;
    @Value("${nginx.config.template-path}")
    private String templatePath;

    @GetMapping
    public String doGet() {
        return "hello";
    }

    @PostMapping
    public String doPost(@RequestParam String host, @RequestParam int port) {
        setHost(host, port);
        reloadNginx();
        return "hello";
    }

    private void reloadNginx() {
        try {
            Process proc = Runtime.getRuntime().exec("service nginx reload");
            proc.waitFor();
            proc.destroy();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setHost(String host, int port) {
        try (FileWriter writer = new FileWriter(confPath, false);
             FileReader reader = new FileReader(templatePath)) {
            StringBuilder sb = new StringBuilder();
            int c;
            while ((c = reader.read()) != -1) {
                sb.append((char) c);
            }
            Formatter formatter = new Formatter();
            formatter.format(sb.toString(), host, port);
            writer.write(String.valueOf(formatter));
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
