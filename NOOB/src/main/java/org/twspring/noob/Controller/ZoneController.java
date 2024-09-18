package org.twspring.noob.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.twspring.noob.Model.PC;
import org.twspring.noob.Model.PcCentres;
import org.twspring.noob.Model.User;
import org.twspring.noob.Model.Zone;
import org.twspring.noob.Service.ZoneService;

@RestController
@RequestMapping("/api/v1/zone")
@RequiredArgsConstructor
public class ZoneController {

    private final ZoneService zoneService;

    @GetMapping("/get-all")
    public ResponseEntity getAllZone() {
        return ResponseEntity.status(200).body(zoneService.getAllPcZone());
    }

    @PostMapping("/add-zone/{centreId}/{vendorId}")
    public ResponseEntity addZone(@AuthenticationPrincipal User user, @PathVariable Integer centreId, @Valid @RequestBody Zone zone) {
        zoneService.addZone(zone, centreId, user.getId());
        return ResponseEntity.status(200).body("Zone added successfully");
    }

    @PutMapping("/update-zone/{zoneId}")
    public ResponseEntity updateZone(@PathVariable Integer zoneId,@AuthenticationPrincipal User user, @Valid @RequestBody Zone zone) {
        zoneService.updateZone(zoneId, zone, user.getId());
        return ResponseEntity.status(200).body("Zone updated successfully");
    }

    @DeleteMapping("/delete-zone/{zoneId}")
    public ResponseEntity deleteZone(@PathVariable Integer zoneId,@AuthenticationPrincipal User user) {
        zoneService.deleteZone(zoneId, user.getId());
        return ResponseEntity.status(200).body("Zone deleted successfully");
    }

    /////
    @GetMapping("/get-zoneBy/{pcCentreId}")
    public ResponseEntity getZoneByPccentre(@PathVariable Integer pcCentreId) {
        return ResponseEntity.status(200).body(zoneService.getZoneByPcCentre(pcCentreId));
    }

    @PutMapping("/chang-zone-status/{PcCentre}/{zone_id}")
    public ResponseEntity changZoneStatus(@PathVariable Integer pcCentreId, @PathVariable Integer zone_id, @RequestBody Zone zone,@AuthenticationPrincipal User user) {
        zoneService.isAvailableZone(pcCentreId, zone_id, user.getId());
        return ResponseEntity.status(200).body("status changed successfully");

    }
}
