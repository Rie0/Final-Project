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
//Hussam

public class ParticipantController {

    private final ParticipantService participantService;

    @GetMapping("/get")
    public ResponseEntity getParticipants(@AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.OK).body(participantService.getParticipants());
    }

    @PostMapping("/add/{tournamentId}")
    public ResponseEntity addParticipant(@AuthenticationPrincipal User user,
                                         @RequestParam String name,
                                         @PathVariable Integer tournamentId
    ) {
        // The security check is handled by the security configuration
        participantService.participateInTournament(  user.getId(),tournamentId,name);
        return ResponseEntity.status(HttpStatus.OK).body("Participant added successfully");
    }



    @DeleteMapping("/delete/{tournamentId}")
    public ResponseEntity<String> deleteParticipant(@AuthenticationPrincipal User user,
                                                    @PathVariable Integer tournamentId) {
        participantService.deleteParticipant(user.getId(),tournamentId);
        return ResponseEntity.status(HttpStatus.OK).body("Participant deleted successfully");
    }

    @GetMapping("/get/{participantId}")
    public ResponseEntity getParticipant(@AuthenticationPrincipal User user,
                                         @PathVariable Integer participantId) {
        // The security check is handled by the security configuration
        return ResponseEntity.status(HttpStatus.OK).body(participantService.getParticipantById(participantId));
    }
}
