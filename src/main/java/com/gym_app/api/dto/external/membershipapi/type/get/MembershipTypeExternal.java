package com.gym_app.api.dto.external.membershipapi.type.get;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class MembershipTypeExternal {
    private UUID id;
    private String name;
    private Double price;
}

