package org.twspring.noob.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.twspring.noob.Api.ApiResponse;
import org.twspring.noob.DTO.PlayerDTO;
import org.twspring.noob.Service.PlayerService;

@RestController
@RequestMapping("/api/v1/player")
@RequiredArgsConstructor
public class PlayerController {
    private final PlayerService playerService;

    @GetMapping("/get-all")
    public ResponseEntity getAllPlayers(){
        return ResponseEntity.status(200).body(playerService.getPlayers());
    }

    @PostMapping("/register")
    public ResponseEntity registerPlayer(@RequestBody@Valid PlayerDTO playerDTO){
        playerService.registerPlayer(playerDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Player registered successfully"));
    }
    @PutMapping("/{playerId}/update-my-info")
    public ResponseEntity updateMyInfo(@PathVariable Integer playerId, @RequestBody@Valid  PlayerDTO playerDTO){
        playerService.updatePlayer(playerId, playerDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Player updated successfully"));
    }
    @DeleteMapping("/{playerId}/delete-my-account")
    public ResponseEntity deleteMyAccount(@PathVariable Integer playerId){
        playerService.deletePlayer(playerId);
        return ResponseEntity.status(200).body(new ApiResponse("Player deleted successfully"));
    }
}