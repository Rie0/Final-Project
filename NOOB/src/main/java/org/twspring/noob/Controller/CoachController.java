package org.twspring.noob.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.twspring.noob.Api.ApiResponse;
import org.twspring.noob.DTO.CoachDTO;
import org.twspring.noob.Service.CoachService;

@RestController
@RequestMapping("api/v1/coach")
@RequiredArgsConstructor
public class CoachController {

    private final CoachService coachService;

    // CRUD get all
    @GetMapping("/get-all")
    public ResponseEntity getAllCoaches() {
        return ResponseEntity.status(200).body(coachService.getAllCoaches());
    }

    // CRUD register
    @PostMapping("/register")
    public ResponseEntity registerCoach(@RequestBody @Valid CoachDTO coachDTO) {
        coachService.addCoach(coachDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Coach registered successfully"));
    }

    // CRUD update
    @PutMapping("/update/{coachId}")
    public ResponseEntity updateMyInfo(@PathVariable Integer coachId, @RequestBody @Valid CoachDTO coachDTO) {
        coachService.updateCoach(coachId, coachDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Coach updated successfully"));
    }

    // CRUD delete
    @DeleteMapping("/delete/{coachId}")
    public ResponseEntity deleteMyAccount(@PathVariable Integer coachId) {
        coachService.deleteCoach(coachId);
        return ResponseEntity.status(200).body(new ApiResponse("Coach deleted successfully"));
    }

    // EXTRA endpoint: getting a coach by id
    @GetMapping("/get/{coachId}")
    public ResponseEntity getCoachById(@PathVariable Integer coachId) {
        return ResponseEntity.status(200).body(coachService.getCoachById(coachId));
    }

    @GetMapping("/get-by-hourly-rate-range/{minRate}/{maxRate}")
    public ResponseEntity getCoachesByHourlyRateRange(@PathVariable Integer minRate, @PathVariable Integer maxRate) {
        return ResponseEntity.status(200).body(coachService.getCoachesByHourlyRateRange(minRate, maxRate));
    }


}
