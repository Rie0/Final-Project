package org.twspring.noob.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.twspring.noob.Api.ApiResponse;
import org.twspring.noob.DTO.TeamDTO;
import org.twspring.noob.DTO.TeamInviteDTO;
import org.twspring.noob.Model.User;
import org.twspring.noob.Service.TeamService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/team")
@RequiredArgsConstructor
public class TeamController {
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

    @GetMapping("/get/{teamId}")
    public ResponseEntity getTeamById(@PathVariable Integer teamId) {
        return ResponseEntity.status(200).body(teamService.getTeamById(teamId));
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

//    @PostMapping("/{teamId}/invite-multiple-players") //not working yet
//    public ResponseEntity inviteMultiplePlayersToTeam(@AuthenticationPrincipal User team,
//                                                      @RequestBody @Valid TeamInviteDTO teamInviteDTO,
//                                                      @RequestBody List<String> playerUsernames) {
//        teamService.inviteMultiPlayersToTeam(team.getId(), playerUsernames, teamInviteDTO);
//        return ResponseEntity.status(200).body(new ApiResponse("Players invited successfully"));
//    }

    @DeleteMapping("/invites/{inviteId}/delete")
    public ResponseEntity deleteTeamInvite(@AuthenticationPrincipal User team,
                                           @PathVariable Integer inviteId) {
        teamService.deleteTeamInvite(team.getId(), inviteId);
        return ResponseEntity.status(200).body(new ApiResponse("Invite deleted successfully"));
    }
}
