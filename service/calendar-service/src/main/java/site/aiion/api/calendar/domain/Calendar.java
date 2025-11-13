package site.aiion.api.calendar.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "calendars")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Calendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   // PK

    @Column(nullable = false, length = 100)
    private String name;       // 캘린더 이름

    @Column(columnDefinition = "TEXT")
    private String description; // 설명

    private String color;       // UI 색상
    private String timezone;    // 타임존

    @Column(name = "user_id", nullable = false)
    private Long userId;  // 마이크로서비스 간 참조는 ID만 사용

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
