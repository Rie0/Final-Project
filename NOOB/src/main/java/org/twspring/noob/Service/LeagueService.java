package org.twspring.noob.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.twspring.noob.Api.ApiException;
import org.twspring.noob.Model.League;
import org.twspring.noob.Model.Organizer;
import org.twspring.noob.Repository.LeagueRepository;
import org.twspring.noob.Repository.OrganizerRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeagueService {
    private final LeagueRepository leagueRepository;
    private final OrganizerRepository organizerRepository;

    public List<League> getLeagues() {
        return leagueRepository.findAll();
    }

    public League getLeagueById(Integer id) {
        League league = leagueRepository.findLeagueById(id);
        if (league == null) {
            throw new ApiException("League not found");
        }
        return league;
    }

    public void saveLeague(Integer organizerId, League league) {
        Organizer organizer = organizerRepository.findOrganizerById(organizerId);


        league.setOrganizer(organizer);
        leagueRepository.save(league);
    }

    public void updateLeague(Integer leagueId, League updatedLeague) {
        League league = leagueRepository.findLeagueById(leagueId);
        if (league != null) {
            league.setName(updatedLeague.getName());
            league.setDescription(updatedLeague.getDescription());
            league.setStartDate(updatedLeague.getStartDate());
            league.setEndDate(updatedLeague.getEndDate());
            league.setStatus(updatedLeague.getStatus());
            league.setLocation(updatedLeague.getLocation());
            league.setMaxParticipants(updatedLeague.getMaxParticipants());
            league.setCurrentParticipants(updatedLeague.getCurrentParticipants());
            leagueRepository.save(league);
        } else {
            throw new ApiException("League not found");
        }
    }

    public void deleteLeague(Integer leagueId) {
        League league = leagueRepository.findLeagueById(leagueId);
        if (league == null) {
            throw new ApiException("League not found");
        }
        leagueRepository.deleteById(leagueId);
    }
}
