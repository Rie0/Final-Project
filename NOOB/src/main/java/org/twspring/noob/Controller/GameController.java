package org.twspring.noob.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.twspring.noob.Model.Game;
import org.twspring.noob.Model.PC;
import org.twspring.noob.Service.GameService;

@RestController
@RequestMapping("/api/v1/game")
@RequiredArgsConstructor
public class GameController {
    private final GameService gameService;

    @GetMapping("/get-all")
    public ResponseEntity getAllPc(){
        return ResponseEntity.status(200).body(gameService.getAllGame());
    }
////
    @PostMapping("add-game/{pcId}")
    public ResponseEntity addGame(@PathVariable Integer pcId,@Valid @RequestBody Game game){
        gameService.addGame(game,pcId);
        return ResponseEntity.status(200).body("game added successfully");

    }
    @PutMapping("update-game/{id}")
    public ResponseEntity updateGame(@PathVariable Integer id, @Valid @RequestBody Game game){
        gameService.updateGame(id,game);
        return ResponseEntity.status(200).body("game updated successfully");
    }
    @DeleteMapping("/delete-game/{id}")
    public ResponseEntity deleteGame(@PathVariable Integer id){
        gameService.deleteGame(id);
        return ResponseEntity.status(200).body("game deleted successfully");
    }
}
