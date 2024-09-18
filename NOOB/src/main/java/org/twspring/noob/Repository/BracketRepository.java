package org.twspring.noob.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.twspring.noob.Model.Bracket;
import org.twspring.noob.Model.Tournament;

@Repository
public interface BracketRepository extends JpaRepository<Bracket, Integer> {
    Bracket findBracketById(Integer id);
    Bracket findByTournamentAndBracketType(Tournament tournament, String bracketType);
    @Query("SELECT b FROM Bracket b WHERE b.id = ?1 AND b.tournament.id = ?2")
    Bracket findByIdAndTournamentId(Integer bracketId, Integer tournamentId);
}
