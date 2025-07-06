package com.ticketrush.eventservice.controller;

import com.ticketrush.commoncore.dto.ApiResponseDto;
import com.ticketrush.eventservice.dto.EventRequestDto;
import com.ticketrush.eventservice.dto.EventResponseDto;
import com.ticketrush.eventservice.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
public class EventController {
    private final EventService eventService;

    @PostMapping
    public ResponseEntity<ApiResponseDto> createEvent(EventRequestDto requestDto) {
        EventResponseDto result = eventService.createEvent(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseDto("이벤트 생성 성공", HttpStatus.CREATED.value()));
    }
}
