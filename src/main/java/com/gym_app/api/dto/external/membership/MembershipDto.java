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
    private UUID id;
    private String name;
    private Double price;
    private PaymentStatusEnum status;
    private String test;

    public enum PaymentStatusEnum {
        PENDING
    }
}
