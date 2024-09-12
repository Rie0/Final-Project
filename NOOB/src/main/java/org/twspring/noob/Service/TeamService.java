package org.twspring.noob.Service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.twspring.noob.DTO.TeamDTO;
import org.twspring.noob.Model.Team;
import org.twspring.noob.Model.User;
import org.twspring.noob.Repository.AuthRepository;
import org.twspring.noob.Repository.TeamRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final AuthRepository authRepository;

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
}
