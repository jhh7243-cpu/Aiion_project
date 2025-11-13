package site.aiion.api.soccer.player.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.aiion.api.common.domain.Messenger;
import site.aiion.api.soccer.player.domain.PlayerDTO;
import site.aiion.api.soccer.player.domain.Player;
import site.aiion.api.soccer.player.repository.PlayerRepository;
import site.aiion.api.soccer.team.repository.TeamRepository;
import site.aiion.api.soccer.team.domain.Team;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    private PlayerDTO entityToDTO(Player entity) {
        return PlayerDTO.builder()
                .id(entity.getId())
                .player_uk(entity.getPlayer_uk())
                .player_name(entity.getPlayer_name())
                .e_player_name(entity.getE_player_name())
                .nickname(entity.getNickname())
                .join_yyyy(entity.getJoin_yyyy())
                .position(entity.getPosition())
                .back_no(entity.getBack_no())
                .nation(entity.getNation())
                .birth_date(entity.getBirth_date())
                .solar(entity.getSolar())
                .height(entity.getHeight())
                .weight(entity.getWeight())
                .team_uk(entity.getTeam_uk())
                .build();
    }

    private Player dtoToEntity(PlayerDTO dto) {
        Team team = null;
        if (dto.team_uk != null) {
            team = teamRepository.findByTeam_uk(dto.team_uk).orElse(null);
        }
        return Player.builder()
                .id(dto.id)
                .player_uk(dto.player_uk)
                .player_name(dto.player_name)
                .e_player_name(dto.e_player_name)
                .nickname(dto.nickname)
                .join_yyyy(dto.join_yyyy)
                .position(dto.position)
                .back_no(dto.back_no)
                .nation(dto.nation)
                .birth_date(dto.birth_date)
                .solar(dto.solar)
                .height(dto.height)
                .weight(dto.weight)
                .team_uk(dto.team_uk)
                .team(team)
                .build();
    }

    @Override
    public Messenger findById(PlayerDTO playerDTO) {
        Optional<Player> entity = playerRepository.findById(playerDTO.id);
        if (entity.isPresent()) {
            PlayerDTO dto = entityToDTO(entity.get());
            return Messenger.builder()
                    .Code(200)
                    .message("조회 성공")
                    .data(dto)
                    .build();
        } else {
            return Messenger.builder()
                    .Code(404)
                    .message("선수를 찾을 수 없습니다.")
                    .build();
        }
    }

    @Override
    public Messenger findAll() {
        List<Player> entities = playerRepository.findAll();
        List<PlayerDTO> dtoList = entities.stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList());
        return Messenger.builder()
                .Code(200)
                .message("전체 조회 성공: " + dtoList.size() + "개")
                .data(dtoList)
                .build();
    }

    @Override
    @Transactional
    public Messenger save(PlayerDTO playerDTO) {
        Player entity = dtoToEntity(playerDTO);
        Player saved = playerRepository.save(entity);
        PlayerDTO dto = entityToDTO(saved);
        return Messenger.builder()
                .Code(200)
                .message("저장 성공: " + saved.getId())
                .data(dto)
                .build();
    }

    @Override
    @Transactional
    public Messenger saveAll(List<PlayerDTO> playerDTOList) {
        List<Player> entities = playerDTOList.stream()
                .map(this::dtoToEntity)
                .collect(Collectors.toList());
        
        List<Player> saved = playerRepository.saveAll(entities);
        return Messenger.builder()
                .Code(200)
                .message("일괄 저장 성공: " + saved.size() + "개")
                .build();
    }

    @Override
    @Transactional
    public Messenger update(PlayerDTO playerDTO) {
        Optional<Player> optionalEntity = playerRepository.findById(playerDTO.id);
        if (optionalEntity.isPresent()) {
            Player existing = optionalEntity.get();
            Team team = playerDTO.team_uk != null 
                    ? teamRepository.findByTeam_uk(playerDTO.team_uk).orElse(existing.getTeam()) 
                    : existing.getTeam();
            
            Player updated = Player.builder()
                    .id(existing.getId())
                    .player_uk(playerDTO.player_uk != null ? playerDTO.player_uk : existing.getPlayer_uk())
                    .player_name(playerDTO.player_name != null ? playerDTO.player_name : existing.getPlayer_name())
                    .e_player_name(playerDTO.e_player_name != null ? playerDTO.e_player_name : existing.getE_player_name())
                    .nickname(playerDTO.nickname != null ? playerDTO.nickname : existing.getNickname())
                    .join_yyyy(playerDTO.join_yyyy != null ? playerDTO.join_yyyy : existing.getJoin_yyyy())
                    .position(playerDTO.position != null ? playerDTO.position : existing.getPosition())
                    .back_no(playerDTO.back_no != null ? playerDTO.back_no : existing.getBack_no())
                    .nation(playerDTO.nation != null ? playerDTO.nation : existing.getNation())
                    .birth_date(playerDTO.birth_date != null ? playerDTO.birth_date : existing.getBirth_date())
                    .solar(playerDTO.solar != null ? playerDTO.solar : existing.getSolar())
                    .height(playerDTO.height != null ? playerDTO.height : existing.getHeight())
                    .weight(playerDTO.weight != null ? playerDTO.weight : existing.getWeight())
                    .team_uk(playerDTO.team_uk != null ? playerDTO.team_uk : existing.getTeam_uk())
                    .team(team)
                    .build();
            
            Player saved = playerRepository.save(updated);
            PlayerDTO dto = entityToDTO(saved);
            return Messenger.builder()
                    .Code(200)
                    .message("수정 성공: " + playerDTO.id)
                    .data(dto)
                    .build();
        } else {
            return Messenger.builder()
                    .Code(404)
                    .message("수정할 선수를 찾을 수 없습니다.")
                    .build();
        }
    }

    @Override
    @Transactional
    public Messenger delete(PlayerDTO playerDTO) {
        Optional<Player> optionalEntity = playerRepository.findById(playerDTO.id);
        if (optionalEntity.isPresent()) {
            playerRepository.deleteById(playerDTO.id);
            return Messenger.builder()
                    .Code(200)
                    .message("삭제 성공: " + playerDTO.id)
                    .build();
        } else {
            return Messenger.builder()
                    .Code(404)
                    .message("삭제할 선수를 찾을 수 없습니다.")
                    .build();
        }
    }

}

