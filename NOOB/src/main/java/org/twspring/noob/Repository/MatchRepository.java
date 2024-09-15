package org.twspring.noob.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.twspring.noob.Model.Match;
import org.twspring.noob.Model.Tournament;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Integer> {
    Match findMatchById(Integer id);
    List<Match> findByTournament(Tournament tournament);
    List<Match> findByTournamentAndStatus(Tournament tournament, String status);

    @Query("SELECT m FROM Match m WHERE " +
            "(m.participant1.player.id = ?1 AND m.participant2.player.id = ?2) " +
            "OR (m.participant1.player.id = ?2 AND m.participant2.player.id = ?1)")
    List<Match> findMatchHistoryBetweenPlayers(Integer playerId1, Integer playerId2);// Custom query to find matches by tournament


}
