package site.aiion.api.calendar.service;

import site.aiion.api.calendar.domain.CalendarDTO;
import site.aiion.api.common.domain.Messenger;

public interface CalendarService {

    Messenger findById(Long calendarId);

    Messenger findAll();

    Messenger create(CalendarDTO calendarDTO);

    Messenger update(Long calendarId, CalendarDTO calendarDTO);

    Messenger delete(Long calendarId);
}

