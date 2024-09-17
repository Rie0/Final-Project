package org.twspring.noob.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.twspring.noob.Api.ApiResponse;
import org.twspring.noob.DTO.PlayerDTO;
import org.twspring.noob.Model.User;
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
    @PutMapping("update-my-info")
    public ResponseEntity updateMyInfo(@AuthenticationPrincipal User player, @RequestBody@Valid  PlayerDTO playerDTO){
        playerService.updatePlayer(player.getId(), playerDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Player updated successfully"));
    }
    @DeleteMapping("delete-my-account")
    public ResponseEntity deleteMyAccount(@AuthenticationPrincipal User player){
        playerService.deletePlayer(player.getId());
        return ResponseEntity.status(200).body(new ApiResponse("Player deleted successfully"));
    }

    //EXTRA

    @GetMapping("/get/{playerId}")
    public ResponseEntity getPlayer(@PathVariable Integer playerId){
        return ResponseEntity.status(200).body(playerService.getPlayerById(playerId));
    }

    @GetMapping("/get/profile/{playerId}")
    public ResponseEntity getPlayerProfile(@PathVariable Integer playerId){
        return ResponseEntity.status(200).body(playerService.getPlayerProfileById(playerId));
    }

    @GetMapping("/get/profile/by-username/{username}")
    public ResponseEntity getPlayerProfiles(@PathVariable String username){
        return ResponseEntity.status(200).body(playerService.getPlayerProfilesByUsernameContaining(username));
    }

    @PutMapping("/edit-bio")
    public ResponseEntity editBio(@AuthenticationPrincipal User player, @RequestBody String bio){
        playerService.updateBio(player.getId(), bio);
        return ResponseEntity.status(200).body(new ApiResponse("Player bio updated successfully"));
    }

    @GetMapping("/invites/get-invites")
    public ResponseEntity getInvites(@AuthenticationPrincipal User player){
        return ResponseEntity.status(200).body(playerService.getInvitesByPlayerId(player.getId()));
    }

    @PutMapping("/invites/{inviteId}/accept")
    public ResponseEntity acceptInvite(@AuthenticationPrincipal User player, @PathVariable Integer inviteId){
        playerService.acceptInvite(player.getId(), inviteId);
        return ResponseEntity.status(200).body(new ApiResponse("Invite accepted successfully, you're now a member of the team"));
    }

    @PutMapping("/invites/{inviteId}/decline")
    public ResponseEntity declineInvite(@AuthenticationPrincipal User player, @PathVariable Integer inviteId) {
        playerService.declineInvite(player.getId(), inviteId);
        return ResponseEntity.status(200).body(new ApiResponse("Invite declined successfully"));
    }

    @PutMapping("/team/leave")
    public ResponseEntity leaveTeam(@AuthenticationPrincipal User player){
        playerService.leaveTeam(player.getId());
        return ResponseEntity.status(200).body(new ApiResponse("You left the team successfully"));
    }

}