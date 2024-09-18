package org.twspring.noob.Service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.twspring.noob.Api.ApiException;
import org.twspring.noob.DTO.*;
import org.twspring.noob.Model.Player;
import org.twspring.noob.Model.Team;
import org.twspring.noob.Model.TeamInvite;
import org.twspring.noob.Model.User;
import org.twspring.noob.Repository.AuthRepository;
import org.twspring.noob.Repository.PlayerRepository;
import org.twspring.noob.Repository.TeamInviteRepository;
import org.twspring.noob.Repository.TeamRepository;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService { //RAFEEF

    private final TeamRepository teamRepository;
    private final AuthRepository authRepository;
    private final PlayerRepository playerRepository;
    private final TeamInviteRepository teamInviteRepository;

    //START OF CRUD
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public void registerTeam(TeamDTO teamDTO) {
        String hash= new BCryptPasswordEncoder().encode(teamDTO.getPassword());
        User user = new User();
        user.setUsername(teamDTO.getUsername());
        user.setPassword(hash);
        user.setEmail(teamDTO.getEmail());
        user.setPhoneNumber(teamDTO.getPhoneNumber());
        user.setBirthday(teamDTO.getBirthday());
        user.setAge(Period.between(user.getBirthday(), LocalDate.now()).getYears());
        if (user.getAge()<13){
            throw new ApiException("Players under the age of 13 are prohibited from registering in our system");
        }
        user.setRole("TEAM");
        authRepository.save(user);

        Team team = new Team();
        team.setUser(user);
        team.setWinnings(0.0);
        teamRepository.save(team);
    }

    public void updateTeam(Integer teamId,TeamDTO teamDTO) {
        Team oldTeam = teamRepository.findTeamById(teamId);
        User oldUser = oldTeam.getUser();

        String hash= new BCryptPasswordEncoder().encode(teamDTO.getPassword());

        oldUser.setEmail(teamDTO.getEmail());
        oldUser.setPassword(hash);
        oldUser.setPhoneNumber(teamDTO.getPhoneNumber());
        authRepository.save(oldUser);
        teamRepository.save(oldTeam);
    }

    public void deleteTeam(Integer teamId) {
        authRepository.deleteById(teamId);
    }
    //END OF CRUD

    //GENERAL EXTRA
    public Team getTeamById(Integer teamId) {
        Team team = teamRepository.findTeamById(teamId);
        if(team == null) {
            throw new ApiException("Team not found");
        }
        return team;
    }

    public ProfileDTO getTeamProfileById(Integer teamId){
        Team team = getTeamById(teamId);
        if(team == null) {
            throw new ApiException("Team not found");
        }
        return new ProfileDTO(team.getUser().getUsername(),team.getBio(),team.getUser().getJoinedAt());
    }

    public List<PlayerProfileDTO> getPlayersByTeamId(Integer teamId) {
        Team team = getTeamById(teamId);
        List<PlayerProfileDTO> teamPlayers = new ArrayList<>();
        for (Player player: playerRepository.findPlayerByTeamId(teamId)){
            PlayerProfileDTO teamPlayer = new PlayerProfileDTO(player.getUser().getUsername(),player.getUser().getAge(),player.getBio(),team.getUser().getUsername(),player.getUser().getJoinedAt());
            teamPlayers.add(teamPlayer);
        }
        return teamPlayers;
    }

    //START OF TEAM INVITES
    public List<TeamInvite> getInvitesByTeamId(Integer teamId) {
        return teamInviteRepository.findTeamInvitesByTeamId(teamId);
    }

    public void updateBio (Integer teamId, String bio) {
        Team team = teamRepository.findTeamById(teamId);
        team.setBio(bio);
        teamRepository.save(team);
    }

    public void invitePlayerToTeam(Integer teamId, String playerUsername, TeamInviteDTO teamInviteDTO) {
        Team team = teamRepository.findTeamById(teamId);
        User user = authRepository.findByUsername(playerUsername);

        if (user == null) {
            throw new ApiException("Player not found");
        }
        if (!user.getRole().equalsIgnoreCase("PLAYER")) {
            throw new ApiException("Only players can be invited to teams");
        }

        Player player = playerRepository.findPlayerById(user.getId());
        if (player.getTeam() != null) {
            if (player.getTeam().getId()==team.getId()){
                throw new ApiException("Player is already a member of this team");
            }
        }

        //create a team invite.
        TeamInvite teamInvite = new TeamInvite();
        teamInvite.setTeam(team);
        teamInvite.setPlayer(player);
        teamInvite.setTeamName(team.getUser().getUsername());
        teamInvite.setStatus(TeamInvite.Status.PENDING);
        teamInvite.setTitle(teamInviteDTO.getTitle());
        teamInvite.setMessage(teamInviteDTO.getMessage());
        teamInviteRepository.save(teamInvite);
    }

    public void inviteMultiPlayersToTeam(Integer teamId, MultiInviteDTO multiInviteDTO) {
        Team team = teamRepository.findTeamById(teamId);

        List<String> playerUsernames = multiInviteDTO.getPlayerUsernames();
        for (String playerUsername : playerUsernames) {
            User user = authRepository.findByUsername(playerUsername);

            if (user == null) {
                throw new ApiException("Player with username " + playerUsername + " not found");
            }

            if (!user.getRole().equals("PLAYER")) {
                throw new ApiException("Only players can be invited to teams, user " + user.getUsername() + " is not a player");
            }

            Player player = playerRepository.findPlayerById(user.getId());

            if (player == null) {
                throw new ApiException("Player not found for user " + playerUsername);
            }

            if (player.getTeam() != null) {
                if (player.getTeam().getId().equals(team.getId())) {
                    throw new ApiException("Player " + playerUsername + " is already a member of this team");
                } else {
                    throw new ApiException("Player " + playerUsername + " is already a member of another team");
                }
            }

            // Create a team invite
            TeamInvite teamInvite = new TeamInvite();
            teamInvite.setTeam(team);
            teamInvite.setPlayer(player);
            teamInvite.setStatus(TeamInvite.Status.PENDING);
            teamInvite.setTeamName(team.getUser().getUsername());
            teamInvite.setTitle(multiInviteDTO.getTitle());
            teamInvite.setMessage(multiInviteDTO.getMessage());

            teamInviteRepository.save(teamInvite);
        }
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
