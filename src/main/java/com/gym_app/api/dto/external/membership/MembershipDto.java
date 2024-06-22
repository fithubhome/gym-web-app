package com.gym_app.api.dto.external.membership;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.YearMonth;
import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class MembershipDto {

    private UUID id;
//    private UUID profileID;
//    private String name;
//    private Double price;
//    private PaymentStatusEnum status;
    private String personName;
    private String cardNr;
    private String cvc;
    private YearMonth cardExpirationDate;



    public enum PaymentStatusEnum {
        PENDING
    }




}
