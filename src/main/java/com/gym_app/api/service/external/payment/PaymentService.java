package com.gym_app.api.service.external.payment;

import com.gym_app.api.dto.external.membership.MembershipTypeExternal;
import com.gym_app.api.dto.external.membership.PaymentDto;
import com.gym_app.api.exceptions.external.payment.PaymentSelectionException;
import com.gym_app.api.exceptions.external.payment.ProfileException;
import com.gym_app.api.model.Profile;
import com.gym_app.api.model.UserEntity;
import com.gym_app.api.security.CustomUserDetails;
import com.gym_app.api.service.ProfileService;
import com.gym_app.api.service.UserService;
import com.gym_app.api.service.external.membership.MembershipTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.List;

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


    public void validatePaymentData(PaymentDto paymentDto) throws PaymentSelectionException, ProfileException {
        validateSelectedMembership(paymentDto);
        validatePersonName(paymentDto);
        validateCardNr(paymentDto);
        validateCardExpirationDate(paymentDto);
        validateCvc(paymentDto);
        validateProfileId();
    }


    private void validateSelectedMembership(PaymentDto paymentDto) throws PaymentSelectionException {
        List<MembershipTypeExternal> mbTypeList =  membershipTypeService.getAllMembershipsType();
        if (paymentDto.getSelectedMembershipId() == null || mbTypeList.stream().noneMatch(mbType -> paymentDto.getSelectedMembershipId().equals(mbType.getId()))){
            throw new PaymentSelectionException(PaymentDto.class.getSimpleName(), paymentDto.getSelectedMembershipId().toString());
        }

    }

    private void validatePersonName(PaymentDto paymentDto) throws PaymentSelectionException {
        if (paymentDto.getPersonName() == null || paymentDto.getPersonName().matches(".*\\d.*")){
            throw new PaymentSelectionException(PaymentDto.class.getSimpleName(), paymentDto.getPersonName());
        }
    }
    private void validateCardNr(PaymentDto paymentDto) throws PaymentSelectionException {
        if (paymentDto.getCardNr() == null || !paymentDto.getCardNr().matches("^\\d{16}$")){
            throw new PaymentSelectionException(PaymentDto.class.getSimpleName(), paymentDto.getCardNr());
        }
    }

    private void validateCvc(PaymentDto paymentDto) throws PaymentSelectionException {
        if (paymentDto.getCvc() == null || !paymentDto.getCvc().matches("^\\d{3}$")){
            throw new PaymentSelectionException(PaymentDto.class.getSimpleName(), paymentDto.getCvc());
        }
    }

    private void validateCardExpirationDate(PaymentDto paymentDto) throws PaymentSelectionException {
      if (paymentDto.getCardExpirationDate() == null || paymentDto.getCardExpirationDate().isBefore(YearMonth.now())){
          throw new PaymentSelectionException(PaymentDto.class.getSimpleName(), paymentDto.getCardExpirationDate().toString());
      }
    }

    private void validateProfileId() throws ProfileException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Profile currentProfile = profileService.findProfileByUserId(userService.findByEmail(userDetails.getUsername()).getId());

        if (currentProfile.getId()==null){
            throw new ProfileException(PaymentService.class.getSimpleName(), currentProfile.getId().toString());
        }
    }

    public PaymentDto setProfileIdAndStatusToPaymentDto(PaymentDto paymentDto) {
        paymentDto.setStatus(PaymentDto.PaymentStatusEnum.PENDING);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Profile currentProfile = profileService.findProfileByUserId(userService.findByEmail(userDetails.getUsername()).getId());
        paymentDto.setProfileID(currentProfile.getId());

        return paymentDto;
    }




}
