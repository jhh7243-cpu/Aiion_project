package site.aiion.api.soccer.stadium.service;

import java.util.List;
import site.aiion.api.soccer.stadium.domain.StadiumDTO;
import site.aiion.api.common.domain.Messenger;

public interface StadiumService {
    public Messenger findById(StadiumDTO stadiumDTO);
    public Messenger findAll();
    public Messenger save(StadiumDTO stadiumDTO);
    public Messenger saveAll(List<StadiumDTO> stadiumDTOList);
    public Messenger update(StadiumDTO stadiumDTO);
    public Messenger delete(StadiumDTO stadiumDTO);
}
