package org.twspring.noob.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.twspring.noob.Model.Match;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Integer> {
    Match findMatchById(Integer id);
    List<Match> findMatchByRoundId(Integer roundId);
    List<Match> findMatchByLeagueId(Integer leagueId);

}
