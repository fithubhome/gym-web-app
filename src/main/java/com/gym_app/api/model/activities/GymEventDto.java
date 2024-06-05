package com.gym_app.api.model.activities;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

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
    private Long organizerId;
    private String eventType;
    private Integer maxParticipants;
}
