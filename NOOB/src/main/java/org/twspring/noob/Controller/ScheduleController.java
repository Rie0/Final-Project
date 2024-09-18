package org.twspring.noob.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.twspring.noob.Api.ApiResponse;
import org.twspring.noob.Model.Schedule;
import org.twspring.noob.Model.User;
import org.twspring.noob.Service.ScheduleService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;




// Mohammed
@RestController
@RequestMapping("/api/v1/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    // Mohammed -CRUD get all
    @GetMapping("/get-all")
    public ResponseEntity getAllSchedules() {
        return ResponseEntity.status(200).body(scheduleService.getAllSchedules());
    }

    //Mohammed - CRUD register
    @PostMapping("/register")
    public ResponseEntity registerSchedule(@RequestBody @Valid Schedule schedule, @AuthenticationPrincipal User coach) {
        scheduleService.addSchedule(schedule, coach.getId());
        return ResponseEntity.status(200).body(new ApiResponse("Schedule registered successfully"));
    }

    // Mohammed - CRUD update
    @PutMapping("/update/{scheduleId}")
    public ResponseEntity updateSchedule(@PathVariable Integer scheduleId, @RequestBody @Valid Schedule scheduleDetails, @AuthenticationPrincipal User coach) {
        scheduleService.updateSchedule(scheduleId, scheduleDetails);
        return ResponseEntity.status(200).body(new ApiResponse("Schedule updated successfully"));
    }

    // Mohammed - CRUD delete
    @DeleteMapping("/delete/{scheduleId}")
    public ResponseEntity deleteSchedule(@PathVariable Integer scheduleId, @AuthenticationPrincipal User coach) {
        scheduleService.deleteSchedule(scheduleId);
        return ResponseEntity.status(200).body(new ApiResponse("Schedule deleted successfully"));
    }

    // Mohammed - EXTRA endpoint: getting a schedule by id
    @GetMapping("/get/{scheduleId}")
    public ResponseEntity getScheduleById(@PathVariable Integer scheduleId) {
        return ResponseEntity.status(200).body(scheduleService.getScheduleById(scheduleId));
    }



    // Mohammed - EXTRA endpoint: getting schedules by coach id
    @GetMapping("/get-by-coach/{coachId}")
    public ResponseEntity<List<Schedule>> getSchedulesByCoachId(@PathVariable Integer coachId) {
        List<Schedule> schedules = scheduleService.getSchedulesByCoachId(coachId);
        return ResponseEntity.ok(schedules);
    }




}


