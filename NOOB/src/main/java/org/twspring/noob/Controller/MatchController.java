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

    // Get all matches
    @GetMapping("/get")
    public ResponseEntity getMatches() {
        return ResponseEntity.status(HttpStatus.OK).body(matchService.getMatches());
    }

    // Add a new match
    @PostMapping("/add")
    public ResponseEntity addMatch(@AuthenticationPrincipal User user, @Valid @RequestBody Match match) {
        matchService.saveMatch(user, match);
        return ResponseEntity.status(HttpStatus.OK).body("Match added successfully");
    }

    // Update an existing match
    @PutMapping("/update/{id}")
    public ResponseEntity updateMatch(@AuthenticationPrincipal User user, @Valid @RequestBody Match match, @PathVariable Integer id) {
        matchService.updateMatch(id, match, user);
        return ResponseEntity.status(HttpStatus.OK).body("Match updated successfully");
    }

    // Delete a match
    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteMatch(@AuthenticationPrincipal User user, @PathVariable Integer id) {
        matchService.deleteMatch(id, user);
        return ResponseEntity.status(HttpStatus.OK).body("Match deleted successfully");
    }

    // Set participant 1 as ready
    @PostMapping("/{matchId}/ready/participant1")
    public ResponseEntity setParticipant1Ready(@PathVariable Integer matchId, @AuthenticationPrincipal User user) {
        matchService.setParticipant1Ready(matchId, user.getId());
        return ResponseEntity.ok("Participant 1 is now ready.");
    }

    // Set participant 2 as ready
    @PostMapping("/{matchId}/ready/participant2")
    public ResponseEntity<String> setParticipant2Ready(@PathVariable Integer matchId, @AuthenticationPrincipal User user) {
        matchService.setParticipant2Ready(matchId, user.getId());
        return ResponseEntity.ok("Participant 2 is now ready.");
    }

    // Get participants of a match
    @GetMapping("/{matchId}/players")
    public ResponseEntity getMatchParticipants(@PathVariable Integer matchId) {
        return ResponseEntity.ok(
                matchService.getMatchParticipants(matchId)
        );
    }

    // Set winner and loser of a match
    @PostMapping("/{matchId}/setWinner")
    public ResponseEntity<String> setWinnerAndLoser(
            @PathVariable Integer matchId,
            @RequestParam Integer winnerId,
            @RequestParam int scoreWinner,
            @RequestParam int scoreLoser,
            @AuthenticationPrincipal User user) {
        matchService.setMatchWinnerAndLoser(matchId, winnerId, scoreWinner, scoreLoser, user);
        return ResponseEntity.ok("Winner and loser have been set successfully.");
    }
    // Advance the winner to the next match
    @PostMapping("/{matchId}/advanceWinner")
    public ResponseEntity advanceWinner(@PathVariable Integer matchId, @AuthenticationPrincipal User user) {
        matchService.advanceWinnerToNextMatch(matchId, user);
        return ResponseEntity.ok("Winner advanced to the next match successfully.");
    }

    // Set a match winner by bye
    @PostMapping("/{matchId}/winner/bye")
    public ResponseEntity<String> setMatchWinnerByBye(@PathVariable Integer matchId, @RequestParam Integer participantId, @AuthenticationPrincipal User user) {
        matchService.setMatchWinnerByBye(matchId, participantId, user);
        return ResponseEntity.ok("Winner set by bye successfully.");
    }

    // Get match history between two players
    @GetMapping("/history-between-two-players")
    public ResponseEntity getMatchHistoryBetweenPlayers(
            @RequestParam Integer playerId1,
            @RequestParam Integer playerId2) {
        return ResponseEntity.ok(
                matchService.getMatchHistoryBetweenPlayersGroupedByWinner(playerId1, playerId2));
    }

    // Get matches by participant
    @GetMapping("/get-by-participant/{participantId}")
    public ResponseEntity getParticipantMatches(@PathVariable Integer participantId) {
        return ResponseEntity.status(HttpStatus.OK).body(matchService.participantGetMatches(participantId));
    }

    // Get match by ID
    @GetMapping("/get-by-id/{matchId}")
    public ResponseEntity getMatchById(@PathVariable Integer matchId) {
        return ResponseEntity.status(HttpStatus.OK).body(matchService.getMatchById(matchId));
    }
}
