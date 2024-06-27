package com.gym_app.api.controller.external;

import com.gym_app.api.dto.membership.membership.MembershipHistory;
import com.gym_app.api.dto.membership.membership.MembershipType;
import com.gym_app.api.dto.membership.payment.PaymentDTO;
import com.gym_app.api.exceptions.payment.PaymentSelectionException;
import com.gym_app.api.exceptions.payment.ProfileException;
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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
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
        List<MembershipHistory> membershipHistory = membershipHistoryService.getMembershipHistory();
        modelAndView.addObject("membershipHistory", membershipHistory);

        // This is the logic for Membership Active or not which impacts the use display:
        boolean isMembershipActive = membershipHistory.stream()
                .anyMatch(mbHist -> mbHist.getEndDate().isAfter(LocalDate.now()));
        modelAndView.addObject("isMembershipActive", isMembershipActive);

        return modelAndView;
    }

    @GetMapping("/membershipTypes")
    public ResponseEntity<List<MembershipType>> getAllMembershipsType() {
        try {
            return ResponseEntity.status(200).body(membershipTypeService.getAllMembershipsType());
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @PostMapping("/submitMembership")
    public String submitMembership(@ModelAttribute PaymentDTO paymentDTO) {
        try {
            paymentService.validatePaymentData(paymentDTO);

            String paymentResponse = paymentService.sendPaymentRequest(paymentDTO);
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

    @GetMapping("managemembershiptypes")
    public ModelAndView getManageMembershipTypePage() {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("membership/managemembershiptypes");
        List<MembershipType> membershipTypesList = membershipTypeService.getAllMembershipsType();
        modelAndView.addObject("membershipTypesList", membershipTypesList);

        return modelAndView;
    }

    @DeleteMapping("/deleteMembershipType")
    public String deleteMembershipType(@ModelAttribute MembershipType membershipType, RedirectAttributes redirectAttributes) {
        String BASE_URL = "http://localhost:8105";
        String BASE_URI = "/membershipType";
        RestTemplate CLIENT = new RestTemplate();
        CLIENT.delete(String.format("%s%s/%s", BASE_URL, BASE_URI, membershipType.getId()));
        redirectAttributes.addFlashAttribute("message", "Membership type removed successfully");
        return "redirect:/membership/managemembershiptypes";
    }

    //This method is for testing iwth POSTMAN; it can be deleted afterwards
    @Autowired
    MembershipHistoryService membershipHistoryService;

    @GetMapping("/getHistory")
    public ResponseEntity<List<MembershipHistory>> getMembershipHistory() {
        try {
            membershipHistoryService.getMembershipHistory().forEach(System.out::println);
            return ResponseEntity.status(200).body(membershipHistoryService.getMembershipHistory());
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(404).body(null);
        }
    }

}
