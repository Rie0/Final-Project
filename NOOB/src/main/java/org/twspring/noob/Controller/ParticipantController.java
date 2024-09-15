package org.twspring.noob.Controller;

import org.twspring.noob.Model.Participant;
import org.twspring.noob.Model.User;
import org.twspring.noob.Service.ParticipantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/participant")
public class ParticipantController {

    private final ParticipantService participantService;

    @GetMapping("/get")
    public ResponseEntity getParticipants(@AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.OK).body(participantService.getParticipants());
    }

    @PostMapping("/add/{tournamentId}/{playerId}")
    public ResponseEntity addParticipant(@AuthenticationPrincipal User user,
                                         @Valid @RequestBody Participant participant,
                                         @PathVariable Integer tournamentId,
                                         @PathVariable Integer playerId) {
        // The security check is handled by the security configuration
        participantService.saveParticipant(participant, tournamentId, playerId);
        return ResponseEntity.status(HttpStatus.OK).body("Participant added successfully");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateParticipant(@AuthenticationPrincipal User user,
                                            @Valid @RequestBody Participant participant,
                                            @PathVariable Integer id) {
        // The security check is handled by the security configuration
        participantService.updateParticipant(id, participant);
        return ResponseEntity.status(HttpStatus.OK).body("Participant updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteParticipant(@AuthenticationPrincipal User user,
                                            @PathVariable Integer id) {
        // The security check is handled by the security configuration
        participantService.deleteParticipant(id);
        return ResponseEntity.status(HttpStatus.OK).body("Participant deleted successfully");
    }

    @GetMapping("/get/{participantId}")
    public ResponseEntity getParticipant(@AuthenticationPrincipal User user,
                                         @PathVariable Integer participantId) {
        // The security check is handled by the security configuration
        return ResponseEntity.status(HttpStatus.OK).body(participantService.getParticipantById(participantId));
    }
}
