package org.twspring.noob.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.twspring.noob.Model.CoachingSession;


@Repository
public interface CoachingSessionRepository extends JpaRepository<CoachingSession, Integer> {

    CoachingSession findCoachingSessionById(Integer id);

}
