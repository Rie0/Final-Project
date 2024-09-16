package org.twspring.noob.Controller;

import org.twspring.noob.DTO.BracketDTO;
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
    public ResponseEntity getAllBrackets() {
        return ResponseEntity.ok(bracketService.getAllBrackets());
    }

//    @PostMapping("/add")
//    public ResponseEntity  addBracket(@Valid @RequestBody BracketDTO bracketDTO) {
//        bracketService.addBracket(bracketDTO);
//        return ResponseEntity.status(HttpStatus.CREATED).body("Bracket added successfully");
//    }
//
//    @PutMapping("/update/{id}")
//    public ResponseEntity updateBracket(@Valid @RequestBody BracketDTO bracketDTO, @PathVariable Integer id) {
//        bracketService.updateBracket(id, bracketDTO);
//        return ResponseEntity.ok("Bracket updated successfully");
//    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteBracket(@PathVariable Integer id) {
        bracketService.deleteBracket(id);
        return ResponseEntity.ok("Bracket deleted successfully");
    }
}
