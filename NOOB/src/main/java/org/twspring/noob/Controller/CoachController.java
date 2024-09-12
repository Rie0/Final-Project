package org.twspring.noob.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.twspring.noob.Model.Coach;
import org.twspring.noob.Service.CoachService;

import java.util.List;

@RestController
@RequestMapping("api/v1/coach")
@RequiredArgsConstructor
public class CoachController {

    private final CoachService coachService;

    @GetMapping("/get-all")
    public ResponseEntity<List<Coach>> getAllCoaches() {
        List<Coach> coaches = coachService.getAllCoaches();
        return ResponseEntity.ok(coaches);
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addCoach(@RequestBody Coach coach) {
        coachService.addCoach(coach);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Void> updateCoach(@PathVariable Integer id, @RequestBody Coach coachDetails) {
        coachService.updateCoach(id, coachDetails);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCoach(@PathVariable Integer id) {
        coachService.deleteCoach(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Coach> getCoachById(@PathVariable Integer id) {
        Coach coach = coachService.getCoachById(id);
        return ResponseEntity.ok(coach);
    }
}
