package com.gym_app.api.dto.activities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class EventsAndParticipantsDto {
    private List<GymEventDto> events;
    private List<ParticipantDto> participants;
}
