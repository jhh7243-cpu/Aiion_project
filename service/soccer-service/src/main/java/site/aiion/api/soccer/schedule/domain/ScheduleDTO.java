package site.aiion.api.soccer.schedule.domain;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleDTO implements Serializable {
    public Long id;
    public String sche_date;
    public String stadium_uk;
    public String gubun;
    public String hometeam_uk;
    public String awayteam_uk;
    public String home_score;
    public String away_score;
}

