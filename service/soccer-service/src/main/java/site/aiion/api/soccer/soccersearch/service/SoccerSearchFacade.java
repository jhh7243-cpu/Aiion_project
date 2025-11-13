package site.aiion.api.soccer.soccersearch.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import site.aiion.api.common.domain.Messenger;
import site.aiion.api.soccer.player.domain.Player;
import site.aiion.api.soccer.player.domain.PlayerDTO;
import site.aiion.api.soccer.player.repository.PlayerRepository;
import site.aiion.api.soccer.schedule.domain.Schedule;
import site.aiion.api.soccer.schedule.domain.ScheduleDTO;
import site.aiion.api.soccer.schedule.repository.ScheduleRepository;
import site.aiion.api.soccer.soccersearch.domain.SearchRequestDTO;
import site.aiion.api.soccer.stadium.domain.Stadium;
import site.aiion.api.soccer.stadium.domain.StadiumDTO;
import site.aiion.api.soccer.stadium.repository.StadiumRepository;
import site.aiion.api.soccer.team.domain.Team;
import site.aiion.api.soccer.team.domain.TeamDTO;
import site.aiion.api.soccer.team.repository.TeamRepository;

/**
 * Facade 패턴을 사용한 통합 검색 서비스
 * player, team, schedule, stadium에 대한 검색을 단일 인터페이스로 제공
 * 어떤 검색어가 들어와도 처리 가능
 */
@Service
@RequiredArgsConstructor
public class SoccerSearchFacade {

    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final ScheduleRepository scheduleRepository;
    private final StadiumRepository stadiumRepository;

