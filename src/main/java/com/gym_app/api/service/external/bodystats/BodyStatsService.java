package com.gym_app.api.service.external.bodystats;

import com.gym_app.api.dto.bodystats.BodyStatsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class BodyStatsService {
    private static final Logger logger = LoggerFactory.getLogger(BodyStatsService.class);
    @Autowired
    RestTemplate restTemplate;
    @Value("${bodystats.url}")
    private String bodystatsUrl;

    public void createBodyStatsRecords(BodyStatsDTO bodyStatsDTO) {
        try {
            restTemplate.postForEntity(bodystatsUrl + "/record", bodyStatsDTO, BodyStatsDTO.class);
        } catch (Exception exception) {
            logger.error("Failed to create BodyStats record: {}", exception.getMessage());
        }
    }

    public List<BodyStatsDTO> getBodystatsByProfileId(UUID profileId) {
        String url = bodystatsUrl + "/" + profileId;
        ResponseEntity<BodyStatsDTO[]> response = restTemplate.getForEntity(url, BodyStatsDTO[].class);

        return Arrays.asList(Objects.requireNonNull(response.getBody()));
    }
}
