package org.twspring.noob.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.twspring.noob.Api.ApiException;
import org.twspring.noob.Model.*;
import org.twspring.noob.Repository.MatchRepository;
import org.twspring.noob.Repository.OrganizerRepository;
import org.twspring.noob.Repository.TournamentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TournamentService {
    private final OrganizerRepository organizerRepository;
    private final TournamentRepository tournamentRepository;
    private final MatchRepository matchRepository;

    public List<Tournament> getTournaments() {
        return tournamentRepository.findAll();
    }

    public Tournament getTournamentById(Integer id) {
        return tournamentRepository.findTournamentById(id);
    }

    public void saveTournament(Tournament tournament , Integer organizer) {

        Organizer org = organizerRepository.findOrganizerById(organizer);
        if (org == null) {
            throw new ApiException("organizer not found");
        }



        tournament.setOrganizer(org);

        tournamentRepository.save(tournament);
    }

    public void updateTournament(Integer tournamentId, Tournament updatedTournament) {
        Tournament tournament = tournamentRepository.findTournamentById(tournamentId);
        if (tournament != null) {
            tournament.setName(updatedTournament.getName());
            tournament.setDescription(updatedTournament.getDescription());
            tournament.setType(updatedTournament.getType());
            tournament.setStartDate(updatedTournament.getStartDate());
            tournament.setEndDate(updatedTournament.getEndDate());
            tournament.setStatus(updatedTournament.getStatus());
            tournament.setLocation(updatedTournament.getLocation());
            tournament.setMaxParticipants(updatedTournament.getMaxParticipants());
            tournament.setCurrentParticipants(updatedTournament.getCurrentParticipants());
            tournament.setParticipants(updatedTournament.getParticipants());
            tournament.setMatches(updatedTournament.getMatches());
            tournament.setBracket(updatedTournament.getBracket());
            tournamentRepository.save(tournament);
        }
    }

    public void deleteTournament(Integer tournamentId) {

        tournamentRepository.deleteById(tournamentId);
    }
    public List<Tournament> getTournamentsByGame(String game) {
        return tournamentRepository.findByGame(game);
    }

    public List<Tournament> getTournamentsByCity(String city) {
        return tournamentRepository.findByCity(city);
    }

    public List<Tournament> getTournamentsByAttendanceTypeOnline() {
        return tournamentRepository.findByAttendanceType("Online");
    }

    public List<Tournament> getTournamentsByAttendanceTypeOnsite() {
        return tournamentRepository.findByAttendanceType("Onsite");
    }

    public List<Tournament> getTournamentsByStatusOngoing() {
        return tournamentRepository.findByStatus("ONGOING");
    }

    public List<Tournament> getTournamentsByStatusActive() {
        return tournamentRepository.findByStatus("ACTIVE");
    }

    public List<Tournament> getTournamentsByStatusClosingSoon() {
        return tournamentRepository.findByStatus("CLOSING_SOON");
    }

    public List<Tournament> getTournamentsByStatusFinished() {
        return tournamentRepository.findByStatus("FINISHED");
    }



    public String getTournamentDescriptionById(Integer id) {
        return tournamentRepository.findTournamentById(id).getDescription();
    }

    public List<Match> getTournamentMatchesById(Integer id) {
        Tournament tournament = tournamentRepository.findTournamentById(id);
        return matchRepository.findByTournament(tournament);
    }

    public Bracket getTournamentBracketById(Integer id) {
        Tournament tournament = tournamentRepository.findTournamentById(id);
        return tournament.getBracket();
    }

    public List<Participant> getTournamentStandingById(Integer id) {
        Tournament tournament = tournamentRepository.findTournamentById(id);
        return tournament.getParticipants().stream()
                .sorted((p1, p2) -> Integer.compare(p2.getSeed(), p1.getSeed()))
                .collect(Collectors.toList());
    }
    public List<Match> getMatchesByStatusCompleted(Integer tournamentId) {
        Tournament tournament = tournamentRepository.findTournamentById(tournamentId);
        if (tournament == null) {
            throw new ApiException("Tournament not found");
        }
        return matchRepository.findByTournamentAndStatus(tournament, "Completed");
    }

    public List<Match> getMatchesByStatusInProgress(Integer tournamentId) {
        Tournament tournament = tournamentRepository.findTournamentById(tournamentId);
        if (tournament == null) {
            throw new ApiException("Tournament not found");
        }
        return matchRepository.findByTournamentAndStatus(tournament, "InProgress");
    }

    public List<Match> getMatchesByStatusNotStarted(Integer tournamentId) {
        Tournament tournament = tournamentRepository.findTournamentById(tournamentId);
        if (tournament == null) {
            throw new ApiException("Tournament not found");
        }
        return matchRepository.findByTournamentAndStatus(tournament, "NotStarted");
    }
}
