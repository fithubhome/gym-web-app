package com.gym_app.api.controller.external;

import com.gym_app.api.dto.external.membershipapi.history.get.MembershipHistoryExternal;
import com.gym_app.api.dto.external.membershipapi.type.get.MembershipTypeExternal;
import com.gym_app.api.dto.external.paymentapi.put.PaymentResponseDto;
import com.gym_app.api.exceptions.external.payment.PaymentSelectionException;
import com.gym_app.api.exceptions.external.payment.ProfileException;
import com.gym_app.api.model.UserEntity;
import com.gym_app.api.service.external.membership.history.MembershipHistoryService;
import com.gym_app.api.service.external.membership.type.MembershipTypeService;
import com.gym_app.api.service.external.payment.PaymentService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/membership")
public class MembershipController {

    @Autowired
    MembershipTypeService membershipTypeService;
    @Autowired
    private UserEntity userEntity;

    @Autowired
    PaymentService paymentService;

    @GetMapping
    public ModelAndView getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return new ModelAndView("redirect:/auth/login");
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("membership/index");
        modelAndView.addObject("objects", membershipTypeService.getAllMembershipsType());

        // Adaug aici metoday pe care o fac de MembershipHistory

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

    @PostMapping("/submitMembership")
    public String submitMembership(@ModelAttribute PaymentResponseDto paymentResponseDto) {
        try {
            paymentService.validatePaymentData(paymentResponseDto);

            String paymentResponse = paymentService.sendPaymentRequest(paymentResponseDto);
            if ("PAID".equals(paymentResponse)) {
                return "membership/paymentSuccessful";
            } else {
                return "membership/error";
            }

        } catch (PaymentSelectionException ex) {
            System.out.println(ex.getMessage());
            return "/membership/error";
        } catch (ProfileException ex) {
            System.out.println(ex.getMessage());
            return "/error";
        }
    }

    //This method is for testing iwth POSTMAN; it can be deleted afterwards
    @Autowired
    MembershipHistoryService membershipHistoryService;
    @GetMapping("/getHistory")
    public ResponseEntity<List<MembershipHistoryExternal>> getMembershipHistory() {
        try {
            membershipHistoryService.getMembershipHistory().forEach(System.out::println);
            return ResponseEntity.status(200).body(membershipHistoryService.getMembershipHistory());
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(404).body(null);
        }
    }

}
