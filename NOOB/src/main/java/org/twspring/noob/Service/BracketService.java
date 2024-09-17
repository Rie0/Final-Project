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
    private final ParticipantRepository participantRepository;

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

    // **Single Elimination Logic**

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

        // Check if a bracket for single elimination already exists
        Bracket bracket = bracketRepository.findByTournamentAndBracketType(tournament, "Single Elimination");
        if (bracket == null) {
            bracket = new Bracket();
            bracket.setTournament(tournament);
            bracket.setBracketType("Single Elimination");
            bracketRepository.save(bracket);
        }

        // Proceed with initializing rounds
        Set<Participant> participants = new HashSet<>(tournament.getParticipants());
        Participant firstParticipantBye = null;
        if (participants.size() % 2 != 0) {
            firstParticipantBye = participants.iterator().next();
            participants.remove(firstParticipantBye);
        }

        int numberOfParticipants = participants.size();
        int bracketSize = nextPowerOfTwo(numberOfParticipants);
        int numberOfByes = bracketSize - numberOfParticipants;

        initializeRounds(bracket, bracketSize, numberOfByes, firstParticipantBye, participants);
        return bracket;
    }

    private void initializeRounds(Bracket bracket, int bracketSize, int numberOfByes, Participant firstParticipantBye, Set<Participant> participants) {
        int numberOfRounds = (int) (Math.log(bracketSize) / Math.log(2));
        List<Round> rounds = new ArrayList<>();

        for (int i = 0; i < numberOfRounds; i++) {
            Round round = roundRepository.findByBracketAndRoundNumber(bracket, i + 1); // Ensure no duplicates
            if (round == null) {
                round = new Round();
                round.setBracket(bracket);
                round.setRoundNumber(i + 1);
                roundRepository.save(round);
            }
            rounds.add(round);
        }

        bracket.setRounds(new HashSet<>(rounds));
        if (firstParticipantBye != null) {
            advanceParticipantToNextRound(firstParticipantBye, rounds.get(1), bracket.getTournament());
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
        match.setParticipant1Name(p1.getName()); // Ensure participant1name is set
        match.setParticipant2(p2);
        match.setParticipant2Name(p2.getName()); // Ensure participant2name is set
        match.setRound(round);
        match.setTournament(tournament);
        match.setStatus("Scheduled");
        matchRepository.save(match);
    }

    private void createMatchesForRound(Round round, int matchesCount, Tournament tournament) {
        for (int i = 0; i < matchesCount; i++) {
            Match match = new Match();
            match.setRound(round);
            match.setTournament(tournament);
            match.setStatus("Pending");
            matchRepository.save(match);
        }
    }

    private void advanceParticipantToNextRound(Participant participant, Round nextRound, Tournament tournament) {
        if (nextRound != null) {
            Match byeMatch = new Match();
            byeMatch.setParticipant1(participant);
            byeMatch.setRound(nextRound);
            byeMatch.setTournament(tournament);
            byeMatch.setStatus("Bye");
            matchRepository.save(byeMatch);
        }
    }

    // **Double Elimination Logic**

    public Bracket createDoubleEliminationBracket(Integer tournamentId) {
        Tournament tournament = tournamentRepository.findTournamentById(tournamentId);
        if (tournament == null) {
            throw new RuntimeException("Tournament not found");
        }

        // Retrieve or create Winners' Bracket
        Bracket winnersBracket = bracketRepository.findByTournamentAndBracketType(tournament, "Double Elimination - Winners");
        if (winnersBracket == null) {
            winnersBracket = new Bracket();
            winnersBracket.setBracketType("Double Elimination - Winners");
            winnersBracket.setTournament(tournament);
            bracketRepository.save(winnersBracket);
        }

        initializeBracket(tournamentId); // Initialize the Winners' Bracket

        // Retrieve or create Losers' Bracket
        Bracket losersBracket = bracketRepository.findByTournamentAndBracketType(tournament, "Double Elimination - Losers");
        if (losersBracket == null) {
            losersBracket = new Bracket();
            losersBracket.setBracketType("Double Elimination - Losers");
            losersBracket.setTournament(tournament);
            bracketRepository.save(losersBracket);
        }

        // Return the Winners' Bracket (or both if needed)
        return winnersBracket;
    }

    public void advanceParticipantFromWinnersToLosersBracket(Integer loserParticipantId, Integer tournamentId) {
        Tournament tournament = tournamentRepository.findTournamentById(tournamentId);
        if (tournament == null) {
            throw new RuntimeException("Tournament not found");
        }

        Bracket losersBracket = bracketRepository.findByTournamentAndBracketType(tournament, "Double Elimination - Losers");
        if (losersBracket == null) {
            throw new RuntimeException("Losers' Bracket not found");
        }

        // Directly use participantRepository method
        Participant loser = participantRepository.findParticipantById(loserParticipantId);
        if (loser == null) {
            throw new RuntimeException("Participant not found");
        }

        // Get the current round and add the loser to the next available match
        Round nextAvailableRound = getNextAvailableRound(losersBracket);
        if (nextAvailableRound == null) {
            throw new RuntimeException("No available rounds in Losers' Bracket");
        }

        Match match = new Match();
        match.setParticipant1(loser);
        match.setRound(nextAvailableRound);
        match.setTournament(tournament);
        match.setStatus("Pending");
        matchRepository.save(match);
    }

    private Round getNextAvailableRound(Bracket losersBracket) {
        // Ensure rounds are sorted by round number before checking
        List<Round> sortedRounds = new ArrayList<>(losersBracket.getRounds());
        sortedRounds.sort(Comparator.comparingInt(Round::getRoundNumber));

        // Iterate over the sorted rounds to find the next round with available match slots
        for (Round round : sortedRounds) {
            int expectedNumberOfMatches = getNumberOfMatches(round.getRoundNumber(), losersBracket.getTournament().getMaxParticipants());

            // If the number of existing matches is less than expected, return this round
            if (round.getMatches().size() < expectedNumberOfMatches) {
                return round;
            }
        }

        // If no available round is found, return null
        return null;
    }

    private int getNumberOfMatches(int roundNumber, int bracketSize) {
        // In the first round, the number of matches is half the bracket size
        if (roundNumber == 1) {
            return bracketSize / 2;
        }

        // For subsequent rounds, the number of matches is half of the matches in the previous round
        return (int) Math.ceil(bracketSize / Math.pow(2, roundNumber));
    }

    public void manageFinalMatch(Tournament tournament) {
        Bracket winnersBracket = bracketRepository.findByTournamentAndBracketType(tournament, "Double Elimination - Winners");
        Bracket losersBracket = bracketRepository.findByTournamentAndBracketType(tournament, "Double Elimination - Losers");

        Participant winnerFinalist = findFinalistFromBracket(winnersBracket);
        Participant loserFinalist = findFinalistFromBracket(losersBracket);

        Round finalRound = new Round();
        finalRound.setBracket(winnersBracket);
        finalRound.setRoundNumber(winnersBracket.getRounds().size() + 1);
        roundRepository.save(finalRound);

        Match finalMatch = new Match();
        finalMatch.setParticipant1(winnerFinalist);
        finalMatch.setParticipant2(loserFinalist);
        finalMatch.setRound(finalRound);
        finalMatch.setTournament(tournament);
        finalMatch.setStatus("Final");
        matchRepository.save(finalMatch);
    }

    private Participant findFinalistFromBracket(Bracket bracket) {
        return bracket.getRounds().stream()
                .flatMap(round -> round.getMatches().stream())
                .filter(match -> match.getStatus().equals("Completed"))
                .map(Match::getWinner)
                .reduce((first, second) -> second).orElse(null);
    }
}
