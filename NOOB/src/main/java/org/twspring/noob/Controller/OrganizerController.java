package org.twspring.noob.Controller;

import org.twspring.noob.Model.Organizer;
import org.twspring.noob.Service.OrganizerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/organizer")
public class OrganizerController {

    private final OrganizerService organizerService;

    @GetMapping("/get")
    public ResponseEntity getOrganizers() {
        return ResponseEntity.status(HttpStatus.OK).body(organizerService.getOrganizers());
    }

    @PostMapping("/add")
    public ResponseEntity addOrganizer(@Valid @RequestBody Organizer organizer) {
        organizerService.saveOrganizer(organizer);
        return ResponseEntity.status(HttpStatus.OK).body("Organizer added successfully");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateOrganizer(@Valid @RequestBody Organizer organizer, @PathVariable Integer id) {
        organizerService.updateOrganizer(id, organizer);
        return ResponseEntity.status(HttpStatus.OK).body("Organizer updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteOrganizer(@PathVariable Integer id) {
        organizerService.deleteOrganizer(id);
        return ResponseEntity.status(HttpStatus.OK).body("Organizer deleted successfully");
    }
}
