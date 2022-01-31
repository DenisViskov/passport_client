package com.example.passport_client.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "passport-service-api")
public class PassportServiceApiInfoHolder {
    private CommonDetails common;

    private Entry entry;
}
