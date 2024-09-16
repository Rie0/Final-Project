package org.twspring.noob.Controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.twspring.noob.Model.Round;
import org.twspring.noob.Model.User;
import org.twspring.noob.Service.RoundService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/round")
public class RoundController {

    private final RoundService roundService;

    @GetMapping("/get")
    public ResponseEntity getRounds(@AuthenticationPrincipal User user) {
        // Assuming that the role checks are handled in the security configuration
        return ResponseEntity.status(HttpStatus.OK).body(roundService.getRounds());
    }

    @PostMapping("/add")
    public ResponseEntity addRound(@Valid @RequestBody Round round, @AuthenticationPrincipal User user) {
        // Security configuration is handling role validation
        roundService.saveRound(round);
        return ResponseEntity.status(HttpStatus.OK).body("Round added successfully");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateRound(@Valid @RequestBody Round round, @PathVariable Integer id, @AuthenticationPrincipal User user) {
        // Security configuration is handling role validation
        roundService.updateRound(id, round);
        return ResponseEntity.status(HttpStatus.OK).body("Round updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteRound(@PathVariable Integer id, @AuthenticationPrincipal User user) {
        // Security configuration is handling role validation
        roundService.deleteRound(id);
        return ResponseEntity.status(HttpStatus.OK).body("Round deleted successfully");
    }
}
