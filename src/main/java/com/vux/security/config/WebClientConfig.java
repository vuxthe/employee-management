package com.vux.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Profile("dev")
@Configuration
public class WebClientConfig {
    @Value("${application.web-client.api-key}")
    private String key;
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("http://api.weatherapi.com/v1/current.json?key="+key)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
