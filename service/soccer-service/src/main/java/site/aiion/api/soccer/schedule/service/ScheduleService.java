package site.aiion.api.soccer.schedule.service;

import java.util.List;
import site.aiion.api.soccer.schedule.domain.ScheduleDTO;
import site.aiion.api.common.domain.Messenger;

public interface ScheduleService {
    public Messenger findById(ScheduleDTO scheduleDTO);
    public Messenger findAll();
    public Messenger save(ScheduleDTO scheduleDTO);
    public Messenger saveAll(List<ScheduleDTO> scheduleDTOList);
    public Messenger update(ScheduleDTO scheduleDTO);
    public Messenger delete(ScheduleDTO scheduleDTO);
}

