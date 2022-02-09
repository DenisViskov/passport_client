package com.example.passport_client.integration;

import com.example.passport_client.config.CommonDetails;
import com.example.passport_client.config.Entry;
import com.example.passport_client.config.PassportServiceApiInfoHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;

@TestConfiguration
public class WebTestClientConfig {

    @Autowired
    private PassportServiceApiInfoHolder apiInfoHolder;

    private UriComponents uriComponents;

    @PostConstruct
    private void initialize() {
        final CommonDetails common = apiInfoHolder.getCommon();
        final Entry entry = apiInfoHolder.getEntry();

        uriComponents = UriComponentsBuilder.newInstance()
            .scheme(common.getProtocol())
            .host(common.getHost())
            .port(common.getPort())
            .path(entry.getPassport())
            .build();
    }

    @Bean
    public WebTestClient webTestClient() {
        return WebTestClient
            .bindToServer()
            .baseUrl(uriComponents.toUriString())
            .build();
    }
}
