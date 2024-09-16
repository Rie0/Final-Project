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

    //EXTRA

    @GetMapping("/get/{playerId}")
    public ResponseEntity getPlayer(@PathVariable Integer playerId){
        return ResponseEntity.status(200).body(playerService.getPlayerById(playerId));
    }

    @PutMapping("/{playerId}/edit-bio")
    public ResponseEntity editBio(@PathVariable Integer playerId, @RequestBody String bio){
        playerService.updateBio(playerId, bio);
        return ResponseEntity.status(200).body(new ApiResponse("Player bio updated successfully"));
    }

    @GetMapping("/{playerId}/invites/get-invites")
    public ResponseEntity getInvites(@PathVariable Integer playerId){
        return ResponseEntity.status(200).body(playerService.getInvitesByPlayerId(playerId));
    }

    @PutMapping("/{playerId}/invites/{inviteId}/accept")
    public ResponseEntity acceptInvite(@PathVariable Integer playerId, @PathVariable Integer inviteId){
        playerService.acceptInvite(playerId, inviteId);
        return ResponseEntity.status(200).body(new ApiResponse("Invite accepted successfully, you're now a member of the team"));
    }

    @PutMapping("/{playerId}/invites/{inviteId}/decline")
    public ResponseEntity declineInvite(@PathVariable Integer playerId, @PathVariable Integer inviteId) {
        playerService.declineInvite(playerId, inviteId);
        return ResponseEntity.status(200).body(new ApiResponse("Invite declined successfully"));
    }

    @PutMapping("/{playerId}/team/leave")
    public ResponseEntity leaveTeam(@PathVariable Integer playerId){
        playerService.leaveTeam(playerId);
        return ResponseEntity.status(200).body(new ApiResponse("You left the team successfully"));
    }

}