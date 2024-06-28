package com.gym_app.api.config;

import com.gym_app.api.controller.external.BodyStatsController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.stream.Collectors;

@Configuration
public class RestTemplateConfig {
    private static final Logger logger = LoggerFactory.getLogger(BodyStatsController.class);

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(new LoggingInterceptor()));
        return new RestTemplate();
    }

    static class LoggingInterceptor implements ClientHttpRequestInterceptor {
        @Override
        public ClientHttpResponse intercept(org.springframework.http.HttpRequest request, byte[] body, org.springframework.http.client.ClientHttpRequestExecution execution) throws IOException, IOException {
            logRequest(request, body);
            ClientHttpResponse response = execution.execute(request, body);
            logResponse(response);
            return response;
        }

        private void logRequest(org.springframework.http.HttpRequest request, byte[] body) throws IOException {
            logger.info("URI: {}", request.getURI());
            logger.info("HTTP Method: {}", request.getMethod());
            logger.info("HTTP Headers: {}", request.getHeaders());
            logger.info("Request Body: {}", new String(body, StandardCharsets.UTF_8));
        }

        private void logResponse(ClientHttpResponse response) throws IOException {
            StringBuilder inputStringBuilder = new StringBuilder();
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getBody(), StandardCharsets.UTF_8))) {
                inputStringBuilder.append(bufferedReader.lines().collect(Collectors.joining("\n")));
            }
            logger.info("HTTP Status Code: {}", response.getStatusCode());
            logger.info("HTTP Status Text: {}", response.getStatusText());
            logger.info("HTTP Response Body: {}", inputStringBuilder.toString());
        }
    }
}
