package org.twspring.noob.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.twspring.noob.Model.Tournament;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Integer> {
    Tournament findTournamentById(Integer id);
}
