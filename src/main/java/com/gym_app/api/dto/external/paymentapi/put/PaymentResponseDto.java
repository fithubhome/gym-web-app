package com.gym_app.api.dto.external.paymentapi.put;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.YearMonth;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class PaymentResponseDto {

    private UUID profileID;
    private UUID selectedMembershipId;
    private String personName;
    private String cardNr;
    private String cvc;
    private YearMonth cardExpirationDate;
    private PaymentStatusEnum status; // Every time this Object will be sent must have the status Pending as the Payment API has not validated the payment yet

    public enum PaymentStatusEnum {
        PENDING
    }




}
