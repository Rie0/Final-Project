package org.twspring.noob.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.twspring.noob.Api.ApiException;
import org.twspring.noob.Model.Organizer;
import org.twspring.noob.Model.Tournament;
import org.twspring.noob.Repository.OrganizerRepository;
import org.twspring.noob.Repository.TournamentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TournamentService {
    private final OrganizerRepository organizerRepository;
    private final TournamentRepository tournamentRepository;

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
}
