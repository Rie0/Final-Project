package org.twspring.noob.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.twspring.noob.Api.ApiException;
import org.twspring.noob.DTO.CoachDTO;
import org.twspring.noob.Model.Coach;
import org.twspring.noob.Model.User;
import org.twspring.noob.Repository.AuthRepository;
import org.twspring.noob.Repository.CoachRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoachService {

    private final CoachRepository coachRepository;
    private final AuthRepository userRepository;

    // Get all coaches
    public List<Coach> getAllCoaches() {
        return coachRepository.findAll();
    }

    // Register a new coach
    public void registerCoach(CoachDTO coachDTO) {
        // Create and save the User entity
        User user = new User();
        String hash = new BCryptPasswordEncoder().encode(coachDTO.getPassword());

        user.setUsername(coachDTO.getUsername());
        user.setPassword(hash);
        user.setEmail(coachDTO.getEmail());
        user.setPhoneNumber(coachDTO.getPhoneNumber());
        user.setRole("COACH");
        userRepository.save(user);

        // Create and save the Coach entity
        Coach coach = new Coach();
        coach.setName(coachDTO.getName());
        coach.setBio(coachDTO.getBio());
        coach.setExpertise(coachDTO.getExpertise());
        coach.setUser(user);
        coachRepository.save(coach);
    }

    // Update an existing coach
    public void updateCoach(Integer coachId, CoachDTO coachDTO) {
        Coach existingCoach = coachRepository.findCoachById(coachId);
        if (existingCoach == null) {
            throw new ApiException("Coach not found");
        }

        User existingUser = existingCoach.getUser();
        String hash = new BCryptPasswordEncoder().encode(coachDTO.getPassword());

        existingUser.setUsername(coachDTO.getUsername());
        existingUser.setPassword(hash);
        existingUser.setEmail(coachDTO.getEmail());
        existingUser.setPhoneNumber(coachDTO.getPhoneNumber());
        userRepository.save(existingUser);

        existingCoach.setName(coachDTO.getName());
        existingCoach.setBio(coachDTO.getBio());
        existingCoach.setExpertise(coachDTO.getExpertise());
        coachRepository.save(existingCoach);
    }

    // Delete a coach
    public void deleteCoach(Integer coachId) {
        Coach coach = coachRepository.findCoachById(coachId);
        if (coach == null) {
            throw new ApiException("Coach not found");
        }

        userRepository.deleteById(coach.getUser().getId());
        coachRepository.deleteById(coachId);
    }

    // Get a coach by id
    public Coach getCoachById(Integer coachId) {
        Coach coach = coachRepository.findCoachById(coachId);
        if (coach == null) {
            throw new ApiException("Coach not found");
        }
        return coach;
    }
}
