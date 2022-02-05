package com.example.passport_client.service;

import com.example.passport_client.config.PassportServiceApiInfoHolder;
import com.example.passport_client.dto.PassportDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.internal.Utils;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@SpringBootTest
class PassportServiceImplTest implements WithAssertions {

    @Autowired
    private PassportService<PassportDto> service;

    @Autowired
    private PassportServiceApiInfoHolder infoHolder;

    private final ObjectMapper mapper = new ObjectMapper();

    private static MockWebServer WEB_SERVER;

    @BeforeAll
    static void beforeAll() throws IOException {
        WEB_SERVER = new MockWebServer();
        WEB_SERVER.start(9090);
    }

    @Test
    void save() throws InterruptedException {
        final Long expected = 1L;
        WEB_SERVER.enqueue(new MockResponse()
            .setBody(String.valueOf(expected))
            .setHeader("Content-Type", MediaType.APPLICATION_JSON.toString())
        );

        final Long result = service.save(PassportDto.builder().build());
        final RecordedRequest recordedRequest = WEB_SERVER.takeRequest();

        assertThat(result).isNotNull()
            .isEqualTo(expected);

        assertThat(recordedRequest.getMethod()).isNotNull()
            .isEqualToIgnoringCase("post");

        assertThat(recordedRequest.getPath())
            .isEqualTo(Utils.concat(
                    infoHolder.getEntry().getPassport(),
                    infoHolder.getEntry().getSave()
                )
            );
    }

    @Test
    void update() throws InterruptedException {
        final boolean expected = true;
        WEB_SERVER.enqueue(new MockResponse()
            .setBody(String.valueOf(expected))
            .setHeader("Content-Type", MediaType.APPLICATION_JSON.toString())
        );

        final boolean result = service.update(PassportDto.builder().id(1L).build());
        final RecordedRequest recordedRequest = WEB_SERVER.takeRequest();

        assertThat(result).isNotNull()
            .isTrue();

        assertThat(recordedRequest.getMethod()).isNotNull()
            .isEqualToIgnoringCase("put");

        assertThat(recordedRequest.getPath())
            .isEqualTo(Utils.concat(
                    infoHolder.getEntry().getPassport(),
                    infoHolder.getEntry().getUpdate(),
                    "?id=1"
                )
            );
    }

    @Test
    void delete() throws InterruptedException {
        final boolean expected = true;
        WEB_SERVER.enqueue(new MockResponse()
            .setBody(String.valueOf(expected))
            .setHeader("Content-Type", MediaType.APPLICATION_JSON.toString())
        );

        final boolean result = service.delete(1L);
        final RecordedRequest recordedRequest = WEB_SERVER.takeRequest();

        assertThat(result).isNotNull()
            .isTrue();

        assertThat(recordedRequest.getMethod()).isNotNull()
            .isEqualToIgnoringCase("delete");

        assertThat(recordedRequest.getPath())
            .isEqualTo(Utils.concat(
                    infoHolder.getEntry().getPassport(),
                    infoHolder.getEntry().getDelete(),
                    "?id=1"
                )
            );
    }

    @Test
    void findAll() throws JsonProcessingException, InterruptedException {
        List<PassportDto> expected = Collections.singletonList(
            PassportDto.builder()
                .id(2L)
                .serial(1234L)
                .name("Test")
                .build()
        );
        WEB_SERVER.enqueue(new MockResponse()
            .setBody(mapper.writeValueAsString(expected))
            .setHeader("Content-Type", MediaType.APPLICATION_JSON.toString())
        );

        final List<PassportDto> result = service.findAll();
        final RecordedRequest recordedRequest = WEB_SERVER.takeRequest();

        assertThat(result).isNotNull()
            .hasSize(1)
            .isEqualTo(expected);

        assertThat(recordedRequest.getMethod()).isNotNull()
            .isEqualToIgnoringCase("get");

        assertThat(recordedRequest.getPath()).isNotNull()
            .isEqualTo(Utils.concat())
    }

    @Test
    void findBySerial() {
    }

    @Test
    void findUnavailable() {
    }

    @Test
    void findReplaceable() {
    }

    @AfterEach
    void tearDown() {

    }
}