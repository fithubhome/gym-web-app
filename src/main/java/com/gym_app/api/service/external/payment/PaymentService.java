package com.gym_app.api.service.external.payment;

import com.gym_app.api.dto.external.membership.MembershipTypeExternal;
import com.gym_app.api.dto.external.membership.PaymentDto;
import com.gym_app.api.exceptions.external.membership.MembershipSelectionException;
import com.gym_app.api.service.external.membership.MembershipTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Locale;

@Service
public class PaymentService {
    @Autowired
    MembershipTypeService membershipTypeService;

    public PaymentDto validatePaymentData(PaymentDto paymentDto) throws MembershipSelectionException {
        validateSelectedMembership(paymentDto);
        validatePersonName(paymentDto);
        validateCardNr(paymentDto);
        validateCardExpirationDate(paymentDto);
        return paymentDto;
    }


    private void validateSelectedMembership(PaymentDto paymentDto) throws MembershipSelectionException {
        List<MembershipTypeExternal> mbTypeList =  membershipTypeService.getAllMembershipsType();
        if (paymentDto.getSelectedMembershipId() == null || mbTypeList.stream().noneMatch(mbType -> paymentDto.getSelectedMembershipId().equals(mbType.getId()))){
            throw new MembershipSelectionException(PaymentDto.class.getSimpleName(), paymentDto.getSelectedMembershipId().toString());
        }

    }

    private void validatePersonName(PaymentDto paymentDto) throws MembershipSelectionException {
        if (paymentDto.getPersonName() == null || paymentDto.getPersonName().matches(".*\\d.*")){
            throw new MembershipSelectionException(PaymentDto.class.getSimpleName(), paymentDto.getPersonName());
        }
    }

    private void validateCardNr(PaymentDto paymentDto) throws MembershipSelectionException {
        if (paymentDto.getCardNr() == null || !paymentDto.getCardNr().matches("^\\d{16}$")){
            throw new MembershipSelectionException(PaymentDto.class.getSimpleName(), paymentDto.getCardNr());
        }
    }
    private void validateCvc(PaymentDto paymentDto) throws MembershipSelectionException {
        if (paymentDto.getCvc() == null || !paymentDto.getCvc().matches("^\\d{3}$")){
            throw new MembershipSelectionException(PaymentDto.class.getSimpleName(), paymentDto.getCvc());
        }
    }

    private void validateCardExpirationDate(PaymentDto paymentDto) throws MembershipSelectionException {
      if (paymentDto.getCardExpirationDate() == null || paymentDto.getCardExpirationDate().isBefore(YearMonth.now())){
          throw new MembershipSelectionException(PaymentDto.class.getSimpleName(), paymentDto.getCardExpirationDate().toString());
      }
    }






}
