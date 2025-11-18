package site.aiion.api.calendar.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.aiion.api.calendar.domain.Calendar;
import site.aiion.api.calendar.domain.CalendarModel;
import site.aiion.api.calendar.repository.CalendarRepository;
import site.aiion.api.calendar.common.domain.Messenger;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CalendarServiceImpl implements CalendarService {

    private final CalendarRepository calendarRepository;
    private CalendarModel entityToModel(Calendar calendar) {
        return CalendarModel.builder()
                .id(calendar.getId())
                .name(calendar.getName())
                .build();
    }

    @Override
    public Messenger findById(Long calendarId) {
        Objects.requireNonNull(calendarId, "calendarId must not be null");
        return calendarRepository.findById(calendarId)
                .map(calendar -> Messenger.builder()
                        .Code(200)
                        .message("조회 성공")
                        .data(entityToModel(calendar))
                        .build())
                .orElseGet(() -> Messenger.builder()
                        .Code(404)
                        .message("캘린더를 찾을 수 없습니다.")
                        .build());
    }

    @Override
    public Messenger findAll() {
        List<CalendarModel> calendars = calendarRepository.findAll()
                .stream()
                .map(this::entityToModel)
                .collect(Collectors.toList());

        return Messenger.builder()
                .Code(200)
                .message("전체 조회 성공: " + calendars.size() + "개")
                .data(calendars)
                .build();
    }

    @Override
    @Transactional
    public Messenger create(CalendarModel calendarModel) {
        Objects.requireNonNull(calendarModel, "calendarModel must not be null");
        Calendar calendar = Calendar.builder()
                .name(calendarModel.getName())
                .build();

        Calendar savedCalendar = calendarRepository.save(Objects.requireNonNull(calendar, "calendar must not be null"));

        return Messenger.builder()
                .Code(200)
                .message("저장 성공: ID " + savedCalendar.getId())
                .data(entityToModel(savedCalendar))
                .build();
    }

    @Override
    @Transactional
    public Messenger update(Long calendarId, CalendarModel calendarModel) {
        Objects.requireNonNull(calendarId, "calendarId must not be null");
        Objects.requireNonNull(calendarModel, "calendarModel must not be null");
        return calendarRepository.findById(calendarId)
                .map(calendarEntity -> {
                    Calendar calendar = Objects.requireNonNull(calendarEntity, "calendar must not be null");
                    if (calendarModel.getName() != null) {
                        calendar.setName(calendarModel.getName());
                    }

                    Calendar savedCalendar = calendarRepository.save(calendar);

                    return Messenger.builder()
                            .Code(200)
                            .message("수정 성공: ID " + calendarId)
                            .data(entityToModel(savedCalendar))
                            .build();
                })
                .orElseGet(() -> Messenger.builder()
                        .Code(404)
                        .message("수정할 캘린더를 찾을 수 없습니다.")
                        .build());
    }

    @Override
    @Transactional
    public Messenger delete(Long calendarId) {
        Objects.requireNonNull(calendarId, "calendarId must not be null");
        return calendarRepository.findById(calendarId)
                .map(calendarEntity -> {
                    Calendar calendar = Objects.requireNonNull(calendarEntity, "calendar must not be null");
                    calendarRepository.delete(calendar);

                    return Messenger.builder()
                            .Code(200)
                            .message("삭제 성공: ID " + calendarId)
                            .build();
                })
                .orElseGet(() -> Messenger.builder()
                        .Code(404)
                        .message("삭제할 캘린더를 찾을 수 없습니다.")
                        .build());
    }
}

