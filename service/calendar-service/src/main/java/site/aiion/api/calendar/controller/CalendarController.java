package site.aiion.api.calendar.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import site.aiion.api.calendar.domain.CalendarDTO;
import site.aiion.api.calendar.service.CalendarService;
import site.aiion.api.common.domain.Messenger;

@RestController
@RequiredArgsConstructor
@RequestMapping("/calendars")
public class CalendarController {

    private final CalendarService calendarService;

    @GetMapping("/{calendarId}")
    public Messenger findById(@PathVariable Long calendarId) 
    {
        return calendarService.findById(calendarId);
    }

    @GetMapping
    public Messenger findAll() {
        return calendarService.findAll();
    }

    @PostMapping
    public Messenger create(@RequestBody CalendarDTO calendarDTO) {
        return calendarService.create(calendarDTO);
    }

    @PutMapping("/{calendarId}")
    public Messenger update(@PathVariable Long calendarId, @RequestBody CalendarDTO calendarDTO) {
        return calendarService.update(calendarId, calendarDTO);
    }

    @DeleteMapping("/{calendarId}")
    public Messenger delete(@PathVariable Long calendarId) {
        return calendarService.delete(calendarId);
    }
}

