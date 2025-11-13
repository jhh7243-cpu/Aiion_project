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
import site.aiion.api.diary.domain.DiaryDTO;
import site.aiion.api.diary.service.DiaryService;
import site.aiion.api.common.domain.Messenger;

@RestController
@RequiredArgsConstructor
@RequestMapping("/diaries")
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping("/findById")
    public Messenger findById(@RequestBody DiaryDTO diaryDTO) {
        return diaryService.findById(diaryDTO);
    }

    @GetMapping
    public Messenger findAll() {
        return diaryService.findAll();
    }

    @PostMapping
    public Messenger save(@RequestBody DiaryDTO diaryDTO) {
        return diaryService.save(diaryDTO);
    }

    @PostMapping("/saveAll")
    public Messenger saveAll(@RequestBody List<DiaryDTO> diaryDTOList) {
        return diaryService.saveAll(diaryDTOList);
    }

    @PutMapping
    public Messenger update(@RequestBody DiaryDTO diaryDTO) {
        return diaryService.update(diaryDTO);
    }

    @DeleteMapping
    public Messenger delete(@RequestBody DiaryDTO diaryDTO) {
        return diaryService.delete(diaryDTO);
    }

}
