package org.twspring.noob.Controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.twspring.noob.Model.User;
import org.twspring.noob.Service.AdminService;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    //ONLY EXECUTED ONCE, COMMENTED LATER
    @PostMapping("/add")
    public ResponseEntity addAdmin(@RequestBody@Valid User user) {
        adminService.createAdmin(user);
        return ResponseEntity.ok().build();
    }
}
