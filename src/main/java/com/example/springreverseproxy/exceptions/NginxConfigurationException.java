package com.example.springreverseproxy.exceptions;

public class NginxConfigurationException extends RuntimeException {
    public NginxConfigurationException() {
        super();
    }

    public NginxConfigurationException(String message) {
        super(message);
    }

    public NginxConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public NginxConfigurationException(Throwable cause) {
        super(cause);
    }
}
