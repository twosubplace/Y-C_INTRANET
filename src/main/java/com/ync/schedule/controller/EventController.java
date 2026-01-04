package com.ync.schedule.controller;

import com.ync.schedule.dto.EventDto;
import com.ync.schedule.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "*")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<List<EventDto>> getAllEvents(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        if (startDate != null && endDate != null) {
            List<EventDto> events = eventService.getEventsBetweenDates(startDate, endDate);
            return ResponseEntity.ok(events);
        } else {
            List<EventDto> events = eventService.getAllEvents();
            return ResponseEntity.ok(events);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDto> getEvent(@PathVariable Long id) {
        EventDto event = eventService.getEventById(id);
        return ResponseEntity.ok(event);
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<EventDto>> getMemberEvents(@PathVariable Long memberId) {
        List<EventDto> events = eventService.getMemberEvents(memberId);
        return ResponseEntity.ok(events);
    }

    @PostMapping
    public ResponseEntity<EventDto> createEvent(@RequestBody EventDto dto) {
        EventDto created = eventService.createEvent(dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventDto> updateEvent(@PathVariable Long id, @RequestBody EventDto dto) {
        EventDto updated = eventService.updateEvent(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<EventDto> submitEvent(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        try {
            Object approverIdObj = payload.get("approverId");
            if (approverIdObj == null) {
                throw new RuntimeException("approverId is required");
            }
            Long approverId = Long.valueOf(approverIdObj.toString());
            EventDto submitted = eventService.submitEvent(id, approverId);
            return ResponseEntity.ok(submitted);
        } catch (Exception e) {
            throw new RuntimeException("Submit event failed: " + e.getMessage(), e);
        }
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<EventDto> approveEvent(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        Long approverId = Long.valueOf(payload.get("approverId").toString());
        String comment = payload.get("comment") != null ? payload.get("comment").toString() : null;
        EventDto approved = eventService.approveEvent(id, approverId, comment);
        return ResponseEntity.ok(approved);
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<EventDto> rejectEvent(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        Long approverId = Long.valueOf(payload.get("approverId").toString());
        String comment = payload.get("comment") != null ? payload.get("comment").toString() : null;
        EventDto rejected = eventService.rejectEvent(id, approverId, comment);
        return ResponseEntity.ok(rejected);
    }

    @PostMapping("/{id}/request-cancellation")
    public ResponseEntity<EventDto> requestCancellation(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        try {
            Object approverIdObj = payload.get("approverId");
            if (approverIdObj == null) {
                throw new RuntimeException("approverId is required");
            }
            Long approverId = Long.valueOf(approverIdObj.toString());
            EventDto result = eventService.requestCancellation(id, approverId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            throw new RuntimeException("Request cancellation failed: " + e.getMessage(), e);
        }
    }

    @PostMapping("/{id}/approve-cancellation")
    public ResponseEntity<EventDto> approveCancellation(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        Long approverId = Long.valueOf(payload.get("approverId").toString());
        String comment = payload.get("comment") != null ? payload.get("comment").toString() : null;
        EventDto result = eventService.approveCancellation(id, approverId, comment);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{id}/reject-cancellation")
    public ResponseEntity<EventDto> rejectCancellation(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        Long approverId = Long.valueOf(payload.get("approverId").toString());
        String comment = payload.get("comment") != null ? payload.get("comment").toString() : null;
        EventDto result = eventService.rejectCancellation(id, approverId, comment);
        return ResponseEntity.ok(result);
    }
}
