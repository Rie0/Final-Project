package org.twspring.noob.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.twspring.noob.Api.ApiException;
import org.twspring.noob.Model.*;
import org.twspring.noob.Repository.*;
import lombok.RequiredArgsConstructor;

import java.util.*;
//Hussam
@Service
@Transactional
@RequiredArgsConstructor
public class BracketService {

    @PersistenceContext
    private final EntityManager entityManager;

    private final BracketRepository bracketRepository;
    private final TournamentRepository tournamentRepository;
    private final RoundRepository roundRepository;
    private final MatchRepository matchRepository;
    private final ParticipantRepository participantRepository;

    private static final Logger logger = LoggerFactory.getLogger(BracketService.class);

    public List<Bracket> getAllBrackets() {
        return bracketRepository.findAll();
    }

    public void deleteBracket(Integer organizerId, Integer bracketId) {
        logger.info("Attempting to delete bracket with ID: {} by user ID: {}", bracketId, organizerId);

        // Fetch the bracket by its ID
        Bracket bracket = bracketRepository.findBracketById(bracketId);
        if (bracket == null) {
            throw new RuntimeException("Bracket not found");
        }

        // Check if the bracket is associated with a tournament created by the organizer
        Tournament tournament = bracket.getTournament();
        if (tournament == null || tournament.getOrganizer() == null || !tournament.getOrganizer().getId().equals(organizerId)) {
            throw new RuntimeException("Bracket not associated with the specified organizer");
        }

        // Remove rounds and their matches
        for (Round round : bracket.getRounds()) {
            round.getMatches().clear();
            roundRepository.delete(round);
        }

        // Delete the bracket
        bracketRepository.delete(bracket);
    }

    public Bracket createBracketForTournament(Integer tournamentId) {
        return initializeBracket(tournamentId, "Single Elimination");
    }

    private int nextPowerOfTwo(int number) {
        int power = 1;
        while (power < number) {
            power *= 2;
        }
        return power;
    }

    private Bracket initializeBracket(Integer tournamentId, String bracketType) {
        Tournament tournament = tournamentRepository.findTournamentById(tournamentId);
        if (tournament == null) {
            throw new RuntimeException("Tournament not found");
        }

        // Check if a bracket for the given type already exists
        Bracket bracket = bracketRepository.findByTournamentAndBracketType(tournament, bracketType);
        if (bracket == null) {
            bracket = new Bracket();
            bracket.setTournament(tournament);
            bracket.setBracketType(bracketType);
            bracketRepository.save(bracket);
        }else {
            throw new ApiException("A " + bracketType + " bracket already exists for this tournament");

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
            Round round = roundRepository.findByBracketAndRoundNumber(bracket, i + 1);
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
        match.setParticipant1Name(p1.getName());
        match.setParticipant2Name(p2.getName());
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

    public Bracket createDoubleEliminationBracket(Integer tournamentId) {
        entityManager.clear();

        Tournament tournament = tournamentRepository.findTournamentById(tournamentId);
        if (tournament == null) {
            throw new RuntimeException("Tournament not found");
        }

        Bracket winnersBracket = initializeBracket(tournamentId, "Double Elimination - Winners");

        Bracket losersBracket = bracketRepository.findByTournamentAndBracketType(tournament, "Double Elimination - Losers");
        if (losersBracket == null) {
            losersBracket = new Bracket();
            losersBracket.setBracketType("Double Elimination - Losers");
            losersBracket.setTournament(tournament);
            bracketRepository.save(losersBracket);
        }

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

        Participant loser = participantRepository.findParticipantById(loserParticipantId);
        if (loser == null) {
            throw new RuntimeException("Participant not found");
        }

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
        List<Round> sortedRounds = new ArrayList<>(losersBracket.getRounds());
        sortedRounds.sort(Comparator.comparingInt(Round::getRoundNumber));

        for (Round round : sortedRounds) {
            int expectedNumberOfMatches = getNumberOfMatches(round.getRoundNumber(), losersBracket.getTournament().getMaxParticipants());

            if (round.getMatches().size() < expectedNumberOfMatches) {
                return round;
            }
        }

        return null;
    }

    private int getNumberOfMatches(int roundNumber, int bracketSize) {
        if (roundNumber == 1) {
            return bracketSize / 2;
        }

        return (int) Math.ceil(bracketSize / Math.pow(2, roundNumber));
    }

    public void manageFinalMatch(Tournament tournament) {
        Bracket winnersBracket = bracketRepository.findByTournamentAndBracketType(tournament, "Double Elimination - Winners");
        Bracket losersBracket = bracketRepository.findByTournamentAndBracketType(tournament, "Double Elimination - Losers");

        if (winnersBracket == null || losersBracket == null) {
            throw new RuntimeException("Both winners and losers brackets must be present to manage the final match.");
        }

        Participant winnerFinalist = findFinalistFromBracket(winnersBracket);
        Participant loserFinalist = findFinalistFromBracket(losersBracket);

        if (winnerFinalist == null || loserFinalist == null) {
            throw new RuntimeException("Finalists could not be determined from the brackets.");
        }

        // Create final round and match
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
        // Find the last completed match in the bracket to determine the finalist
        return bracket.getRounds().stream()
                .flatMap(round -> round.getMatches().stream())
                .filter(match -> match.getStatus().equals("Completed"))
                .map(Match::getWinner)
                .reduce((first, second) -> second)
                .orElse(null);
    }
}

