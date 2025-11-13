package site.aiion.api.soccer.team.service;

import java.util.List;
import site.aiion.api.soccer.team.domain.TeamDTO;
import site.aiion.api.common.domain.Messenger;

public interface TeamService {
    public Messenger findById(TeamDTO teamDTO);
    public Messenger findAll();
    public Messenger save(TeamDTO teamDTO);
    public Messenger saveAll(List<TeamDTO> teamDTOList);
    public Messenger update(TeamDTO teamDTO);
    public Messenger delete(TeamDTO teamDTO);
}
