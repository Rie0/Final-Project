package org.twspring.noob.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.twspring.noob.Model.Round;

import java.util.List;

@Repository
public interface RoundRepository extends JpaRepository<Round, Integer> {
    Round findRoundById(Integer id);
<<<<<<< HEAD
    List<Round> findRoundByLeagueId(Integer leagueId);
=======


>>>>>>> Hussam
}
