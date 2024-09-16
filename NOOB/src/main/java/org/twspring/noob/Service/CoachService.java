package org.twspring.noob.Service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.twspring.noob.Api.ApiException;
import org.twspring.noob.DTO.ProfileDTO;
import org.twspring.noob.Model.Coach;
import org.twspring.noob.Repository.CoachRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoachService {


    private final CoachRepository coachRepository;


    // CRUD get all
    public List<Coach> getAllCoaches() {
        return coachRepository.findAll();
    }

    // CRUD register
    public void addCoach(Coach coach) {
        coachRepository.save(coach);
    }

    // CRUD update
    public void updateCoach(Integer id, Coach coachDetails) {
        Coach coach = coachRepository.findCoachById(id);
        if (coach == null) {
            throw new ApiException("Coach not found");
        }
        coach.setName(coachDetails.getName());
        coach.setExpertise(coachDetails.getExpertise());
        coachRepository.save(coach);
    }

    // CRUD delete
    public void deleteCoach(Integer id) {
        Coach coach = coachRepository.findCoachById(id);
        if (coach == null) {
            throw new ApiException("Coach not found");
        }
        coachRepository.delete(coach);
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

}
