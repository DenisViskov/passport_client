package com.example.passport_client.api;

import com.example.passport_client.StubBuilder;
import com.example.passport_client.config.PassportServiceApiInfoHolder;
import com.example.passport_client.dto.PassportDto;
import com.example.passport_client.service.PassportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PassportClientApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PassportService<PassportDto> service;

    @Autowired
    private PassportServiceApiInfoHolder apiHolder;

    private final ObjectMapper mapper = new ObjectMapper();

    private static final String ROOT_PATH = "/client";

    @Test
    void save() throws Exception {
        final Long result = 1L;
        final PassportDto toSave = StubBuilder.builder().build();
        final String expectedContent = result.toString();
        when(service.save(any())).thenReturn(result);

        assertResponse(
            post(ROOT_PATH.concat(apiHolder.getEntry().getSave()))
                .content(mapper.writeValueAsString(toSave)),
            expectedContent
        );
    }

    @Test
    void update() throws Exception {
        final boolean result = true;
        final PassportDto toUpdate = StubBuilder.builder().build();
        final String expectedContent = String.valueOf(result);
        when(service.update(any(), anyLong())).thenReturn(result);

        assertResponse(
            put(ROOT_PATH.concat(apiHolder.getEntry().getUpdate()))
                .queryParam("id", "1")
                .content(mapper.writeValueAsString(toUpdate)),
            expectedContent
        );
    }

    @Test
    void deleteTest() throws Exception {
        final boolean result = true;
        final String expectedContent = String.valueOf(result);
        when(service.delete(anyLong())).thenReturn(result);

        assertResponse(
            delete(ROOT_PATH.concat(apiHolder.getEntry().getDelete()))
                .queryParam("id", "1"),
            expectedContent
        );
    }

    @Test
    void find() throws Exception {
        final List<PassportDto> expectedList = Collections.singletonList(StubBuilder.builder().build());
        final String expectedContent = mapper.writeValueAsString(expectedList);
        when(service.findAll()).thenReturn(expectedList);

        assertResponse(
            get(ROOT_PATH.concat(apiHolder.getEntry().getFind())),
            expectedContent
        );
    }

    @Test
    void findBySerial() throws Exception {
        final Long serial = 1234L;
        final List<PassportDto> expectedList = Collections.singletonList(
            StubBuilder.builder()
                .serial(serial)
                .build()
        );
        final String expectedContent = mapper.writeValueAsString(expectedList);
        when(service.findBySerial(anyLong())).thenReturn(expectedList);

        assertResponse(
            get(ROOT_PATH.concat(apiHolder.getEntry().getFindBySerial()))
                .queryParam("serial", serial.toString()),
            expectedContent
        );
    }

    @Test
    void unavailable() throws Exception {
        final List<PassportDto> expectedList = Collections.singletonList(
            StubBuilder.builder()
                .expiredDate(LocalDate.now().minusMonths(1))
                .build()
        );
        final String expectedContent = mapper.writeValueAsString(expectedList);
        when(service.findUnavailable()).thenReturn(expectedList);

        assertResponse(
            get(ROOT_PATH.concat(apiHolder.getEntry().getUnavailable())),
            expectedContent
        );
    }

    @Test
    void findReplaceable() throws Exception {
        final List<PassportDto> expectedList = Collections.singletonList(
            StubBuilder.builder()
                .expiredDate(LocalDate.now().plusMonths(1))
                .build()
        );
        final String expectedContent = mapper.writeValueAsString(expectedList);
        when(service.findReplaceable()).thenReturn(expectedList);

        assertResponse(
            get(ROOT_PATH.concat(apiHolder.getEntry().getFindReplaceable())),
            expectedContent
        );
    }

    private void assertResponse(
        final MockHttpServletRequestBuilder reqBuilder,
        final String expectedContent
    ) throws Exception {
        mockMvc.perform(reqBuilder.contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().string(expectedContent));
    }
}