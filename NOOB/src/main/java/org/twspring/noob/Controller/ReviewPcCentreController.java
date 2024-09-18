package org.twspring.noob.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.twspring.noob.Model.ReviewPcCentre;
import org.twspring.noob.Model.User;
import org.twspring.noob.Service.ReviewPcCentreService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/PcCetreRating")
@RequiredArgsConstructor
public class ReviewPcCentreController {
    private final ReviewPcCentreService reviewPcCentreService;

    @GetMapping("/getAll")
    public ResponseEntity getAll() {
        return ResponseEntity.status(200).body(reviewPcCentreService.getAllReviewPcCentre());

    }

    @PostMapping("/add/{pcCentreId}")
    public ResponseEntity add(@AuthenticationPrincipal User user , @PathVariable Integer pcCentreId, @RequestBody ReviewPcCentre reviewPcCentre) {
        reviewPcCentreService.addReviewToPcCentre(user.getId(), pcCentreId, reviewPcCentre);
        return ResponseEntity.status(200).body("review added successfully");
    }
    @PutMapping("/update/{reviewPcCentreId}")
    public ResponseEntity update(@AuthenticationPrincipal User user , @PathVariable Integer reviewPcCentreId, @RequestBody @Valid ReviewPcCentre reviewPcCentre) {
        reviewPcCentreService.updateReviewPcCentre(user.getId(), reviewPcCentreId, reviewPcCentre);
        return ResponseEntity.status(200).body("review updated successfully");
    }
    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity delete(@AuthenticationPrincipal User user ,@PathVariable Integer reviewId) {
        reviewPcCentreService.deleteReview(user.getId(), reviewId);
        return ResponseEntity.status(200).body("review deleted successfully");
    }

    @GetMapping("/get-review-by-playerId")
    public ResponseEntity getReviewPcCentreByPlayerId(@AuthenticationPrincipal User user) {
        return ResponseEntity.status(200).body(reviewPcCentreService.getReviewCentreByPlayerId(user.getId()));

    }
}