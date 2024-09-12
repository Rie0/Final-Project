package org.twspring.noob.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.twspring.noob.Api.ApiException;
import org.twspring.noob.Model.Schedule;
import org.twspring.noob.Repository.ScheduleRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public void addSchedule(Schedule schedule) {
        scheduleRepository.save(schedule);
    }

    public void updateSchedule(Integer id, Schedule scheduleDetails) {
        Schedule schedule = scheduleRepository.findById(id).orElseThrow(() -> new ApiException("Schedule not found"));
        schedule.setStartTime(scheduleDetails.getStartTime());
        schedule.setEndTime(scheduleDetails.getEndTime());
        schedule.setIsBooked(scheduleDetails.getIsBooked());
        scheduleRepository.save(schedule);
    }

    public void deleteSchedule(Integer id) {
        Schedule schedule = scheduleRepository.findById(id).orElseThrow(() -> new ApiException("Schedule not found"));
        scheduleRepository.delete(schedule);
    }

    public Schedule getScheduleById(Integer id) {
        return scheduleRepository.findById(id).orElseThrow(() -> new ApiException("Schedule not found"));
    }


}



