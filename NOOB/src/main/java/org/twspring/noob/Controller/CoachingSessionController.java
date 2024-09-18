package org.twspring.noob.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.twspring.noob.Api.ApiException;
import org.twspring.noob.Api.ApiResponse;
import org.twspring.noob.DTO.DateTimeDTO;
import org.twspring.noob.Model.Coach;
import org.twspring.noob.Model.CoachingSession;
import org.twspring.noob.Model.Review;
import org.twspring.noob.Model.User;
import org.twspring.noob.Service.CoachService;
import org.twspring.noob.Service.CoachingSessionService;
import org.twspring.noob.Service.PlayerService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

// Mohammed
@RestController
@RequestMapping("/api/v1/coaching-session")
@RequiredArgsConstructor
public class CoachingSessionController {

    private final CoachingSessionService coachingSessionService;


    // Mohammed CRUD get all
    @GetMapping("/get-all")
    public ResponseEntity getAllCoachingSessions() {
        return ResponseEntity.status(200).body(coachingSessionService.getAllCoachingSessions());
    }

    // Mohammed CRUD register
    @PostMapping("/reserve/{scheduleId}")
    public ResponseEntity<ApiResponse> reserveCoachingSession(@PathVariable Integer scheduleId, @AuthenticationPrincipal User player, @RequestBody @Valid DateTimeDTO dateTimeDTO) {
        coachingSessionService.reserveCoachingSession(scheduleId, player.getId(), dateTimeDTO); //sessionStyle
        return ResponseEntity.status(200).body(new ApiResponse("Coaching session reserved successfully"));
    }

    // Mohammed CRUD update
    @PutMapping("/update/{sessionId}")
    public ResponseEntity updateCoachingSession(@PathVariable Integer sessionId, @RequestBody @Valid CoachingSession coachingSessionDetails) {
        coachingSessionService.updateCoachingSession(sessionId, coachingSessionDetails);
        return ResponseEntity.status(200).body(new ApiResponse("Coaching session updated successfully"));
    }

    // Mohammed CRUD delete
    @DeleteMapping("/delete/{sessionId}")
    public ResponseEntity deleteCoachingSession(@PathVariable Integer sessionId) {
        coachingSessionService.deleteCoachingSession(sessionId);
        return ResponseEntity.status(200).body(new ApiResponse("Coaching session deleted successfully"));
    }
    // Mohammed EXTRA endpoint: getting a coaching session by id
    @GetMapping("/get/{sessionId}")
    public ResponseEntity getCoachingSessionById(@PathVariable Integer sessionId) {
        return ResponseEntity.status(200).body(coachingSessionService.getCoachingSessionById(sessionId));
    }
     // Mohammed
    @PostMapping("/request-reschedule/{coachingSessionId}")
    public ResponseEntity requestReschedule(@PathVariable Integer coachingSessionId, @AuthenticationPrincipal User player, @RequestBody @Valid DateTimeDTO dateTimeDTO) {
        coachingSessionService.requestReschedule(coachingSessionId, dateTimeDTO, player.getId());
        return ResponseEntity.status(200).body(new ApiResponse("Reschedule request submitted successfully"));
    }
    // Mohammed
    @GetMapping("/pending-approval-sessions")
    public ResponseEntity<List<CoachingSession>> getPendingApprovalSessionsByCoach(@AuthenticationPrincipal User coach) {
        List<CoachingSession> pendingApprovalSessions = coachingSessionService.getPendingApprovalSessionsByCoach(coach.getId());
        return ResponseEntity.status(200).body(pendingApprovalSessions);
    }
    // Mohammed
    @PostMapping("/approve-reschedule/{coachingSessionId}")
    public ResponseEntity approveReschedule(@PathVariable Integer coachingSessionId, @AuthenticationPrincipal User coach) {
        coachingSessionService.approveReschedule(coachingSessionId, coach.getId());
        return ResponseEntity.status(200).body(new ApiResponse("Reschedule approved successfully"));
    }
    // Mohammed
    @PostMapping("/reject-reschedule/{coachingSessionId}/{coachId}")
    public ResponseEntity rejectReschedule(@PathVariable Integer coachingSessionId, @AuthenticationPrincipal User coach) {
        coachingSessionService.rejectedReschedule(coachingSessionId, coach.getId());
        return ResponseEntity.status(200).body(new ApiResponse("Reschedule rejected successfully"));
    }
    // Mohammed
    @PostMapping("/end/{coachingSessionId}")
    public ResponseEntity endCoachingSession(@PathVariable Integer coachingSessionId, @AuthenticationPrincipal User coach) {
        coachingSessionService.endCoachingSession(coachingSessionId, coach.getId());
        return ResponseEntity.ok(new ApiResponse("Coaching session ended successfully"));
    }



}
