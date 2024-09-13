package org.twspring.noob.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.twspring.noob.Model.League;
import org.twspring.noob.Model.Match;

@Repository
public interface LeagueRepository extends JpaRepository<League, Integer> {
    League findLeagueById(int id);
}
