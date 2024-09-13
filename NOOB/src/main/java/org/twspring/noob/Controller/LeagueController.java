package org.twspring.noob.Controller;

import org.twspring.noob.Model.League;
import org.twspring.noob.Service.LeagueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/league")
public class LeagueController {

    private final LeagueService leagueService;

    @GetMapping("/get")
    public ResponseEntity getLeagues() {
        return ResponseEntity.status(200).body(leagueService.getLeagues());
    }

    @PostMapping("/organizer/{organizerId}/add")
    public ResponseEntity addLeague(@PathVariable Integer organizerId,
                                    @Valid @RequestBody League league) {
        leagueService.createLeague(organizerId, league);
        return ResponseEntity.status(200).body("League added successfully");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateLeague(@Valid @RequestBody League league, @PathVariable Integer id) {
        leagueService.updateLeague(id, league);
        return ResponseEntity.status(200).body("League updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteLeague(@PathVariable Integer id) {
        leagueService.deleteLeague(id);
        return ResponseEntity.status(200).body("League deleted successfully");
    }
    //EXTRA
    @GetMapping("/get/{leagueId}")
    public ResponseEntity getLeague(@PathVariable Integer leagueId) {
        return ResponseEntity.status(200).body(leagueService.getLeagueById(leagueId));
    }

    //PARTICIPATION

    @GetMapping("/{leagueId}/get-all-participants")
    public ResponseEntity getAllParticipants(@PathVariable Integer leagueId) {
        return ResponseEntity.status(200).body(leagueService.getParticipantsByLeague(leagueId));
    }

    @PutMapping("/{leagueId}/participate/{playerId}") //DTO FOR FORM?
    public ResponseEntity participateInLeague(@PathVariable Integer leagueId,
                                              @PathVariable Integer playerId,
                                              @RequestBody String name) {
        leagueService.participateInLeague(leagueId, playerId, name);
        return ResponseEntity.status(200).body("Player participated successfully in league");
    }

    @PutMapping("/participant/{participantId}/league/{leagueId}/withdraw")
    public ResponseEntity withdrawFromLeague(@PathVariable Integer leagueId,
                                             @PathVariable Integer participantId){
        leagueService.withdrawFromLeague(leagueId, participantId);
        return ResponseEntity.status(200).body("Player withdrawn from league successfully");
    }
}
