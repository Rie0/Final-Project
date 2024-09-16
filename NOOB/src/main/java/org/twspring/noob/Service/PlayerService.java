package org.twspring.noob.Service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.twspring.noob.Api.ApiException;
import org.twspring.noob.DTO.PlayerDTO;
import org.twspring.noob.DTO.PlayerProfileDTO;
import org.twspring.noob.Model.*;
import org.twspring.noob.Repository.AuthRepository;
import org.twspring.noob.Repository.PlayerRepository;
import org.twspring.noob.Repository.TeamInviteRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final AuthRepository authRepository;
    private final TeamInviteRepository teamInviteRepository;

    //START OF CRUD
    public List<Player> getPlayers() {
        return playerRepository.findAll();
    }

    public void registerPlayer(PlayerDTO playerDTO) {
        String hash= new BCryptPasswordEncoder().encode(playerDTO.getPassword());
        User user = new User();
        user.setUsername(playerDTO.getUsername());
        user.setPassword(hash);
        user.setEmail(playerDTO.getEmail());
        user.setPhoneNumber(playerDTO.getPhoneNumber());
        user.setBirthday(playerDTO.getBirthday());
        user.setAge(Period.between(user.getBirthday(), LocalDate.now()).getYears());
        if (user.getAge()<13){
            throw new ApiException("Players under the age of 13 are prohibited from registering in our system");
        }
        user.setRole("PLAYER");
        authRepository.save(user);

        Player player = new Player();
        player.setUser(user);
        playerRepository.save(player);

    }
    public void updatePlayer(Integer playerId,PlayerDTO playerDTO) {
        Player oldPlayer = playerRepository.findPlayerById(playerId);
        User oldUser = oldPlayer.getUser();

        String hash= new BCryptPasswordEncoder().encode(playerDTO.getPassword());

        oldUser.setEmail(playerDTO.getEmail());
        oldUser.setPassword(hash);
        oldUser.setPhoneNumber(playerDTO.getPhoneNumber());
        authRepository.save(oldUser);
    }

    public void deletePlayer(Integer playerId) {
        authRepository.deleteById(playerId);
    }
    //END OF CRUD

    //GENERAL EXTRA
    public Player getPlayerById(Integer playerId) {
        Player player = playerRepository.findPlayerById(playerId);
        if (player == null) {throw new ApiException("Player not found");}
        return player;
    }

    public PlayerProfileDTO getPlayerProfile(Integer playerId) {
        Player player= playerRepository.findPlayerById(playerId);
        if (player == null) {
            throw new ApiException("Player not found");
        }
        return new PlayerProfileDTO(player.getUser().getUsername(),player.getUser().getAge(),player.getBio(),player.getTeam().getUser().getUsername());
    }

    public void updateBio(Integer playerId, String bio) {
        Player player = playerRepository.findPlayerById(playerId);

        player.setBio(bio);
        playerRepository.save(player);
    }

    //PLAYER INVITES
    public List<TeamInvite> getInvitesByPlayerId(Integer playerId) {
        return teamInviteRepository.findTeamInvitesByPlayerId(playerId);
    }

    public void acceptInvite(Integer playerId, Integer teamInviteId) {
        TeamInvite invite = teamInviteRepository.findTeamInviteById(teamInviteId);
        Player player = playerRepository.findPlayerById(playerId);

        if (invite==null){
            throw new ApiException("No invite found");
        }
        if (!invite.getPlayer().equals(player)){
            throw new ApiException("Player doesn't own this invite");
        }
        if (player.getTeam()!=null){
            throw new ApiException("You are already in a team, leave your current team to accept this invite");
        }
        if (invite.getStatus()!= TeamInvite.Status.PENDING){
            throw new ApiException("Only pending invites can be accepted");
        }
        invite.setStatus(TeamInvite.Status.APPROVED);
        teamInviteRepository.save(invite);

        //ADD PLAYER TO TEAM
        player.setTeam(invite.getTeam());
        player.setJoinedTeamAt(LocalDateTime.now());
        playerRepository.save(player);
    }

    public void declineInvite(Integer playerId, Integer teamInviteId) {
        TeamInvite invite = teamInviteRepository.findTeamInviteById(teamInviteId);
        Player player = playerRepository.findPlayerById(playerId);

        if (invite==null){
            throw new ApiException("No invite found");
        }
        if (!invite.getPlayer().equals(player)){
            throw new ApiException("Player doesn't own this invite");
        }
        if (invite.getStatus()!= TeamInvite.Status.PENDING){
            throw new ApiException("Only pending invites can be declined");
        }

        invite.setStatus(TeamInvite.Status.DECLINED);
        teamInviteRepository.save(invite);
    }

    //TEAM
    public void leaveTeam(Integer playerId) {
        Player player = playerRepository.findPlayerById(playerId);
        if (player.getTeam()==null){
            throw new ApiException("You are not in a team");
        }
        player.setTeam(null);
        player.setJoinedTeamAt(null);
        playerRepository.save(player);
    }
}
