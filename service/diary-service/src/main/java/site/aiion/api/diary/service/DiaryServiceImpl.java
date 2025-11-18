package site.aiion.api.diary.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.aiion.api.diary.common.domain.Messenger;
import site.aiion.api.diary.domain.Diary;
import site.aiion.api.diary.domain.DiaryModel;
import site.aiion.api.diary.repository.DiaryRepository;

@Service
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {

    private final DiaryRepository diaryRepository;

    private DiaryModel entityToModel(Diary entity) {
        LocalDate diaryDate = entity.getDiaryDate();
        return DiaryModel.builder()
                .id(entity.getId())
                .diaryDate(diaryDate)
                .title(entity.getTitle())
                .content(entity.getContent())
                .build();
    }

    private Diary modelToEntity(DiaryModel model) {
        return Diary.builder()
                .id(model.getId())
                .diaryDate(model.getDiaryDate())
                .title(model.getTitle())
                .content(model.getContent())
                .build();
    }

    @Override
    public Messenger findById(DiaryModel diaryModel) {
        Objects.requireNonNull(diaryModel, "diaryModel must not be null");
        Long id = diaryModel.getId();
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
                .data(entityToModel(optionalDiary.get()))
                .build();
    }

    @Override
    public Messenger findAll() {
        List<DiaryModel> diaries = diaryRepository.findAll()
                .stream()
                .map(this::entityToModel)
                .collect(Collectors.toList());

        return Messenger.builder()
                .Code(200)
                .message("전체 조회 성공: " + diaries.size() + "개")
                .data(diaries)
                .build();
    }

    @Override
    @Transactional
    public Messenger save(DiaryModel diaryModel) {
        Objects.requireNonNull(diaryModel, "diaryModel must not be null");
        if (diaryModel.getDiaryDate() == null) {
            return Messenger.builder()
                    .Code(400)
                    .message("일자 정보는 필수 값입니다.")
                    .build();
        }

        Diary toSave = modelToEntity(diaryModel);
        Diary saved = diaryRepository.save(Objects.requireNonNull(toSave, "entity must not be null"));

        return Messenger.builder()
                .Code(200)
                .message("저장 성공: ID " + saved.getId())
                .data(entityToModel(saved))
                .build();
    }

    @Override
    @Transactional
    public Messenger saveAll(List<DiaryModel> diaryModelList) {
        Objects.requireNonNull(diaryModelList, "diaryModelList must not be null");

        List<Diary> entities = diaryModelList.stream()
                .map(model -> {
                    if (model.getDiaryDate() == null) {
                        throw new IllegalArgumentException("일자 정보는 필수 값입니다.");
                    }
                    return modelToEntity(model);
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
    public Messenger update(DiaryModel diaryModel) {
        Objects.requireNonNull(diaryModel, "diaryModel must not be null");
        Long id = diaryModel.getId();
        if (id == null) {
            return Messenger.builder()
                    .Code(400)
                    .message("ID는 필수 값입니다.")
                    .build();
        }
        if (diaryModel.getDiaryDate() == null) {
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
        diary.setDiaryDate(diaryModel.getDiaryDate());
        diary.setTitle(diaryModel.getTitle());
        diary.setContent(diaryModel.getContent());

        Diary saved = diaryRepository.save(diary);
        return Messenger.builder()
                .Code(200)
                .message("수정 성공: ID " + saved.getId())
                .data(entityToModel(saved))
                .build();
    }

    @Override
    @Transactional
    public Messenger delete(DiaryModel diaryModel) {
        Objects.requireNonNull(diaryModel, "diaryModel must not be null");
        Long id = diaryModel.getId();
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

