package site.aiion.api.diary.service;

import java.util.List;

import site.aiion.api.diary.domain.DiaryModel;
import site.aiion.api.diary.common.domain.Messenger;

public interface DiaryService {
    public Messenger findById(DiaryModel diaryModel);
    public Messenger findAll();
    public Messenger save(DiaryModel diaryModel);
    public Messenger saveAll(List<DiaryModel> diaryModelList);
    public Messenger update(DiaryModel diaryModel);
    public Messenger delete(DiaryModel diaryModel);
}
