package com.example.springreverseproxy.services;

import com.example.springreverseproxy.configs.NginxConfig;
import com.example.springreverseproxy.exceptions.NginxConfigurationException;
import com.example.springreverseproxy.models.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Formatter;
import java.util.HashSet;

@Service
public class NginxService {

    private static final String NGINX_RELOAD = "service nginx reload";
    private static final String IP_BLOCKED_ERROR = "IP \"%s\" in stop list! Please, remove it or contact your system administrator!";
    private static final String RELOAD_NGINX_ERROR = "Can't reload Nginx Server. Please, try later or contact yor system administrator.";
    private static final String TEMPLATE_NOT_EXISTS_ERROR = "It seems that file \"nginx.conf\" not exists. Please, check it or contact your system administrator.";
    private static final String TEMPLATE_ACCESS_DENIED_ERROR = "It seems that file \"nginx.conf\" exists, but can't read. Please, fix it or contact your system administrator.";
    private static final String SAVE_CONFIG_ERROR = "Can't write config to \"nginx.conf\". Please, contact your system administrator.";
    private final NginxConfig nginxConfig;

    @Autowired
    public NginxService(NginxConfig nginxConfig) {
        this.nginxConfig = nginxConfig;
    }

    public void setHostTo(Application application) throws NginxConfigurationException {
        if (getBlockList().contains(application.getHost())) {
            throw new NginxConfigurationException(String.format(IP_BLOCKED_ERROR, application.getHost()));
        }
        String template = readTemplate();
        String config = formatTemplateToConfig(template, application);
        saveConfig(config);
        reloadNginx();
    }

    public HashSet<String> getBlockList() {
        return new HashSet(nginxConfig.getIpBlockList());
    }

    public String getRedirectHost() {
        return nginxConfig.getRedirectHost();
    }

    private void reloadNginx() {
        try {
            Process proc = Runtime.getRuntime().exec(NGINX_RELOAD);
            proc.waitFor();
            proc.destroy();
        } catch (IOException | InterruptedException e) {
            throw new NginxConfigurationException(RELOAD_NGINX_ERROR, e);
        }
    }

    private String readTemplate() {
        try (FileReader reader = new FileReader(nginxConfig.getTemplatePath())) {
            StringBuilder sb = new StringBuilder();
            int c;
            while ((c = reader.read()) != -1) {
                sb.append((char) c);
            }
            return sb.toString();
        } catch (FileNotFoundException e) {
            throw new NginxConfigurationException(TEMPLATE_NOT_EXISTS_ERROR, e);
        } catch (IOException e) {
            throw new RuntimeException(TEMPLATE_ACCESS_DENIED_ERROR, e);
        }
    }

    private String formatTemplateToConfig(String template, Application application) {
        Formatter formatter = new Formatter();
        formatter.format(template, application.getHost(), application.getPort());
        return String.valueOf(formatter);
    }

    private void saveConfig(String config) {
        try (FileWriter writer = new FileWriter(nginxConfig.getConfigPath())) {
            writer.write(config);
            writer.flush();
        } catch (IOException e) {
            throw new NginxConfigurationException(SAVE_CONFIG_ERROR, e);
        }
    }
}
