package io.hexlet.typoreporter.service;

import io.hexlet.typoreporter.handler.exception.OAuth2Exception;
import io.hexlet.typoreporter.service.dto.oauth2.PrivateEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
public class GithubService {
    @Autowired
    private RestTemplate restTemplate;
    private static final String GITHUB_API_USER_PRIVATE_EMAILS = "https://api.github.com/user/emails";

    public PrivateEmail getPrivateEmail(String accessToken) {
        if (accessToken.isBlank()) {
            throw new OAuth2Exception(HttpStatus.FORBIDDEN, ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN,
                "Access token is not valid. Token is: " + accessToken), null);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        var response = restTemplate.exchange(
            GITHUB_API_USER_PRIVATE_EMAILS, HttpMethod.GET,  requestEntity, PrivateEmail[].class);
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new OAuth2Exception(HttpStatus.UNAUTHORIZED, ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED,
                "HTTP code response is not 200. Code: " + response.getStatusCode()), null);
        }
        return Arrays.stream(response.getBody())
            .filter(PrivateEmail::isPrimary)
            .findFirst()
            .orElseThrow(() -> new RuntimeException("no available email"));
    }
}
