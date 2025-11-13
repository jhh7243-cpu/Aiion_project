package site.aiion.api.diary.domain;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiaryDTO {
    private Long id;
    private LocalDate diaryDate;
    private String title;
    private String content;
}
