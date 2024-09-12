package org.twspring.noob.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.twspring.noob.Model.Participant;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Integer> {
    Participant findParticipantById(Integer id);
    Participant findParticipantByPlayerIdAndTournamentId(Integer playerId, Integer tournamentId);


}
