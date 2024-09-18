package org.twspring.noob.Service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.twspring.noob.Api.ApiException;
import org.twspring.noob.Model.User;
import org.twspring.noob.Repository.AuthRepository;

import java.util.List;
@Service
@RequiredArgsConstructor
public class AdminService {

    private final AuthRepository authRepository;

    //ADMIN
    public User createAdmin(User user) {
        String hash = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setRole("ADMIN");
        user.setPassword(hash);
        return authRepository.save(user);
    }
}
