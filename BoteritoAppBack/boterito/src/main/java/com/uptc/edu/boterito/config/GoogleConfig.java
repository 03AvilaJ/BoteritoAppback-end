package com.uptc.edu.boterito.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GoogleConfig {

    @Value("${google.client.id}")
    private String googleClientId;

    public String getGoogleClientId() {
        return googleClientId;
    }
}

