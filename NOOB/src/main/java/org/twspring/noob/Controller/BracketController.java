package org.twspring.noob.Controller;

import org.twspring.noob.Service.BracketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.twspring.noob.Model.User;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bracket")
public class BracketController {

    private final BracketService bracketService;

    @GetMapping("/get")
    public ResponseEntity getAllBrackets(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(bracketService.getAllBrackets());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteBracket(@PathVariable Integer id, @AuthenticationPrincipal User user) {
        bracketService.deleteBracket(id);
        return ResponseEntity.ok("Bracket deleted successfully");
    }
}
