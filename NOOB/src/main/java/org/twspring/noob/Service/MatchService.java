package org.twspring.noob.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.twspring.noob.Model.Match;
import org.twspring.noob.Repository.MatchRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final MatchRepository matchRepository;

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
            match.setRoundNumber(updatedMatch.getRoundNumber());
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
        matchRepository.deleteById(matchId);
    }
}
