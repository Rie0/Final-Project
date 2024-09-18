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
import java.time.LocalTime;
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
        coach.getSchedules().add(schedule);
        scheduleRepository.save(schedule);
        coachRepository.save(coach);
    }

    public void updateSchedule(Integer id, Schedule scheduleDetails) {
        Schedule schedule = scheduleRepository.findScheduleById(id);
        if (schedule == null) {
            throw new ApiException("Schedule not found");
        }
        schedule.setStartDate(scheduleDetails.getStartDate());
        schedule.setEndDate(scheduleDetails.getEndDate());
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


}



