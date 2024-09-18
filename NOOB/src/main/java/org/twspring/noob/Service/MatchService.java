package org.twspring.noob.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.twspring.noob.Api.ApiException;
import org.twspring.noob.Model.*;
import org.twspring.noob.Repository.MatchRepository;
import org.twspring.noob.Repository.ParticipantRepository;
import org.twspring.noob.Repository.PlayerRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor


//Hussam
public class MatchService {
    private final MatchRepository matchRepository;
    private final PlayerRepository playerRepository;
    private final ParticipantRepository participantRepository;

    public List<Match> getMatches() {
        return matchRepository.findAll();
    }

    public Match getMatchById(Integer id) {
        return matchRepository.findMatchById(id);
    }

    public Match saveMatch( User user,Match match) {
        validateOrganizerForMatch(user, match); // Validate if the user is the correct organizer
        return matchRepository.save(match);
    }

    public void updateMatch(Integer matchId, Match updatedMatch, User user) {
        Match match = matchRepository.findMatchById(matchId);
        if (match == null) {
            throw new ApiException("Match not found");
        }

        validateOrganizerForMatch(user, match); // Validate if the user is the correct organizer

        match.setParticipant1(updatedMatch.getParticipant1());
        match.setParticipant2(updatedMatch.getParticipant2());
        match.setWinner(updatedMatch.getWinner());
        match.setLoser(updatedMatch.getLoser());
        match.setStartTime(updatedMatch.getStartTime());
        match.setEndTime(updatedMatch.getEndTime());
        match.setStatus(updatedMatch.getStatus());
        match.setTournament(updatedMatch.getTournament());
        match.setRound(updatedMatch.getRound());
        matchRepository.save(match);
    }

    public void deleteMatch(Integer matchId, User user) {
        Match match = matchRepository.findMatchById(matchId);
        if (match == null) {
            throw new ApiException("Match not found");
        }

        validateOrganizerForMatch(user, match); // Validate if the user is the correct organizer

        matchRepository.deleteById(matchId);
    }

    public void setParticipant1Ready(Integer matchId, Integer userId) {
        Match match = matchRepository.findMatchById(matchId);
        if (match == null) {
            throw new ApiException("Match not found");
        }
        Player player = playerRepository.findPlayerById(userId);
        if (player == null) {
            throw new ApiException("Player not found");
        }
        if (match.getParticipant1() != null && match.getParticipant1().getPlayer().getId().equals(userId)) {
            match.setParticipant1Ready(true);
            matchRepository.save(match);
        } else {
            throw new IllegalStateException("Requester is not participant 1 for this match.");
        }
    }

    public void setParticipant2Ready(Integer matchId, Integer userId) {
        Match match = matchRepository.findMatchById(matchId);
        if (match == null) {
            throw new ApiException("Match not found");
        }
        Player player = playerRepository.findPlayerById(userId);
        if (player == null) {
            throw new ApiException("Player not found");
        }
        if (match.getParticipant2() != null && match.getParticipant2().getPlayer().getId().equals(userId)) {
            match.setParticipant2Ready(true);
            matchRepository.save(match);
        } else {
            throw new IllegalStateException("Requester is not participant 2 for this match.");
        }
    }

    public Map<String, Player> getMatchParticipants(Integer matchId) {
        Match match = matchRepository.findMatchById(matchId);
        if (match == null) {
            throw new ApiException("Match not found");
        }

        Map<String, Player> participants = new HashMap<>();
        participants.put("participant1", match.getParticipant1().getPlayer());
        participants.put("participant2", match.getParticipant2().getPlayer());

        return participants;
    }

    public void setMatchWinnerAndLoser(Integer matchId, Integer winnerId, int score_winner, int score_loser, User user) {
        Match match = matchRepository.findMatchById(matchId);
        if (match == null) {
            throw new ApiException("Match not found");
        }

        validateOrganizerForMatch(user, match); // Validate if the user is the correct organizer

        Participant winner = participantRepository.findParticipantById(winnerId);
        if (winner == null) {
            throw new ApiException("Participant not found");
        }

        // Validate the winner is one of the participants
        if (!winner.equals(match.getParticipant1()) && !winner.equals(match.getParticipant2())) {
            throw new IllegalArgumentException("The declared winner is not a participant in this match.");
        }

        // Determine and set the loser based on the winner
        Participant loser = (winner.equals(match.getParticipant1())) ? match.getParticipant2() : match.getParticipant1();
        if (loser == null) {
            throw new IllegalArgumentException("No opponent present to set as loser.");
        }

        // Set the winner and loser of the match
        match.setWinner(winner);
        match.setLoser(loser);

        if (winner.equals(match.getParticipant1())) {
            match.setParticipant1score(score_winner);
            match.setParticipant2score(score_loser);
        } else {
            match.setParticipant1score(score_loser);
            match.setParticipant2score(score_winner);
        }

        match.setStatus("COMPLETED");  // Update the match status to completed

        // Save the updated match object
        matchRepository.save(match);
    }

