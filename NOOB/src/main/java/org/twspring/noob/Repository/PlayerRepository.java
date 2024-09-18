package org.twspring.noob.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.twspring.noob.Model.Player;
import org.twspring.noob.Model.Review;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {
    Player findPlayerById(Integer id);
    List<Player> findPlayersByUserUsernameContaining(String username);
    List<Player> findPlayerByTeamId(Integer teamId);

}
