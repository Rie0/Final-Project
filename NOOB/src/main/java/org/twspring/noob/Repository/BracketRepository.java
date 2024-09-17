package org.twspring.noob.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.twspring.noob.Model.Bracket;
import org.twspring.noob.Model.Tournament;

@Repository
public interface BracketRepository extends JpaRepository<Bracket, Integer> {
    Bracket findBracketById(Integer id);
    Bracket findByTournamentAndBracketType(Tournament tournament, String bracketType);

}
