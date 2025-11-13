package site.aiion.api.soccer.soccersearch.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import site.aiion.api.common.domain.Messenger;
import site.aiion.api.soccer.soccersearch.domain.SearchRequestDTO;
import site.aiion.api.soccer.soccersearch.service.SoccerSearchFacade;

/**
 * 통합 검색 컨트롤러
 * Facade 패턴을 통해 player, team, schedule, stadium에 대한 검색을 제공
 * React에서 axios로 연결 가능
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/soccer")
@CrossOrigin(origins = "http://localhost:3000")
public class SoccerSearchController {

    private final SoccerSearchFacade soccerSearchFacade;

    /**
     * 검색어로 통합 검색 수행 (GET 요청)
     * React에서 axios.get()으로 호출 가능
     * 
     * @param type 검색 타입 (player, team, schedule, stadium)
     * @param keyword 검색어 (어떤 검색어든 가능)
     * @return 검색 결과 (Messenger 객체 - message 필드에 알럿 메시지 포함)
     */
    @GetMapping("/findByWord")
    public Messenger findByWordGet(
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "keyword", required = false) String keyword) {
        
        SearchRequestDTO request = new SearchRequestDTO();
        request.setType(type);
        request.setKeyword(keyword);
        
        return soccerSearchFacade.findByWord(request);
    }

    /**
     * 검색어로 통합 검색 수행 (POST 요청)
     * React에서 axios.post()로 호출 가능
     * 
     * @param request 검색 요청 (type, keyword)
     * @return 검색 결과 (Messenger 객체 - message 필드에 알럿 메시지 포함)
     */
    @PostMapping("/findByWord")
    public Messenger findByWordPost(@RequestBody SearchRequestDTO request) {
        
        if (request == null) {
            request = new SearchRequestDTO();
        }
        
        return soccerSearchFacade.findByWord(request);
    }
}

