package com.gym_app.api.controller.external;

import com.gym_app.api.dto.external.membership.MembershipTypeExternal;
import com.gym_app.api.dto.external.membership.PaymentDto;
import com.gym_app.api.exceptions.external.membership.MembershipSelectionException;
import com.gym_app.api.model.UserEntity;
import com.gym_app.api.service.external.membership.MembershipTypeService;
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
//        modelAndView.addObject("membershipDto", new MembershipDto());
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

    @PostMapping("/submitMembership")
    public String submitMembership(@ModelAttribute PaymentDto paymentDto) {
        try {
            paymentService.validatePaymentData(paymentDto);

            System.out.println("MemmbershipTypeExternal id:  " + paymentDto.getSelectedMembershipId());
            System.out.println("MemmbershipTypeExternal name:  " + paymentDto.getPersonName());
            System.out.println("MemmbershipTypeExternal cardNr:  " + paymentDto.getCardNr());
            System.out.println("MemmbershipTypeExternal CVC:  " + paymentDto.getCvc());
            System.out.println("MemmbershipTypeExternal exp date:  " + paymentDto.getCardExpirationDate());
            System.out.println("MemmbershipTypeExternal Status:  " + paymentDto.getStatus());



            return "/membership/processingPayment.html";
        } catch ( MembershipSelectionException ex){
            System.out.println(ex.getMessage());
            return "/membership/error";
        }


    }

    @GetMapping("/error")
    public String getErrorPage() {

        return "membership/error";
    }


}
