package com.ticketrush.eventservice.service;

import com.ticketrush.eventservice.dto.EventRequestDto;
import com.ticketrush.eventservice.dto.EventResponseDto;
import com.ticketrush.eventservice.entity.Event;
import com.ticketrush.eventservice.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;

    @Transactional
    public EventResponseDto createEvent(EventRequestDto requestDto){
        Event event = Event.builder()
                .name(requestDto.getName())
                .location(requestDto.getLocation())
                .build();
        eventRepository.save(event);
        return EventResponseDto.fromEntity(event);
    }
}
