package com.gym_app.api.dto.bodystats;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BodyStatsDTO {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;
    @JdbcTypeCode(Types.VARCHAR)
    private UUID profileId;

    private int startingWeight;
    private int registerDay;
    private int bmi;
    private int muscleMass;
    private int fatPercentage;
    private int waist;
    private int buttocks;
    private int chest;
    private int leftArm;
    private int rightArm;
    private int leftThigh;
    private int rightThigh;
    private int leftCalf;
    private int rightCalf;

}
