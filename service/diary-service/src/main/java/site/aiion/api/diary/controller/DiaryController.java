package site.aiion.api.diary.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import site.aiion.api.diary.domain.DiaryModel;
import site.aiion.api.diary.service.DiaryService;
import site.aiion.api.diary.common.domain.Messenger;

@RestController
@RequiredArgsConstructor
@RequestMapping("/diaries")
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping("/findById")
    public Messenger findById(@RequestBody DiaryModel diaryModel) {
        return diaryService.findById(diaryModel);
    }

    @GetMapping
    public Messenger findAll() {
        return diaryService.findAll();
    }

    @PostMapping
    public Messenger save(@RequestBody DiaryModel diaryModel) {
        return diaryService.save(diaryModel);
    }

    @PostMapping("/saveAll")
    public Messenger saveAll(@RequestBody List<DiaryModel> diaryModelList) {
        return diaryService.saveAll(diaryModelList);
    }

    @PutMapping
    public Messenger update(@RequestBody DiaryModel diaryModel) {
        return diaryService.update(diaryModel);
    }

    @DeleteMapping
    public Messenger delete(@RequestBody DiaryModel diaryModel) {
        return diaryService.delete(diaryModel);
    }

}
