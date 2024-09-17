package org.twspring.noob.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.twspring.noob.Model.CoachingSession;
import org.springframework.data.jpa.repository.Query;


@Repository
public interface CoachingSessionRepository extends JpaRepository<CoachingSession, Integer> {

    CoachingSession findCoachingSessionById(Integer id);
    @Query("SELECT cs FROM CoachingSession cs WHERE cs.player.id = ?1 AND cs.coach.id = ?2")
    CoachingSession findCoachingSessionByPlayerIdAndCoachId(Integer playerId, Integer coachId);

}
