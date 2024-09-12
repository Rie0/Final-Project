package org.twspring.noob.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.twspring.noob.Model.MasterClass;
import org.twspring.noob.Service.MasterClassService;

import java.util.List;


@RestController
@RequestMapping("/api/v1/masterclass")
@RequiredArgsConstructor
public class MasterClassController {


    private final MasterClassService masterClassService;

    @GetMapping("get-all")
    public ResponseEntity<List<MasterClass>> getAllMasterClasses() {
        List<MasterClass> masterClasses = masterClassService.getAllMasterClasses();
        return ResponseEntity.ok(masterClasses);
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addMasterClass(@RequestBody MasterClass masterClass) {
        masterClassService.addMasterClass(masterClass);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/assign")
    public ResponseEntity<Void> addMasterClassAssign(@RequestBody MasterClass masterClass, @RequestParam Integer coachId) {
        masterClassService.addMasterClassAssign(masterClass, coachId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Void> updateMasterClass(@PathVariable Integer id, @RequestBody MasterClass masterClassDetails) {
        masterClassService.updateMasterClass(id, masterClassDetails);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteMasterClass(@PathVariable Integer id) {
        masterClassService.deleteMasterClass(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/assign/{masterClassId}/assign-coach/{coachId}")
    public ResponseEntity<Void> assignCoachIdMasterClass(@PathVariable Integer masterClassId, @PathVariable Integer coachId) {
        masterClassService.assignCoachIdMasterClass(coachId, masterClassId);
        return ResponseEntity.ok().build();
    }

}
