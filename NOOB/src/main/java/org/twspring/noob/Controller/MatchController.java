package org.twspring.noob.Controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.twspring.noob.Model.Match;
import org.twspring.noob.Model.User;
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
    public ResponseEntity getMatches(@AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.OK).body(matchService.getMatches());
    }

    @PostMapping("/add")
    public ResponseEntity addMatch(@Valid @RequestBody Match match, @AuthenticationPrincipal User user) {
        matchService.saveMatch(match,user);
        return ResponseEntity.status(HttpStatus.OK).body("Match added successfully");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateMatch(@Valid @RequestBody Match match, @PathVariable Integer id, @AuthenticationPrincipal User user) {
        matchService.updateMatch(id, match,user);
        return ResponseEntity.status(HttpStatus.OK).body("Match updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteMatch(@PathVariable Integer id, @AuthenticationPrincipal User user) {
        matchService.deleteMatch(id,user);
        return ResponseEntity.status(HttpStatus.OK).body("Match deleted successfully");
    }

    @PostMapping("/{matchId}/ready/participant1/{playerId}")
    public ResponseEntity setParticipant1Ready(@PathVariable Integer matchId, @PathVariable Integer playerId, @AuthenticationPrincipal User user) {
        matchService.setParticipant1Ready(matchId, playerId);
        return ResponseEntity.ok("Participant 1 is now ready.");
    }

    @PostMapping("/{matchId}/ready/participant2/{playerId}")
    public ResponseEntity<String> setParticipant2Ready(@PathVariable Integer matchId, @PathVariable Integer playerId, @AuthenticationPrincipal User user) {
        matchService.setParticipant2Ready(matchId, playerId);
        return ResponseEntity.ok("Participant 2 is now ready.");
    }

    @GetMapping("/{matchId}/players")
    public ResponseEntity getMatchParticipants(@PathVariable Integer matchId, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(matchService.getMatchParticipants(matchId));
    }

    @PostMapping("/{matchId}/setWinner")
    public ResponseEntity setWinnerAndLoser(
            @PathVariable Integer matchId,
            @RequestParam Integer winnerId,
            @RequestParam int scoreWinner,
            @RequestParam int scoreLoser,
            @AuthenticationPrincipal User user) {

        matchService.setMatchWinnerAndLoser(matchId, winnerId, scoreWinner, scoreLoser,user);
        return ResponseEntity.ok("Winner and loser have been set successfully.");
    }

    @PostMapping("/{matchId}/advanceWinner")
    public ResponseEntity advanceWinner(@PathVariable Integer matchId, @AuthenticationPrincipal User user) {
        matchService.advanceWinnerToNextMatch(matchId,user);
        return ResponseEntity.ok("Winner advanced to the next match successfully.");
    }

    @PostMapping("/{matchId}/winner/bye")
    public ResponseEntity setMatchWinnerByBye(@PathVariable Integer matchId,
                                              @RequestParam Integer participantId,
                                              @AuthenticationPrincipal User user) {
        matchService.setMatchWinnerByBye(matchId, participantId,user);
        return ResponseEntity.ok("Winner set by bye successfully.");
    }

    @GetMapping("/history-between-two-players")
    public ResponseEntity getMatchHistoryBetweenPlayers(
            @RequestParam Integer playerId1,
            @RequestParam Integer playerId2,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(
                matchService.getMatchHistoryBetweenPlayersGroupedByWinner(playerId1, playerId2));
    }
}
