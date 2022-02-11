package com.example.passport_client.service;

import com.example.passport_client.config.PassportServiceApiInfoHolder;
import com.example.passport_client.dto.PassportDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.util.List;
import java.util.function.Function;

@Slf4j
@Service
public class PassportServiceImpl implements PassportService<PassportDto> {

    @Autowired
    private WebClient webClient;

    @Autowired
    private PassportServiceApiInfoHolder apiInfoHolder;

    @Override public Long save(final PassportDto passport) {
        log.debug("Try to save passport");
        return webClient.post()
            .uri(apiInfoHolder.getEntry().getSave())
            .bodyValue(passport)
            .retrieve()
            .bodyToMono(Long.class)
            .doOnError(err -> log.error("couldn't save passport: {}, message: {}", passport, err.getMessage()))
            .block();
    }

    @Override public boolean update(final PassportDto passport, final Long id) {
        log.debug("Try to update passport with id: {}", id);
        return webClient.put()
            .uri(uriBuilder -> uriBuilder
                .path(apiInfoHolder.getEntry().getUpdate())
                .queryParam("id", id)
                .build()
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
        log.debug("Try to delete passport by id: {}", id);
        return webClient.delete()
            .uri(uriBuilder -> uriBuilder
                .path(apiInfoHolder.getEntry().getDelete())
                .queryParam("id", id)
                .build()
            )
            .retrieve()
            .bodyToMono(Boolean.class)
            .doOnError(err -> log.error("couldn't delete passport with id: {}, message: {}", id, err.getMessage()))
            .blockOptional()
            .orElse(false);
    }

    @Override public List<PassportDto> findAll() {
        log.debug("Try to find all");
        return fetchSeveral(uriBuilder -> uriBuilder
            .path(apiInfoHolder.getEntry().getFind())
            .build()
        );
    }

    @Override public List<PassportDto> findBySerial(final Long serial) {
        log.debug("Try to find by serial: {}", serial);
        return fetchSeveral(uriBuilder -> uriBuilder
            .path(apiInfoHolder.getEntry().getFindBySerial())
            .queryParam("serial", serial)
            .build()
        );
    }

    @Override public List<PassportDto> findUnavailable() {
        log.debug("Try to find unavailable");
        return fetchSeveral(uriBuilder -> uriBuilder
            .path(apiInfoHolder.getEntry().getUnavailable())
            .build()
        );
    }

    @Override public List<PassportDto> findReplaceable() {
        log.debug("Try to find replaceable");
        return fetchSeveral(uriBuilder -> uriBuilder
            .path(apiInfoHolder.getEntry().getFindReplaceable())
            .build()
        );
    }

    private List<PassportDto> fetchSeveral(final Function<UriBuilder, URI> uriFunction) {
        return webClient.get()
            .uri(uriFunction)
            .retrieve()
            .bodyToFlux(PassportDto.class)
            .doOnError(err -> log.error("couldn't get passports, message: {}", err.getMessage()))
            .collectList()
            .block();
    }
}
