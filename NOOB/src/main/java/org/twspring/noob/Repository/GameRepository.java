package org.twspring.noob.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.twspring.noob.Model.Game;
@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {

    Game findGameById(Integer id);
}
