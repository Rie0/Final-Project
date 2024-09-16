package org.twspring.noob.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.twspring.noob.Api.ApiResponse;
import org.twspring.noob.DTO.CoachDTO;
import org.twspring.noob.Model.User;
import org.twspring.noob.Service.CoachService;

@RestController
@RequestMapping("api/v1/coach")
@RequiredArgsConstructor
public class CoachController {

    private final CoachService coachService;

    // Get all coaches
    @GetMapping("/get-all")
    public ResponseEntity getAllCoaches(@AuthenticationPrincipal User user) {
        return ResponseEntity.status(200).body(coachService.getAllCoaches());
    }

    // Register a new coach
    @PostMapping("/register")
    public ResponseEntity registerCoach(@RequestBody @Valid CoachDTO coachDTO) {
        coachService.registerCoach(coachDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Coach registered successfully"));
    }

    // Update coach information
    @PutMapping("/update")
    public ResponseEntity updateCoach(
                                      @RequestBody @Valid CoachDTO coachDTO,
                                      @AuthenticationPrincipal User user) {
        coachService.updateCoach(user.getId(), coachDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Coach updated successfully"));
    }

    // Delete a coach
    @DeleteMapping("/delete")
    public ResponseEntity deleteCoach(
                                      @AuthenticationPrincipal User user) {
        coachService.deleteCoach(user.getId());
        return ResponseEntity.status(200).body(new ApiResponse("Coach deleted successfully"));
    }

    // Get a coach by id
    @GetMapping("/get/{coachId}")
    public ResponseEntity getCoachById(
            @AuthenticationPrincipal User user,@PathVariable Integer coachId
                                       ) {
        return ResponseEntity.status(200).body(coachService.getCoachById(coachId));
    }
}
