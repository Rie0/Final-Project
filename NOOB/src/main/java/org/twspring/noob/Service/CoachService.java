package org.twspring.noob.Service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.twspring.noob.Api.ApiException;
import org.twspring.noob.Model.Coach;
import org.twspring.noob.Repository.CoachRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoachService {


    private final CoachRepository coachRepository;

    public List<Coach> getAllCoaches() {
        return coachRepository.findAll();
    }

    public void addCoach(Coach coach) {
        coachRepository.save(coach);
    }

    public void updateCoach(Integer id, Coach coachDetails) {
        Coach coach = coachRepository.findById(id).orElseThrow(() -> new ApiException("Coach not found"));
        coach.setName(coachDetails.getName());
        coach.setExpertise(coachDetails.getExpertise());
        coachRepository.save(coach);
    }

    public void deleteCoach(Integer id) {
        Coach coach = coachRepository.findById(id).orElseThrow(() -> new ApiException("Coach not found"));
        coachRepository.delete(coach);
    }

    public Coach getCoachById(Integer id) {
        return coachRepository.findById(id).orElseThrow(() -> new ApiException("Coach not found"));
    }

}
