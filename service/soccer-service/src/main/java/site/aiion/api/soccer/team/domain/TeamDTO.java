package site.aiion.api.soccer.team.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamDTO {
    public Long id;
    public String team_uk;
    public String region_name;
    public String team_name;
    public String e_team_name;
    public String orig_yyyy;
    public String zip_code1;
    public String zip_code2;
    public String address;
    public String ddd;
    public String tel;
    public String fax;
    public String homepage;
    public String owner;
    public String stadium_uk;
}
