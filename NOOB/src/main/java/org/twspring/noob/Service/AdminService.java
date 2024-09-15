package org.twspring.noob.Service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.twspring.noob.Model.User;
import org.twspring.noob.Repository.AuthRepository;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AuthRepository authRepository;

    //ADMIN
    public void createAdmin(User user) {
        user.setRole("USER");
        authRepository.save(user);
    }
}
