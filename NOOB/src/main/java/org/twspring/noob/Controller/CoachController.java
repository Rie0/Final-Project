package org.twspring.noob.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.twspring.noob.Api.ApiResponse;
import org.twspring.noob.DTO.CoachDTO;
import org.twspring.noob.Model.Review;
import org.twspring.noob.Model.User;
import org.twspring.noob.Service.CoachService;

import java.util.List;
// Mohammed
@RestController
@RequestMapping("api/v1/coach")
@RequiredArgsConstructor
public class CoachController {

    private final CoachService coachService;

    //Mohammed - CRUD get all
    @GetMapping("/get-all")
    public ResponseEntity getAllCoaches() {
        return ResponseEntity.status(200).body(coachService.getAllCoaches());
    }

    // Mohammed - CRUD register
    @PostMapping("/register")
    public ResponseEntity registerCoach(@RequestBody @Valid CoachDTO coachDTO) {
        coachService.addCoach(coachDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Coach registered successfully"));
    }

    // Mohammed - CRUD update
    @PutMapping("/update")
    public ResponseEntity updateMyInfo(@AuthenticationPrincipal User coach, @RequestBody @Valid CoachDTO coachDTO) {
        coachService.updateCoach(coach.getId(), coachDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Coach updated successfully"));
    }

    // Mohammed - CRUD delete
    @DeleteMapping("/delete")
    public ResponseEntity deleteMyAccount(@AuthenticationPrincipal User coach) {
        coachService.deleteCoach(coach.getId());
        return ResponseEntity.status(200).body(new ApiResponse("Coach deleted successfully"));
    }

    // Mohammed - EXTRA endpoint: getting a coach by id
    @GetMapping("/get/{coachId}")
    public ResponseEntity getCoachById(@AuthenticationPrincipal User coach) {
        return ResponseEntity.status(200).body(coachService.getCoachById(coach.getId()));
    }

    // Mohammed
    @GetMapping("/get-by-hourly-rate-range/{minRate}/{maxRate}")
    public ResponseEntity getCoachesByHourlyRateRange(@PathVariable Integer minRate, @PathVariable Integer maxRate) {
        return ResponseEntity.status(200).body(coachService.getCoachesByHourlyRateRange(minRate, maxRate));
    }

    // Mohammed
    @GetMapping("/reviews/{coachId}")
    public ResponseEntity<List<Review>> getAllReviewsByCoach(@AuthenticationPrincipal User coach) {
        List<Review> coachReviews = coachService.getAllReviewsByCoach(coach.getId());
        return ResponseEntity.status(200).body(coachReviews);
    }


}
