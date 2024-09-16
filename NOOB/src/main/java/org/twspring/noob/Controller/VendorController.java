package org.twspring.noob.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.twspring.noob.Api.ApiResponse;
import org.twspring.noob.DTO.PlayerDTO;
import org.twspring.noob.DTO.VendotDTO;
import org.twspring.noob.Model.Game;
import org.twspring.noob.Model.Vendor;
import org.twspring.noob.Service.VendorService;

@RestController
@RequestMapping("/api/v1/vendor")
@RequiredArgsConstructor
public class VendorController {

    private final VendorService vendorService;

    @GetMapping("/get-all")
    public ResponseEntity getAllVendor(){
        return ResponseEntity.status(200).body(vendorService.getAllVendors());
    }
    @PostMapping("/register")
    public ResponseEntity registerVendor(@RequestBody@Valid VendotDTO vendotDTO){
        vendorService.registerVendor(vendotDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Vendor registered successfully"));
    }
    @PutMapping("/{vendorId}/update-my-info")
    public ResponseEntity updateMyInfo(@PathVariable Integer vendorId, @RequestBody@Valid  VendotDTO vendotDTO){
        vendorService.updateVendor(vendorId, vendotDTO);
        return ResponseEntity.status(200).body(new ApiResponse("vendor updated successfully"));
    }
    @DeleteMapping("/{vendorId}/delete-my-account")
    public ResponseEntity deleteMyAccount(@PathVariable Integer vendorId){
        vendorService.deleteVendor(vendorId);
        return ResponseEntity.status(200).body(new ApiResponse("Vendor deleted successfully"));
    }
    /////
    @GetMapping("/get-vendorbyid/{vendorId}")
    public ResponseEntity getVendor(@PathVariable Integer vendorId){
        return ResponseEntity.status(200).body(vendorService.getVendorById(vendorId));
    }
}
