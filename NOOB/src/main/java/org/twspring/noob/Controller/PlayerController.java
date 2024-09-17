package org.twspring.noob.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.twspring.noob.Api.ApiResponse;
import org.twspring.noob.DTO.PlayerDTO;
import org.twspring.noob.Model.Review;
import org.twspring.noob.Service.PlayerService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/player")
@RequiredArgsConstructor
public class PlayerController {
    private final PlayerService playerService;

    @GetMapping("/get-all")
    public ResponseEntity getAllPlayers(){
        return ResponseEntity.status(200).body(playerService.getPlayers());
    }

    @PostMapping("/register")
    public ResponseEntity registerPlayer(@RequestBody@Valid PlayerDTO playerDTO){
        playerService.registerPlayer(playerDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Player registered successfully"));
    }
    @PutMapping("/{playerId}/update-my-info")
    public ResponseEntity updateMyInfo(@PathVariable Integer playerId, @RequestBody@Valid  PlayerDTO playerDTO){
        playerService.updatePlayer(playerId, playerDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Player updated successfully"));
    }
    @DeleteMapping("/{playerId}/delete-my-account")
    public ResponseEntity deleteMyAccount(@PathVariable Integer playerId){
        playerService.deletePlayer(playerId);
        return ResponseEntity.status(200).body(new ApiResponse("Player deleted successfully"));
    }

    //EXTRA

    @GetMapping("/get/{playerId}")
    public ResponseEntity getPlayer(@PathVariable Integer playerId){
        return ResponseEntity.status(200).body(playerService.getPlayerById(playerId));
    }

    //

    @GetMapping("/{playerId}/invites/get-invites")
    public ResponseEntity getInvites(@PathVariable Integer playerId){
        return ResponseEntity.status(200).body(playerService.getInvitesByPlayerId(playerId));
    }

    @PutMapping("/{playerId}/invites/{inviteId}/accept")
    public ResponseEntity acceptInvite(@PathVariable Integer playerId, @PathVariable Integer inviteId){
        playerService.acceptInvite(playerId, inviteId);
        return ResponseEntity.status(200).body(new ApiResponse("Invite accepted successfully, you're now a member of the team"));
    }

    @PutMapping("/{playerId}/invites/{inviteId}/decline")
    public ResponseEntity declineInvite(@PathVariable Integer playerId, @PathVariable Integer inviteId) {
        playerService.declineInvite(playerId, inviteId);
        return ResponseEntity.status(200).body(new ApiResponse("Invite declined successfully"));
    }

    @PutMapping("/{playerId}/team/leave")
    public ResponseEntity leaveTeam(@PathVariable Integer playerId){
        playerService.leaveTeam(playerId);
        return ResponseEntity.status(200).body(new ApiResponse("You left the team successfully"));
    }


    // Mohammed
    @PostMapping("/add-review/{playerId}/{coachId}/{coachingSessionId}/{comment}/{rating}")
    public ResponseEntity addReview(@PathVariable Integer playerId, @PathVariable Integer coachId, @PathVariable Integer coachingSessionId, @PathVariable String comment, @PathVariable Float rating) {
        playerService.addReview(playerId, coachId, coachingSessionId, comment, rating);
        return ResponseEntity.status(200).body(new ApiResponse("Review added successfully"));
    }


        // CRUD get all
        @GetMapping("/get-all-reviews")
        public ResponseEntity<List<Review>> getAllReviews() {
            return ResponseEntity.status(200).body(playerService.getAllReviews());
        }

        // CRUD update
        @PutMapping("/update-review/{reviewId}")
        public ResponseEntity<ApiResponse> updateReview(@PathVariable Integer reviewId, @RequestBody @Valid Review review) {
            playerService.updateReview(reviewId, review);
            return ResponseEntity.status(200).body(new ApiResponse("Review updated successfully"));
        }

        // CRUD delete
        @DeleteMapping("/delete-review/{reviewId}")
        public ResponseEntity<ApiResponse> deleteReview(@PathVariable Integer reviewId) {
            playerService.deleteReview(reviewId);
            return ResponseEntity.status(200).body(new ApiResponse("Review deleted successfully"));
        }

        // EXTRA endpoint: getting a review by id
        @GetMapping("/get/{reviewId}")
        public ResponseEntity getReviewById(@PathVariable Integer reviewId) {
            return ResponseEntity.status(200).body(playerService.getReviewById(reviewId));
        }

        // EXTRA endpoint: getting reviews for a player
        @GetMapping("/get-by-player/{playerId}")
        public ResponseEntity<List<Review>> getReviewsForPlayer(@PathVariable Integer playerId) {
            return ResponseEntity.status(200).body(playerService.getReviewsForPlayer(playerId));
        }



    }


