package com.gym_app.api.service;

import com.gym_app.api.exceptions.activities.EventNotFoundException;
import com.gym_app.api.model.activities.GymEventDto;
import com.gym_app.api.model.activities.ParticipantDto;
import com.gym_app.api.repository.ProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ActivitiesService {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    ProfileRepository profileRepository;

    @Value("${activities.url}")
    private String activitiesUrl;

    public List<GymEventDto> showEventsHistory() {
        log.info(activitiesUrl);
        ResponseEntity<List<GymEventDto>> response = restTemplate.exchange(activitiesUrl + "/event/all-events",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        return response.getBody();
    }

    public List<GymEventDto> showAvailableEvents() throws EventNotFoundException {

        List<GymEventDto> availableEvents = getAvailableEventsByTime();

        for (GymEventDto event : availableEvents) {

            ResponseEntity<List<ParticipantDto>> response = restTemplate.exchange(activitiesUrl + "/participant?eventId=" + event.getId(),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    });

            if (response.getBody() == null || response.getStatusCode().is4xxClientError()) {
                log.error("Event with id " + event.getId() + " does not exist, so there are no participants");
            } else if (response.getStatusCode().is5xxServerError()) {
                throw new InternalError("Something unexpected happen, please try again later");
            } else {
                event.setMaxParticipants(event.getMaxParticipants() - response.getBody().size());
            }
        }

        return availableEvents;

    }

    private List<GymEventDto> getAvailableEventsByTime() throws EventNotFoundException {

        ResponseEntity<List<GymEventDto>> response = restTemplate.exchange(activitiesUrl + "/event/all-events",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        if (response.getBody() == null || response.getStatusCode().is4xxClientError()) {
            throw new EventNotFoundException("No events were found, please try again later");
        } else if (response.getStatusCode().is5xxServerError()) {
            throw new InternalError("Something unexpected happen, please try again later");
        } else {
            return response.getBody()
                    .stream()
                    .filter(e -> e.getDate().after(Date.valueOf(LocalDate.now())) ||
                            (e.getDate().equals(Date.valueOf(LocalDate.now()))
                                    && e.getStartTime().after(Time.valueOf(LocalTime.now())))
                    )
                    .sorted(Comparator.comparing(GymEventDto::getDate).thenComparing(GymEventDto::getStartTime))
                    .toList();
        }
    }


    public void signUpForTheEvent(Long eventId, UUID userId) throws EventNotFoundException {
        log.info(String.valueOf(userId));

        GymEventDto event = getEventById(eventId);
        ParticipantDto participant = new ParticipantDto();
        participant.setEvent(event);
        participant.setProfileId(11L);
//        participant.setProfileId(profileRepository.findProfileByUserId(userId).getId());
        try {
            ResponseEntity<ParticipantDto> response = restTemplate.postForEntity(activitiesUrl + "/participant", participant, ParticipantDto.class);
        } catch (Exception exception) {
            log.info(exception.getMessage());
        }


    }

    private GymEventDto getEventById(Long eventId) throws EventNotFoundException {
        ResponseEntity<GymEventDto> response = restTemplate.exchange(activitiesUrl + "/event?eventId=" + eventId,
                HttpMethod.GET,
                null,
                GymEventDto.class);

        if (response.getBody() == null || response.getStatusCode().is4xxClientError()) {
            throw new EventNotFoundException("Something went wrong and you can not sign Up to this event, Sorry for the inconvenience");
        } else if (response.getStatusCode().is5xxServerError()) {
            throw new InternalError("Something unexpected happen, please try again later");
        } else {
            return response.getBody();
        }
    }
}
