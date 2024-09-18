package org.twspring.noob.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.twspring.noob.Api.ApiResponse;
import org.twspring.noob.Model.MasterClass;
import org.twspring.noob.Model.User;
import org.twspring.noob.Service.MasterClassService;

import java.util.List;


// Mohammed
@RestController
@RequestMapping("/api/v1/masterclass")
@RequiredArgsConstructor
public class MasterClassController {

    private final MasterClassService masterClassService;

    // Mohammed- CRUD get all
    @GetMapping("/get-all")
    public ResponseEntity getAllMasterClasses() {
        return ResponseEntity.status(200).body(masterClassService.getAllMasterClasses());
    }

    // Mohammed- CRUD register
    @PostMapping("/register/{coachId}")
    public ResponseEntity registerMasterClass(@RequestBody @Valid MasterClass masterClass, @AuthenticationPrincipal User coach) {
        masterClassService.addMasterClass(masterClass, coach.getId());
        return ResponseEntity.status(200).body(new ApiResponse("MasterClass registered successfully"));
    }

    // Mohammed - CRUD update
    @PutMapping("/update/{masterClassId}")
    public ResponseEntity updateMasterClass(@PathVariable Integer masterClassId, @RequestBody @Valid MasterClass masterClassDetails, @AuthenticationPrincipal User coach) {
        masterClassService.updateMasterClass(masterClassId, masterClassDetails, coach.getId());
        return ResponseEntity.status(200).body(new ApiResponse("MasterClass updated successfully"));
    }

    //Mohammed - CRUD delete
    @DeleteMapping("/delete/{masterClassId}")
    public ResponseEntity deleteMasterClass(@PathVariable Integer masterClassId, @AuthenticationPrincipal User coach) {
        masterClassService.deleteMasterClass(masterClassId, coach.getId());
        return ResponseEntity.status(200).body(new ApiResponse("MasterClass deleted successfully"));
    }


    // Mohammed endpoint: getting a master class by id
    @GetMapping("/get/{id}")
    public ResponseEntity<MasterClass> getMasterClassById(@PathVariable Integer id) {
        MasterClass masterClass = masterClassService.getMasterClassById(id);
        return ResponseEntity.status(200).body(masterClass);
    }

    // Mohammed endpoint: getting master classes by coach id
    @GetMapping("/get-by-coach/{coachId}")
    public ResponseEntity<List<MasterClass>> getMasterClassesByCoachId(@PathVariable Integer coachId) {
        return ResponseEntity.ok(masterClassService.getMasterClassesByCoachId(coachId));
    }

    // Mohammed endpoint: getting master classes by coach id and status
    @GetMapping("/by-coach-and-status/{coachId}/{status}")
    public ResponseEntity<List<MasterClass>> getMasterClassesByCoachIdAndStatus(@PathVariable Integer coachId, @PathVariable String status) {
        return ResponseEntity.ok(masterClassService.getMasterClassesByCoachIdAndStatus(coachId, status));
    }

    // Mohammed endpoint: starting a master class
    @PostMapping("/start/{masterClassId}")
    public ResponseEntity<ApiResponse> startMasterClass(@PathVariable Integer masterClassId, @AuthenticationPrincipal User coach) {
        masterClassService.startMasterClass(masterClassId, coach.getId());
        return ResponseEntity.status(200).body(new ApiResponse("MasterClass started successfully"));
    }

    // Mohammed endpoint: closing a master class
    @PostMapping("/close/{masterClassId}")
    public ResponseEntity<ApiResponse> closeMasterClass(@PathVariable Integer masterClassId, @AuthenticationPrincipal User coach) {
        masterClassService.closeMasterClass(masterClassId, coach.getId());
        return ResponseEntity.status(200).body(new ApiResponse("MasterClass closed successfully"));
    }

    // Mohammed endpoint: canceling a master class
    @PostMapping("/cancel/{masterClassId}")
    public ResponseEntity<ApiResponse> cancelMasterClass(@PathVariable Integer masterClassId, @AuthenticationPrincipal User coach) {
        masterClassService.cancelMasterClass(masterClassId, coach.getId());
        return ResponseEntity.status(200).body(new ApiResponse("MasterClass canceled successfully"));
    }

    // Mohammed endpoint: joining a master class
    @PostMapping("/join/{masterClassId}")
    public ResponseEntity<ApiResponse> joinMasterClass(@PathVariable Integer masterClassId, @AuthenticationPrincipal User player) {
        masterClassService.joinMasterClass(masterClassId, player.getId());
        return ResponseEntity.status(200).body(new ApiResponse("Player joined the masterclass successfully"));
    }

    // Mohammed endpoint: leaving a master class
    @PostMapping("/leave/{masterClassId}")
    public ResponseEntity<ApiResponse> leaveMasterClass(@PathVariable Integer masterClassId, @AuthenticationPrincipal User player) {
        masterClassService.leaveMasterClass(masterClassId, player.getId());
        return ResponseEntity.status(200).body(new ApiResponse("Player left the masterclass successfully"));
    }

    // Mohammed
    @PostMapping("/kick/{masterClassId}/{playerId}")
    public ResponseEntity<ApiResponse> kickPlayerFromMasterClass(@PathVariable Integer masterClassId, @PathVariable Integer playerId) {
        masterClassService.kickPlayerFromMasterClass(masterClassId, playerId);
        return ResponseEntity.status(200).body(new ApiResponse("Player kicked from the masterclass successfully"));
    }

}