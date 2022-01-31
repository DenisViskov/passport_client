package com.example.passport_client.config.webclient;

import com.example.passport_client.config.CommonDetails;
import com.example.passport_client.config.Entry;
import com.example.passport_client.config.PassportServiceApiInfoHolder;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfiguration {

    @Autowired
    private PassportServiceApiInfoHolder apiInfoHolder;

    private UriComponents uriComponents;

    @PostConstruct
    private void buildUriComponents() {
        final CommonDetails common = apiInfoHolder.getCommon();
        final Entry entries = apiInfoHolder.getEntry();

        uriComponents = UriComponentsBuilder.newInstance()
            .scheme(common.getProtocol())
            .host(common.getHost())
            .port(common.getPort())
            .path(entries.getPassport())
            .build();
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
            .baseUrl(uriComponents.toUriString())
            .defaultHeaders(httpHeaders -> {
                httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                httpHeaders.add("client", "passport_client");
            })
            .clientConnector(new ReactorClientHttpConnector(HttpClient.from(getTcpClient())))
            .build();
    }

    private TcpClient getTcpClient() {
        final int timeout = ((int) TimeUnit.SECONDS.toMillis(2));
        return TcpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeout)
            .doOnConnected(connection -> {
                connection.addHandlerLast(new ReadTimeoutHandler(timeout, TimeUnit.MILLISECONDS));
                connection.addHandlerLast(new WriteTimeoutHandler(timeout, TimeUnit.MILLISECONDS));
            });
    }
}
