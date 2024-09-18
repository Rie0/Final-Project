package org.twspring.noob.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.twspring.noob.Api.ApiResponse;
import org.twspring.noob.DTO.PlayerDTO;
import org.twspring.noob.Model.Review;
import org.twspring.noob.Model.User;
import org.twspring.noob.Service.PlayerService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/player")
@RequiredArgsConstructor
public class PlayerController { //Rafeef
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
    @PutMapping("/update-my-info")
    public ResponseEntity updateMyInfo(@AuthenticationPrincipal User player, @RequestBody@Valid  PlayerDTO playerDTO){
        playerService.updatePlayer(player.getId(), playerDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Player updated successfully"));
    }
    @DeleteMapping("/delete-my-account")
    public ResponseEntity deleteMyAccount(@AuthenticationPrincipal User player){
        playerService.deletePlayer(player.getId());
        return ResponseEntity.status(200).body(new ApiResponse("Player deleted successfully"));
    }

    @DeleteMapping("/delete-account-by-admin/{id}")
    public ResponseEntity deleteAccount(@PathVariable Integer id){
        playerService.deletePlayer(id);
        return ResponseEntity.status(200).body(new ApiResponse("Player deleted successfully"));
    }

    //EXTRA

    @GetMapping("/get/{playerId}")
    public ResponseEntity getPlayer(@PathVariable Integer playerId){
        return ResponseEntity.status(200).body(playerService.getPlayerById(playerId));
    }

    @GetMapping("/get/profile/{playerId}")
    public ResponseEntity getPlayerProfile(@PathVariable Integer playerId){
        return ResponseEntity.status(200).body(playerService.getPlayerProfileById(playerId));
    }

    @GetMapping("/get/profile/by-username/{username}")
    public ResponseEntity getPlayerProfiles(@PathVariable String username){
        return ResponseEntity.status(200).body(playerService.getPlayerProfilesByUsernameContaining(username));
    }

    @PutMapping("/edit-bio")
    public ResponseEntity editBio(@AuthenticationPrincipal User player, @RequestBody String bio){
        playerService.updateBio(player.getId(), bio);
        return ResponseEntity.status(200).body(new ApiResponse("Player bio updated successfully"));
    }

    @GetMapping("/invites/get-invites")
    public ResponseEntity getInvites(@AuthenticationPrincipal User player){
        return ResponseEntity.status(200).body(playerService.getInvitesByPlayerId(player.getId()));
    }

    @PutMapping("/invites/{inviteId}/accept")
    public ResponseEntity acceptInvite(@AuthenticationPrincipal User player, @PathVariable Integer inviteId){
        playerService.acceptInvite(player.getId(), inviteId);
        return ResponseEntity.status(200).body(new ApiResponse("Invite accepted successfully, you're now a member of the team"));
    }

    @PutMapping("/invites/{inviteId}/decline")
    public ResponseEntity declineInvite(@AuthenticationPrincipal User player, @PathVariable Integer inviteId) {
        playerService.declineInvite(player.getId(), inviteId);
        return ResponseEntity.status(200).body(new ApiResponse("Invite declined successfully"));
    }

    @PutMapping("/team/leave")
    public ResponseEntity leaveTeam(@AuthenticationPrincipal User player){
        playerService.leaveTeam(player.getId());
        return ResponseEntity.status(200).body(new ApiResponse("You left the team successfully"));
    }


    // Mohammed Area below
    @PostMapping("/add-review/{coachId}/{coachingSessionId}/{comment}/{rating}")
    public ResponseEntity addReview(@AuthenticationPrincipal User player, @PathVariable Integer coachId, @PathVariable Integer coachingSessionId, @PathVariable String comment, @PathVariable Float rating) {
        playerService.addReview(player.getId(), coachId, coachingSessionId, comment, rating);
        return ResponseEntity.status(200).body(new ApiResponse("Review added successfully"));
    }


        //Mohammed - CRUD get all
        @GetMapping("/get-all-reviews")
        public ResponseEntity<List<Review>> getAllReviews() {
            return ResponseEntity.status(200).body(playerService.getAllReviews());
        }

        // Mohammed - CRUD update
        @PutMapping("/update-review/{reviewId}")
        public ResponseEntity<ApiResponse> updateReview(@PathVariable Integer reviewId, @RequestBody @Valid Review review, @AuthenticationPrincipal User player) {
            playerService.updateReview(reviewId, review);
            return ResponseEntity.status(200).body(new ApiResponse("Review updated successfully"));
        }

        // Mohammed -CRUD delete
        @DeleteMapping("/delete-review/{reviewId}")
        public ResponseEntity<ApiResponse> deleteReview(@PathVariable Integer reviewId, @AuthenticationPrincipal User player) {
            playerService.deleteReview(reviewId);
            return ResponseEntity.status(200).body(new ApiResponse("Review deleted successfully"));
        }

        // Mohammed - EXTRA endpoint: getting a review by id
        @GetMapping("/get/{reviewId}")
        public ResponseEntity getReviewById(@PathVariable Integer reviewId) {
            return ResponseEntity.status(200).body(playerService.getReviewById(reviewId));
        }

        // Mohammed - EXTRA endpoint: getting reviews for a player
        @GetMapping("/get-by-player/{playerId}")
        public ResponseEntity<List<Review>> getReviewsForPlayer(@PathVariable Integer playerId) {
            return ResponseEntity.status(200).body(playerService.getReviewsForPlayer(playerId));
        }





    }


