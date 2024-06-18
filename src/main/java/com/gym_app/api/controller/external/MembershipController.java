package com.gym_app.api.controller.external;

import com.gym_app.api.dto.external.membership.MembershipTypeExternal;
import com.gym_app.api.service.MembershipTypeService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/membership")
public class MembershipController {

    @Autowired
    MembershipTypeService membershipTypeService;

    @GetMapping
    public ModelAndView getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return new ModelAndView("redirect:/auth/login");
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("membership/index");
        modelAndView.addObject("objects", membershipTypeService.getAllMembershipsType());
        return modelAndView;
    }

    @GetMapping("/membershipTypes")
    public ResponseEntity<List<MembershipTypeExternal>> getAllMembershipsType() {
        try {
            return ResponseEntity.status(200).body(membershipTypeService.getAllMembershipsType());
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(404).body(null);
        }
    }

}
