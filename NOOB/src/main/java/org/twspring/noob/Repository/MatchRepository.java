package org.twspring.noob.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.twspring.noob.Model.Match;

@Repository
public interface MatchRepository extends JpaRepository<Match, Integer> {
    Match findMatchById(Integer id);
}
