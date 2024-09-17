package org.twspring.noob.Service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.twspring.noob.Api.ApiException;
import org.twspring.noob.Model.User;
import org.twspring.noob.Repository.AuthRepository;

import java.time.LocalDate;
import java.time.Period;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AuthRepository authRepository;

    //ADMIN
    public void createAdmin(User user) {
        String hash= new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(hash);
        user.setAge(Period.between(user.getBirthday(), LocalDate.now()).getYears());
        if (user.getAge()<18){
            throw new ApiException("Admin of the system must be over 18");
        }
        user.setRole("ADMIN");
        authRepository.save(user);
    }
}
