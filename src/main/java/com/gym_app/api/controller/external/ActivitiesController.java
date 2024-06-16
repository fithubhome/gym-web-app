package com.gym_app.api.controller.external;

import com.gym_app.api.exceptions.activities.EventNotFoundException;
import com.gym_app.api.dto.activities.EventsAndParticipantsDto;
import com.gym_app.api.dto.activities.GymEventDto;
import com.gym_app.api.dto.activities.ParticipantDto;
import com.gym_app.api.service.ActivitiesService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/activities")
@Slf4j
public class ActivitiesController {
    @Autowired
    ActivitiesService activitiesService;

    @GetMapping("/events-history")
    public String showEventsHistory(Model model) {
        model.addAttribute("events", activitiesService.showEventsHistory());

        return "activities/eventsHistoryPage";
    }

    @GetMapping
    public String showAvailableEvents(Model model, HttpServletRequest httpServletRequest) {
        Map<GymEventDto, Boolean> availableEvents = new LinkedHashMap<>();

        EventsAndParticipantsDto eventsAndParticipants = activitiesService.showAvailableEvents(httpServletRequest.getUserPrincipal().getName());

        for (GymEventDto event : eventsAndParticipants.getEvents()) {
            availableEvents.put(event, false);
            for (ParticipantDto participant : eventsAndParticipants.getParticipants()) {
                if (Objects.equals(event.getId(), participant.getEvent().getId())) {
                    availableEvents.put(event, true);
                }
            }
        }

        model.addAttribute("availableEvents", availableEvents);
        return "activities/availableEvents";
    }

    @GetMapping(value = "/signMeUp", params = "eventId")
    public String signUpForTheEvent(@RequestParam Long eventId, HttpServletRequest httpServletRequest) throws EventNotFoundException {

        activitiesService.signUpForTheEvent(eventId, httpServletRequest.getUserPrincipal().getName());

        return "redirect:/activities";
    }

    @GetMapping(value = "/withdraw", params = "eventId")
    public String withdrawFromEvent(@RequestParam Long eventId, HttpServletRequest httpServletRequest) throws EventNotFoundException {

        activitiesService.withdrawFromEvent(eventId, httpServletRequest.getUserPrincipal().getName());

        return "redirect:/activities";
    }

    @ExceptionHandler(EventNotFoundException.class)
    public String handleEventNotFoundException(EventNotFoundException exception) {
        log.error(exception.getMessage());
        return "activities/eventNotFoundError";
    }
}
