package org.twspring.noob.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.twspring.noob.Model.CoachingSession;
import org.twspring.noob.Service.CoachingSessionService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/coaching-session")
@RequiredArgsConstructor
public class CoachingSessionController {

    private final CoachingSessionService coachingSessionService;

    @GetMapping("/get-all")
    public ResponseEntity<List<CoachingSession>> getAllCoachingSessions() {
        List<CoachingSession> coachingSessions = coachingSessionService.getAllCoachingSessions();
        return ResponseEntity.ok(coachingSessions);
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addCoachingSession(@RequestBody CoachingSession coachingSession) {
        coachingSessionService.addCoachingSession(coachingSession);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Void> updateCoachingSession(@PathVariable Integer id, @RequestBody CoachingSession coachingSessionDetails) {
        coachingSessionService.updateCoachingSession(id, coachingSessionDetails);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCoachingSession(@PathVariable Integer id) {
        coachingSessionService.deleteCoachingSession(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<CoachingSession> getCoachingSessionById(@PathVariable Integer id) {
        CoachingSession coachingSession = coachingSessionService.getCoachingSessionById(id);
        return ResponseEntity.ok(coachingSession);
    }

}
