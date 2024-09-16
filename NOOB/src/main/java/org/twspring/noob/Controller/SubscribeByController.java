package org.twspring.noob.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.twspring.noob.Model.PC;
import org.twspring.noob.Model.SubscripeBy;
import org.twspring.noob.Service.SubscribeByService;

@RestController
@RequestMapping("/api/v1/SubscripeBy")
@RequiredArgsConstructor
public class SubscribeByController {

    private final SubscribeByService subscribeByService;


    @GetMapping("/get-all")
    public ResponseEntity getAllSubscripeBy(){
        return ResponseEntity.status(200).body(subscribeByService.getAllsubscribeBy());
    }

    @PostMapping("add-SubscripeBy/{vendorId}/{pcCenterID}/{zoneId}")
    public ResponseEntity addSubscripeBy(@PathVariable Integer vendorId,@PathVariable Integer pcCenterID,@PathVariable Integer zoneId,@Valid @RequestBody SubscripeBy subscripeBy){
        subscribeByService.addSubscripeBy(subscripeBy,vendorId,pcCenterID,zoneId);
        return ResponseEntity.status(200).body("Subscribe By added successfully");

    }
    @PutMapping("update-SubscripeBy/{id}")
    public ResponseEntity updateSubscripeBy(@PathVariable Integer id, @Valid @RequestBody SubscripeBy subscripeBy){
        subscribeByService.updateSubscribeBy(id,subscripeBy);
        return ResponseEntity.status(200).body("Subscribe By updated successfully");
    }
    @DeleteMapping("/delete-SubscripeBy/{id}")
    public ResponseEntity deleteSubscripeBy(@PathVariable Integer id){
        subscribeByService.deleteSubscripeBy(id);
        return ResponseEntity.status(200).body("Subscribe By deleted successfully");
    }

////
@GetMapping("/get-supscripe-by/{playerId}")
    public ResponseEntity getSubscripeByPlayerId(@PathVariable Integer playerId){
        return ResponseEntity.status(200).body(subscribeByService.getSubscribedByPlayerId(playerId));
}




}
