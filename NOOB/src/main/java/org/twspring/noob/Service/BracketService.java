package org.twspring.noob.Service;

import org.twspring.noob.DTO.BracketDTO;
import org.twspring.noob.Model.*;
import org.twspring.noob.Repository.BracketRepository;
import org.twspring.noob.Repository.MatchRepository;
import org.twspring.noob.Repository.RoundRepository;
import org.twspring.noob.Repository.TournamentRepository;
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
        bracket.getRounds().clear();
        Tournament tournament = tournamentRepository.findTournamentByBracketId(bracketId);
        tournament.setBracket(null);
        bracketRepository.deleteById(bracketId);
    }


    private int nextPowerOfTwo(int number) {
        int power = 1;
        while (power < number) {
            power *= 2;
        }
        return power;
    }

    public Bracket initializeBracket(Integer tournamentId) {
        Tournament tournament = tournamentRepository.findTournamentById(tournamentId);
        if (tournament == null) {
            throw new RuntimeException("Tournament not found");
        }

        Set<Participant> participants = new HashSet<>(tournament.getParticipants());
        boolean isOdd = participants.size() % 2 != 0;
        Participant firstParticipant = isOdd ? participants.iterator().next() : null;

        if (isOdd) {
            // Assume first participant gets a bye
            participants.remove(firstParticipant);
        }

        int numberOfParticipants = participants.size();
        int bracketSize = nextPowerOfTwo(numberOfParticipants);
        int numberOfByes = bracketSize - numberOfParticipants;

        Bracket bracket = new Bracket();
        bracket.setTournament(tournament);
        bracket.setBracketType("Single Elimination");
        bracketRepository.save(bracket);

        initializeRounds(bracket, bracketSize, numberOfByes, firstParticipant);
        return bracket;
    }

    private void initializeRounds(Bracket bracket, int bracketSize, int numberOfByes, Participant firstParticipantBye) {
        int numberOfRounds = (int) (Math.log(bracketSize) / Math.log(2));
        Set<Round> rounds = new HashSet<>();

        for (int i = 0; i < numberOfRounds; i++) {
            Round round = new Round();
            round.setBracket(bracket);
            round.setRoundNumber(i + 1);
            rounds.add(round);
            roundRepository.save(round);
        }

        bracket.setRounds(rounds);
        if (firstParticipantBye != null) {
            advanceParticipantToNextRound(firstParticipantBye, rounds.iterator().next());
        }
        scheduleMatchesForFirstRound(bracket, rounds.iterator().next(), numberOfByes);
    }

    private void advanceParticipantToNextRound(Participant participant, Round round) {
        // Create a dummy match or log that the participant advances due to a bye
        Match byeMatch = new Match();
        byeMatch.setParticipant1(participant);
        byeMatch.setRound(round);
        byeMatch.setStatus("Bye");
        matchRepository.save(byeMatch);
    }

    private void scheduleMatchesForFirstRound(Bracket bracket, Round firstRound, int numberOfByes) {
        Set<Participant> participants = new HashSet<>(bracket.getTournament().getParticipants());

        // Remove byes if necessary
        while (numberOfByes-- > 0 && !participants.isEmpty()) {
            participants.remove(participants.iterator().next());
        }

        List<Participant> shuffledParticipants = new ArrayList<>(participants);
        Collections.shuffle(shuffledParticipants); // Randomize remaining participants

        for (int i = 0; i < shuffledParticipants.size() - 1; i += 2) {
            Match match = new Match();
            match.setParticipant1(shuffledParticipants.get(i));
            match.setParticipant2(shuffledParticipants.get(i + 1));
            match.setRound(firstRound);
            match.setStatus("Scheduled");
            matchRepository.save(match);
        }
    }





}
