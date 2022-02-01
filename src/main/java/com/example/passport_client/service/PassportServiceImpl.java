package com.example.passport_client.service;

import com.example.passport_client.config.PassportServiceApiInfoHolder;
import com.example.passport_client.dto.PassportDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Slf4j
@Service
public class PassportServiceImpl implements PassportService<PassportDto> {

    @Autowired
    private WebClient webClient;

    @Autowired
    private PassportServiceApiInfoHolder apiInfoHolder;

    @Override public Long save(final PassportDto passport) {
        return webClient.post()
            .uri(apiInfoHolder.getEntry().getSave())
            .bodyValue(passport)
            .retrieve()
            .bodyToMono(Long.class)
            .doOnError(err -> log.error("couldn't save passport: {}, message: {}", passport, err.getMessage()))
            .block();
    }

    @Override public boolean update(final PassportDto passport) {
        return webClient.put()
            .uri(uriBuilder -> uriBuilder
                .pathSegment(apiInfoHolder.getEntry().getUpdate())
                .query("id")
                .build(passport.getId())
            )
            .bodyValue(passport)
            .retrieve()
            .bodyToMono(Boolean.class)
            .doOnError(err -> log.error(
                "couldn't update passport with id: {}, message: {}",
                passport.getId(),
                err.getMessage()
            ))
            .blockOptional()
            .orElse(false);
    }

    @Override public boolean delete(final Long id) {
        return webClient.delete()
            .uri(uriBuilder -> uriBuilder
                .pathSegment(apiInfoHolder.getEntry().getDelete())
                .query("id")
                .build(id)
            )
            .retrieve()
            .bodyToMono(Boolean.class)
            .doOnError(err -> log.error("couldn't delete passport with id: {}, message: {}", id, err.getMessage()))
            .blockOptional()
            .orElse(false);
    }

    @Override public List<PassportDto> findAll() {
        return webClient.get()
            .uri(apiInfoHolder.getEntry().getFind())
            .retrieve()
            .bodyToMono(PassportDto.class)
            .doOnError(err -> log.error("couldn't get passports"));
    }

    @Override public List<PassportDto> findBySerial(final Long serial) {
        return null;
    }

    @Override public List<PassportDto> findUnavailable() {
        return null;
    }

    @Override public List<PassportDto> findReplaceable() {
        return null;
    }
}
