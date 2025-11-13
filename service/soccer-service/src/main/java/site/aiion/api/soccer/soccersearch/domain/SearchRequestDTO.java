package site.aiion.api.soccer.soccersearch.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 검색 요청 DTO
 * React에서 보내는 검색 요청을 받기 위한 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequestDTO {
    /**
     * 검색 타입: player, team, schedule, stadium
     */
    private String type;
    
    /**
     * 검색어 (어떤 검색어든 가능)
     */
    private String keyword;
}

