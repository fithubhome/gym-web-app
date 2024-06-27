package com.gym_app.api.service.external.bodystats;

import com.gym_app.api.dto.bodystats.BodystatsDTO;
import com.gym_app.api.exceptions.bodystats.BodystatsNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class BodystatsService {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${bodystats.url}")
    private String bodystatsUrl;

    private static final Logger logger = LoggerFactory.getLogger(BodystatsService.class);

    public BodystatsDTO getLastBodystats(UUID profileId) throws BodystatsNotFoundException {
        ResponseEntity<BodystatsDTO> response = null;
        try {
            String url = bodystatsUrl;
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<UUID> request = new HttpEntity<>(profileId, headers);

            response = restTemplate.exchange(url, HttpMethod.POST, request, BodystatsDTO.class);
        } catch (Exception exception) {
            logger.error("Exception occurred while fetching the latest body stats: {}", exception.getMessage());
            throw new BodystatsNotFoundException("Exception occurred while fetching the latest body stats");
        }

        if (response.getBody() != null) {
            return response.getBody();
        } else {
            throw new BodystatsNotFoundException("Could not retrieve the latest body stats for profile ID: " + profileId);
        }
    }

    public List<BodystatsDTO> getBodystatsByProfileId(UUID profileId) {
        String url = bodystatsUrl + "/history";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UUID> request = new HttpEntity<>(profileId, headers);
        ResponseEntity<BodystatsDTO[]> response = restTemplate.exchange(url, HttpMethod.POST, request, BodystatsDTO[].class);

        return Arrays.asList(Objects.requireNonNull(response.getBody()));
    }

    public Optional<BodystatsDTO> getBodystatsById(UUID id) {
        String url = bodystatsUrl + "/" + id;
        BodystatsDTO bodyStats = restTemplate.getForObject(url, BodystatsDTO.class);

        return Optional.ofNullable(bodyStats);
    }

    public void saveBodystats(BodystatsDTO bodyStats) {
        String url = bodystatsUrl + "/new";
        try {
            restTemplate.postForEntity(url, bodyStats, BodystatsDTO.class);
            logger.info("Successfully saved body stats for profile ID: {}", bodyStats.getProfileId());
        } catch (Exception exception) {
            logger.error("Exception occurred while saving body stats: {}", exception.getMessage());
            throw new RuntimeException("Could not save body stats", exception);
        }
    }
}
