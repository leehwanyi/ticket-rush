package com.ticketrush.eventservice.dto;

import lombok.Getter;

@Getter
public class EventResponseDto {
    private Long id;
    private String name;
    private String location;

    public EventResponseDto(Long id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public static EventResponseDto fromEntity(com.ticketrush.eventservice.entity.Event event) {
        return new EventResponseDto(
                event.getId(),
                event.getName(),
                event.getLocation()
        );
    }

}
