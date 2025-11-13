package site.aiion.api.diary.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import site.aiion.api.diary.domain.Diary;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {
    boolean existsByCalendarId(Long calendarId);

    boolean existsByDiaryDate(LocalDate diaryDate);

    Optional<Diary> findByDiaryDate(LocalDate diaryDate);
    
    List<Diary> findByCalendarId(Long calendarId);
}
