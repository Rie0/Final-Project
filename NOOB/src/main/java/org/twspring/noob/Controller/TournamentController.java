package org.twspring.noob.Controller;

import org.twspring.noob.Model.Bracket;
import org.twspring.noob.Model.Tournament;
import org.twspring.noob.Service.BracketService;
import org.twspring.noob.Service.TournamentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/add/{organizer}")
    public ResponseEntity addTournament(@Valid @RequestBody Tournament tournament,
                                        @PathVariable Integer organizer) {
        tournamentService.saveTournament(tournament, organizer);
        return ResponseEntity.status(HttpStatus.OK).body("Tournament added successfully");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateTournament(@Valid @RequestBody Tournament tournament, @PathVariable Integer id) {
        tournamentService.updateTournament(id, tournament);
        return ResponseEntity.status(HttpStatus.OK).body("Tournament updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteTournament(@PathVariable Integer id) {
        tournamentService.deleteTournament(id);
        return ResponseEntity.status(HttpStatus.OK).body("Tournament deleted successfully");
    }

    @PostMapping("/{tournamentId}/initializeBracket")
    public ResponseEntity initializeBracket(@PathVariable Integer tournamentId) {
        Bracket bracket = bracketService.createBracketForTournament(tournamentId);
        return ResponseEntity.ok(bracket);

    }
    @PostMapping("/{id}/start")
    public ResponseEntity startTournament(@PathVariable Integer id) {
            tournamentService.startTournament(id);
            return ResponseEntity.ok("Tournament started successfully.");

    }
    @GetMapping("/by-game")
    public ResponseEntity getTournamentsByGame(@RequestParam String game) {
        return ResponseEntity.status(HttpStatus.OK).body(tournamentService.getTournamentsByGame(game));
    }

    @GetMapping("/by-city")
    public ResponseEntity  getTournamentsByCity(@RequestParam String city) {
        return ResponseEntity.status(HttpStatus.OK).body(tournamentService.getTournamentsByCity(city));
    }

    @GetMapping("/online")
    public ResponseEntity  getTournamentsByAttendanceTypeOnline() {
        return ResponseEntity.status(HttpStatus.OK).body(tournamentService.getTournamentsByAttendanceTypeOnline());
    }

    @GetMapping("/onsite")
    public ResponseEntity getTournamentsByAttendanceTypeOnsite() {
        return ResponseEntity.status(HttpStatus.OK).body(tournamentService.getTournamentsByAttendanceTypeOnsite());
    }

    @GetMapping("/status/ongoing")
    public ResponseEntity  getTournamentsByStatusOngoing() {
        return ResponseEntity.status(HttpStatus.OK).body(tournamentService.getTournamentsByStatusOngoing());
    }

    @GetMapping("/status/active")
    public ResponseEntity  getTournamentsByStatusActive() {
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
    public ResponseEntity<String> getTournamentDescriptionById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(tournamentService.getTournamentDescriptionById(id));
    }

    @GetMapping("/{id}/matches")
    public ResponseEntity getTournamentMatchesById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(tournamentService.getTournamentMatchesById(id));
    }

    @GetMapping("/{id}/bracket")
    public ResponseEntity<Bracket> getTournamentBracketById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(tournamentService.getTournamentBracketById(id));
    }

    @GetMapping("/{id}/standing")
    public ResponseEntity  getTournamentStandingById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(tournamentService.getTournamentStandingById(id));
    }
    @GetMapping("/tournament/{tournamentId}/matches/completed")
    public ResponseEntity getMatchesByStatusCompleted(@PathVariable Integer tournamentId) {
        return ResponseEntity.ok(tournamentService.getMatchesByStatusCompleted(tournamentId));
    }

    @GetMapping("/tournament/{tournamentId}/matches/in-progress")
    public ResponseEntity getMatchesByStatusInProgress(@PathVariable Integer tournamentId) {
        return ResponseEntity.ok(tournamentService.getMatchesByStatusInProgress(tournamentId));
    }

    @GetMapping("/tournament/{tournamentId}/matches/not-started")
    public ResponseEntity getMatchesByStatusNotStarted(@PathVariable Integer tournamentId) {
        return ResponseEntity.ok(tournamentService.getMatchesByStatusNotStarted(tournamentId));
    }

    @PostMapping("/{tournamentId}/finalize")
    public ResponseEntity finalizeTournament(@PathVariable Integer tournamentId) {
            tournamentService.finalizeTournament(tournamentId);
            return ResponseEntity.status(HttpStatus.OK).body("Tournament finalized successfully.");

    }
    @PostMapping("/{tournamentId}/participant/{participantId}/checkin")
    public ResponseEntity<String> checkInParticipant(@PathVariable Integer tournamentId, @PathVariable Integer participantId) {

            tournamentService.checkInParticipant(tournamentId, participantId);
            return ResponseEntity.ok("Participant checked in successfully for the tournament.");

    }


        @GetMapping("/{tournamentId}/matches/completed")
        public ResponseEntity getTournamentMatchesByStatusCompleted(@PathVariable Integer tournamentId) {

                return ResponseEntity.ok(
                        tournamentService.getTournamentMatchesByStatusCompleted(tournamentId)
                );

        }

        @GetMapping("/{tournamentId}/matches/in-progress")
        public ResponseEntity getTournamentMatchesByStatusInProgress(@PathVariable Integer tournamentId) {
                ;
                return ResponseEntity.ok(tournamentService.getTournamentMatchesByStatusInProgress(tournamentId));

        }

        @GetMapping("/{tournamentId}/matches/not-started")
        public ResponseEntity getTournamentMatchesByStatusNotStarted(@PathVariable Integer tournamentId) {
                return ResponseEntity.ok(tournamentService.getTournamentMatchesByStatusNotStarted(tournamentId));

        }



    }


