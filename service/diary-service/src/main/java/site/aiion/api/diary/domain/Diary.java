package site.aiion.api.diary.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "diaries")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Diary 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   // PK

    @Column(nullable = false)
    private LocalDate diaryDate; // 날짜 (year, month, day, weekday 모두 포함)

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "calendar_id", nullable = false)
    private Long calendarId;  // 마이크로서비스 간 참조는 ID만 사용

    // 분석 결과는 별도 서비스로 분리하거나, JSON으로 저장
    // @OneToMany 관계는 같은 서비스 내에서만 사용
}
