package org.twspring.noob.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.twspring.noob.Api.ApiResponse;
import org.twspring.noob.Model.Coach;
import org.twspring.noob.Service.CoachService;

import java.util.List;

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
    public ResponseEntity registerCoach(@RequestBody @Valid Coach coach) {
        coachService.addCoach(coach);
        return ResponseEntity.status(200).body(new ApiResponse("Coach registered successfully"));
    }

    // CRUD update
    @PutMapping("/update/{coachId}")
    public ResponseEntity updateMyInfo(@PathVariable Integer coachId, @RequestBody @Valid Coach coachDetails) {
        coachService.updateCoach(coachId, coachDetails);
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


}
