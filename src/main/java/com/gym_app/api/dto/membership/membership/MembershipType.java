package com.gym_app.api.dto.membership.membership;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class MembershipType {
    private UUID id;
    private String name;
    private Double price;
}

