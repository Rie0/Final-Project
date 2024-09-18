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
////Hassan Alzahrani
public class PcController {

    private final PcService pcService;

    @GetMapping("/get-all")
    public ResponseEntity getAllPc() {
        return ResponseEntity.status(200).body(pcService.getAllPc());
    }

    @PostMapping("add-pc/{pcCenterId}")
    public ResponseEntity addPc(@PathVariable Integer pcCenterId,@AuthenticationPrincipal User user ,@Valid @RequestBody PC pc) {
        pcService.addPc(pc, user.getId(), pcCenterId);
        return ResponseEntity.status(200).body("pc added successfully");

    }

    @PutMapping("update-pc/{pcId}")
    public ResponseEntity updatePc(@PathVariable Integer pcId, @AuthenticationPrincipal User user, @Valid @RequestBody PC pc) {
        pcService.updatePc(pcId, pc, user.getId());
        return ResponseEntity.status(200).body("pc updated successfully");
    }

    @DeleteMapping("/delete-pc/{pcId}")
    public ResponseEntity deletePc(@PathVariable Integer pcId,@AuthenticationPrincipal User user ) {
        pcService.deletePc(pcId, user.getId());
        return ResponseEntity.status(200).body("pc deleted successfully");

    }



}
