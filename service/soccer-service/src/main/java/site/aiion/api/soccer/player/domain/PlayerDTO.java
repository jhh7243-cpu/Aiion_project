package site.aiion.api.soccer.player.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerDTO {
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    public Long id;
    
    public String player_uk;
    
    public String player_name;
    
    public String e_player_name;
    
    public String nickname;
    
    public String join_yyyy;
    
    public String position;
    
    public String back_no;
    
    public String nation;
    
    public String birth_date;
    
    public String solar;
    
    public String height;
    
    public String weight;
    
    public String team_uk;
}
