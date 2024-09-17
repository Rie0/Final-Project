package org.twspring.noob.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.twspring.noob.Api.ApiResponse;
import org.twspring.noob.Model.MasterClass;
import org.twspring.noob.Service.MasterClassService;

import java.util.List;


@RestController
@RequestMapping("/api/v1/masterclass")
@RequiredArgsConstructor
public class MasterClassController {

    private final MasterClassService masterClassService;

    // CRUD get all
    @GetMapping("/get-all")
    public ResponseEntity getAllMasterClasses() {
        return ResponseEntity.status(200).body(masterClassService.getAllMasterClasses());
    }

    // CRUD register
    @PostMapping("/register/{coachId}")
    public ResponseEntity registerMasterClass(@RequestBody @Valid MasterClass masterClass, @PathVariable Integer coachId) {
        masterClassService.addMasterClass(masterClass, coachId);
        return ResponseEntity.status(200).body(new ApiResponse("MasterClass registered successfully"));
    }

    // CRUD update
    @PutMapping("/update/{masterClassId}")
    public ResponseEntity updateMasterClass(@PathVariable Integer masterClassId, @RequestBody @Valid MasterClass masterClassDetails) {
        masterClassService.updateMasterClass(masterClassId, masterClassDetails);
        return ResponseEntity.status(200).body(new ApiResponse("MasterClass updated successfully"));
    }

    // CRUD delete
    @DeleteMapping("/delete/{masterClassId}")
    public ResponseEntity deleteMasterClass(@PathVariable Integer masterClassId) {
        masterClassService.deleteMasterClass(masterClassId);
        return ResponseEntity.status(200).body(new ApiResponse("MasterClass deleted successfully"));
    }


    // EXTRA endpoint: getting a master class by id
    @GetMapping("/get/{id}")
    public ResponseEntity<MasterClass> getMasterClassById(@PathVariable Integer id) {
        MasterClass masterClass = masterClassService.getMasterClassById(id);
        return ResponseEntity.status(200).body(masterClass);
    }

    // EXTRA endpoint: getting master classes by coach id
    @GetMapping("/get-by-coach/{coachId}")
    public ResponseEntity<List<MasterClass>> getMasterClassesByCoachId(@PathVariable Integer coachId) {
        return ResponseEntity.ok(masterClassService.getMasterClassesByCoachId(coachId));
    }

    // EXTRA endpoint: getting master classes by coach id and status
    @GetMapping("/by-coach-and-status/{coachId}/{status}")
    public ResponseEntity<List<MasterClass>> getMasterClassesByCoachIdAndStatus(@PathVariable Integer coachId, @PathVariable String status) {
        return ResponseEntity.ok(masterClassService.getMasterClassesByCoachIdAndStatus(coachId, status));
    }

    // EXTRA endpoint: starting a master class
    @PostMapping("/start/{masterClassId}")
    public ResponseEntity<ApiResponse> startMasterClass(@PathVariable Integer masterClassId) {
        masterClassService.startMasterClass(masterClassId);
        return ResponseEntity.status(200).body(new ApiResponse("MasterClass started successfully"));
    }

    // EXTRA endpoint: closing a master class
    @PostMapping("/close/{masterClassId}")
    public ResponseEntity<ApiResponse> closeMasterClass(@PathVariable Integer masterClassId) {
        masterClassService.closeMasterClass(masterClassId);
        return ResponseEntity.status(200).body(new ApiResponse("MasterClass closed successfully"));
    }

    // EXTRA endpoint: canceling a master class
    @PostMapping("/cancel/{masterClassId}")
    public ResponseEntity<ApiResponse> cancelMasterClass(@PathVariable Integer masterClassId) {
        masterClassService.cancelMasterClass(masterClassId);
        return ResponseEntity.status(200).body(new ApiResponse("MasterClass canceled successfully"));
    }

    // EXTRA endpoint: joining a master class
    @PostMapping("/join/{masterClassId}/{playerId}")
    public ResponseEntity<ApiResponse> joinMasterClass(@PathVariable Integer masterClassId, @PathVariable Integer playerId) {
        masterClassService.joinMasterClass(masterClassId, playerId);
        return ResponseEntity.status(200).body(new ApiResponse("Player joined the masterclass successfully"));
    }

    // EXTRA endpoint: leaving a master class
    @PostMapping("/leave/{masterClassId}/{playerId}")
    public ResponseEntity<ApiResponse> leaveMasterClass(@PathVariable Integer masterClassId, @PathVariable Integer playerId) {
        masterClassService.leaveMasterClass(masterClassId, playerId);
        return ResponseEntity.status(200).body(new ApiResponse("Player left the masterclass successfully"));
    }

    @PostMapping("/kick/{masterClassId}/{playerId}")
    public ResponseEntity<ApiResponse> kickPlayerFromMasterClass(@PathVariable Integer masterClassId, @PathVariable Integer playerId) {
        masterClassService.kickPlayerFromMasterClass(masterClassId, playerId);
        return ResponseEntity.status(200).body(new ApiResponse("Player kicked from the masterclass successfully"));
    }

}