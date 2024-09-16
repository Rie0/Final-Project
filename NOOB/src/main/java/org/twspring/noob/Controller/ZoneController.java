package org.twspring.noob.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.twspring.noob.Model.PC;
import org.twspring.noob.Model.PcCentres;
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

    @PostMapping("add-zone/{centreId}/{vendorId}")
    public ResponseEntity addZone(@PathVariable Integer vendorId, @PathVariable Integer centreId, @Valid @RequestBody Zone zone) {
        zoneService.addZone(zone, centreId, vendorId);
        return ResponseEntity.status(200).body("Zone added successfully");
    }

    @PutMapping("update-zone/{id}")
    public ResponseEntity updateZone(@PathVariable Integer id, @Valid @RequestBody Zone zone) {
        zoneService.updateZone(id, zone);
        return ResponseEntity.status(200).body("Zone updated successfully");
    }

    @DeleteMapping("/delete-zone/{id}")
    public ResponseEntity deleteZone(@PathVariable Integer id) {
        zoneService.deleteZone(id);
        return ResponseEntity.status(200).body("Zone deleted successfully");
    }

    /////
    @GetMapping("/get-zoneBy/{pcCentreId}")
    public ResponseEntity getZoneByPccentre(@PathVariable Integer pcCentreId) {
        return ResponseEntity.status(200).body(zoneService.getZoneByPcCentre(pcCentreId));
    }

    @PutMapping("chang-zone-status/{PcCentre}/{zone_id}/{vendorId}")
    public ResponseEntity changZoneStatus(@PathVariable Integer PcCentre, @PathVariable Integer zone_id, @PathVariable Integer vendorId, @RequestBody Zone zone) {
        zoneService.isAvailableZone(zone_id, PcCentre, vendorId);
        return ResponseEntity.status(200).body("status changed successfully");

    }
}
