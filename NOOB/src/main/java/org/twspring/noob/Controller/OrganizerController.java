package org.twspring.noob.Controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.twspring.noob.DTO.OrganizerDTO;
import org.twspring.noob.Model.Organizer;
import org.twspring.noob.Model.User;
import org.twspring.noob.Service.OrganizerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/organizer")

//Hussam

public class OrganizerController {


    private final OrganizerService organizerService;

    @GetMapping("/get")
    public ResponseEntity getOrganizers( @AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.OK).body(organizerService.getOrganizers());
    }

    @PostMapping("/add")
    public ResponseEntity addOrganizer( @Valid @RequestBody OrganizerDTO organizer) {
        organizerService.registerOrganizer(organizer);
        return ResponseEntity.status(HttpStatus.OK).body("Organizer added successfully");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateOrganizer(@Valid @RequestBody OrganizerDTO organizer,
                                          @AuthenticationPrincipal User user ) {
        organizerService.updateOrganizer(user.getId(), organizer);
        return ResponseEntity.status(HttpStatus.OK).body("Organizer updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteOrganizer(@AuthenticationPrincipal User user) {
        organizerService.deleteOrganizer(user.getId());
        return ResponseEntity.status(HttpStatus.OK).body("Organizer deleted successfully");
    }
}