package site.aiion.api.soccer.player.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import site.aiion.api.soccer.player.domain.Player;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    
    /**
     * 검색어로 선수 검색 (이름, 영문명, 닉네임에서 검색)
     */
    @Query("SELECT p FROM Player p WHERE " +
           "LOWER(COALESCE(p.player_name, '')) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(COALESCE(p.e_player_name, '')) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(COALESCE(p.nickname, '')) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(COALESCE(p.position, '')) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(COALESCE(p.nation, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Player> findByKeyword(@Param("keyword") String keyword);
}

