package com.gym_app.api.service.external.payment;

import com.gym_app.api.dto.external.membershipapi.get.MembershipTypeExternal;
import com.gym_app.api.dto.external.paymentapi.put.PaymentResponseDto;
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
    @Autowired
    PaymentServiceClient paymentServiceClient;


    public void validatePaymentData(PaymentResponseDto paymentResponseDto) throws PaymentSelectionException, ProfileException {
        validateSelectedMembership(paymentResponseDto);
        validatePersonName(paymentResponseDto);
        validateCardNr(paymentResponseDto);
        validateCardExpirationDate(paymentResponseDto);
        validateCvc(paymentResponseDto);
        validateProfileId();
    }


    private void validateSelectedMembership(PaymentResponseDto paymentResponseDto) throws PaymentSelectionException {
        List<MembershipTypeExternal> mbTypeList =  membershipTypeService.getAllMembershipsType();
        if (paymentResponseDto.getSelectedMembershipId() == null || mbTypeList.stream().noneMatch(mbType -> paymentResponseDto.getSelectedMembershipId().equals(mbType.getId()))){
            throw new PaymentSelectionException(PaymentResponseDto.class.getSimpleName(), paymentResponseDto.getSelectedMembershipId().toString());
        }

    }

    private void validatePersonName(PaymentResponseDto paymentResponseDto) throws PaymentSelectionException {
        if (paymentResponseDto.getPersonName() == null || paymentResponseDto.getPersonName().matches(".*\\d.*")){
            throw new PaymentSelectionException(PaymentResponseDto.class.getSimpleName(), paymentResponseDto.getPersonName());
        }
    }
    private void validateCardNr(PaymentResponseDto paymentResponseDto) throws PaymentSelectionException {
        if (paymentResponseDto.getCardNr() == null || !paymentResponseDto.getCardNr().matches("^\\d{16}$")){
            throw new PaymentSelectionException(PaymentResponseDto.class.getSimpleName(), paymentResponseDto.getCardNr());
        }
    }

    private void validateCvc(PaymentResponseDto paymentResponseDto) throws PaymentSelectionException {
        if (paymentResponseDto.getCvc() == null || !paymentResponseDto.getCvc().matches("^\\d{3}$")){
            throw new PaymentSelectionException(PaymentResponseDto.class.getSimpleName(), paymentResponseDto.getCvc());
        }
    }

    private void validateCardExpirationDate(PaymentResponseDto paymentResponseDto) throws PaymentSelectionException {
      if (paymentResponseDto.getCardExpirationDate() == null || paymentResponseDto.getCardExpirationDate().isBefore(YearMonth.now())){
          throw new PaymentSelectionException(PaymentResponseDto.class.getSimpleName(), paymentResponseDto.getCardExpirationDate().toString());
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

    public void setProfileIdAndStatusToPaymentDto(PaymentResponseDto paymentResponseDto) {
        paymentResponseDto.setStatus(PaymentResponseDto.PaymentStatusEnum.PENDING);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Profile currentProfile = profileService.findProfileByUserId(userService.findByEmail(userDetails.getUsername()).getId());
        paymentResponseDto.setProfileID(currentProfile.getId());

        paymentServiceClient.postRequest(paymentResponseDto);
    }




}
