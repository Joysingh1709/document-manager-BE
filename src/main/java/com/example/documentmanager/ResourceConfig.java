package com.example.documentmanager;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class ResourceConfig {

    @Value("classpath:serviceAccountKey.json")
    private Resource serviceAccountKey;

    @Value("${service.account.path}")
    private String serviceAccKeyPath;

    @Bean
    public String getServicePath() {
        return serviceAccKeyPath;
    }

    @Bean
    public Resource getServiceResource() {
        return serviceAccountKey;
    }

}
