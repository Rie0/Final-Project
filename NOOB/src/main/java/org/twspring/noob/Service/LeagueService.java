package org.twspring.noob.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.twspring.noob.Api.ApiException;
import org.twspring.noob.DTO.DateTimeDTO;
import org.twspring.noob.Model.*;
import org.twspring.noob.Repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    //CRUD WITH LOGIC
    public void createLeague(Integer organizerId, League league) { //DATE LOGIC MISSING
        Organizer organizer = organizerRepository.findOrganizerById(organizerId);

        if (league.getMaxParticipants()%2!=0){
            throw new ApiException("Max participants number must be even");
        }

        league.setStatus("PENDING");
        league.setOrganizer(organizer);
        leagueRepository.save(league);

        //Create rounds
        for (int i = 0; i < league.getMaxParticipants()-1; i++) {
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

    //===========================================================================================================
    //================================================DATES======================================================
    public void setLeagueRoundDate(Integer organizerId, Integer leagueId, Integer roundId, LocalDate roundDate) {

        Organizer organizer = organizerRepository.findOrganizerById(organizerId);
        League league = leagueRepository.findLeagueById(leagueId);
        Round round = roundRepository.findRoundById(roundId);

        if(league==null){
            throw new ApiException("League not found");
        }
        if(round==null){
            throw new ApiException("Round not found");
        }
        if (league.getOrganizer().getId() != organizer.getId()) {
            throw new ApiException("Organizer doesn't own this league");
        }
        if (round.getLeague().getId() != league.getId()) {
            throw new ApiException("This round doesn't belong to this league");
        }

        //validate the dates
        if (roundDate.isBefore(league.getStartDate().toLocalDate())){
            throw new ApiException("Round cannot start before the league start date");
        }
        if (roundDate.isAfter(league.getEndDate().toLocalDate())){
            throw new ApiException("Round cannot end before the league end date");
        }
        round.setDate(roundDate);
        roundRepository.save(round);
    }

    public void setLeagueMatchDate(Integer organizerId, Integer leagueId, Integer matchId, DateTimeDTO matchDate) {
        Organizer organizer = organizerRepository.findOrganizerById(organizerId);
        League league = leagueRepository.findLeagueById(leagueId);
        Match match = matchRepository.findMatchById(matchId);

        if(league==null){
            throw new ApiException("League not found");
        }
        if(match==null){
            throw new ApiException("Match not found");
        }
        if (league.getOrganizer().getId() != organizer.getId()) {
            throw new ApiException("Organizer doesn't own this league");
        }
        if (match.getLeague().getId() != league.getId()) {
            throw new ApiException("This match doesn't belong to this league");
        }
        //validate the dates
        Round round = match.getRound();
        if (round.getDate()==null){
            throw new ApiException("Set the date of the round first");
        }
        if (!matchDate.getStartDate().toLocalDate().isEqual(round.getDate())){
            throw new ApiException("Matches must only occur during the round date");
        }
        if (matchDate.getEndDate().isBefore(matchDate.getStartDate())){
            throw new ApiException("End date cannot be before start date");
        }
        match.setStartTime(matchDate.getStartDate());
        match.setEndTime(matchDate.getEndDate());
        matchRepository.save(match);
    }
}