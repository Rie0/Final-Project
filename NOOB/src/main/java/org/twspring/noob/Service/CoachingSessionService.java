package org.twspring.noob.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.twspring.noob.Api.ApiException;
import org.twspring.noob.DTO.DateTimeDTO;
import org.twspring.noob.Model.*;
import org.twspring.noob.Repository.CoachingSessionRepository;
import org.twspring.noob.Repository.PlayerRepository;
import org.twspring.noob.Repository.ScheduleRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CoachingSessionService {

    private final CoachingSessionRepository coachingSessionRepository;
    private final ScheduleRepository scheduleRepository;
    private final PlayerRepository playerRepository;

    // CRUD get all
    public List<CoachingSession> getAllCoachingSessions() {
        return coachingSessionRepository.findAll();
    }

    // CRUD register String sessionStyle
    public void reserveCoachingSession(Integer scheduleId, Integer playerId, DateTimeDTO dateTimeDTO) {


        Player player = playerRepository.findPlayerById(playerId);
        if (player == null) {
            throw new ApiException("Player not found");
        }

        Schedule schedule = scheduleRepository.findScheduleById(scheduleId);
                if(schedule == null) {
                         new ApiException("Schedule not found");
                }
        Coach coach = schedule.getCoach();

        if (schedule.getIsBooked()) {
            throw new ApiException("Invalid or unavailable schedule for this coach");
        }

        CoachingSession coachingSession = new CoachingSession();

        coachingSession.setPlayer(player);
        coachingSession.setCoach(coach);
        coachingSession.setSchedule(schedule);
        //coachingSession.setSessionStyle(sessionStyle);
        coachingSession.setStartDate(dateTimeDTO.getStartDate());
        coachingSession.setEndDate(dateTimeDTO.getEndDate());

        schedule.setIsBooked(true);

        coachingSessionRepository.save(coachingSession);
        scheduleRepository.save(schedule);
    }
    // CRUD update
    public void updateCoachingSession(Integer id, CoachingSession coachingSessionDetails) {
        CoachingSession coachingSession = coachingSessionRepository.findCoachingSessionById(id);
        if (coachingSession == null) {
            throw new ApiException("Coaching session not found");
        }
       // coachingSession.setSessionStyle(coachingSession.getSessionStyle());
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

    public void requestReschedule(Integer coachingSessionId, DateTimeDTO dateTimeDTO, Integer playerId) {
        CoachingSession coachingSession = coachingSessionRepository.findCoachingSessionById(coachingSessionId);
        if (coachingSession == null) {
            throw new ApiException("Coaching session not found");
        } ;

        if (coachingSession.getPlayer().getId() != playerId) {
            throw new ApiException("Player does not own this coaching session");
        }
        
        coachingSession.setStartDate(dateTimeDTO.getStartDate());
        coachingSession.setEndDate(dateTimeDTO.getEndDate());
        coachingSession.setStatus("Waiting For Coach Approval");
        coachingSessionRepository.save(coachingSession);
        
    }

    public void approveReschedule(Integer coachingSessionId, Integer coachId) {
        CoachingSession coachingSession = coachingSessionRepository.findCoachingSessionById(coachingSessionId);

        if (coachingSession == null) {
            new ApiException("Coaching session not found");
        }

        if (coachingSession.getCoach().getId() != coachId) {
            throw new ApiException("Coach does not own this coaching session");
        }



       coachingSession.setStatus("Approved");
        coachingSessionRepository.save(coachingSession);

    }


    public void rejectedReschedule(Integer coachingSessionId, Integer coachId) {
        CoachingSession coachingSession = coachingSessionRepository.findCoachingSessionById(coachingSessionId);

        if (coachingSession == null) {
            new ApiException("Coaching session not found");
        }

        if (coachingSession.getCoach().getId() != coachId) {
            throw new ApiException("Coach does not own this coaching session");
        }



        coachingSession.setStatus("Rescheduling Rejected");

        coachingSessionRepository.save(coachingSession);

    }


    public void endCoachingSession(Integer coachingSessionId, Integer coachId) {
        CoachingSession coachingSession = coachingSessionRepository.findCoachingSessionById(coachingSessionId);
        if (coachingSession == null) {
            new ApiException("Coaching session not found");
        }

        if (coachingSession.getCoach().getId() != coachId) {
            throw new ApiException("Coach does not own this coaching session");
        }

        coachingSession.setStatus("Ended");
        coachingSessionRepository.save(coachingSession);
    }


    public List<CoachingSession> getPendingApprovalSessionsByCoach(Integer coachId) {
        return coachingSessionRepository.findByStatusAndCoachId("Waiting For Coach Approval", coachId);
    }






}