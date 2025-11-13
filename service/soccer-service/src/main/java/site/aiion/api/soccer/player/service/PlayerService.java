package site.aiion.api.soccer.player.service;

import java.util.List;
import site.aiion.api.soccer.player.domain.PlayerDTO;
import site.aiion.api.common.domain.Messenger;

public interface PlayerService {
    public Messenger findById(PlayerDTO playerDTO);
    public Messenger findAll();
    public Messenger save(PlayerDTO playerDTO);
    public Messenger saveAll(List<PlayerDTO> playerDTOList);
    public Messenger update(PlayerDTO playerDTO);
    public Messenger delete(PlayerDTO playerDTO);
}
