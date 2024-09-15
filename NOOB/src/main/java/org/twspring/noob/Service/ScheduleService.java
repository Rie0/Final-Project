package org.twspring.noob.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.twspring.noob.Api.ApiException;
import org.twspring.noob.Model.Coach;
import org.twspring.noob.Model.Player;
import org.twspring.noob.Model.Schedule;
import org.twspring.noob.Repository.CoachRepository;
import org.twspring.noob.Repository.PlayerRepository;
import org.twspring.noob.Repository.ScheduleRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final CoachRepository coachRepository;
    private final PlayerRepository playerRepository;

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public void addSchedule(Schedule schedule, Integer coachId) {
        Coach coach = coachRepository.findCoachById(coachId);
        if (coach == null) {
            throw new ApiException("Coach not found");
        }
        schedule.setCoach(coach);
        scheduleRepository.save(schedule);
    }

    public void updateSchedule(Integer id, Schedule scheduleDetails) {
        Schedule schedule = scheduleRepository.findScheduleById(id);
        if (schedule == null) {
            throw new ApiException("Schedule not found");
        }
        schedule.setDate(scheduleDetails.getDate());
        schedule.setStartTime(scheduleDetails.getStartTime());
        schedule.setEndTime(scheduleDetails.getEndTime());
        schedule.setIsBooked(scheduleDetails.getIsBooked());
        scheduleRepository.save(schedule);
    }

    public void deleteSchedule(Integer id) {
        Schedule schedule = scheduleRepository.findScheduleById(id);
        if (schedule == null) {
            throw new ApiException("Schedule not found");
        }
        scheduleRepository.delete(schedule);
    }

    public Schedule getScheduleById(Integer id) {
        Schedule schedule = scheduleRepository.findScheduleById(id);
        if (schedule == null) {
            throw new ApiException("Schedule not found");
        }
        return schedule;
    }


    // EXTRA endpoint: getting schedules by coach id
    public List<Schedule> getSchedulesByCoachId(Integer coachId) {
        return scheduleRepository.findSchedulesByCoachId(coachId);
    }


    // EXTRA endpoint: booking a coaching session
    public void bookCoachingSession(Integer scheduleId, Integer playerId) {
        Schedule schedule = scheduleRepository.findScheduleById(scheduleId);
        if (schedule == null) {
            throw new ApiException("Schedule not found");
        }
        if (schedule.getIsBooked()) {
            throw new ApiException("Schedule is already booked");
        }
        Player player = playerRepository.findPlayerById(playerId);
        if (player == null) {
            throw new ApiException("Player not found");
        }
        schedule.setIsBooked(true);
        schedule.setPlayer(player);
        scheduleRepository.save(schedule);
    }



    // EXTRA endpoint: request a reschedule
    public void requestReschedule(Integer scheduleId, String newTime) {
        Schedule schedule = scheduleRepository.findScheduleById(scheduleId);
        if (schedule == null) {
            throw new ApiException("Schedule not found");
        }
        schedule.setRescheduleRequested(true);
        schedule.setNewTime(newTime);
        scheduleRepository.save(schedule);
    }

    // EXTRA endpoint: respond to a reschedule request
    public void respondReschedule(Integer scheduleId, boolean accept) {
        Schedule schedule = scheduleRepository.findScheduleById(scheduleId);
        if (schedule == null) {
            throw new ApiException("Schedule not found");
        }
        if (!schedule.isRescheduleRequested()) {
            throw new ApiException("No reschedule request found");
        }
        if (accept) {
            schedule.setStartTime(LocalDate.parse(schedule.getNewTime()));
        }
        schedule.setRescheduleRequested(false);
        schedule.setNewTime(null);
        scheduleRepository.save(schedule);
    }

}



