package org.twspring.noob.Controller;

import org.twspring.noob.Model.Participant;
import org.twspring.noob.Service.ParticipantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/participant")
public class ParticipantController {

    private final ParticipantService participantService;

    @GetMapping("/get")
    public ResponseEntity getParticipants() {
        return ResponseEntity.status(HttpStatus.OK).body(participantService.getParticipants());
    }

@PostMapping("/add/{tournamentId}/{playerId}")
    public ResponseEntity addParticipant(@Valid @RequestBody Participant participant,
    @PathVariable Integer tournamentId,
    @PathVariable Integer playerId ) {
        participantService.saveParticipant(participant,tournamentId,playerId);
        return ResponseEntity.status(HttpStatus.OK).body("Participant added successfully");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateParticipant(@Valid @RequestBody Participant participant, @PathVariable Integer id) {
        participantService.updateParticipant(id, participant);
        return ResponseEntity.status(HttpStatus.OK).body("Participant updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteParticipant(@PathVariable Integer id) {
        participantService.deleteParticipant(id);
        return ResponseEntity.status(HttpStatus.OK).body("Participant deleted successfully");
    }

    //Extra
    @GetMapping("/get/{participantId}")
    public ResponseEntity getParticipant(@PathVariable Integer participantId) {
        return ResponseEntity.status(200).body(participantService.getParticipantById(participantId));
    }
}
