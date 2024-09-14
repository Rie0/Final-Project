package org.twspring.noob.Controller;

import org.twspring.noob.DTO.DateDTO;
import org.twspring.noob.DTO.DateTimeDTO;
import org.twspring.noob.Model.League;
import org.twspring.noob.Service.LeagueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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

    //SETTING DATES
    @PutMapping("/organizer/{organizerId}/league/{leagueId}/change-dates")
    public ResponseEntity changeLeagueDates(@PathVariable Integer organizerId,
                                            @PathVariable Integer leagueId,
                                            @RequestBody DateDTO dates){
        leagueService.changeLeagueDates(organizerId,leagueId,dates);
        return ResponseEntity.status(200).body("League dates changed successfully");
    }

    @PutMapping("/organizer/{organizerId}/league/{leagueId}/round/{roundId}/set-dates")
    public ResponseEntity setLeagueRoundDate(@PathVariable Integer organizerId,
                                             @PathVariable Integer leagueId,
                                             @PathVariable Integer roundId,
                                             @RequestBody LocalDate date){
        leagueService.setLeagueRoundDate(organizerId,leagueId,roundId,date);
        return ResponseEntity.status(200).body("League round date set successfully");
    }

    @PutMapping("/organizer/{organizerId}/league/{leagueId}/match/{matchId}/set-dates")
    public ResponseEntity setLeagueMatchDate(@PathVariable Integer organizerId,
                                             @PathVariable Integer leagueId,
                                             @PathVariable Integer matchId,
                                             @RequestBody DateTimeDTO dates){
        leagueService.setLeagueMatchDate(organizerId,leagueId,matchId,dates);
        return ResponseEntity.status(200).body("League match date set successfully");
    }

    //starting League

    @PutMapping("/organizer/{organizerId}/league/{leagueId}/set-ready")
    public ResponseEntity setLeagueToReady(@PathVariable Integer organizerId,
                                           @PathVariable Integer leagueId){
        leagueService.setLeagueToReady(organizerId,leagueId);
        return ResponseEntity.status(200).body("League ready set successfully");
    }

    //GET INFO
    @GetMapping("/{leagueId}/get-rounds")
    public ResponseEntity getRounds(@PathVariable Integer leagueId) {
        return ResponseEntity.status(200).body(leagueService.getLeagueRounds(leagueId));
    }
    @GetMapping("/{leagueId}/get-matches")
    public ResponseEntity getMatches(@PathVariable Integer leagueId) {
        return ResponseEntity.status(200).body(leagueService.getLeagueMatches(leagueId));
    }


}
