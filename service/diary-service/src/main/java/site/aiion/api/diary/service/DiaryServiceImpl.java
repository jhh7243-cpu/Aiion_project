package site.aiion.api.diary.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.aiion.api.common.domain.Messenger;
import site.aiion.api.diary.domain.Diary;
import site.aiion.api.diary.domain.DiaryDTO;
import site.aiion.api.diary.repository.DiaryRepository;

@Service
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {

    private final DiaryRepository diaryRepository;

    private DiaryDTO entityToDto(Diary entity) {
        LocalDate diaryDate = entity.getDiaryDate();
        return DiaryDTO.builder()
                .id(entity.getId())
                .diaryDate(diaryDate)
                .title(entity.getTitle())
                .content(entity.getContent())
                .build();
    }

    private Diary dtoToEntity(DiaryDTO dto) {
        return Diary.builder()
                .id(dto.getId())
                .diaryDate(dto.getDiaryDate())
                .title(dto.getTitle())
                .content(dto.getContent())
                .build();
    }

    @Override
    public Messenger findById(DiaryDTO diaryDTO) {
        Objects.requireNonNull(diaryDTO, "diaryDTO must not be null");
        Long id = diaryDTO.getId();
        if (id == null) {
            return Messenger.builder()
                    .Code(400)
                    .message("ID는 필수 값입니다.")
                    .build();
        }

        Optional<Diary> optionalDiary = diaryRepository.findById(id);
        if (optionalDiary.isEmpty()) {
            return Messenger.builder()
                    .Code(404)
                    .message("일기를 찾을 수 없습니다.")
                    .build();
        }

        return Messenger.builder()
                .Code(200)
                .message("조회 성공")
                .data(entityToDto(optionalDiary.get()))
                .build();
    }

    @Override
    public Messenger findAll() {
        List<DiaryDTO> diaries = diaryRepository.findAll()
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());

        return Messenger.builder()
                .Code(200)
                .message("전체 조회 성공: " + diaries.size() + "개")
                .data(diaries)
                .build();
    }

    @Override
    @Transactional
    public Messenger save(DiaryDTO diaryDTO) {
        Objects.requireNonNull(diaryDTO, "diaryDTO must not be null");
        if (diaryDTO.getDiaryDate() == null) {
            return Messenger.builder()
                    .Code(400)
                    .message("일자 정보는 필수 값입니다.")
                    .build();
        }

        Diary toSave = dtoToEntity(diaryDTO);
        Diary saved = diaryRepository.save(Objects.requireNonNull(toSave, "entity must not be null"));

        return Messenger.builder()
                .Code(200)
                .message("저장 성공: ID " + saved.getId())
                .data(entityToDto(saved))
                .build();
    }

    @Override
    @Transactional
    public Messenger saveAll(List<DiaryDTO> diaryDTOList) {
        Objects.requireNonNull(diaryDTOList, "diaryDTOList must not be null");

        List<Diary> entities = diaryDTOList.stream()
                .map(dto -> {
                    if (dto.getDiaryDate() == null) {
                        throw new IllegalArgumentException("일자 정보는 필수 값입니다.");
                    }
                    return dtoToEntity(dto);
                })
                .collect(Collectors.toList());

        List<Diary> saved = diaryRepository.saveAll(Objects.requireNonNull(entities, "entities must not be null"));

        return Messenger.builder()
                .Code(200)
                .message("일괄 저장 성공: " + saved.size() + "개")
                .build();
    }

    @Override
    @Transactional
    public Messenger update(DiaryDTO diaryDTO) {
        Objects.requireNonNull(diaryDTO, "diaryDTO must not be null");
        Long id = diaryDTO.getId();
        if (id == null) {
            return Messenger.builder()
                    .Code(400)
                    .message("ID는 필수 값입니다.")
                    .build();
        }
        if (diaryDTO.getDiaryDate() == null) {
            return Messenger.builder()
                    .Code(400)
                    .message("일자 정보는 필수 값입니다.")
                    .build();
        }

        Optional<Diary> optionalDiary = diaryRepository.findById(id);
        if (optionalDiary.isEmpty()) {
            return Messenger.builder()
                    .Code(404)
                    .message("수정할 일기를 찾을 수 없습니다.")
                    .build();
        }

        Diary diary = optionalDiary.get();
        diary.setDiaryDate(diaryDTO.getDiaryDate());
        diary.setTitle(diaryDTO.getTitle());
        diary.setContent(diaryDTO.getContent());

        Diary saved = diaryRepository.save(diary);
        return Messenger.builder()
                .Code(200)
                .message("수정 성공: ID " + saved.getId())
                .data(entityToDto(saved))
                .build();
    }

    @Override
    @Transactional
    public Messenger delete(DiaryDTO diaryDTO) {
        Objects.requireNonNull(diaryDTO, "diaryDTO must not be null");
        Long id = diaryDTO.getId();
        if (id == null) {
            return Messenger.builder()
                    .Code(400)
                    .message("ID는 필수 값입니다.")
                    .build();
        }

        boolean exists = diaryRepository.existsById(id);
        if (!exists) {
            return Messenger.builder()
                    .Code(404)
                    .message("삭제할 일기를 찾을 수 없습니다.")
                    .build();
        }

        diaryRepository.deleteById(id);
        return Messenger.builder()
                .Code(200)
                .message("삭제 성공: ID " + id)
                .build();
    }
}

