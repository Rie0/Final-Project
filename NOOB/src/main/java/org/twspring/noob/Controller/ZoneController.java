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
    public ResponseEntity getAllZone(){
        return ResponseEntity.status(200).body(zoneService.getAllPcZone());
    }

    @PostMapping("add-zone/{centreId}")
    public ResponseEntity addZone(@PathVariable Integer centreId, @Valid @RequestBody Zone zone){
        zoneService.addZone(zone,centreId);
        return ResponseEntity.status(200).body("Zone added successfully");

    }
    @PutMapping("update-zone/{id}")
    public ResponseEntity updateZone(@PathVariable Integer id, @Valid @RequestBody Zone zone){
        zoneService.updateZone(id,zone);
        return ResponseEntity.status(200).body("Zone updated successfully");
    }
    @DeleteMapping("/delete-zone/{id}")
    public ResponseEntity deleteZone(@PathVariable Integer id){
        zoneService.deleteZone(id);
        return ResponseEntity.status(200).body("Zone deleted successfully");
    }
}
