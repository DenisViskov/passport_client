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
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

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
    void save() throws InterruptedException, JsonProcessingException {
        final Long expected = 1L;

        final var result = exchange(
            expected,
            service -> service.save(PassportDto.builder().serial(1234L).build())
        );

        assertThat(result).isNotNull()
            .isEqualTo(expected);

        assertRequest(
            HttpMethod.POST,
            Utils.concat(
                infoHolder.getEntry().getPassport(),
                infoHolder.getEntry().getSave()
            )
        );
    }

    @Test
    void update() throws InterruptedException, JsonProcessingException {
        final boolean expected = true;
        final Long expectedId = 1L;

        final var result = exchange(
            expected,
            service -> service.update(PassportDto.builder().id(expectedId).build())
        );

        assertThat(result).isNotNull()
            .isTrue();

        assertRequest(
            HttpMethod.PUT,
            Utils.concat(
                infoHolder.getEntry().getPassport(),
                infoHolder.getEntry().getUpdate(),
                "?id=" + expectedId
            )
        );
    }

    @Test
    void delete() throws InterruptedException, JsonProcessingException {
        final boolean expected = true;
        final Long expectedId = 1L;

        final var result = exchange(
            expected,
            service -> service.delete(expectedId)
        );

        assertThat(result).isNotNull()
            .isTrue();

        assertRequest(
            HttpMethod.DELETE,
            Utils.concat(
                infoHolder.getEntry().getPassport(),
                infoHolder.getEntry().getDelete(),
                "?id=" + expectedId
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

        final var result = exchange(expected, PassportService::findAll);

        assertThat(result).isNotNull()
            .hasSize(1)
            .isEqualTo(expected);

        assertRequest(
            HttpMethod.GET,
            Utils.concat(
                infoHolder.getEntry().getPassport(),
                infoHolder.getEntry().getFind()
            )
        );
    }

    @Test
    void findBySerial() throws JsonProcessingException, InterruptedException {
        final Long serial = 1234L;
        List<PassportDto> expected = Collections.singletonList(
            PassportDto.builder()
                .id(2L)
                .serial(serial)
                .name("Test")
                .build()
        );

        final var result = exchange(
            expected,
            service -> service.findBySerial(serial)
        );

        assertThat(result).isNotNull()
            .hasSize(1)
            .isEqualTo(expected);

        assertRequest(
            HttpMethod.GET,
            Utils.concat(
                infoHolder.getEntry().getPassport(),
                infoHolder.getEntry().getFindBySerial(),
                "?serial=" + serial
            )
        );
    }

    @Test
    void findUnavailable() throws JsonProcessingException, InterruptedException {
        List<PassportDto> expected = Collections.singletonList(
            PassportDto.builder()
                .id(2L)
                .serial(1234L)
                .name("Test")
                .build()
        );

        final var result = exchange(expected, PassportService::findUnavailable);

        assertThat(result).isNotNull()
            .hasSize(1)
            .isEqualTo(expected);

        assertRequest(
            HttpMethod.GET,
            Utils.concat(
                infoHolder.getEntry().getPassport(),
                infoHolder.getEntry().getUnavailable()
            )
        );
    }

    @Test
    void findReplaceable() throws JsonProcessingException, InterruptedException {
        List<PassportDto> expected = Collections.singletonList(
            PassportDto.builder()
                .id(2L)
                .serial(1234L)
                .name("Test")
                .build()
        );

        final var result = exchange(expected, PassportService::findReplaceable);

        assertThat(result).isNotNull()
            .hasSize(1)
            .isEqualTo(expected);

        assertRequest(
            HttpMethod.GET,
            Utils.concat(
                infoHolder.getEntry().getPassport(),
                infoHolder.getEntry().getFindReplaceable()
            )
        );
    }

    private <T, V> T exchange(
        final V expected,
        final Function<PassportService<PassportDto>, T> action
    ) throws JsonProcessingException {
        WEB_SERVER.enqueue(
            new MockResponse()
                .setBody(mapper.writeValueAsString(expected))
                .setHeader("Content-Type", MediaType.APPLICATION_JSON.toString())
        );

        return action.apply(service);
    }

    private void assertRequest(final HttpMethod expectedMethod, final String path) throws InterruptedException {
        final RecordedRequest recordedRequest = WEB_SERVER.takeRequest();

        assertThat(recordedRequest.getMethod()).isNotNull()
            .isEqualTo(expectedMethod.name());

        assertThat(recordedRequest.getPath()).isNotNull()
            .isEqualTo(path);
    }

    @AfterAll
    static void afterAll() throws IOException {
        WEB_SERVER.close();
    }

    private enum HttpMethod {
        GET,
        POST,
        PUT,
        DELETE
    }
}