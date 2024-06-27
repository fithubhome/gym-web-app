package com.gym_app.api.service.external.activities;

import com.gym_app.api.dto.activities.GymEventDto;
import com.gym_app.api.dto.activities.ParticipantDto;
import com.gym_app.api.exceptions.activities.EventNotFoundException;
import com.gym_app.api.model.UserEntity;
import com.gym_app.api.repository.ProfileRepository;
import com.gym_app.api.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class ActivitiesService {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    UserRepository userRepository;

    @Value("${activities.url}")
    private String activitiesUrl;

    public List<GymEventDto> showEventsHistory() {
        ResponseEntity<List<GymEventDto>> response = restTemplate.exchange(activitiesUrl + "/event/all-events",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        return response.getBody()
                .stream()
                .sorted(Comparator.comparing(GymEventDto::getDate).thenComparing(GymEventDto::getStartTime))
                .toList();
    }

    public Map<GymEventDto, Boolean> showAvailableEvents(String email) {
        UUID profileIdOfCurrentUser = getProfileIdOfCurrentUser(email);
        List<GymEventDto> availableEvents = getAvailableEventsByTime();
        List<ParticipantDto> participants = new ArrayList<>();
        Map<GymEventDto, Boolean> eventAndIsSignedUpForEvent = new LinkedHashMap<>();

        try {
            for (GymEventDto event : availableEvents) {

                ResponseEntity<List<ParticipantDto>> response = restTemplate.exchange(activitiesUrl + "/participant?eventId=" + event.getId(),
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        });

                event.setMaxParticipants(event.getMaxParticipants() - response.getBody().size());
                participants.addAll(response.getBody()
                        .stream()
                        .filter(p -> Objects.equals(p.getProfileId(), profileIdOfCurrentUser))
                        .toList());

                eventAndIsSignedUpForEvent.put(event, false);
                for (ParticipantDto participant : participants) {
                    if (Objects.equals(event.getId(), participant.getEvent().getId())) {
                        eventAndIsSignedUpForEvent.put(event, true);
                    }
                }
            }

        } catch (Exception exception) {
            log.error(exception.getMessage());
        }

        return eventAndIsSignedUpForEvent;

    }

    private UUID getProfileIdOfCurrentUser(String email) {
        Optional<UserEntity> currentUser = userRepository.findByEmail(email);
        return profileRepository.findProfileByUserId(currentUser.get().getId()).getId();
    }

    private List<GymEventDto> getAvailableEventsByTime() {
        ResponseEntity<List<GymEventDto>> response = null;
        try {
            response = restTemplate.exchange(activitiesUrl + "/event/all-events",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    });


        } catch (Exception exception) {
            log.error(exception.getMessage());
        }


        return response.getBody()
                .stream()
                .filter(e -> e.getDate().after(Date.valueOf(LocalDate.now())) ||
                        (e.getDate().equals(Date.valueOf(LocalDate.now()))
                                && e.getStartTime().after(Time.valueOf(LocalTime.now())))
                )
                .sorted(Comparator.comparing(GymEventDto::getDate).thenComparing(GymEventDto::getStartTime))
                .toList();
    }


    public void signUpForTheEvent(Long eventId, String email) throws EventNotFoundException {
        GymEventDto event = getEventById(eventId);
        ParticipantDto participant = new ParticipantDto();
        participant.setEvent(event);
        participant.setProfileId(getProfileIdOfCurrentUser(email));
        try {
            restTemplate.postForObject(activitiesUrl + "/participant", participant, ParticipantDto.class);
        } catch (Exception exception) {
            log.info(exception.getMessage());
        }
    }

    public GymEventDto getEventById(Long eventId) throws EventNotFoundException {
        ResponseEntity<GymEventDto> response = null;
        try {
            response = restTemplate.exchange(activitiesUrl + "/event?eventId=" + eventId,
                HttpMethod.GET,
                null,
                GymEventDto.class);
        } catch (Exception exception) {
            log.info(exception.getMessage());
        }

        if (response.getBody() != null) {
            return response.getBody();
        } else {
            throw new EventNotFoundException("Something went wrong and you can not sign Up to this event, Sorry for the inconvenience");
        }
    }

    public void withdrawFromEvent(Long eventId, String email) throws EventNotFoundException {
        GymEventDto event = getEventById(eventId);
        ParticipantDto participant = new ParticipantDto();
        participant.setEvent(event);
        participant.setProfileId(getProfileIdOfCurrentUser(email));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ParticipantDto> requestEntity = new HttpEntity<>(participant, headers);

        try {
            restTemplate.exchange(activitiesUrl + "/participant", HttpMethod.DELETE, requestEntity, Void.class);
        } catch (Exception exception) {
            log.info(exception.getMessage());
        }
    }

    public void createEvent(GymEventDto gymEventDto, String email) {

        gymEventDto.setOrganizerId(getProfileIdOfCurrentUser(email));

        try {
            restTemplate.postForEntity(activitiesUrl + "/event", gymEventDto, GymEventDto.class);
        } catch (HttpClientErrorException.Conflict exception) {
            throw exception;
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }

    }

    public void updateEvent(GymEventDto gymEventDto, String email) {

        gymEventDto.setOrganizerId(getProfileIdOfCurrentUser(email));

        try {
            restTemplate.put(activitiesUrl + "/event", gymEventDto, GymEventDto.class);
        } catch (HttpClientErrorException.Conflict exception) {
            throw exception;
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }
    }

    public void deleteEvent(Long eventId) {

        try {
            restTemplate.delete(activitiesUrl + "/event?eventId=" + eventId);
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }

    }
}
