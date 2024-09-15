package org.twspring.noob.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.twspring.noob.Api.ApiException;
import org.twspring.noob.Model.Match;
import org.twspring.noob.Model.Player;
import org.twspring.noob.Repository.MatchRepository;
import org.twspring.noob.Repository.PlayerRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final MatchRepository matchRepository;
    private final PlayerRepository playerRepository;

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
            //match.setScore(updatedMatch.getScore());
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
}
