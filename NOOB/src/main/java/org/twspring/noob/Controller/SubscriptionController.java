package org.twspring.noob.Controller;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.twspring.noob.Model.PC;
import org.twspring.noob.Model.Subscription;
import org.twspring.noob.Model.User;
import org.twspring.noob.Service.SubscriptionService;

@RestController
@RequestMapping("/api/v1/subscription")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;



    @GetMapping("/get-all")
    public ResponseEntity getAllsubscription(){
        return ResponseEntity.status(200).body(subscriptionService.getAllsubscription());
    }

    @PostMapping("/add-subscription/{id}")
    public ResponseEntity addsubscription(@PathVariable Integer subscriprionId,@AuthenticationPrincipal User user,@Valid @RequestBody Subscription subscription){
        subscriptionService.addsubscription(subscription,subscriprionId, user.getId());
        return ResponseEntity.status(200).body("subscription added successfully");

    }
    @PutMapping("/update-subscription/{subscriptionId}")
    public ResponseEntity updatesubscription(@PathVariable Integer subscriptionId,@Valid @RequestBody Subscription subscription,@AuthenticationPrincipal User user){
        subscriptionService.updatesubScription(subscriptionId,subscription, user.getId());
        return ResponseEntity.status(200).body("subscription updated successfully");
    }
    @DeleteMapping("/delete-subscription/{subscriptionId}")
    public ResponseEntity deletesubscription(@PathVariable Integer subscriptionId, @AuthenticationPrincipal User user){
        subscriptionService.deleteSubscription(user.getId(), subscriptionId);
        return ResponseEntity.status(200).body("subscription deleted successfully");
    }

    @GetMapping("/get-subscription/pc-center/{pcCenterId}")
    public ResponseEntity getSubscriptionsByPcCenter(@PathVariable Integer pcCenterId){
        return ResponseEntity.status(200).body(subscriptionService.getsubscriptionbyPcCentreId(pcCenterId));
    }



}
