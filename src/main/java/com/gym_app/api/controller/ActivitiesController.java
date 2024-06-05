package com.gym_app.api.controller;

import com.gym_app.api.UserContext;
import com.gym_app.api.exceptions.activities.EventNotFoundException;
import com.gym_app.api.model.User;
import com.gym_app.api.model.activities.GymEventDto;
import com.gym_app.api.service.ActivitiesService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/activities")
public class ActivitiesController {
    @Autowired
    ActivitiesService activitiesService;

    @GetMapping("/events-history")
    public String showEventsHistory(Model model) {
        model.addAttribute("events", activitiesService.showEventsHistory());

        return "activities/eventsHistoryPage";
    }

    @GetMapping
    public String showAvailableEvents(Model model) throws EventNotFoundException {
        List<GymEventDto> availableEvents = activitiesService.showAvailableEvents();
        model.addAttribute("availableEvents", availableEvents);
        return "activities/availableEvents";
    }

    @GetMapping(value = "/signMeUp", params = "eventId")
    public String signUpForTheEvent(@RequestParam Long eventId, HttpSession session) throws EventNotFoundException {
        UUID sessionId = (UUID) session.getAttribute("sessionId");
        User currentUser = UserContext.getCurrentUser(sessionId);
        currentUser.getId();

        activitiesService.signUpForTheEvent(eventId, currentUser.getId());

        return "redirect:/activities";
    }

}
