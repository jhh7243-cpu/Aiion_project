package site.aiion.api.diary.service;

import java.util.List;

import site.aiion.api.diary.domain.DiaryDTO;
import site.aiion.api.common.domain.Messenger;

public interface DiaryService {
    public Messenger findById(DiaryDTO diaryDTO);
    public Messenger findAll();
    public Messenger save(DiaryDTO diaryDTO);
    public Messenger saveAll(List<DiaryDTO> diaryDTOList);
    public Messenger update(DiaryDTO diaryDTO);
    public Messenger delete(DiaryDTO diaryDTO);
}