    public void advanceWinnerToNextMatch(Integer matchId, User user) {
        Match currentMatch = matchRepository.findMatchById(matchId);
        if (currentMatch == null) {
            throw new ApiException("Match not found");
        }

        validateOrganizerForMatch(user, currentMatch); // Validate if the user is the correct organizer

        // Check if a winner has been set
        Participant winner = currentMatch.getWinner();
        if (winner == null) {
            throw new IllegalStateException("No winner set for this match. Cannot advance to next match.");
        }

        // Find the next round and the next match for the winner
        Round currentRound = currentMatch.getRound();
        Round nextRound = findNextRound(currentRound);
        if (nextRound == null) {
            throw new RuntimeException("No next round available. This might be the final match.");
        }

        Match nextMatch = getNextMatchForWinner(nextRound);
        if (nextMatch == null) {
            throw new RuntimeException("No appropriate next match found for the winner to advance.");
        }

        // Determine the slot (participant1 or participant2) to place the winner in the next match
        if (nextMatch.getParticipant1() == null) {
            nextMatch.setParticipant1(winner);
        } else if (nextMatch.getParticipant2() == null) {
            nextMatch.setParticipant2(winner);
        } else {
            throw new RuntimeException("Next match is already fully populated. Cannot advance winner.");
        }

        // Save the updated next match
        matchRepository.save(nextMatch);
    }

    public void setMatchWinnerByBye(Integer matchId, Integer participantId, User user) {
        Match match = matchRepository.findMatchById(matchId);
        if (match == null) {
            throw new ApiException("Match not found");
        }

        validateOrganizerForMatch(user, match); // Validate if the user is the correct organizer

        Participant participant = participantRepository.findParticipantById(participantId);
        if (participant == null) {
            throw new ApiException("Participant not found");
        }

        if (!participant.equals(match.getParticipant1()) && !participant.equals(match.getParticipant2())) {
            throw new IllegalArgumentException("The participant is not in this match.");
        }

        Participant winner;
        Participant loser = null;

        if (match.getParticipant1() == null || match.getParticipant2() == null) {
            winner = participant;
            if (match.getParticipant1() == null && match.getParticipant2() == null) {
                throw new IllegalStateException("Both participants cannot be null for a match.");
            }
        } else {
            throw new IllegalStateException("This method should only be used for matches with a bye.");
        }

        match.setWinner(winner);
        match.setLoser(loser); // No loser in a bye match
        match.setStatus("COMPLETED_BYE");

        matchRepository.save(match);
    }

    public Map<Integer, List<Match>> getMatchHistoryBetweenPlayersGroupedByWinner(Integer playerId1, Integer playerId2) {
        List<Match> matchHistory = matchRepository.findMatchHistoryBetweenPlayers(playerId1, playerId2);
        if (matchHistory.isEmpty()) {
            throw new ApiException("No matches found between the given players");
        }

        return matchHistory.stream()
                .filter(match -> match.getWinner() != null)
                .collect(Collectors.groupingBy(match -> match.getWinner().getId()));
    }

    private void validateOrganizerForMatch(User user, Match match) {
        if (!user.getRole().equals("ORGANIZER")) {
            throw new ApiException("Access denied: Only organizers can perform this action.");
        }

        if (!match.getTournament().getOrganizer().getId().equals(user.getId())) {
            throw new ApiException("Access denied: You are not the organizer of this tournament.");
        }
    }

    private Round findNextRound(Round currentRound) {
        Bracket bracket = currentRound.getBracket();
        List<Round> rounds = new ArrayList<>(bracket.getRounds());

        rounds.sort(Comparator.comparingInt(Round::getId));

        for (int i = 0; i < rounds.size(); i++) {
            if (rounds.get(i).equals(currentRound)) {
                if (i + 1 < rounds.size()) {
                    return rounds.get(i + 1);
                }
                break;
            }
        }
        return null;
    }

    private Match getNextMatchForWinner(Round nextRound) {
        for (Match match : nextRound.getMatches()) {
            if (match.getParticipant1() == null || match.getParticipant2() == null) {
                return match;
            }
        }
        return null;
    }


    //rafeef
    public List<Match> participantGetMatches(Integer participantId) {
        Participant participant = participantRepository.findParticipantById(participantId);
        return matchRepository.findMatchByParticipantId(participant.getId());
    }
}
