package com.gym_app.api.service.external.payment;

import com.gym_app.api.dto.membership.membership.MembershipType;
import com.gym_app.api.dto.membership.payment.PaymentDTO;
import com.gym_app.api.exceptions.payment.PaymentSelectionException;
import com.gym_app.api.exceptions.payment.ProfileException;
import com.gym_app.api.model.Profile;
import com.gym_app.api.model.UserEntity;
import com.gym_app.api.security.CustomUserDetails;
import com.gym_app.api.service.ProfileService;
import com.gym_app.api.service.UserService;
import com.gym_app.api.service.external.membership.type.MembershipTypeService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.List;

@Getter
@Service
public class PaymentService {
    @Autowired
    MembershipTypeService membershipTypeService;
    @Autowired
    UserEntity userEntity;
    @Autowired
    ProfileService profileService;
    @Autowired
    UserService userService;
    @Autowired
    PaymentServiceClient paymentServiceClient;

    public void validatePaymentData(PaymentDTO paymentDTO) throws PaymentSelectionException, ProfileException {
        validateSelectedMembership(paymentDTO);
        validatePersonName(paymentDTO);
        validateCardNr(paymentDTO);
        validateCardExpirationDate(paymentDTO);
        validateCvc(paymentDTO);
        validateProfileId();
    }


    private void validateSelectedMembership(PaymentDTO paymentDTO) throws PaymentSelectionException {
        List<MembershipType> mbTypeList = membershipTypeService.getAllMembershipsType();
        if (paymentDTO.getSelectedMembershipId() == null || mbTypeList.stream().noneMatch(mbType -> paymentDTO.getSelectedMembershipId().equals(mbType.getId()))) {
            throw new PaymentSelectionException(PaymentDTO.class.getSimpleName(), paymentDTO.getSelectedMembershipId().toString());
        }

    }

    private void validatePersonName(PaymentDTO paymentDTO) throws PaymentSelectionException {
        if (paymentDTO.getPersonName() == null || paymentDTO.getPersonName().matches(".*\\d.*")) {
            throw new PaymentSelectionException(PaymentDTO.class.getSimpleName(), paymentDTO.getPersonName());
        }
    }

    private void validateCardNr(PaymentDTO paymentDTO) throws PaymentSelectionException {
        if (paymentDTO.getCardNr() == null || !paymentDTO.getCardNr().matches("^\\d{16}$")) {
            throw new PaymentSelectionException(PaymentDTO.class.getSimpleName(), paymentDTO.getCardNr());
        }
    }

    private void validateCvc(PaymentDTO paymentDTO) throws PaymentSelectionException {
        if (paymentDTO.getCvc() == null || !paymentDTO.getCvc().matches("^\\d{3}$")) {
            throw new PaymentSelectionException(PaymentDTO.class.getSimpleName(), paymentDTO.getCvc());
        }
    }

    private void validateCardExpirationDate(PaymentDTO paymentDTO) throws PaymentSelectionException {
        if (paymentDTO.getCardExpirationDate() == null || paymentDTO.getCardExpirationDate().isBefore(YearMonth.now())) {
            throw new PaymentSelectionException(PaymentDTO.class.getSimpleName(), paymentDTO.getCardExpirationDate().toString());
        }
    }

    private void validateProfileId() throws ProfileException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Profile currentProfile = profileService.findProfileByUserId(userService.findByEmail(userDetails.getUsername()).getId());

        if (currentProfile.getId() == null) {
            throw new ProfileException(PaymentService.class.getSimpleName(), currentProfile.getId().toString());
        }
    }

    public String sendPaymentRequest(PaymentDTO paymentDTO) {
        paymentDTO.setStatus(PaymentDTO.PaymentStatusEnum.PENDING);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Profile currentProfile = profileService.findProfileByUserId(userService.findByEmail(userDetails.getUsername()).getId());
        paymentDTO.setProfileID(currentProfile.getId());

        return paymentServiceClient.postRequest(paymentDTO);
    }


}
