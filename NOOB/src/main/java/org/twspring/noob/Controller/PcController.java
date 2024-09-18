package org.twspring.noob.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.twspring.noob.Model.PC;
import org.twspring.noob.Model.User;
import org.twspring.noob.Service.PcService;

@RestController
@RequestMapping("/api/v1/pc")
@RequiredArgsConstructor
public class PcController {

    private final PcService pcService;

    @GetMapping("/get-all")
    public ResponseEntity getAllPc() {
        return ResponseEntity.status(200).body(pcService.getAllPc());
    }

    @PostMapping("add-pc/{pcCenterID}/{vendorId}")
    public ResponseEntity addPc(@PathVariable Integer pcCenterID,@AuthenticationPrincipal User user ,@Valid @RequestBody PC pc) {
        pcService.addPc(pc, user.getId(), pcCenterID);
        return ResponseEntity.status(200).body("pc added successfully");

    }

    @PutMapping("update-pc/{pcId}")
    public ResponseEntity updatePc(@PathVariable Integer pcId, @AuthenticationPrincipal User user, @Valid @RequestBody PC pc) {
        pcService.updatePc(user.getId(), pc,pcId);
        return ResponseEntity.status(200).body("pc updated successfully");
    }

    @DeleteMapping("/delete-pc/{pcId}")
    public ResponseEntity deletePc(@PathVariable Integer pcId,@AuthenticationPrincipal User user ) {
        pcService.deletePc(user.getId(), pcId);
        return ResponseEntity.status(200).body("pc deleted successfully");

    }



}
