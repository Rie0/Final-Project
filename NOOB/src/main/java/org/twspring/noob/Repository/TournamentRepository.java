package org.twspring.noob.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.twspring.noob.Model.Tournament;

import java.util.List;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Integer> {
    Tournament findTournamentById(Integer id);
    // Custom method to find a tournament by a specific bracket ID
    @Query("SELECT t FROM Tournament t JOIN t.brackets b WHERE b.id = ?1")
    Tournament findByBracketId(Integer bracketId);
    List<Tournament> findByGame(String game);
    List<Tournament> findByCity(String city);
    List<Tournament> findByAttendanceType(String attendanceType);
    List<Tournament> findByStatus(Tournament.Status status);
    @Query("SELECT t FROM Tournament t WHERE t.startDate >= CURRENT_DATE")
    List<Tournament> findUpcomingTournaments();
    @Query("SELECT t FROM Tournament t WHERE t.id = ?1 AND t.organizer.id = ?2")
    Tournament findByTournamentIdAndOrganizerId(Integer tournamentId, Integer organizerId);
}
