package org.twspring.noob.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.twspring.noob.Model.Player;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {
    Player findPlayerById(Integer id);

    List<Player> findPlayerByTeamId(Integer teamId);
}
