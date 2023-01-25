package com.example.springreverseproxy.services;

import com.example.springreverseproxy.configs.NginxConfig;
import com.example.springreverseproxy.models.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Formatter;
import java.util.HashSet;

@Service
public class NginxService {

    private static final String NGINX_RELOAD = "service nginx reload";
    private final NginxConfig nginxConfig;

    @Autowired
    public NginxService(NginxConfig nginxConfig) {
        this.nginxConfig = nginxConfig;
    }

    public void setHostTo(Application application) {
        setHost(application);
        reloadNginx();
    }

    public HashSet<String> getBlockList() {
        return new HashSet(nginxConfig.getIpBlockList());
    }

    private void reloadNginx() {
        try {
            Process proc = Runtime.getRuntime().exec(NGINX_RELOAD);
            proc.waitFor();
            proc.destroy();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setHost(Application application) {
        try (FileWriter writer = new FileWriter(nginxConfig.getConfigPath(), false);
             FileReader reader = new FileReader(nginxConfig.getTemplatePath())) {
            StringBuilder sb = new StringBuilder();
            int c;
            while ((c = reader.read()) != -1) {
                sb.append((char) c);
            }
            Formatter formatter = new Formatter();
            formatter.format(sb.toString(), application.getHost(), application.getPort());
            writer.write(String.valueOf(formatter));
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
