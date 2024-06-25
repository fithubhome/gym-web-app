package com.gym_app.api.dto.external.membershipapi.history.get;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MembershipHistoryExternal {
    private String membershipName;
    private Double price;
    private LocalDate startDate;
    private LocalDate endDate;


}
