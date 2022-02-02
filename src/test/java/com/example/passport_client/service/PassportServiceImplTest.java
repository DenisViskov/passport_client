package com.example.passport_client.service;

import com.example.passport_client.dto.PassportDto;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.io.IOException;

@SpringBootTest
class PassportServiceImplTest implements WithAssertions {

    @Autowired
    private PassportService<PassportDto> service;

    private static MockWebServer WEB_SERVER;

    @BeforeAll
    static void beforeAll() throws IOException {
        WEB_SERVER = new MockWebServer();
        WEB_SERVER.start(9090);
    }

    @Test
    void save() {
        final Long expected = 1L;
        WEB_SERVER.enqueue(new MockResponse()
            .setBody(String.valueOf(expected))
            .setHeader("Content-Type", MediaType.APPLICATION_JSON.toString())
        );

        final Long result = service.save(PassportDto.builder().build());

        assertThat(result).isNotNull()
            .isEqualTo(expected);
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void findAll() {
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