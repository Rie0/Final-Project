package org.twspring.noob.Controller;

import org.twspring.noob.Model.League;
import org.twspring.noob.Service.LeagueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/league")
public class LeagueController {

    private final LeagueService leagueService;

    @GetMapping("/get")
    public ResponseEntity getLeagues() {
        return ResponseEntity.status(HttpStatus.OK).body(leagueService.getLeagues());
    }

    @PostMapping("/organizer/{organizerId}/add")
    public ResponseEntity addLeague(@PathVariable Integer organizerId,
                                    @Valid @RequestBody League league) {
        leagueService.saveLeague(organizerId, league);
        return ResponseEntity.status(HttpStatus.OK).body("League added successfully");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateLeague(@Valid @RequestBody League league, @PathVariable Integer id) {
        leagueService.updateLeague(id, league);
        return ResponseEntity.status(HttpStatus.OK).body("League updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteLeague(@PathVariable Integer id) {
        leagueService.deleteLeague(id);
        return ResponseEntity.status(HttpStatus.OK).body("League deleted successfully");
    }
}
