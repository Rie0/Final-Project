package org.twspring.noob.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.twspring.noob.Model.Tournament;

import java.util.List;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Integer> {
    Tournament findTournamentById(Integer id);
    Tournament findTournamentByBracketId(Integer bracketId);
    List<Tournament> findByGame(String game);
    List<Tournament> findByCity(String city);
    List<Tournament> findByAttendanceType(String attendanceType);
    List<Tournament> findByStatus(String status);
}
