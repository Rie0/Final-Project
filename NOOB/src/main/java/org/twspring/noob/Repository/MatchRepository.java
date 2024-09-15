package org.twspring.noob.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.twspring.noob.Model.Match;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Integer> {
    Match findMatchById(Integer id);
    List<Match> findMatchByRoundId(Integer roundId);
    List<Match> findMatchByLeagueId(Integer leagueId);


    List<Match> findMatchByParticipant1Id(Integer participantId);
    List<Match> findMatchByParticipant2Id(Integer participantId);


    @Query("SELECT m from Match m where m.participant1.player.id=?1 OR m.participant2.player.id=?1")
    List<Match> findMatchByParticipantId(Integer participantId);

}
