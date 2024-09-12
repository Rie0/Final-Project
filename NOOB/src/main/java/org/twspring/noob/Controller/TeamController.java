package org.twspring.noob.Controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.twspring.noob.Api.ApiResponse;
import org.twspring.noob.DTO.TeamDTO;
import org.twspring.noob.DTO.TeamInviteDTO;
import org.twspring.noob.Service.TeamService;

@RestController
@RequestMapping("/api/v1/team")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    @GetMapping("/get-all")
    public ResponseEntity GetAllTeams() {
        return ResponseEntity.status(200).body(teamService.getAllTeams());
    }
    @PostMapping("/register")
    public ResponseEntity registerTeam(@RequestBody@Valid TeamDTO teamDTO){
        teamService.registerTeam(teamDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Team registered successfully"));
    }
    @PutMapping("/{teamId}/update-my-info")
    public ResponseEntity updateMyInfo(@PathVariable Integer teamId, @RequestBody @Valid TeamDTO teamDTO){
        teamService.updateTeam(teamId, teamDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Team updated successfully"));
    }
    @DeleteMapping("/{teamId}/delete-my-account")
    public ResponseEntity deleteMyAccount(@PathVariable Integer teamId){
        teamService.deleteTeam(teamId);
        return ResponseEntity.status(200).body(new ApiResponse("Team deleted successfully"));
    }

    //INVITES
    @PostMapping("/{teamId}/invite/{PlayerUsername}")
    public ResponseEntity invitePlayerToTeam(@PathVariable Integer teamId,
                                             @PathVariable String PlayerUsername,
                                             @RequestBody@Valid TeamInviteDTO teamInviteDTO){
        teamService.invitePlayerToTeam(teamId,PlayerUsername,teamInviteDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Player invited successfully"));
    }
    @DeleteMapping("/{teamId}/invite/{inviteId}/delete")
    public ResponseEntity deleteTeamInvite(@PathVariable Integer teamId,
                                           @PathVariable Integer inviteId){
        teamService.deleteTeamInvite(teamId,inviteId);
        return ResponseEntity.status(200).body(new ApiResponse("Invite deleted successfully"));
    }
}