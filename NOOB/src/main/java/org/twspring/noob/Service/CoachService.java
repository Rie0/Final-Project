package org.twspring.noob.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.twspring.noob.Api.ApiException;
import org.twspring.noob.DTO.ProfileDTO;
import org.twspring.noob.DTO.CoachDTO;
import org.twspring.noob.Model.Coach;
import org.twspring.noob.Model.Review;
import org.twspring.noob.Model.User;
import org.twspring.noob.Repository.AuthRepository;
import org.twspring.noob.Repository.CoachRepository;
import org.twspring.noob.Repository.CoachingSessionRepository;
import org.twspring.noob.Repository.ReviewRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoachService {

    private final CoachRepository coachRepository;
    private final AuthRepository userRepository;
    private final AuthRepository authRepository;
    private final ReviewRepository reviewRepository;

    // CRUD get all
    public List<Coach> getAllCoaches() {
        return coachRepository.findAll();
    }

    // CRUD register
    public void addCoach(CoachDTO coachDTO) {
        User user = new User();
        user.setUsername(coachDTO.getUsername());
        user.setPassword(coachDTO.getPassword());
        user.setEmail(coachDTO.getEmail());
        user.setPhoneNumber(coachDTO.getPhoneNumber());
        user.setRole("COACH");

        Coach coach = new Coach();
        coach.setUser(user);
        coach.setName(coachDTO.getName());
        coach.setBio(coachDTO.getBio());
        coach.setExpertise(coachDTO.getExpertise());
        coach.setHourlyRate(coachDTO.getHourlyRate());

        user.setCoach(coach); // Set the bidirectional relationship
        userRepository.save(user);
    }

    // CRUD update
    public void updateCoach(Integer id, CoachDTO coachDTO) {
        Coach coach = coachRepository.findCoachById(id);
        if (coach == null) {
            throw new ApiException("Coach not found");
        }
        User user = coach.getUser();
        user.setUsername(coachDTO.getUsername());
        user.setPassword(coachDTO.getPassword());
        user.setEmail(coachDTO.getEmail());
        user.setPhoneNumber(coachDTO.getPhoneNumber());
        coach.setName(coachDTO.getName());
        coach.setBio(coachDTO.getBio());
        coach.setExpertise(coachDTO.getExpertise());
        coach.setHourlyRate(coachDTO.getHourlyRate());
        userRepository.save(user); // Save both coach and user details
    }

    // CRUD delete
    public void deleteCoach(Integer id) {
        Coach coach = coachRepository.findCoachById(id);
        if (coach == null) {
            throw new ApiException("Coach not found");
        }
        User user = authRepository.findUserById(id);
        authRepository.delete(user);
    }

    // EXTRA endpoint: getting a coach by id
    public Coach getCoachById(Integer id) {
        Coach coach = coachRepository.findCoachById(id);
        if (coach == null) {
            throw new ApiException("Coach not found");
        }
        return coach;
    }

    public void updateBio(Integer id, String bio) {
        Coach coach = coachRepository.findCoachById(id);
        coach.setBio(bio);
    }

    public void getCoachProfile(Integer id) {
        Coach coach = coachRepository.findCoachById(id);
        if (coach == null) {
            throw new ApiException("Coach not found");
        }
       // ProfileDTO profileDTO = new ProfileDTO(coach.getUser(), coach.getBio());
    }
    public List<Coach> getCoachesByHourlyRateRange(Integer minRate, Integer maxRate) {
        if (minRate == null || maxRate == null || minRate < 0 || maxRate < 0 || minRate > maxRate) {
            throw new ApiException("Invalid hourly rate range");
        }
        return coachRepository.findByHourlyRateBetween(minRate, maxRate);
    }


    public List<Review> getAllReviewsByCoach(Integer coachId) {
        return reviewRepository.findByCoachId(coachId);
    }

}
