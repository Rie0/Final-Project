package org.twspring.noob.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.twspring.noob.Api.ApiResponse;
import org.twspring.noob.Model.Schedule;
import org.twspring.noob.Service.ScheduleService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    // CRUD get all
    @GetMapping("/get-all")
    public ResponseEntity getAllSchedules() {
        return ResponseEntity.status(200).body(scheduleService.getAllSchedules());
    }

    // CRUD register
    @PostMapping("/register/{coachId}")
    public ResponseEntity registerSchedule(@RequestBody @Valid Schedule schedule,
    @PathVariable Integer coachId) {
        scheduleService.addSchedule(schedule, coachId);
        return ResponseEntity.status(200).body(new ApiResponse("Schedule registered successfully"));
    }

    // CRUD update
    @PutMapping("/update/{scheduleId}")
    public ResponseEntity updateSchedule(@PathVariable Integer scheduleId, @RequestBody @Valid Schedule scheduleDetails) {
        scheduleService.updateSchedule(scheduleId, scheduleDetails);
        return ResponseEntity.status(200).body(new ApiResponse("Schedule updated successfully"));
    }

    // CRUD delete
    @DeleteMapping("/delete/{scheduleId}")
    public ResponseEntity deleteSchedule(@PathVariable Integer scheduleId) {
        scheduleService.deleteSchedule(scheduleId);
        return ResponseEntity.status(200).body(new ApiResponse("Schedule deleted successfully"));
    }

    // EXTRA endpoint: getting a schedule by id
    @GetMapping("/get/{scheduleId}")
    public ResponseEntity getScheduleById(@PathVariable Integer scheduleId) {
        return ResponseEntity.status(200).body(scheduleService.getScheduleById(scheduleId));
    }



    // EXTRA endpoint: getting schedules by coach id
    @GetMapping("/get-by-coach/{coachId}")
    public ResponseEntity<List<Schedule>> getSchedulesByCoachId(@PathVariable Integer coachId) {
        List<Schedule> schedules = scheduleService.getSchedulesByCoachId(coachId);
        return ResponseEntity.ok(schedules);
    }

    // EXTRA endpoint: booking a coaching session
    @PostMapping("/book/{scheduleId}")
    public ResponseEntity<ApiResponse> bookCoachingSession(@PathVariable Integer scheduleId, @RequestParam Integer playerId) {
        scheduleService.bookCoachingSession(scheduleId, playerId);
        return ResponseEntity.status(200).body(new ApiResponse("Coaching session booked successfully"));
    }


}


