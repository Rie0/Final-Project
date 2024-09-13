package org.twspring.noob.Controller;

import org.twspring.noob.Model.Match;
import org.twspring.noob.Service.MatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/match")
public class MatchController {

    private final MatchService matchService;

    @GetMapping("/get")
    public ResponseEntity getMatches() {
        return ResponseEntity.status(HttpStatus.OK).body(matchService.getMatches());
    }

    @PostMapping("/add")
    public ResponseEntity addMatch(@Valid @RequestBody Match match) {
        matchService.saveMatch(match);
        return ResponseEntity.status(HttpStatus.OK).body("Match added successfully");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateMatch(@Valid @RequestBody Match match, @PathVariable Integer id) {
        matchService.updateMatch(id, match);
        return ResponseEntity.status(HttpStatus.OK).body("Match updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteMatch(@PathVariable Integer id) {
        matchService.deleteMatch(id);
        return ResponseEntity.status(HttpStatus.OK).body("Match deleted successfully");
    }

    @PatchMapping("/{matchId}/ready/participant1/{playerId}")
    public ResponseEntity setParticipant1Ready(@PathVariable Integer matchId, @PathVariable Integer playerId) {
         matchService.setParticipant1Ready(matchId, playerId);

            return ResponseEntity.ok("Participant 1 is now ready.");

    }

    @PatchMapping("/{matchId}/ready/participant2/{playerId}")
    public ResponseEntity<String> setParticipant2Ready(@PathVariable Integer matchId, @PathVariable Integer playerId) {
        matchService.setParticipant2Ready(matchId, playerId);
            return ResponseEntity.ok("Participant 2 is now ready.");

    }

    @GetMapping("/{matchId}/players")
    public ResponseEntity getMatchParticipants(@PathVariable Integer matchId) {
        return ResponseEntity.ok(
                matchService.getMatchParticipants(matchId)
        );
    }

}
