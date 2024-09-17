package org.twspring.noob.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.twspring.noob.Model.User;

import java.util.List;

@Repository
public interface AuthRepository extends JpaRepository<User,Integer> {
    User findUserById(Integer id);
    User findByUsername(String username);
    List<User> findByUsernameContaining(String username);
}
