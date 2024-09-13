package org.twspring.noob.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.twspring.noob.Api.ApiException;
import org.twspring.noob.Model.*;
import org.twspring.noob.Repository.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeagueService {
    private final LeagueRepository leagueRepository;
    private final OrganizerRepository organizerRepository;
    private final PlayerRepository playerRepository;
    private final ParticipantRepository participantRepository;
    private final MatchRepository matchRepository;
    private final RoundRepository roundRepository;

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

    public void createLeague(Integer organizerId, League league) { //DATE LOGIC MISSING
        Organizer organizer = organizerRepository.findOrganizerById(organizerId);

        if (league.getMaxParticipants()%2!=0){
            throw new ApiException("Max participants number must be even");
        }

        league.setStatus("PENDING");
        league.setOrganizer(organizer);
        leagueRepository.save(league);

        //Create rounds
        for (int i = 0; i < league.getMaxParticipants()/2+1; i++) {
            Round round = new Round();
            round.setRoundNumber(i+1);
            round.setLeague(league);
            roundRepository.save(round);
            //create matches for the round
            for (int j = 0; j < league.getMaxParticipants()/2; j++) {
                Match match = new Match();
                match.setStatus("PENDING");
                match.setLeague(league);
                match.setRound(round);
                match.setScore("TBA");
                matchRepository.save(match);
            }
        }
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


//===========================================================================================================
//===========================================PARTICIPANTS====================================================

    public List<Participant> getParticipantsByLeague(Integer leagueId) {
        League league = leagueRepository.findLeagueById(leagueId);
        if (league == null) {
            throw new ApiException("League not found");
        }
        return participantRepository.findParticipantByLeagueId(league.getId());
    }

    public void participateInLeague(Integer playerId, Integer leagueId, String name) {
        Player player = playerRepository.findPlayerById(playerId);
        League league = leagueRepository.findLeagueById(leagueId);

        if (league == null) {
            throw new ApiException("League not found");
        }
        if (league.getParticipants().contains(participantRepository.findParticipantById(playerId))) {
            throw new ApiException("Participant is already registered for this league");
        }
        if (league.getCurrentParticipants() >= league.getMaxParticipants()) {
            throw new ApiException("League has reached it's maximum number of participants");
        }
        Participant participant = new Participant();
        participant.setName(name);
        participant.setPlayer(player);
        participant.setLeague(league);
        participant.setStatus("ACTIVE");
        participantRepository.save(participant);

        league.setCurrentParticipants(league.getCurrentParticipants() + 1);
        leagueRepository.save(league);
    }

    public void withdrawFromLeague(Integer participantId, Integer leagueId) {
        Participant participant = participantRepository.findParticipantById(participantId);
        League league = leagueRepository.findLeagueById(leagueId);

        if (league == null) {
            throw new ApiException("League not found");
        }
        if (!league.getParticipants().contains(participant)) {
            throw new ApiException("Participant is not registered for this league");
        }
        league.getParticipants().remove(participant);
        participant.setPlayer(null);
        participantRepository.delete(participant);
        league.setCurrentParticipants(league.getCurrentParticipants() - 1);
        leagueRepository.save(league);
    }
}