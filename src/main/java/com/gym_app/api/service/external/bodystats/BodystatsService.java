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
public class BodystatsService {
    private static final Logger logger = LoggerFactory.getLogger(BodystatsService.class);
    @Autowired
    RestTemplate restTemplate;

    @Value("${bodystats.url}")
    private String bodystatsUrl;

    public void createBodyStatsRecords(BodyStatsDTO bodyStatsDTO) {
         try {
            restTemplate.postForEntity(bodystatsUrl + "/record", bodyStatsDTO, BodyStatsDTO.class);
        } catch (Exception exception) {
            logger.error(exception.getMessage());
        }
    }

    public List<BodyStatsDTO> getBodystatsByProfileId(UUID profileId) {
        String url = bodystatsUrl + "/history";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UUID> request = new HttpEntity<>(profileId, headers);
        ResponseEntity<BodyStatsDTO[]> response = restTemplate.exchange(url, HttpMethod.POST, request, BodyStatsDTO[].class);

        return Arrays.asList(Objects.requireNonNull(response.getBody()));
    }
}
