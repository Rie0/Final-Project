package org.twspring.noob.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.twspring.noob.Api.ApiResponse;
import org.twspring.noob.DTO.MultiInviteDTO;
import org.twspring.noob.DTO.TeamDTO;
import org.twspring.noob.DTO.TeamInviteDTO;
import org.twspring.noob.Model.User;
import org.twspring.noob.Service.TeamService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/team")
@RequiredArgsConstructor
public class TeamController { //RAFEEF
    private final TeamService teamService;

    // Public endpoints
    @GetMapping("/get-all")
    public ResponseEntity getAllTeams() {
        return ResponseEntity.status(200).body(teamService.getAllTeams());
    }

    @PostMapping("/register")
    public ResponseEntity registerTeam(@RequestBody@Valid TeamDTO teamDTO) {
        teamService.registerTeam(teamDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Team registered successfully"));
    }

    // Team-related actions requiring authentication
    @PutMapping("/update-my-info")
    public ResponseEntity updateMyInfo(@AuthenticationPrincipal User team, @RequestBody @Valid TeamDTO teamDTO) {
        teamService.updateTeam(team.getId(), teamDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Team updated successfully"));
    }

    @DeleteMapping("/delete-my-account")
    public ResponseEntity deleteMyAccount(@AuthenticationPrincipal User team) {
        teamService.deleteTeam(team.getId());
        return ResponseEntity.status(200).body(new ApiResponse("Team deleted successfully"));
    }

    //EXTRA
    @GetMapping("/get/{teamId}")
    public ResponseEntity getTeamById(@PathVariable Integer teamId) {
        return ResponseEntity.status(200).body(teamService.getTeamById(teamId));
    }
    @GetMapping("/get/profile/{teamId}")
    public ResponseEntity getTeamProfile(@PathVariable Integer teamId) {
        return ResponseEntity.status(200).body(teamService.getTeamProfileById(teamId));
    }
    // Player management
    @GetMapping("/players/get-all")
    public ResponseEntity getAllTeamPlayers(@AuthenticationPrincipal User team) {
        return ResponseEntity.status(200).body(teamService.getPlayersByTeamId(team.getId()));
    }

    // Invite management
    @GetMapping("/invites/get-invites")
    public ResponseEntity getInvites(@AuthenticationPrincipal User team) {
        return ResponseEntity.status(200).body(teamService.getInvitesByTeamId(team.getId()));
    }

    @PostMapping("/invites/invite/{PlayerUsername}")
    public ResponseEntity invitePlayerToTeam(@AuthenticationPrincipal User team,
                                             @PathVariable String PlayerUsername,
                                             @RequestBody @Valid TeamInviteDTO teamInviteDTO) {
        teamService.invitePlayerToTeam(team.getId(), PlayerUsername, teamInviteDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Player invited successfully"));
    }

    @PostMapping("/invites/invite-multiple-players")
    public ResponseEntity inviteMultiplePlayersToTeam(@RequestBody MultiInviteDTO multiInviteDTO,
                                                      @AuthenticationPrincipal User team) {
        teamService.inviteMultiPlayersToTeam(team.getId(), multiInviteDTO);
        return ResponseEntity.status(200).body("Players invited to the team successfully");
    }




    @PutMapping("/update-bio")
    public ResponseEntity updateBio(@AuthenticationPrincipal User team, @RequestBody String bio) {
        teamService.updateBio(team.getId(), bio);
        return ResponseEntity.status(200).body(new ApiResponse("Team updated successfully"));
    }

    @DeleteMapping("/invites/{inviteId}/delete")
    public ResponseEntity deleteTeamInvite(@AuthenticationPrincipal User team,
                                           @PathVariable Integer inviteId) {
        teamService.deleteTeamInvite(team.getId(), inviteId);
        return ResponseEntity.status(200).body(new ApiResponse("Invite deleted successfully"));
    }
}
