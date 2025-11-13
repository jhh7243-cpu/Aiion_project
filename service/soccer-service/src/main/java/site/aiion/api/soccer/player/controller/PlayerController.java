package site.aiion.api.soccer.player.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import site.aiion.api.common.domain.Messenger;
import site.aiion.api.soccer.player.domain.PlayerDTO;
import site.aiion.api.soccer.player.service.PlayerService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/players")
public class PlayerController {

    private final PlayerService playerService;

    @PostMapping("/findById")
    public Messenger findById(@RequestBody PlayerDTO playerDTO) {
        return playerService.findById(playerDTO);
    }

    @GetMapping
    public Messenger findAll() {
        return playerService.findAll();
    }

    @PostMapping
    public Messenger save(@RequestBody PlayerDTO playerDTO) {
        return playerService.save(playerDTO);
    }

    @PostMapping("/saveAll")
    public Messenger saveAll(@RequestBody List<PlayerDTO> playerDTOList) {
        return playerService.saveAll(playerDTOList);
    }

    @PutMapping
    public Messenger update(@RequestBody PlayerDTO playerDTO) {
        return playerService.update(playerDTO);
    }

    @DeleteMapping
    public Messenger delete(@RequestBody PlayerDTO playerDTO) {
        return playerService.delete(playerDTO);
    }

}
