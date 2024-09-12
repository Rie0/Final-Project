package org.twspring.noob.Service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.twspring.noob.Api.ApiException;
import org.twspring.noob.DTO.TeamDTO;
import org.twspring.noob.DTO.TeamInviteDTO;
import org.twspring.noob.Model.Player;
import org.twspring.noob.Model.Team;
import org.twspring.noob.Model.TeamInvite;
import org.twspring.noob.Model.User;
import org.twspring.noob.Repository.AuthRepository;
import org.twspring.noob.Repository.PlayerRepository;
import org.twspring.noob.Repository.TeamInviteRepository;
import org.twspring.noob.Repository.TeamRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final AuthRepository authRepository;
    private final PlayerRepository playerRepository;
    private final TeamInviteService teamInviteService;
    private final TeamInviteRepository teamInviteRepository;

    //START OF CRUD
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public void registerTeam(TeamDTO teamDTO) {
        //String hash= new BCryptPasswordEncoder().encode(customerDTO.getPassword());
        User user = new User();
        user.setUsername(teamDTO.getUsername());
        user.setPassword(teamDTO.getPassword());
        user.setEmail(teamDTO.getEmail());
        user.setPhoneNumber(teamDTO.getPhoneNumber());
        user.setRole("TEAM");
        authRepository.save(user);

        Team team = new Team();
        team.setBio(teamDTO.getBio());
        team.setWinnings(0.0);
        team.setUser(user);
        teamRepository.save(team);
    }

    public void updateTeam(Integer teamId,TeamDTO teamDTO) {
        Team oldTeam = teamRepository.findTeamById(teamId);
        User oldUser = oldTeam.getUser();

        //String hash= new BCryptPasswordEncoder().encode(customerDTO.getPassword());

        oldUser.setEmail(teamDTO.getEmail());
        oldUser.setPassword(teamDTO.getPassword());
        oldUser.setPhoneNumber(teamDTO.getPhoneNumber());
        authRepository.save(oldUser);

        oldTeam.setBio(teamDTO.getBio());
        teamRepository.save(oldTeam);
    }

    public void deleteTeam(Integer teamId) {
        authRepository.deleteById(teamId);
    }
    //END OF CRUD

    //START OF TEAM INVITES
    public List<TeamInvite> getInvitesByTeamId(Integer teamId) {
        return teamInviteRepository.findTeamInvitesByTeamId(teamId);
    }

    public void invitePlayerToTeam(Integer teamId, String playerUsername, TeamInviteDTO teamInviteDTO) {
        Team team = teamRepository.findTeamById(teamId);
        User user = authRepository.findByUsername(playerUsername);

        if (user == null) {
            throw new ApiException("Player not found");
        }
        if (!user.getRole().equals("PLAYER")) {
            throw new ApiException("Only players can be invited to teams");
        }

        Player player = playerRepository.findPlayerById(user.getId());
        if (player.getTeam()!=null){
            if (player.getTeam()==team){
                throw new ApiException("Player is already a member of this team");
            }
            throw new ApiException("Player is already a member of a team");
        }

        //create a team invite.
        TeamInvite teamInvite = new TeamInvite();
        teamInvite.setTeam(team);
        teamInvite.setPlayer(player);
        teamInvite.setStatus(TeamInvite.Status.PENDING);
        teamInvite.setTitle(teamInviteDTO.getTitle());
        teamInvite.setMessage(teamInviteDTO.getMessage());
        teamInviteRepository.save(teamInvite);
    }

    public void deleteTeamInvite(Integer teamId, Integer teamInviteId) {
        Team team = teamRepository.findTeamById(teamId);
        TeamInvite teamInvite = teamInviteRepository.findTeamInviteById(teamInviteId);

        if (!teamInvite.getTeam().equals(team)) {
            throw new ApiException("Team Invite does not belong to Team"); //throw unauthorized???
        }
        teamInviteRepository.delete(teamInvite);
    }
    //END OF TEAM INVITES

}
