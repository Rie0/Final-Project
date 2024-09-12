package org.twspring.noob.Service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.twspring.noob.Api.ApiException;
import org.twspring.noob.DTO.PlayerDTO;
import org.twspring.noob.Model.Player;
import org.twspring.noob.Model.TeamInvite;
import org.twspring.noob.Model.User;
import org.twspring.noob.Repository.AuthRepository;
import org.twspring.noob.Repository.PlayerRepository;
import org.twspring.noob.Repository.TeamInviteRepository;

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
        //String hash= new BCryptPasswordEncoder().encode(customerDTO.getPassword());
        User user = new User();
        user.setUsername(playerDTO.getUsername());
        user.setPassword(playerDTO.getPassword());
        user.setEmail(playerDTO.getEmail());
        user.setPhoneNumber(playerDTO.getPhoneNumber());
        user.setRole("PLAYER");
        authRepository.save(user);

        Player player = new Player();
        player.setBio(playerDTO.getBio());
        player.setUser(user);
        playerRepository.save(player);

    }
    public void updatePlayer(Integer playerId,PlayerDTO playerDTO) {
        Player oldPlayer = playerRepository.findPlayerById(playerId);
        User oldUser = oldPlayer.getUser();

        //String hash= new BCryptPasswordEncoder().encode(customerDTO.getPassword());

        oldUser.setEmail(playerDTO.getEmail());
        oldUser.setPassword(playerDTO.getPassword());
        oldUser.setPhoneNumber(playerDTO.getPhoneNumber());
        authRepository.save(oldUser);

        oldPlayer.setBio(playerDTO.getBio());
        playerRepository.save(oldPlayer);
    }

    public void deletePlayer(Integer playerId) {
        authRepository.deleteById(playerId);
    }
    //END OF CRUD

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
        invite.setStatus(TeamInvite.Status.DECLINED);
        teamInviteRepository.save(invite);

        //ADD PLAYER TO TEAM
        
    }

    public void declineInvite(TeamInvite teamInvite) {}
}
