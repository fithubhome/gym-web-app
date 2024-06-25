package com.bodystats.api.dto.activities;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class GymEventDto {

    private Long id;
    private String eventName;
    private String eventDescription;
    private List<WorkoutTypeDto> workoutType;
    private Date date;
    private Time startTime;
    private Time endTime;
    private UUID organizerId;
    private String eventType;
    private Integer maxParticipants;
}
