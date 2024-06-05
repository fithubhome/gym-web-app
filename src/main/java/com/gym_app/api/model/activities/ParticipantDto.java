package com.gym_app.api.model.activities;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ParticipantDto {

    private Long id;
    private Long profileId;
    private GymEventDto event;
}
