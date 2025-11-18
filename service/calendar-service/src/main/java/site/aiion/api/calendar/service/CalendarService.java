package site.aiion.api.calendar.service;

import site.aiion.api.calendar.domain.CalendarModel;
import site.aiion.api.calendar.common.domain.Messenger;

public interface CalendarService {

    Messenger findById(Long calendarId);

    Messenger findAll();

    Messenger create(CalendarModel calendarModel);

    Messenger update(Long calendarId, CalendarModel calendarModel);

    Messenger delete(Long calendarId);
}

