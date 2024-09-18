package org.twspring.noob.Controller;

import org.twspring.noob.Model.Bracket;
import org.twspring.noob.Model.Tournament;
import org.twspring.noob.Model.User;
import org.twspring.noob.Service.BracketService;
import org.twspring.noob.Service.TournamentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

//Hussam

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tournament")
public class TournamentController {

    private final TournamentService tournamentService;
    private final BracketService bracketService;

    @GetMapping("/get")
    public ResponseEntity getTournaments() {
        return ResponseEntity.status(HttpStatus.OK).body(tournamentService.getTournaments());
    }

    @PostMapping("/add")
    public ResponseEntity addTournament(@AuthenticationPrincipal User user,@Valid @RequestBody Tournament tournament) {
        tournamentService.saveTournament(tournament, user.getId());
        return ResponseEntity.status(HttpStatus.OK).body("Tournament added successfully");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateTournament(
                                           @AuthenticationPrincipal User user,
                                           @Valid @RequestBody Tournament tournament,
                                           @PathVariable Integer id) {
        tournamentService.updateTournament(id, tournament, user.getId());
        return ResponseEntity.status(HttpStatus.OK).body("Tournament updated successfully");
    }

    @DeleteMapping("/delete/{tournamentId}")
    public ResponseEntity deleteTournament(
                                            @AuthenticationPrincipal User user,
                                            @PathVariable Integer tournamentId) {
        tournamentService.deleteTournament(tournamentId, user.getId());
        return ResponseEntity.status(HttpStatus.OK).body("Tournament deleted successfully");
    }
    @PostMapping("/{tournamentId}/initializeDoubleEliminationBracket")
    public ResponseEntity initializeDoubleEliminationBracket(@AuthenticationPrincipal User user,
                                                             @PathVariable Integer tournamentId) {
        // Ensure the user has the correct authorization
        tournamentService.checkOrganizerAuthorization(tournamentId, user.getId());
        // Call the service method to create a double elimination bracket
        Bracket doubleEliminationBracket = bracketService.createDoubleEliminationBracket(tournamentId);
        return ResponseEntity.ok(doubleEliminationBracket);
    }
    @PostMapping("/{tournamentId}/distribute-prizes")
    public ResponseEntity distributePrizes(@PathVariable Integer tournamentId) {
        tournamentService.distributePrizes(tournamentId);
        return ResponseEntity.ok("Prizes have been successfully distributed for tournament ID: " + tournamentId);

    }

    @PostMapping("/{tournamentId}/initializeBracket")
    public ResponseEntity initializeBracket( @AuthenticationPrincipal User user,
                                             @PathVariable Integer tournamentId
                                           ) {
        tournamentService.checkOrganizerAuthorization(tournamentId, user.getId());
        Bracket bracket = bracketService.createBracketForTournament(tournamentId);
        return ResponseEntity.ok(bracket);
    }

    @PostMapping("/{id}/start")
    public ResponseEntity startTournament(
            @AuthenticationPrincipal User user, @PathVariable Integer id) {
        tournamentService.startTournament(id, user.getId());
        return ResponseEntity.ok("Tournament started successfully.");
    }
    @PostMapping("/{tournamentId}/finalize")
    public ResponseEntity finalizeTournament(
            @AuthenticationPrincipal User user,
            @PathVariable Integer tournamentId) {
        tournamentService.finalizeTournament(tournamentId, user.getId());
        return ResponseEntity.status(HttpStatus.OK).body("Tournament finalized successfully.");
    }

    @PostMapping("/{tournamentId}/participant/{participantId}/checkin")
    public ResponseEntity checkInParticipant(@PathVariable Integer tournamentId,
                                             @PathVariable Integer participantId,
                                             @AuthenticationPrincipal User user) {
        tournamentService.checkInParticipant(tournamentId, participantId, user.getId());
        return ResponseEntity.ok("Participant checked in successfully for the tournament.");
    }

    @GetMapping("/by-game")
    public ResponseEntity getTournamentsByGame(@RequestParam String game) {
        return ResponseEntity.status(HttpStatus.OK).body(tournamentService.getTournamentsByGame(game));
    }

    @GetMapping("/by-city")
    public ResponseEntity getTournamentsByCity(@RequestParam String city) {
        return ResponseEntity.status(HttpStatus.OK).body(tournamentService.getTournamentsByCity(city));
    }

    @GetMapping("/online")
    public ResponseEntity getTournamentsByAttendanceTypeOnline() {
        return ResponseEntity.status(HttpStatus.OK).body(tournamentService.getTournamentsByAttendanceTypeOnline());
    }

    @GetMapping("/onsite")
    public ResponseEntity getTournamentsByAttendanceTypeOnsite() {
        return ResponseEntity.status(HttpStatus.OK).body(tournamentService.getTournamentsByAttendanceTypeOnsite());
    }

    @GetMapping("/status/ongoing")
    public ResponseEntity getTournamentsByStatusOngoing() {
        return ResponseEntity.status(HttpStatus.OK).body(tournamentService.getTournamentsByStatusOngoing());
    }

    @GetMapping("/status/active")
    public ResponseEntity getTournamentsByStatusActive() {
        return ResponseEntity.status(HttpStatus.OK).body(tournamentService.getTournamentsByStatusActive());
    }

    @GetMapping("/status/closing-soon")
    public ResponseEntity getTournamentsByStatusClosingSoon() {
        return ResponseEntity.status(HttpStatus.OK).body(tournamentService.getTournamentsByStatusClosingSoon());
    }

    @GetMapping("/status/finished")
    public ResponseEntity getTournamentsByStatusFinished() {
        return ResponseEntity.status(HttpStatus.OK).body(tournamentService.getTournamentsByStatusFinished());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tournament> getTournamentById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(tournamentService.getTournamentById(id));
    }

    @GetMapping("/{id}/description")
    public ResponseEntity getTournamentDescriptionById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(tournamentService.getTournamentDescriptionById(id));
    }

    @GetMapping("/{id}/matches")
    public ResponseEntity getTournamentMatchesById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(tournamentService.getTournamentMatchesById(id));
    }

    @GetMapping("/{id}/bracket{bracketId}")
    public ResponseEntity<Bracket> getTournamentBracketById(@PathVariable Integer id,@PathVariable Integer bracketId) {
        return ResponseEntity.status(HttpStatus.OK).body(tournamentService.getTournamentBracketById(id,bracketId));
    }

    @GetMapping("/{id}/standing")
    public ResponseEntity getTournamentStandingById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(tournamentService.getTournamentStandingById(id));
    }



    @GetMapping("/{tournamentId}/matches/completed")
    public ResponseEntity getTournamentMatchesByStatusCompleted(@PathVariable Integer tournamentId) {
        return ResponseEntity.ok(tournamentService.getTournamentMatchesByStatusCompleted(tournamentId));
    }

    @GetMapping("/{tournamentId}/matches/in-progress")
    public ResponseEntity getTournamentMatchesByStatusInProgress(@PathVariable Integer tournamentId) {
        return ResponseEntity.ok(tournamentService.getTournamentMatchesByStatusInProgress(tournamentId));
    }

    @GetMapping("/{tournamentId}/matches/not-started")
    public ResponseEntity getTournamentMatchesByStatusNotStarted(@PathVariable Integer tournamentId) {
        return ResponseEntity.ok(tournamentService.getTournamentMatchesByStatusNotStarted(tournamentId));
    }
}
