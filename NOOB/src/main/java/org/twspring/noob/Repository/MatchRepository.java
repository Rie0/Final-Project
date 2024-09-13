package org.twspring.noob.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.twspring.noob.Model.Match;
import org.twspring.noob.Model.Tournament;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Integer> {
    Match findMatchById(Integer id);
    List<Match> findByTournament(Tournament tournament);
    List<Match> findByTournamentAndStatus(Tournament tournament, String status);
// Custom query to find matches by tournament

}
