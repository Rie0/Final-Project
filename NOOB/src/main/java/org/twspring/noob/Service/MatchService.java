package org.twspring.noob.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.twspring.noob.Api.ApiException;
import org.twspring.noob.Model.*;
import org.twspring.noob.Repository.MatchRepository;
import org.twspring.noob.Repository.ParticipantRepository;
import org.twspring.noob.Repository.PlayerRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
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

    public Match saveMatch(Match match) {

        return matchRepository.save(match);
    }

    public void updateMatch(Integer matchId, Match updatedMatch) {
        Match match = matchRepository.findMatchById(matchId);
        if (match != null) {
            match.setParticipant1(updatedMatch.getParticipant1());
            match.setParticipant2(updatedMatch.getParticipant2());
            match.setWinner(updatedMatch.getWinner());
            match.setLoser(updatedMatch.getLoser());
            match.setStartTime(updatedMatch.getStartTime());
            match.setEndTime(updatedMatch.getEndTime());
            match.setStatus(updatedMatch.getStatus());
            match.setScore(updatedMatch.getScore());
            match.setTournament(updatedMatch.getTournament());
            match.setRound(updatedMatch.getRound());
            matchRepository.save(match);
        }
    }

    public void deleteMatch(Integer matchId) {
        Match match = matchRepository.findMatchById(matchId);
        if (match == null) {
            throw new ApiException("Match not found");
        }


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

    public void setMatchWinnerAndLoser(Integer matchId, Integer winnerId) {
        Match match = matchRepository.findMatchById(matchId);
        if (match == null) {
            throw new ApiException("Match not found");
        }


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
        match.setStatus("COMPLETED");  // Update the match status to completed

        // Save the updated match object
        matchRepository.save(match);
    }



    public void advanceWinnerToNextMatch(Integer matchId) {
        // Retrieve the current match
        Match currentMatch = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found"));

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

    private Round findNextRound(Round currentRound) {
        Bracket bracket = currentRound.getBracket(); // Get the bracket this round belongs to
        List<Round> rounds = new ArrayList<>(bracket.getRounds()); // Convert the rounds set to a list

        // Sort rounds based on some identifiable attribute, like an ID or a sequence number if available
        rounds.sort(Comparator.comparingInt(Round::getId)); // Assuming each round has a unique ID that increases sequentially

        // Find the current round in the list and return the next one
        for (int i = 0; i < rounds.size(); i++) {
            if (rounds.get(i).equals(currentRound)) {
                if (i + 1 < rounds.size()) { // Ensure there's a next round in the list
                    return rounds.get(i + 1);
                }
                break; // Current round is the last one in the list
            }
        }
        return null; // No next round found, or current round is the last round
    }

    private Match getNextMatchForWinner(Round nextRound) {
        // Check all matches in the given round to find an available slot
        for (Match match : nextRound.getMatches()) {
            // Check if either participant1 or participant2 is null (i.e., slot available)
            if (match.getParticipant1() == null || match.getParticipant2() == null) {
                return match; // Return the first match found with an available slot
            }
        }
        return null; // Return null if no match is found with an available slot
    }



}
