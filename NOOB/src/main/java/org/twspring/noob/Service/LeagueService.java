package org.twspring.noob.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.twspring.noob.Api.ApiException;
import org.twspring.noob.DTO.DateDTO;
import org.twspring.noob.DTO.DateTimeDTO;
import org.twspring.noob.Model.*;
import org.twspring.noob.Repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeagueService { //RAFEEF
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

        league.setStatus(League.Status.INACTIVE);
        league.setOrganizer(organizer);
        league.setCurrentParticipants(0);
        league.setOrganizerName(organizer.getUser().getUsername());
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
                match.setParticipant1score(0);
                match.setParticipant2score(0);
                match.setParticipant1Name("TBA");
                match.setParticipant2Name("TBA");
                matchRepository.save(match);
            }
        }
    }

    public void updateLeagueByAdmin(Integer leagueId, League updatedLeague) { //for admin
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
    public void updateLeagueByOrganizer(Integer leagueId, Integer organizerId, League updatedLeague) {
        Organizer organizer = organizerRepository.findOrganizerById(organizerId);
        League league = leagueRepository.findLeagueById(leagueId);

        if (organizer.getId()!=league.getOrganizer().getId()){
            throw new ApiException("Organizer doesn't own this league");
        }

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

    public void deleteLeagueByAdmin(Integer leagueId) {
        League league = leagueRepository.findLeagueById(leagueId);
        if (league == null) {
            throw new ApiException("League not found");
        }
        leagueRepository.deleteById(leagueId);
    }

    public void deleteLeagueByOrganizer(Integer leagueId, Integer organizerId) {
        Organizer organizer = organizerRepository.findOrganizerById(organizerId);
        League league = leagueRepository.findLeagueById(leagueId);

        if (organizer.getId()!=league.getOrganizer().getId()){
            throw new ApiException("Organizer doesn't own this league");
        }

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
        if (league.getParticipants().contains(participantRepository.findParticipantByPlayerIdAndLeagueId(playerId, leagueId))) {
            throw new ApiException("Participant is already registered for this league");
        }
        if (league.getStatus()== League.Status.FULL) {
            throw new ApiException("League has reached it's maximum number of participants");
        }
        Participant participant = new Participant();
        participant.setName(name);
        participant.setPlayer(player);
        participant.setLeague(league);
        participant.setStatus(Participant.Status.ACTIVE);
        participantRepository.save(participant);

        league.setCurrentParticipants(league.getCurrentParticipants() + 1);
        if (league.getCurrentParticipants() >= league.getMaxParticipants()) {
            league.setStatus(League.Status.FULL);
        }
        leagueRepository.save(league);
    }

    public void withdrawFromLeague(Integer playerId, Integer leagueId) {
        Participant participant = participantRepository.findParticipantByPlayerIdAndLeagueId(playerId, leagueId);
        League league = leagueRepository.findLeagueById(leagueId);

        if (league == null) {
            throw new ApiException("League not found");
        }
        if (!league.getParticipants().contains(participantRepository.findParticipantByPlayerIdAndLeagueId(playerId, leagueId))) {
            throw new ApiException("Participant is not registered for this league");
        }
        //in case it didn't get started, no consequences
        if (league.getStatus() == League.Status.FULL||league.getStatus()==League.Status.OPEN) {
            league.getParticipants().remove(participant);
            participant.setPlayer(null);
            participantRepository.delete(participant);
            if (league.getStatus() == League.Status.FULL){
                league.setStatus(League.Status.OPEN);
            }
        }

        //in case the league started; cancel all matches with participant but don't delete.
        if (league.getStatus() == League.Status.ONGOING){
            for(Match match : matchRepository.findMatchByLeagueId(leagueId)){
                if (match.getParticipant1().getPlayer().getId()==participant.getPlayer().getId()||match.getParticipant2().getPlayer().getId()==participant.getPlayer().getId()){
                    match.setStatus("CANCELLED");
                    matchRepository.save(match);
                }
            }
        }
        league.setCurrentParticipants(league.getCurrentParticipants() - 1);
        leagueRepository.save(league);
    }

    public void kickParticipant(Integer organizerId, Integer participantId, Integer leagueId) {
        League league = leagueRepository.findLeagueById(leagueId);
        Participant participant = participantRepository.findParticipantById(participantId);

        if (league == null) {
            throw new ApiException("League not found");
        }

        if (!league.getOrganizer().getId().equals(organizerId)) {
            throw new ApiException("You do not have permission to kick participants from this league");
        }

        if (participant == null) {
            throw new ApiException("Participant not found");
        }

        if (!league.getParticipants().contains(participant)) {
            throw new ApiException("Participant is not registered for this league");
        }

        if (league.getStatus()== League.Status.ENDED){
            throw new ApiException("League has already ended");
        }

        // Before the league starts, just remove the participant
        if (league.getStatus() == League.Status.FULL || league.getStatus() == League.Status.OPEN) {
            league.getParticipants().remove(participant);
            participant.setPlayer(null);
            participantRepository.delete(participant);
            if (league.getStatus() == League.Status.FULL) {
                league.setStatus(League.Status.OPEN);
            }
        }

        // If the league has started, cancel all matches with the participant
        if (league.getStatus() == League.Status.ONGOING) {
            for (Match match : matchRepository.findMatchByLeagueId(leagueId)) {
                if (match.getParticipant1().getId().equals(participant.getId()) ||
                        match.getParticipant2().getId().equals(participant.getId())) {
                    match.setStatus("CANCELLED");
                    matchRepository.save(match);
                }
            }
        }

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
        if (roundDate.isBefore(league.getStartDate())){
            throw new ApiException("Round cannot start before the league start date");
        }
        if (roundDate.isAfter(league.getEndDate())){
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

        //check if the league can now be activated (dates are set)
        for (Match m: league.getMatches()) {
            if (m.getStartTime()==null){
                league.setStatus(League.Status.INACTIVE);
                break;
            }
            league.setStatus(League.Status.OPEN);
        }
        leagueRepository.save(league);
    }

    public void changeLeagueDates(Integer organizerId, Integer leagueId, DateDTO dates) { //enter new dates only when inactive
        Organizer organizer = organizerRepository.findOrganizerById(organizerId);
        League league = leagueRepository.findLeagueById(leagueId);
        LocalDate startDate = dates.getStartDate();
        LocalDate endDate = dates.getEndDate();

        if(league==null){
            throw new ApiException("League not found");
        }
        if (league.getOrganizer().getId() != organizer.getId()) {
            throw new ApiException("Organizer doesn't own this league");
        }
        if (league.getStatus()!=League.Status.INACTIVE){
            throw new ApiException("You cannot change the league dates after activating it");
        }
        //validate the dates
        if (startDate.isBefore(endDate)){
            throw new ApiException("End date cannot be before start date");
        }
        //resets rounds and matches dates
        for (Round round: league.getRounds()) {
            round.setDate(null);
        }
        for (Match match: league.getMatches()) {
            match.setEndTime(null);
            match.setStartTime(null);
        }

        league.setStartDate(startDate);
        league.setEndDate(endDate);
        leagueRepository.save(league);
    }

    //===========================================================================================================
    //===========================================FLOW MANAGEMENT=================================================

    public void setLeagueToReady(Integer organizerId, Integer leagueId) {
        League league = leagueRepository.findLeagueById(leagueId);
        Organizer organizer = organizerRepository.findOrganizerById(organizerId);

        if (league == null) {
            throw new ApiException("League not found");
        }
        if (!league.getOrganizer().getId().equals(organizer.getId())) {
            throw new ApiException("Organizer doesn't own this league");
        }

        if (league.getStatus() != League.Status.OPEN&&league.getStatus() != League.Status.FULL) {
            throw new ApiException("League must be in 'OPEN' or 'FULL' status to be set to 'READY'");
        }
        //organizers can start the match while the maximum players aren't reached
        if (league.getCurrentParticipants()<=0){
            throw new ApiException("You can't  start the league when the number of players is zero");
        }

        if (league.getCurrentParticipants()%2!=0){
            throw new ApiException("You can only start the league when the number of players is even");
        }

        //Randomize players in matches, making sure each player verses the other ONCE
        List<Participant> participants = participantRepository.findParticipantByLeagueId(leagueId);
        int numberOfParticipants = participants.size();

        List<Round> rounds = roundRepository.findRoundByLeagueId(leagueId);

        for (int roundNumber = 0; roundNumber < numberOfParticipants - 1; roundNumber++) {
            Round round = rounds.get(roundNumber);

            List<Match> matches = matchRepository.findMatchByRoundId(round.getId());
            int numberOfMatches = numberOfParticipants / 2;

            for (int matchNumber = 0; matchNumber < numberOfMatches; matchNumber++) {
                Participant player1 = participants.get(matchNumber);
                Participant player2 = participants.get(numberOfParticipants - matchNumber - 1);

                Match match = matches.get(matchNumber);
                match.setParticipant1(player1);
                match.setParticipant2(player2);
                match.setParticipant1Name(player1.getName());
                match.setParticipant2Name(player2.getName());
                matchRepository.save(match);
            }
            // Rotate participants for the next round, except the first one.
            participants.add(1, participants.remove(participants.size() - 1));
        }
        league.setStatus(League.Status.ONGOING);
        leagueRepository.save(league);
    }

    public void startMatch(Integer organizerId, Integer leagueId, Integer matchId) {
        League league = leagueRepository.findLeagueById(leagueId);
        Match match = matchRepository.findMatchById(matchId);
        if (league == null) {
            throw new ApiException("League not found");
        }
        if (!league.getOrganizer().getId().equals(organizerId)) {
            throw new ApiException("Organizer doesn't own this league");
        }
        if (match == null) {
            throw new ApiException("Match not found");
        }
        if (match.getLeague().getId()!=league.getId()) {
            throw new ApiException("This match doesn't belong to this league");
        }
        if (league.getStatus()!=League.Status.ONGOING) {
            throw new ApiException("You can only start the match when the league started");
        }
        if (!match.isParticipant1Ready()&&!match.isParticipant2Ready()) {
            throw new ApiException("You can only start the match when the all participants are ready");
        }

        match.setStatus("IN_PROGRESS");
        matchRepository.save(match);
    }

    //Why are the methods written like this? Because game matches are so much like football,each goal is a score, it's more logical
    //to increase it once. And decrease it once when a mistake/bug(in game) happens.
    public void add1toParticipant1Score(Integer organizerId, Integer leagueId, Integer matchId) {
        League league = leagueRepository.findLeagueById(leagueId);
        Match match = matchRepository.findMatchById(matchId);
        if (league == null) {
            throw new ApiException("League not found");
        }
        if (!league.getOrganizer().getId().equals(organizerId)) {
            throw new ApiException("Organizer doesn't own this league");
        }
        if (match == null) {
            throw new ApiException("Match not found");
        }
        if (match.getLeague().getId()!=league.getId()) {
            throw new ApiException("This match doesn't belong to this league");
        }
        if(!match.getStatus().equals("IN_PROGRESS")){
            throw new ApiException("Match is not in progress");
        }
        match.setParticipant1score(match.getParticipant1score()+1);
        matchRepository.save(match);
    }

    public void add1toParticipant2Score(Integer organizerId, Integer leagueId, Integer matchId) {
        League league = leagueRepository.findLeagueById(leagueId);
        Match match = matchRepository.findMatchById(matchId);
        if (league == null) {
            throw new ApiException("League not found");
        }
        if (!league.getOrganizer().getId().equals(organizerId)) {
            throw new ApiException("Organizer doesn't own this league");
        }
        if (match == null) {
            throw new ApiException("Match not found");
        }
        if (match.getLeague().getId()!=league.getId()) {
            throw new ApiException("This match doesn't belong to this league");
        }
        if(!match.getStatus().equals("IN_PROGRESS")){
            throw new ApiException("Match is not in progress");
        }
        match.setParticipant2score(match.getParticipant2score()+1);
        matchRepository.save(match);
    }

    //note: some cases in some video games calls for negative scores, so it's possible
    public void subtract1fromParticipant1Score(Integer organizerId, Integer leagueId, Integer matchId) {
        League league = leagueRepository.findLeagueById(leagueId);
        Match match = matchRepository.findMatchById(matchId);
        if (league == null) {
            throw new ApiException("League not found");
        }
        if (!league.getOrganizer().getId().equals(organizerId)) {
            throw new ApiException("Organizer doesn't own this league");
        }
        if (match == null) {
            throw new ApiException("Match not found");
        }
        if (match.getLeague().getId()!=league.getId()) {
            throw new ApiException("This match doesn't belong to this league");
        }
        if(!match.getStatus().equals("IN_PROGRESS")){
            throw new ApiException("Match is not in progress");
        }
        match.setParticipant1score(match.getParticipant1score()-1);
        matchRepository.save(match);
    }

    public void subtract1fromParticipant2Score(Integer organizerId, Integer leagueId, Integer matchId) {
        League league = leagueRepository.findLeagueById(leagueId);
        Match match = matchRepository.findMatchById(matchId);
        if (league == null) {
            throw new ApiException("League not found");
        }
        if (!league.getOrganizer().getId().equals(organizerId)) {
            throw new ApiException("Organizer doesn't own this league");
        }
        if (match == null) {
            throw new ApiException("Match not found");
        }
        if (match.getLeague().getId()!=league.getId()) {
            throw new ApiException("This match doesn't belong to this league");
        }
        if(!match.getStatus().equals("IN_PROGRESS")){
            throw new ApiException("Match is not in progress");
        }
        match.setParticipant2score(match.getParticipant2score()-1);
        matchRepository.save(match);
    }

    public void finishMatch(Integer organizerId, Integer leagueId, Integer matchId) {
        League league = leagueRepository.findLeagueById(leagueId);
        Match match = matchRepository.findMatchById(matchId);
        if (league == null) {
            throw new ApiException("League not found");
        }
        if (!league.getOrganizer().getId().equals(organizerId)) {
            throw new ApiException("Organizer doesn't own this league");
        }
        if (match == null) {
            throw new ApiException("Match not found");
        }
        if (match.getLeague().getId()!=league.getId()) {
            throw new ApiException("This match doesn't belong to this league");
        }
        if(!match.getStatus().equals("IN_PROGRESS")){
            throw new ApiException("Match is not in progress");
        }
        if (match.getParticipant1score()==match.getParticipant2score()){
            throw new ApiException("Match cannot end in tie");
        }
        Participant participant1 = match.getParticipant1();
        Participant participant2 = match.getParticipant2();
        match.setStatus("FINISHED");
        if (match.getParticipant1score()>match.getParticipant2score()) {
            match.setWinner(participant1);
            match.setLoser(participant2);
        }else {
            match.setWinner(participant2);
            match.setLoser(participant1);
        }
        matchRepository.save(match);

        participant1.setScore(participant1.getScore()+match.getParticipant1score());
        participant2.setScore(participant2.getScore()+match.getParticipant2score());
        participantRepository.save(participant1);
        participantRepository.save(participant2);
    }

    public void cancelMatch(Integer organizerId, Integer matchId, Integer leagueId) {
        League league = leagueRepository.findLeagueById(leagueId);
        Match match = matchRepository.findMatchById(matchId);

        if (league == null) {
            throw new ApiException("League not found");
        }

        if (!league.getOrganizer().getId().equals(organizerId)) {
            throw new ApiException("You do not have permission to cancel matches for this league");
        }

        if (match == null) {
            throw new ApiException("Match not found");
        }

        if (!match.getLeague().getId().equals(leagueId)) {
            throw new ApiException("This match does not belong to the specified league");
        }

        if (match.getStatus().equals("FINISHED")) {
            throw new ApiException("Match is already finished");
        }

        if (match.getStatus().equals("CANCELLED")) {
            throw new ApiException("Match is already cancelled");
        }

        match.setStatus("CANCELLED");
        matchRepository.save(match);
    }


    public void finalizeLeague(Integer organizerId, Integer leagueId){
        League league = leagueRepository.findLeagueById(leagueId);
        List<Match> matches = matchRepository.findMatchByLeagueId(league.getId());

        if (league == null) {
            throw new ApiException("League not found");
        }
        if (!league.getOrganizer().getId().equals(organizerId)) {
            throw new ApiException("Organizer doesn't own this league");
        }
        //make sure all matches are finished
        for (Match match : matches) {
            if (!match.getStatus().equals("FINISHED")&&!match.getStatus().equals("CANCELLED")) {
                throw new ApiException("All matches must be finished before finalizing the league");
            }
        }
        league.setStatus(League.Status.ENDED);
        league.setEndDate(LocalDate.now()); //Update the end date, since the previous one was the 'expected' date
        leagueRepository.save(league);

    }
    public List<Participant> getLeaderBoard(Integer leagueId){
        League league = leagueRepository.findLeagueById(leagueId);
        if (league==null){
            throw new ApiException("League not found");
        }
        if (!league.getStatus().equals(League.Status.ENDED)&&!league.getStatus().equals(League.Status.ONGOING)){
            throw new ApiException("League didn't start yet");
        }
        return participantRepository.findParticipantByLeagueIdOrderByScoreDesc(leagueId);
    }

    //===========================================================================================================
    //===============================================GET INFO====================================================

    public List<Match> getLeagueMatches(int leagueId){
        League league = leagueRepository.findLeagueById(leagueId);
        if (league == null) {
            throw new ApiException("League not found");
        }
        return matchRepository.findMatchByLeagueId(leagueId);
    }

    public List<Round> getLeagueRounds(Integer leagueId){
        League league = leagueRepository.findLeagueById(leagueId);
        if (league == null) {
            throw new ApiException("League not found");
        }
        return roundRepository.findRoundByLeagueId(leagueId);
    }



}