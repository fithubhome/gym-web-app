package com.gym_app.api.controller.external;

import com.gym_app.api.dto.external.membership.MembershipTypeExternal;
import com.gym_app.api.model.UserEntity;
import com.gym_app.api.service.MembershipTypeService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/membership")
public class MembershipController {

    @Autowired
    MembershipTypeService membershipTypeService;
    @Autowired
    private UserEntity userEntity;

    @GetMapping
    public ModelAndView getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return new ModelAndView("redirect:/auth/login");
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("membership/index");
        modelAndView.addObject("objects",
                new ArrayList<MembershipTypeExternal>() {{
                    add(new MembershipTypeExternal(UUID.randomUUID(), "Name 1", 1.0));
                    add(new MembershipTypeExternal(UUID.randomUUID(), "Name 2", 2.0));
                    add(new MembershipTypeExternal(UUID.randomUUID(), "Name 3", 3.0));
                }});
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


//    @GetMapping("/submitMembership")
//    public String greetingForm(Model model) {
//        model.addAttribute("greeting", new MembershipTypeExternal());
//        return "membership/index";
//    }

    @PostMapping("/submitMembership")
    public String submitMembership(@ModelAttribute Object mem) {
//        model.addAttribute("membershipTypeExternal", membershipTypeExternal);
//
//        System.out.println("membershipTypeExternal " + membershipTypeExternal.getName());
//        return "/membership/paymentStatus";

        System.out.println(mem);
        return "done";

    }


}
