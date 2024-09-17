package org.twspring.noob.Controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.twspring.noob.DTO.DateDTO;
import org.twspring.noob.DTO.DateTimeDTO;
import org.twspring.noob.Model.League;
import org.twspring.noob.Model.User;
import org.twspring.noob.Service.LeagueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/league")
public class LeagueController { //RAFEEF

    private final LeagueService leagueService;

    @GetMapping("/get")
    public ResponseEntity getLeagues() {
        return ResponseEntity.status(200).body(leagueService.getLeagues());
    }

    @PostMapping("/create-new")
    public ResponseEntity addLeague(@AuthenticationPrincipal User organizer,
                                    @Valid @RequestBody League league) {
        leagueService.createLeague(organizer.getId(), league);
        return ResponseEntity.status(200).body("League added successfully");
    }

    @PutMapping("/admin/update/{id}")
    public ResponseEntity updateLeagueByAdmin(@PathVariable Integer id,
                                       @Valid @RequestBody League league) {
        leagueService.updateLeagueByAdmin(id, league);
        return ResponseEntity.status(200).body("League updated successfully");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateLeague(@PathVariable Integer id,
                                       @Valid @RequestBody League league,
                                       @AuthenticationPrincipal User organizer) {
        leagueService.updateLeagueByOrganizer(id, organizer.getId(),league);
        return ResponseEntity.status(200).body("League updated successfully");
    }


    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity deleteLeagueByAdmin(@PathVariable Integer id,
                                       @AuthenticationPrincipal User organizer) {
        leagueService.deleteLeagueByOrganizer(id,organizer.getId());
        return ResponseEntity.status(200).body("League deleted successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteLeague(@PathVariable Integer id) {
        leagueService.deleteLeagueByAdmin(id);
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

    @PutMapping("/{leagueId}/participate")
    public ResponseEntity participateInLeague(@PathVariable Integer leagueId,
                                              @AuthenticationPrincipal User player,
                                              @RequestBody String name) {
        leagueService.participateInLeague(player.getId(), leagueId, name);
        return ResponseEntity.status(200).body("Player participated successfully in league");
    }

    @PutMapping("/{leagueId}/withdraw")
    public ResponseEntity withdrawFromLeague(@PathVariable Integer leagueId,
                                             @AuthenticationPrincipal User player){
        leagueService.withdrawFromLeague(player.getId(), leagueId);
        return ResponseEntity.status(200).body("Player withdrawn from league successfully");
    }

    @PutMapping("/{leagueId}/kick-participant/{playerId}")
    public ResponseEntity kickParticipant(@PathVariable Integer leagueId,
                                          @PathVariable Integer playerId,
                                          @AuthenticationPrincipal User organizer) {
        leagueService.kickParticipant(organizer.getId(), playerId, leagueId);
        return ResponseEntity.status(200).body("Participant kicked successfully");
    }

    //SETTING DATES
    @PutMapping("/{leagueId}/change-dates")
    public ResponseEntity changeLeagueDates(@PathVariable Integer leagueId,
                                            @AuthenticationPrincipal User organizer,
                                            @RequestBody DateDTO dates){
        leagueService.changeLeagueDates(organizer.getId(),leagueId,dates);
        return ResponseEntity.status(200).body("League dates changed successfully");
    }

    @PutMapping("/{leagueId}/round/{roundId}/set-dates")
    public ResponseEntity setLeagueRoundDate(@PathVariable Integer leagueId,
                                             @PathVariable Integer roundId,
                                             @AuthenticationPrincipal User organizer,
                                             @RequestBody LocalDate date){
        leagueService.setLeagueRoundDate(organizer.getId(),leagueId,roundId,date);
        return ResponseEntity.status(200).body("League round date set successfully");
    }

    @PutMapping("{leagueId}/match/{matchId}/set-dates")
    public ResponseEntity setLeagueMatchDate(@PathVariable Integer leagueId,
                                             @PathVariable Integer matchId,
                                             @AuthenticationPrincipal User organizer,
                                             @RequestBody DateTimeDTO dates){
        leagueService.setLeagueMatchDate(organizer.getId(),leagueId,matchId,dates);
        return ResponseEntity.status(200).body("League match date set successfully");
    }

    //League Management
    @PutMapping("/{leagueId}/set-ready")
    public ResponseEntity setLeagueToReady(@PathVariable Integer leagueId,
                                           @AuthenticationPrincipal User organizer){
        leagueService.setLeagueToReady(organizer.getId(),leagueId);
        return ResponseEntity.status(200).body("League set to ready successfully");
    }

    @PutMapping("{leagueId}/match/{matchId}/start-match")
    public ResponseEntity startMatch(@PathVariable Integer leagueId,
                                     @PathVariable Integer matchId,
                                     @AuthenticationPrincipal User organizer) {
        leagueService.startMatch(organizer.getId(),leagueId,matchId);
        return ResponseEntity.status(200).body("Match started successfully");
    }
    @PutMapping("{leagueId}/match/{matchId}/add-1score-to-participant1")
    public ResponseEntity add1toParticipant1Score(@PathVariable Integer leagueId,
                                                  @PathVariable Integer matchId,
                                                  @AuthenticationPrincipal User organizer){
        leagueService.add1toParticipant1Score(organizer.getId(),leagueId,matchId);
        return ResponseEntity.status(200).body("Score added successfully to participant1");
    }
    @PutMapping("/{leagueId}/match/{matchId}/add-1score-to-participant2")
    public ResponseEntity add1toParticipant2Score(@PathVariable Integer leagueId,
                                                  @PathVariable Integer matchId,
                                                  @AuthenticationPrincipal User organizer){
        leagueService.add1toParticipant2Score(organizer.getId(),leagueId,matchId);
        return ResponseEntity.status(200).body("Score added successfully to participant2");
    }
    @PutMapping("/{leagueId}/match/{matchId}/sub-1score-from-participant1")
    public ResponseEntity sub1fromParticipant1Score(@PathVariable Integer leagueId,
                                                    @PathVariable Integer matchId,
                                                    @AuthenticationPrincipal User organizer){
        leagueService.subtract1fromParticipant1Score(organizer.getId(),leagueId,matchId);
        return ResponseEntity.status(200).body("Score removed successfully from participant1");
    }
    @PutMapping("/{leagueId}/match/{matchId}/sub-1score-from-participant2")
    public ResponseEntity sub1fromParticipant2Score(@PathVariable Integer leagueId,
                                                    @PathVariable Integer matchId,
                                                    @AuthenticationPrincipal User organizer){
        leagueService.subtract1fromParticipant2Score(organizer.getId(),leagueId,matchId);
        return ResponseEntity.status(200).body("Score removed successfully from participant2");
    }
    @PutMapping("/{leagueId}/match/{matchId}/finish-match")
    public ResponseEntity finishMatch(@PathVariable Integer leagueId,
                                      @PathVariable Integer matchId,
                                      @AuthenticationPrincipal User organizer){
        leagueService.finishMatch(organizer.getId(),leagueId,matchId);
        return ResponseEntity.status(200).body("Match ended successfully");
    }
    @PutMapping("/{leagueId}/match/{matchId}/cancel-match")
    public ResponseEntity cancelMatch(@PathVariable Integer leagueId,
                                      @PathVariable Integer matchId,
                                      @AuthenticationPrincipal User organizer) {
        leagueService.cancelMatch(organizer.getId(), matchId, leagueId);
        return ResponseEntity.status(200).body("Match canceled successfully");
    }
    @PutMapping("/{leagueId}/finalize")
    public ResponseEntity finalizeLeague(@PathVariable Integer leagueId,
                                         @AuthenticationPrincipal User organizer){
        leagueService.finalizeLeague(organizer.getId(),leagueId);
        return ResponseEntity.status(200).body("League finalized successfully");
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
    @GetMapping("/{leagueId}/leaderboard")
    public ResponseEntity getLeaderboard(@PathVariable Integer leagueId) {
        return ResponseEntity.status(200).body(leagueService.getLeaderBoard(leagueId));
    }

}