    /**
     * 검색 타입에 따라 적절한 서비스를 호출하여 검색 수행
     * Facade 패턴: 클라이언트는 이 메서드 하나만 호출하면 됨
     * 
     * @param request 검색 요청 (type, keyword)
     * @return 검색 결과
     */
    public Messenger findByWord(SearchRequestDTO request) {
        String type = request.getType();
        String keyword = request.getKeyword();
        
        // 콘솔에 검색 요청 정보 출력
        System.out.println("\n========================================");
        System.out.println("🔍 검색 요청이 들어왔습니다.");
        System.out.println("검색 타입: " + (type != null ? type : "null"));
        System.out.println("검색어: " + (keyword != null ? keyword : "null"));
        System.out.println("========================================\n");
        
        // 검색 타입 유효성 검사
        if (type == null || type.trim().isEmpty()) {
            System.out.println("⚠️ 알럿: 검색 타입이 필요합니다.");
            return Messenger.builder()
                    .Code(400)
                    .message("⚠️ 검색 타입이 필요합니다. (player, team, schedule, stadium 중 하나를 선택하세요)")
                    .data(null)
                    .build();
        }
        
        // 검색어 유효성 검사
        if (keyword == null || keyword.trim().isEmpty()) {
            System.out.println("⚠️ 알럿: 검색어가 필요합니다.");
            return Messenger.builder()
                    .Code(400)
                    .message("⚠️ 검색어를 입력해주세요.")
                    .data(null)
                    .build();
        }
        
        // 타입 소문자 변환 및 검증
        String normalizedType = type.toLowerCase().trim();
        String normalizedKeyword = keyword.trim();
        
        System.out.println("정규화된 검색 타입: " + normalizedType);
        System.out.println("정규화된 검색어: " + normalizedKeyword);
        
        // Facade 패턴: 타입에 따라 적절한 서브시스템 호출
        try {
            switch (normalizedType) {
                case "player":
                    return searchPlayer(normalizedKeyword);
                    
                case "team":
                    return searchTeam(normalizedKeyword);
                    
                case "schedule":
                    return searchSchedule(normalizedKeyword);
                    
                case "stadium":
                    return searchStadium(normalizedKeyword);
                    
                default:
                    System.out.println("❌ 알럿: 지원하지 않는 검색 타입입니다: " + normalizedType);
                    return Messenger.builder()
                            .Code(400)
                            .message("❌ 지원하지 않는 검색 타입입니다: '" + normalizedType + "' (player, team, schedule, stadium 중 하나를 선택하세요)")
                            .data(null)
                            .build();
            }
        } catch (Exception e) {
            System.out.println("❌ 검색 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return Messenger.builder()
                    .Code(500)
                    .message("❌ 검색 중 오류가 발생했습니다: " + e.getMessage())
                    .data(null)
                    .build();
        }
    }

    /**
     * Player 검색 (Facade 내부 메서드)
     */
    private Messenger searchPlayer(String keyword) {
        System.out.println("✅ Player 검색 서비스를 호출합니다. 검색어: " + keyword);
        List<Player> players = playerRepository.findByKeyword(keyword);
        
        List<PlayerDTO> playerDTOs = players.stream()
                .map(this::convertToPlayerDTO)
                .collect(Collectors.toList());
        
        System.out.println("검색 결과: " + playerDTOs.size() + "개의 선수를 찾았습니다.");
        
        return Messenger.builder()
                .Code(200)
                .message("✅ Player 검색 성공: '" + keyword + "'에 대한 검색 결과 (" + playerDTOs.size() + "개)")
                .data(playerDTOs)
                .build();
    }

    /**
     * Team 검색 (Facade 내부 메서드)
     */
    private Messenger searchTeam(String keyword) {
        System.out.println("✅ Team 검색 서비스를 호출합니다. 검색어: " + keyword);
        List<Team> teams = teamRepository.findByKeyword(keyword);
        
        List<TeamDTO> teamDTOs = teams.stream()
                .map(this::convertToTeamDTO)
                .collect(Collectors.toList());
        
        System.out.println("검색 결과: " + teamDTOs.size() + "개의 팀을 찾았습니다.");
        
        return Messenger.builder()
                .Code(200)
                .message("✅ Team 검색 성공: '" + keyword + "'에 대한 검색 결과 (" + teamDTOs.size() + "개)")
                .data(teamDTOs)
                .build();
    }

    /**
     * Schedule 검색 (Facade 내부 메서드)
     */
    private Messenger searchSchedule(String keyword) {
        System.out.println("✅ Schedule 검색 서비스를 호출합니다. 검색어: " + keyword);
        List<Schedule> schedules = scheduleRepository.findByKeyword(keyword);
        
        List<ScheduleDTO> scheduleDTOs = schedules.stream()
                .map(this::convertToScheduleDTO)
                .collect(Collectors.toList());
        
        System.out.println("검색 결과: " + scheduleDTOs.size() + "개의 일정을 찾았습니다.");
        
        return Messenger.builder()
                .Code(200)
                .message("✅ Schedule 검색 성공: '" + keyword + "'에 대한 검색 결과 (" + scheduleDTOs.size() + "개)")
                .data(scheduleDTOs)
                .build();
    }

    /**
     * Stadium 검색 (Facade 내부 메서드)
     */
    private Messenger searchStadium(String keyword) {
        System.out.println("✅ Stadium 검색 서비스를 호출합니다. 검색어: " + keyword);
        List<Stadium> stadiums = stadiumRepository.findByKeyword(keyword);
        
        List<StadiumDTO> stadiumDTOs = stadiums.stream()
                .map(this::convertToStadiumDTO)
                .collect(Collectors.toList());
        
        System.out.println("검색 결과: " + stadiumDTOs.size() + "개의 경기장을 찾았습니다.");
        
        return Messenger.builder()
                .Code(200)
                .message("✅ Stadium 검색 성공: '" + keyword + "'에 대한 검색 결과 (" + stadiumDTOs.size() + "개)")
                .data(stadiumDTOs)
                .build();
    }

    // Entity to DTO 변환 메서드들
    private PlayerDTO convertToPlayerDTO(Player player) {
        return PlayerDTO.builder()
                .id(player.getId())
                .player_uk(player.getPlayer_uk())
                .player_name(player.getPlayer_name())
                .e_player_name(player.getE_player_name())
                .nickname(player.getNickname())
                .join_yyyy(player.getJoin_yyyy())
                .position(player.getPosition())
                .back_no(player.getBack_no())
                .nation(player.getNation())
                .birth_date(player.getBirth_date())
                .solar(player.getSolar())
                .height(player.getHeight())
                .weight(player.getWeight())
                .team_uk(player.getTeam_uk())
                .build();
    }

    private TeamDTO convertToTeamDTO(Team team) {
        return TeamDTO.builder()
                .id(team.getId())
                .team_uk(team.getTeam_uk())
                .region_name(team.getRegion_name())
                .team_name(team.getTeam_name())
                .e_team_name(team.getE_team_name())
                .orig_yyyy(team.getOrig_yyyy())
                .zip_code1(team.getZip_code1())
                .zip_code2(team.getZip_code2())
                .address(team.getAddress())
                .ddd(team.getDdd())
                .tel(team.getTel())
                .fax(team.getFax())
                .homepage(team.getHomepage())
                .owner(team.getOwner())
                .stadium_uk(team.getStadium_uk())
                .build();
    }

    private ScheduleDTO convertToScheduleDTO(Schedule schedule) {
        return ScheduleDTO.builder()
                .id(schedule.getId())
                .sche_date(schedule.getSche_date())
                .stadium_uk(schedule.getStadium_uk())
                .gubun(schedule.getGubun())
                .hometeam_uk(schedule.getHometeam_uk())
                .awayteam_uk(schedule.getAwayteam_uk())
                .home_score(schedule.getHome_score())
                .away_score(schedule.getAway_score())
                .build();
    }

    private StadiumDTO convertToStadiumDTO(Stadium stadium) {
        return StadiumDTO.builder()
                .id(stadium.getId())
                .stadium_uk(stadium.getStadium_uk())
                .stadium_name(stadium.getStadium_name())
                .hometeam_uk(stadium.getHometeam_uk())
                .seat_count(stadium.getSeat_count())
                .address(stadium.getAddress())
                .ddd(stadium.getDdd())
                .tel(stadium.getTel())
                .build();
    }
}

