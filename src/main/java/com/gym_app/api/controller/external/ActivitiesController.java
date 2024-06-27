package com.gym_app.api.controller.external;

import com.gym_app.api.dto.activities.GymEventDto;
import com.gym_app.api.exceptions.activities.EventNotFoundException;
import com.gym_app.api.service.external.activities.ActivitiesService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/activity")
@Slf4j
public class ActivitiesController {
    @Autowired
    ActivitiesService activitiesService;

    @GetMapping()
    public String showActivitiesPage() {
        return "activities/activitiesMainPage";
    }

    @GetMapping("/events-history")
    public String showEventsHistory(Model model) {
        try {
            model.addAttribute("events", activitiesService.showEventsHistory());
        } catch (HttpClientErrorException.NotFound exception) {
            model.addAttribute("errorMessage", exception.getMessage());
            return "activities/eventNotFoundError";
        }

        return "activities/eventsHistoryPage";
    }

    @GetMapping("available-events")
    public String showAvailableEvents(Model model, HttpServletRequest httpServletRequest) {

        Map<GymEventDto, Boolean> availableEvents = activitiesService.showAvailableEvents(httpServletRequest.getUserPrincipal().getName());

        model.addAttribute("availableEvents", availableEvents);
        return "activities/availableEvents";
    }

    @GetMapping(value = "/signMeUp", params = "eventId")
    public String signUpForTheEvent(@RequestParam Long eventId, HttpServletRequest httpServletRequest, Model model) throws EventNotFoundException {

        activitiesService.signUpForTheEvent(eventId, httpServletRequest.getUserPrincipal().getName());

        Map<GymEventDto, Boolean> availableEvents = activitiesService.showAvailableEvents(httpServletRequest.getUserPrincipal().getName());
        model.addAttribute("availableEvents", availableEvents);
        return "activities/availableEvents";
    }

    @GetMapping(value = "/withdraw", params = "eventId")
    public String withdrawFromEvent(@RequestParam Long eventId, HttpServletRequest httpServletRequest, Model model) throws EventNotFoundException {

        activitiesService.withdrawFromEvent(eventId, httpServletRequest.getUserPrincipal().getName());

        Map<GymEventDto, Boolean> availableEvents = activitiesService.showAvailableEvents(httpServletRequest.getUserPrincipal().getName());

        model.addAttribute("availableEvents", availableEvents);
        return "activities/availableEvents";
    }

    @GetMapping("/create-event-form")
    public String createEventForm(Model model, GymEventDto gymEventDto) {
        model.addAttribute("events", activitiesService.showEventsHistory());
        return "activities/createEventForm";
    }

    @PostMapping("/create-event")
    public String createEvent(@ModelAttribute GymEventDto gymEventDto, HttpServletRequest httpServletRequest, Model model) throws IOException {
        activitiesService.createEvent(gymEventDto, httpServletRequest.getUserPrincipal().getName());
        try {
            activitiesService.createEvent(gymEventDto, httpServletRequest.getUserPrincipal().getName());
        } catch (HttpClientErrorException.Conflict exception) {
            model.addAttribute("errorMessage", exception.getMessage());
            return "activities/createEventOverLappingError";
        }

        model.addAttribute("events", activitiesService.showEventsHistory());
        return "activities/createEventForm";
    }

    @GetMapping(path = "update-event-page")
    public String updateEventPage(Model model) {
        try {
            model.addAttribute("events", activitiesService.showEventsHistory());
        } catch (HttpClientErrorException.NotFound exception) {
            model.addAttribute("errorMessage", exception.getMessage());
            return "activities/eventNotFoundError";
        }

        return "activities/updateEventPage";
    }

    @GetMapping(path = "update-event-form", params = "eventId")
    public String updateEventForm(@RequestParam Long eventId, Model model) {
        try {
            model.addAttribute("eventToUpdate", activitiesService.getEventById(eventId));
        } catch (EventNotFoundException exception) {
            model.addAttribute("errorMessage", exception.getMessage());
            return "activities/eventNotFoundError";
        }
        return "activities/updateEventForm";
    }

    @PostMapping(path = "update-event")
    public String updateEvent(@ModelAttribute GymEventDto gymEventDto, Model model, HttpServletRequest httpServletRequest) throws EventNotFoundException {
        try {
            activitiesService.updateEvent(gymEventDto, httpServletRequest.getUserPrincipal().getName());
        } catch (HttpClientErrorException.Conflict exception) {
            model.addAttribute("errorMessage", exception.getMessage());
            return "activities/updateEventOverLappingError";
        }


        model.addAttribute("events", activitiesService.showEventsHistory());
        return "redirect:/activity/create-event-form";
    }

    @GetMapping(path = "delete-event", params = "eventId")
    public String deleteEvent(@RequestParam Long eventId, Model model, GymEventDto gymEventDto) {

        activitiesService.deleteEvent(eventId);

        model.addAttribute("events", activitiesService.showEventsHistory());
        return "activities/createEventForm";
    }

}
