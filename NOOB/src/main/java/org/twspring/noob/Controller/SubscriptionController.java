package org.twspring.noob.Controller;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.twspring.noob.Model.PC;
import org.twspring.noob.Model.Subscription;
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

    @PostMapping("add-subscription")
    public ResponseEntity addsubscription(@Valid @RequestBody Subscription subscription){
        subscriptionService.addsubscription(subscription);
        return ResponseEntity.status(200).body("subscription added successfully");

    }
    @PutMapping("update-subscription/{id}")
    public ResponseEntity updatesubscription(@PathVariable Integer id,@Valid @RequestBody Subscription subscription){
        subscriptionService.updatesubScription(id,subscription);
        return ResponseEntity.status(200).body("subscription updated successfully");
    }
    @DeleteMapping("/delete-subscription/{id}")
    public ResponseEntity deletesubscription(@PathVariable Integer id){
        subscriptionService.deleteSubscription(id);
        return ResponseEntity.status(200).body("subscription deleted successfully");
    }

    /////
@PostMapping("/supscripe/{playerId}/{SubscrptionId}/{zoneId}")
    public ResponseEntity supscribe(@PathVariable Integer playerId,@PathVariable Integer SubscrptionId,@PathVariable Integer zoneId){
        subscriptionService.subscribePlayerToSubscription(playerId,SubscrptionId,zoneId);
        return ResponseEntity.status(200).body("subscription added successfully");
}


}
