package org.twspring.noob.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.twspring.noob.Api.ApiResponse;
import org.twspring.noob.Model.Coach;
import org.twspring.noob.Model.CoachingSession;
import org.twspring.noob.Service.CoachService;
import org.twspring.noob.Service.CoachingSessionService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/coaching-session")
@RequiredArgsConstructor
public class CoachingSessionController {

    private final CoachingSessionService coachingSessionService;


    // CRUD get all
    @GetMapping("/get-all")
    public ResponseEntity getAllCoachingSessions() {
        return ResponseEntity.status(200).body(coachingSessionService.getAllCoachingSessions());
    }

    // CRUD register
    @PostMapping("/register")
    public ResponseEntity registerCoachingSession(@RequestBody @Valid CoachingSession coachingSession) {
        coachingSessionService.addCoachingSession(coachingSession);
        return ResponseEntity.status(200).body(new ApiResponse("Coaching session registered successfully"));
    }

    // CRUD update
    @PutMapping("/update/{sessionId}")
    public ResponseEntity updateCoachingSession(@PathVariable Integer sessionId, @RequestBody @Valid CoachingSession coachingSessionDetails) {
        coachingSessionService.updateCoachingSession(sessionId, coachingSessionDetails);
        return ResponseEntity.status(200).body(new ApiResponse("Coaching session updated successfully"));
    }

    // CRUD delete
    @DeleteMapping("/delete/{sessionId}")
    public ResponseEntity deleteCoachingSession(@PathVariable Integer sessionId) {
        coachingSessionService.deleteCoachingSession(sessionId);
        return ResponseEntity.status(200).body(new ApiResponse("Coaching session deleted successfully"));
    }
    // EXTRA endpoint: getting a coaching session by id
    @GetMapping("/get/{sessionId}")
    public ResponseEntity getCoachingSessionById(@PathVariable Integer sessionId) {
        return ResponseEntity.status(200).body(coachingSessionService.getCoachingSessionById(sessionId));
    }




}
