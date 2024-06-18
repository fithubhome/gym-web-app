package com.gym_app.api.dto.external.membership;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class MembershipDto {

    private UUID profileID;
    private UUID membershipTypeID;
    private String name;
    private Double price;
    private PaymentStatusEnum status;

    public enum PaymentStatusEnum {
        PENDING
    }
}
