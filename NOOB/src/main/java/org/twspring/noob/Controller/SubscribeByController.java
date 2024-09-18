package org.twspring.noob.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.twspring.noob.Model.PC;
import org.twspring.noob.Model.SubscripeBy;
import org.twspring.noob.Model.User;
import org.twspring.noob.Service.SubscribeByService;

@RestController
@RequestMapping("/api/v1/SubscripeBy")
@RequiredArgsConstructor
public class SubscribeByController {

    private final SubscribeByService subscribeByService;

    @GetMapping("/get-all")
    public ResponseEntity getAllSubscripeBy() {
        return ResponseEntity.status(200).body(subscribeByService.getAllsubscribeBy());
    }

    @PostMapping("/add-SubscripeBy/{vendorId}/{pcCenterID}/{zoneId}/{playerId}")
    public ResponseEntity addSubscripeBy(@PathVariable Integer vendorId, @PathVariable Integer pcCenterID,
                                         @PathVariable Integer zoneId, @PathVariable Integer playerId, @Valid @RequestBody SubscripeBy subscripeBy) {
        subscribeByService.addSubscripeBy(subscripeBy, vendorId, pcCenterID, zoneId, playerId);
        return ResponseEntity.status(200).body("Subscribe By added successfully");

    }

    @PutMapping("/update-SubscripeBy/{id}")
    public ResponseEntity updateSubscripeBy(@PathVariable Integer id, @Valid @RequestBody SubscripeBy subscripeBy) {
        subscribeByService.updateSubscribeBy(id, subscripeBy);
        return ResponseEntity.status(200).body("Subscribe By updated successfully");
    }

    @DeleteMapping("/delete-SubscripeBy/{id}")
    public ResponseEntity deleteSubscripeBy(@PathVariable Integer id) {
        subscribeByService.deleteSubscripeBy(id);
        return ResponseEntity.status(200).body("Subscribe By deleted successfully");
    }

    ////
    @GetMapping("/get-supscripe-by")
    public ResponseEntity getSubscripeByPlayerId(@AuthenticationPrincipal User user) {
        return ResponseEntity.status(200).body(subscribeByService.getSubscribedByPlayerId(user.getId()));
    }

    @GetMapping("/player-return-subscription/{subscripeById}")
    public ResponseEntity playerReturnSubscription(@PathVariable Integer subscripeById) {
        subscribeByService.playerReturnSubscription(subscripeById);
        return ResponseEntity.status(200).build();
    }

    /////
    @PostMapping("/supscripe/{playerId}/{SubscrptionId}/{zoneId}")
    public ResponseEntity supscribe(@PathVariable Integer playerId, @PathVariable Integer SubscrptionId,
                                    @PathVariable Integer zoneId, @RequestParam int members, @RequestParam String coupan,  @RequestParam String name) {
        subscribeByService.subscribePlayerToSubscription(playerId, SubscrptionId, zoneId, members, coupan, name);
        return ResponseEntity.status(200).body("subscription added successfully");
    }

}
