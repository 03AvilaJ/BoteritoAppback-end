package com.uptc.edu.boterito.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    
    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dhv1dtdu8",
                "api_key", "785195293561712",
                "api_secret", "t1BRP3FERjajukzJmspP-atKdXs"
        ));
    }
}

