package com.example.passport_client.integration;

import com.example.passport_client.StubBuilder;
import com.example.passport_client.config.PassportServiceApiInfoHolder;
import com.example.passport_client.dto.PassportDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest
@Import(value = WebTestClientConfig.class)
public class PassportServiceIntegrationTest {

    @Autowired
    private WebTestClient client;

    @Autowired
    private PassportServiceApiInfoHolder apiInfoHolder;

    @Test
    void save() {
        final var id = createInstance(StubBuilder.builder().build());
        deleteInstance(id);
    }

    @Test
    void update() {
        final var onUpdate = StubBuilder.builder().build();
        final var id = createInstance(onUpdate);
        onUpdate.setName("updatedName");

        client.put()
            .uri(uriBuilder -> uriBuilder
                .path(apiInfoHolder.getEntry().getUpdate())
                .queryParam("id", id)
                .build()
            )
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(onUpdate)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(Boolean.class)
            .isEqualTo(true);

        deleteInstance(id);
    }

    private Long createInstance(final PassportDto passportDto) {
        return client.post()
            .uri(apiInfoHolder.getEntry().getSave())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(passportDto)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(Long.class)
            .returnResult()
            .getResponseBody();
    }

    private void deleteInstance(final Long id) {
        client.delete()
            .uri(uriBuilder -> uriBuilder
                .path(apiInfoHolder.getEntry().getDelete())
                .queryParam("id", id)
                .build()
            )
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(Boolean.class)
            .isEqualTo(true);
    }
}
