package site.aiion.api.soccer.schedule.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.aiion.api.common.domain.Messenger;
import site.aiion.api.soccer.schedule.domain.ScheduleDTO;
import site.aiion.api.soccer.schedule.domain.Schedule;
import site.aiion.api.soccer.schedule.repository.ScheduleRepository;
import site.aiion.api.soccer.stadium.repository.StadiumRepository;
import site.aiion.api.soccer.team.repository.TeamRepository;
import site.aiion.api.soccer.stadium.domain.Stadium;
import site.aiion.api.soccer.team.domain.Team;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final StadiumRepository stadiumRepository;
    private final TeamRepository teamRepository;

    private ScheduleDTO entityToDTO(Schedule entity) {
        return ScheduleDTO.builder()
                .id(entity.getId())
                .sche_date(entity.getSche_date())
                .stadium_uk(entity.getStadium_uk())
                .gubun(entity.getGubun())
                .hometeam_uk(entity.getHometeam_uk())
                .awayteam_uk(entity.getAwayteam_uk())
                .home_score(entity.getHome_score())
                .away_score(entity.getAway_score())
                .build();
    }

    private Schedule dtoToEntity(ScheduleDTO dto) {
        Stadium stadium = null;
        Team hometeam = null;
        Team awayteam = null;
        
        if (dto.stadium_uk != null) {
            stadium = stadiumRepository.findByStadium_uk(dto.stadium_uk).orElse(null);
        }
        if (dto.hometeam_uk != null) {
            hometeam = teamRepository.findByTeam_uk(dto.hometeam_uk).orElse(null);
        }
        if (dto.awayteam_uk != null) {
            awayteam = teamRepository.findByTeam_uk(dto.awayteam_uk).orElse(null);
        }
        
        return Schedule.builder()
                .id(dto.id)
                .sche_date(dto.sche_date)
                .stadium_uk(dto.stadium_uk)
                .gubun(dto.gubun)
                .hometeam_uk(dto.hometeam_uk)
                .awayteam_uk(dto.awayteam_uk)
                .home_score(dto.home_score)
                .away_score(dto.away_score)
                .stadium(stadium)
                .hometeam(hometeam)
                .awayteam(awayteam)
                .build();
    }

    @Override
    public Messenger findById(ScheduleDTO scheduleDTO) {
        Optional<Schedule> entity = scheduleRepository.findById(scheduleDTO.id);
        if (entity.isPresent()) {
            ScheduleDTO dto = entityToDTO(entity.get());
            return Messenger.builder()
                    .Code(200)
                    .message("조회 성공")
                    .data(dto)
                    .build();
        } else {
            return Messenger.builder()
                    .Code(404)
                    .message("일정을 찾을 수 없습니다.")
                    .build();
        }
    }

    @Override
    public Messenger findAll() {
        List<Schedule> entities = scheduleRepository.findAll();
        List<ScheduleDTO> dtoList = entities.stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList());
        return Messenger.builder()
                .Code(200)
                .message("전체 조회 성공: " + dtoList.size() + "개")
                .data(dtoList)
                .build();
    }

    @Override
    @Transactional
    public Messenger save(ScheduleDTO scheduleDTO) {
        Schedule entity = dtoToEntity(scheduleDTO);
        Schedule saved = scheduleRepository.save(entity);
        ScheduleDTO dto = entityToDTO(saved);
        return Messenger.builder()
                .Code(200)
                .message("저장 성공: " + saved.getId())
                .data(dto)
                .build();
    }

    @Override
    @Transactional
    public Messenger saveAll(List<ScheduleDTO> scheduleDTOList) {
        List<Schedule> entities = scheduleDTOList.stream()
                .map(dto -> {
                    Stadium stadium = null;
                    Team hometeam = null;
                    Team awayteam = null;
                    
                    if (dto.stadium_uk != null) {
                        stadium = stadiumRepository.findByStadium_uk(dto.stadium_uk).orElse(null);
                    }
                    if (dto.hometeam_uk != null) {
                        hometeam = teamRepository.findByTeam_uk(dto.hometeam_uk).orElse(null);
                    }
                    if (dto.awayteam_uk != null) {
                        awayteam = teamRepository.findByTeam_uk(dto.awayteam_uk).orElse(null);
                    }
                    
                    return Schedule.builder()
                            .id(dto.id)
                            .sche_date(dto.sche_date)
                            .stadium_uk(dto.stadium_uk)
                            .gubun(dto.gubun)
                            .hometeam_uk(dto.hometeam_uk)
                            .awayteam_uk(dto.awayteam_uk)
                            .home_score(dto.home_score)
                            .away_score(dto.away_score)
                            .stadium(stadium)
                            .hometeam(hometeam)
                            .awayteam(awayteam)
                            .build();
                })
                .collect(Collectors.toList());
        
        List<Schedule> saved = scheduleRepository.saveAll(entities);
        List<ScheduleDTO> dtoList = saved.stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList());
        return Messenger.builder()
                .Code(200)
                .message("일괄 저장 성공: " + dtoList.size() + "개")
                .data(dtoList)
                .build();
    }

    @Override
    @Transactional
    public Messenger update(ScheduleDTO scheduleDTO) {
        Optional<Schedule> optionalEntity = scheduleRepository.findById(scheduleDTO.id);
        if (optionalEntity.isPresent()) {
            Schedule existing = optionalEntity.get();
            
            Stadium stadium = scheduleDTO.stadium_uk != null 
                    ? stadiumRepository.findByStadium_uk(scheduleDTO.stadium_uk).orElse(existing.getStadium()) 
                    : existing.getStadium();
            Team hometeam = scheduleDTO.hometeam_uk != null 
                    ? teamRepository.findByTeam_uk(scheduleDTO.hometeam_uk).orElse(existing.getHometeam()) 
                    : existing.getHometeam();
            Team awayteam = scheduleDTO.awayteam_uk != null 
                    ? teamRepository.findByTeam_uk(scheduleDTO.awayteam_uk).orElse(existing.getAwayteam()) 
                    : existing.getAwayteam();
            
            Schedule updated = Schedule.builder()
                    .id(existing.getId())
                    .sche_date(scheduleDTO.sche_date != null ? scheduleDTO.sche_date : existing.getSche_date())
                    .stadium_uk(scheduleDTO.stadium_uk != null ? scheduleDTO.stadium_uk : existing.getStadium_uk())
                    .gubun(scheduleDTO.gubun != null ? scheduleDTO.gubun : existing.getGubun())
                    .hometeam_uk(scheduleDTO.hometeam_uk != null ? scheduleDTO.hometeam_uk : existing.getHometeam_uk())
                    .awayteam_uk(scheduleDTO.awayteam_uk != null ? scheduleDTO.awayteam_uk : existing.getAwayteam_uk())
                    .home_score(scheduleDTO.home_score != null ? scheduleDTO.home_score : existing.getHome_score())
                    .away_score(scheduleDTO.away_score != null ? scheduleDTO.away_score : existing.getAway_score())
                    .stadium(stadium)
                    .hometeam(hometeam)
                    .awayteam(awayteam)
                    .build();
            
            Schedule saved = scheduleRepository.save(updated);
            ScheduleDTO dto = entityToDTO(saved);
            return Messenger.builder()
                    .Code(200)
                    .message("수정 성공: " + scheduleDTO.id)
                    .data(dto)
                    .build();
        } else {
            return Messenger.builder()
                    .Code(404)
                    .message("수정할 일정을 찾을 수 없습니다.")
                    .build();
        }
    }

    @Override
    @Transactional
    public Messenger delete(ScheduleDTO scheduleDTO) {
        Optional<Schedule> optionalEntity = scheduleRepository.findById(scheduleDTO.id);
        if (optionalEntity.isPresent()) {
            scheduleRepository.deleteById(scheduleDTO.id);
            return Messenger.builder()
                    .Code(200)
                    .message("삭제 성공: " + scheduleDTO.id)
                    .build();
        } else {
            return Messenger.builder()
                    .Code(404)
                    .message("삭제할 일정을 찾을 수 없습니다.")
                    .build();
        }
    }

}

