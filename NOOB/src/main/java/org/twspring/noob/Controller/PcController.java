package org.twspring.noob.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.twspring.noob.Model.PC;
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
    public ResponseEntity addPc(@PathVariable Integer pcCenterID,@PathVariable Integer vendorId ,@Valid @RequestBody PC pc) {
        pcService.addPc(pc,vendorId,pcCenterID);
        return ResponseEntity.status(200).body("pc added successfully");

    }

    @PutMapping("update-pc/{id}")
    public ResponseEntity updatePc(@PathVariable Integer id, @Valid @RequestBody PC pc) {
        pcService.updatePc(id, pc);
        return ResponseEntity.status(200).body("pc updated successfully");
    }

    @DeleteMapping("/delete-pc/{id}")
    public ResponseEntity deletePc(@PathVariable Integer id) {
        pcService.deletePc(id);
        return ResponseEntity.status(200).body("pc deleted successfully");

    }



}
