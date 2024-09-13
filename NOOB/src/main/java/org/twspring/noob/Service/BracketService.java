package org.twspring.noob.Service;

import org.twspring.noob.Model.*;
import org.twspring.noob.Repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class BracketService {

    private final BracketRepository bracketRepository;
    private final TournamentRepository tournamentRepository;
    private final RoundRepository roundRepository;
    private final MatchRepository matchRepository;

    public List<Bracket> getAllBrackets() {
        return bracketRepository.findAll();
    }

    public void deleteBracket(Integer bracketId) {
        Bracket bracket = bracketRepository.findBracketById(bracketId);
        if (bracket == null) {
            throw new RuntimeException("Bracket not found");
        }
        bracket.getRounds().forEach(round -> round.getMatches().clear());
        bracket.getRounds().clear();
        Tournament tournament = bracket.getTournament();
        if (tournament != null) {
            tournament.setBracket(null);
        }
        bracketRepository.delete(bracket);
    }

    public Bracket createBracketForTournament(Integer tournamentId) {
        return initializeBracket(tournamentId);
    }

    private int nextPowerOfTwo(int number) {
        int power = 1;
        while (power < number) {
            power *= 2;
        }
        return power;
    }

    private Bracket initializeBracket(Integer tournamentId) {
        Tournament tournament = tournamentRepository.findTournamentById(tournamentId);
        if (tournament == null) {
            throw new RuntimeException("Tournament not found");
        }

        Set<Participant> participants = new HashSet<>(tournament.getParticipants());
        Participant firstParticipantBye = null;
        if (participants.size() % 2 != 0) {
            firstParticipantBye = participants.iterator().next();
            participants.remove(firstParticipantBye);
        }

        int numberOfParticipants = participants.size();
        int bracketSize = nextPowerOfTwo(numberOfParticipants);
        int numberOfByes = bracketSize - numberOfParticipants;

        Bracket bracket = new Bracket();
        bracket.setTournament(tournament);
        bracket.setBracketType("Single Elimination");
        bracketRepository.save(bracket);

        initializeRounds(bracket, bracketSize, numberOfByes, firstParticipantBye, participants);
        return bracket;
    }

    private void initializeRounds(Bracket bracket, int bracketSize, int numberOfByes, Participant firstParticipantBye, Set<Participant> participants) {
        int numberOfRounds = (int) (Math.log(bracketSize) / Math.log(2));
        List<Round> rounds = new ArrayList<>();

        for (int i = 0; i < numberOfRounds; i++) {
            Round round = new Round();
            round.setBracket(bracket);
            round.setRoundNumber(i + 1);
            roundRepository.save(round);
            rounds.add(round);
        }

        bracket.setRounds(new HashSet<>(rounds));
        if (firstParticipantBye != null) {
            advanceParticipantToNextRound(firstParticipantBye, rounds.get(1), bracket.getTournament()); // Assign tournament to bye match
        }

        scheduleMatchesForFirstRound(bracket, rounds.get(0), numberOfByes, participants);

        for (int i = 1; i < rounds.size(); i++) {
            Round round = rounds.get(i);
            createMatchesForRound(round, (int) Math.ceil(bracketSize / Math.pow(2, i + 1)), bracket.getTournament());
        }
    }

    private void scheduleMatchesForFirstRound(Bracket bracket, Round firstRound, int numberOfByes, Set<Participant> participants) {
        List<Participant> shuffledParticipants = new ArrayList<>(participants);
        Collections.shuffle(shuffledParticipants);

        int activeMatchesCount = shuffledParticipants.size() / 2;
        int index = 0;

        for (int i = 0; i < activeMatchesCount; i++) {
            if (index + 1 < shuffledParticipants.size()) {
                createMatch(firstRound, shuffledParticipants.get(index), shuffledParticipants.get(index + 1), bracket.getTournament());
                index += 2;
            }
        }

        if (numberOfByes > 0) {
            for (int i = 0; i < numberOfByes && index < shuffledParticipants.size(); i++) {
                advanceParticipantToNextRound(shuffledParticipants.get(index++), getNextRound(firstRound, new ArrayList<>(bracket.getRounds())), bracket.getTournament());
            }
        }
    }

    private Round getNextRound(Round currentRound, List<Round> rounds) {
        rounds.sort(Comparator.comparingInt(Round::getRoundNumber));
        int currentIndex = rounds.indexOf(currentRound);
        return currentIndex + 1 < rounds.size() ? rounds.get(currentIndex + 1) : null;
    }

    private void createMatch(Round round, Participant p1, Participant p2, Tournament tournament) {
        Match match = new Match();
        match.setParticipant1(p1);
        match.setParticipant2(p2);
        match.setRound(round);
        match.setTournament(tournament); // Assign the tournament to the match
        match.setStatus("Scheduled");
        matchRepository.save(match);
    }

    private void createMatchesForRound(Round round, int matchesCount, Tournament tournament) {
        for (int i = 0; i < matchesCount; i++) {
            Match match = new Match();
            match.setRound(round);
            match.setTournament(tournament); // Assign the tournament to each match
            match.setStatus("Pending");
            matchRepository.save(match);
        }
    }

    private void advanceParticipantToNextRound(Participant participant, Round nextRound, Tournament tournament) {
        if (nextRound != null) {
            Match byeMatch = new Match();
            byeMatch.setParticipant1(participant);
            byeMatch.setRound(nextRound);
            byeMatch.setTournament(tournament); // Assign the tournament to the bye match
            byeMatch.setStatus("Bye");
            matchRepository.save(byeMatch);
        }
    }
}
