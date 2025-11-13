package site.aiion.api.soccer.stadium.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.aiion.api.common.domain.Messenger;
import site.aiion.api.soccer.stadium.domain.StadiumDTO;
import site.aiion.api.soccer.stadium.domain.Stadium;
import site.aiion.api.soccer.stadium.repository.StadiumRepository;

@Service
@RequiredArgsConstructor
public class StadiumServiceImpl implements StadiumService {

    private final StadiumRepository stadiumRepository;

    private StadiumDTO entityToDTO(Stadium entity) {
        return StadiumDTO.builder()
                .id(entity.getId())
                .stadium_uk(entity.getStadium_uk())
                .stadium_name(entity.getStadium_name())
                .hometeam_uk(entity.getHometeam_uk())
                .seat_count(entity.getSeat_count())
                .address(entity.getAddress())
                .ddd(entity.getDdd())
                .tel(entity.getTel())
                .build();
    }

    private Stadium dtoToEntity(StadiumDTO dto) {
        return Stadium.builder()
                .id(dto.id)
                .stadium_uk(dto.stadium_uk)
                .stadium_name(dto.stadium_name)
                .hometeam_uk(dto.hometeam_uk)
                .seat_count(dto.seat_count)
                .address(dto.address)
                .ddd(dto.ddd)
                .tel(dto.tel)
                .build();
    }

    @Override
    public Messenger findById(StadiumDTO stadiumDTO) {
        Optional<Stadium> entity = stadiumRepository.findById(stadiumDTO.id);
        if (entity.isPresent()) {
            StadiumDTO dto = entityToDTO(entity.get());
            return Messenger.builder()
                    .Code(200)
                    .message("조회 성공")
                    .data(dto)
                    .build();
        } else {
            return Messenger.builder()
                    .Code(404)
                    .message("경기장을 찾을 수 없습니다.")
                    .build();
        }
    }

    @Override
    public Messenger findAll() {
        List<Stadium> entities = stadiumRepository.findAll();
        List<StadiumDTO> dtoList = entities.stream()
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
    public Messenger save(StadiumDTO stadiumDTO) {
        Stadium entity = dtoToEntity(stadiumDTO);
        Stadium saved = stadiumRepository.save(entity);
        StadiumDTO dto = entityToDTO(saved);
        return Messenger.builder()
                .Code(200)
                .message("저장 성공: " + saved.getId())
                .data(dto)
                .build();
    }

    @Override
    @Transactional
    public Messenger saveAll(List<StadiumDTO> stadiumDTOList) {
        List<Stadium> entities = stadiumDTOList.stream()
                .map(this::dtoToEntity)
                .collect(Collectors.toList());
        
        List<Stadium> saved = stadiumRepository.saveAll(entities);
        return Messenger.builder()
                .Code(200)
                .message("일괄 저장 성공: " + saved.size() + "개")
                .build();
    }

    @Override
    @Transactional
    public Messenger update(StadiumDTO stadiumDTO) {
        Optional<Stadium> optionalEntity = stadiumRepository.findById(stadiumDTO.id);
        if (optionalEntity.isPresent()) {
            Stadium existing = optionalEntity.get();
            Stadium updated = Stadium.builder()
                    .id(existing.getId())
                    .stadium_uk(stadiumDTO.stadium_uk != null ? stadiumDTO.stadium_uk : existing.getStadium_uk())
                    .stadium_name(stadiumDTO.stadium_name != null ? stadiumDTO.stadium_name : existing.getStadium_name())
                    .hometeam_uk(stadiumDTO.hometeam_uk != null ? stadiumDTO.hometeam_uk : existing.getHometeam_uk())
                    .seat_count(stadiumDTO.seat_count != null ? stadiumDTO.seat_count : existing.getSeat_count())
                    .address(stadiumDTO.address != null ? stadiumDTO.address : existing.getAddress())
                    .ddd(stadiumDTO.ddd != null ? stadiumDTO.ddd : existing.getDdd())
                    .tel(stadiumDTO.tel != null ? stadiumDTO.tel : existing.getTel())
                    .schedules(existing.getSchedules())
                    .teams(existing.getTeams())
                    .build();
            
            Stadium saved = stadiumRepository.save(updated);
            StadiumDTO dto = entityToDTO(saved);
            return Messenger.builder()
                    .Code(200)
                    .message("수정 성공: " + stadiumDTO.id)
                    .data(dto)
                    .build();
        } else {
            return Messenger.builder()
                    .Code(404)
                    .message("수정할 경기장을 찾을 수 없습니다.")
                    .build();
        }
    }

    @Override
    @Transactional
    public Messenger delete(StadiumDTO stadiumDTO) {
        Optional<Stadium> optionalEntity = stadiumRepository.findById(stadiumDTO.id);
        if (optionalEntity.isPresent()) {
            stadiumRepository.deleteById(stadiumDTO.id);
            return Messenger.builder()
                    .Code(200)
                    .message("삭제 성공: " + stadiumDTO.id)
                    .build();
        } else {
            return Messenger.builder()
                    .Code(404)
                    .message("삭제할 경기장을 찾을 수 없습니다.")
                    .build();
        }
    }

}

