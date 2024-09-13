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
    public ResponseEntity addTournament(@Valid @RequestBody Tournament tournament ,
                                        @PathVariable Integer organizer) {
        tournamentService.saveTournament(tournament,organizer);
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
        try {
            Bracket bracket = bracketService.initializeBracket(tournamentId);
            return ResponseEntity.ok(bracket);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
