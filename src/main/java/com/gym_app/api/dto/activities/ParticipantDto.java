package com.gym_app.api.dto.activities;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ParticipantDto {
    private Long id;
    private UUID profileId;
    private GymEventDto event;
}
