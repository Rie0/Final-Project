package org.twspring.noob.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.twspring.noob.Model.PcCentres;
import org.twspring.noob.Model.User;
import org.twspring.noob.Service.PcCentresService;

import java.util.List;


@RestController
@RequestMapping("/api/v1/pc-centre")
@RequiredArgsConstructor
public class PcCentresController {
    private final PcCentresService pcCentresService;


    @GetMapping("/get-all")
    public ResponseEntity getAllPcCentres(){
        return ResponseEntity.status(200).body(pcCentresService.getAllPcCentres());
    }

    @PostMapping("/add-pcCentre")
    public ResponseEntity addPcCentres(@AuthenticationPrincipal User user, @Valid @RequestBody PcCentres pcCentres){
        pcCentresService.addPcCentres(pcCentres, user.getId());
        return ResponseEntity.status(200).body("PC Centre added successfully");

    }
    @PutMapping("update-pcCentre/{id}")
    public ResponseEntity updatePcCentres(@PathVariable Integer id,@Valid @RequestBody PcCentres pcCentres){
        pcCentresService.updatePcCentres(id, pcCentres);
        return ResponseEntity.status(200).body("PC Centres  updated successfully");
    }
    @DeleteMapping("/delete-pcCentre/{id}")
    public ResponseEntity deletePcCentres(@PathVariable Integer id){
        pcCentresService.deletePcCentres(id);
        return ResponseEntity.status(200).body("PC Centres deleted successfully");
    }

    /////
    @GetMapping("/get-pcCentre-by-Vendor/{vendorId}")
    public ResponseEntity getPcCentreByVendor(@PathVariable Integer vendorId){
        return ResponseEntity.status(200).body(pcCentresService.getPcCentresByVendorID(vendorId));
    }

    /////
    @PutMapping("/admin-Aproved-pcCenter/{PcCenterId}")
    public ResponseEntity approvedPcCenter(@PathVariable Integer PcCenterId) {
        pcCentresService.adminAprovedPcCenter(PcCenterId);
        return ResponseEntity.status(200).body("approved PC Center successfully");
    }

    /////
    @GetMapping("/ratings/{rating}")
    public ResponseEntity getRatings(@PathVariable Integer rating){
        return ResponseEntity.status(200).body(pcCentresService.getPcCentresByRating(rating));

    }

    //////
    @GetMapping("/ratings/{minRating}/{maxRating}")
    public List<PcCentres> getPcCentresByRatingRange(@PathVariable int minRating, @PathVariable int maxRating) {
        return pcCentresService.getPcCentresByRatingRange(minRating, maxRating);
    }

    ///

    @GetMapping("/get-pc-centre-by-name/{name}")
    public ResponseEntity getPcCentreByName(@PathVariable String name){
        return ResponseEntity.status(200).body(pcCentresService.getPcCentreByCentreName(name));
    }

    @GetMapping("/get-pcCentre/by/location/{location}")
    public ResponseEntity getPcCentreByLocation(@PathVariable String location){
        return ResponseEntity.status(200).body(pcCentresService.getPcCentreByLocation(location));

    }
    @GetMapping("/get-all-approved")
    public ResponseEntity getAllApprovedPcCentre(){
        return ResponseEntity.status(200).body(pcCentresService.getApprovedPcCentres());

    }
    @GetMapping("/get-not-approved-pc-centre")
    public ResponseEntity getNotApprovedPcCentre(){
        return ResponseEntity.status(200).body(pcCentresService.getAllNotApprovedPcCentre());
    }


}