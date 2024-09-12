package org.twspring.noob.Controller;

import org.twspring.noob.Model.Round;
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
    public ResponseEntity getRounds() {
        return ResponseEntity.status(HttpStatus.OK).body(roundService.getRounds());
    }

    @PostMapping("/add")
    public ResponseEntity addRound(@Valid @RequestBody Round round) {
        roundService.saveRound(round);
        return ResponseEntity.status(HttpStatus.OK).body("Round added successfully");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateRound(@Valid @RequestBody Round round, @PathVariable Integer id) {
        roundService.updateRound(id, round);
        return ResponseEntity.status(HttpStatus.OK).body("Round updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteRound(@PathVariable Integer id) {
        roundService.deleteRound(id);
        return ResponseEntity.status(HttpStatus.OK).body("Round deleted successfully");
    }
}
