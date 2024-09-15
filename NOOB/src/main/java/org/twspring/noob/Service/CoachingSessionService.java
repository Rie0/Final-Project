package org.twspring.noob.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.twspring.noob.Api.ApiException;
import org.twspring.noob.Model.CoachingSession;
import org.twspring.noob.Repository.CoachingSessionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoachingSessionService {

    private final CoachingSessionRepository coachingSessionRepository;

    // CRUD get all
    public List<CoachingSession> getAllCoachingSessions() {
        return coachingSessionRepository.findAll();
    }

    // CRUD register
    public void addCoachingSession(CoachingSession coachingSession) {
        coachingSessionRepository.save(coachingSession);
    }

    // CRUD update
    public void updateCoachingSession(Integer id, CoachingSession coachingSessionDetails) {
        CoachingSession coachingSession = coachingSessionRepository.findCoachingSessionById(id);
        if (coachingSession == null) {
            throw new ApiException("Coaching session not found");
        }
        coachingSession.setScheduledTime(coachingSessionDetails.getScheduledTime());
        coachingSession.setSessionType(coachingSessionDetails.getSessionType());
        coachingSession.setFeedBack(coachingSessionDetails.getFeedBack());
        coachingSession.setPricing(coachingSessionDetails.getPricing());
        coachingSession.setCoach(coachingSessionDetails.getCoach());
        coachingSessionRepository.save(coachingSession);
    }

    // CRUD delete
    public void deleteCoachingSession(Integer id) {
        CoachingSession coachingSession = coachingSessionRepository.findCoachingSessionById(id);
        if (coachingSession == null) {
            throw new ApiException("Coaching session not found");
        }
        coachingSessionRepository.delete(coachingSession);
    }

    // EXTRA ENDPOINT: getting a coaching session by id
    public CoachingSession getCoachingSessionById(Integer id) {
        CoachingSession coachingSession = coachingSessionRepository.findCoachingSessionById(id);
        if (coachingSession == null) {
            throw new ApiException("Coaching session not found");
        }
        return coachingSession;
    }



}