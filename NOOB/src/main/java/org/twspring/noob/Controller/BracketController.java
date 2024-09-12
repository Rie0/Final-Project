package org.twspring.noob.Controller;

import org.twspring.noob.Model.Bracket;
import org.twspring.noob.Service.BracketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bracket")
public class BracketController {

    private final BracketService bracketService;

    @GetMapping("/get")
    public ResponseEntity getBrackets() {
        return ResponseEntity.status(HttpStatus.OK).body(bracketService.getBrackets());
    }

    @PostMapping("/add")
    public ResponseEntity addBracket(@Valid @RequestBody Bracket bracket) {
        bracketService.saveBracket(bracket);
        return ResponseEntity.status(HttpStatus.OK).body("Bracket added successfully");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateBracket(@Valid @RequestBody Bracket bracket, @PathVariable Integer id) {
        bracketService.updateBracket(id, bracket);
        return ResponseEntity.status(HttpStatus.OK).body("Bracket updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteBracket(@PathVariable Integer id) {
        bracketService.deleteBracket(id);
        return ResponseEntity.status(HttpStatus.OK).body("Bracket deleted successfully");
    }
}
