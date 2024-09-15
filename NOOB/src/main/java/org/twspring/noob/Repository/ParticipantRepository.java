package org.twspring.noob.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.twspring.noob.Model.Participant;

import java.util.List;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Integer> {
    Participant findParticipantById(Integer id);
    Participant findParticipantByPlayerIdAndTournamentId(Integer playerId, Integer tournamentId);

    Participant findParticipantByPlayerId(Integer playerId);
    List<Participant> findParticipantByLeagueId(Integer leagueId);
    Participant findParticipantByPlayerIdAndLeagueId(Integer playerId, Integer league);
    List<Participant> findParticipantByLeagueIdOrderByScoreDesc(Integer leagueId);


    @Query(value = "SELECT * FROM Participant WHERE tournament_id = ?1 ORDER BY seed ASC LIMIT ?2", nativeQuery = true)
    List<Participant> findTopParticipantsByTournamentId(Integer tournamentId, int limit);

}
