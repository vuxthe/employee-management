package com.vux.security.controller;




import com.vux.security.payload.UserResponse;
import com.vux.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/public")
@RequiredArgsConstructor
public class PublicController {
    private final WebClient webClient;
    private final RestTemplate restTemplate;
    private final UserRepository userRepository;

    @GetMapping("/users")
    public List<UserResponse> getUsers() {
        return  userRepository.findUsers();
    }

//     test call api weather
    @GetMapping("/web-client")
//    @PreAuthorize("hasAuthority('ADMIN')")
    public Object getWeather(
            @RequestParam String q,
            @RequestParam(defaultValue = "no") String aqi
    ) {
        String path = "";
        return webClient.get()
                .uri(builder -> builder.path(path)
                        .queryParam("q", q)
                        .queryParam("aqi", aqi)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(WebClientResponseException.class, ex -> {
                    System.out.println("Error response: " + ex.getResponseBodyAsString());
                })
                .block();
    }


    @GetMapping("/rest-template")
    public Object getDataByRestTemplate(
            @RequestHeader("Authorization") String token
    ) {
        String url = "http://localhost:8888/api/v1/admin";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token.substring(7));
        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), Object.class).getBody();
    }
}
