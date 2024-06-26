package com.gym_app.api.dto.membership.payment;

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
public class PaymentDTO {
    private UUID profileID;
    private UUID selectedMembershipId;
    private String personName;
    private String cardNr;
    private String cvc;
    private YearMonth cardExpirationDate;
    private PaymentStatusEnum status;

    public enum PaymentStatusEnum {
        PENDING
    }




}
