package org.twspring.noob.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.twspring.noob.Model.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {
    Team findTeamById(Integer id);
}
