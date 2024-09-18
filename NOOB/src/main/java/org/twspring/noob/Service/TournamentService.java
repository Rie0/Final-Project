package org.twspring.noob.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.twspring.noob.Api.ApiException;
import org.twspring.noob.Model.*;
import org.twspring.noob.Repository.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;



//Hussam
@Service
@RequiredArgsConstructor
public class TournamentService {
    private final OrganizerRepository organizerRepository;
    private final TournamentRepository tournamentRepository;
    private final MatchRepository matchRepository;
    private final ParticipantRepository participantRepository;
    private final BracketRepository bracketRepository;

    public List<Tournament> getTournaments() {
        return tournamentRepository.findAll();
    }

    public Tournament getTournamentById(Integer id) {
        return tournamentRepository.findTournamentById(id);
    }

    public void saveTournament(Tournament tournament, Integer organizerId) {
        Organizer org = organizerRepository.findOrganizerById(organizerId);
        if (org == null) {
            throw new ApiException("Organizer not found");
        }
        if (tournament.getEndDate().isBefore(tournament.getStartDate())) {
            throw new ApiException("End date must be the same as or after the start date");
        }
        // Check if the tournament is onsite and validate the permit
        if ("Onsite".equalsIgnoreCase(tournament.getAttendanceType())) {
            if (tournament.getPermit() == null || tournament.getPermit().trim().isEmpty()) {
                throw new ApiException("Organizer does not have a valid permit to create an onsite tournament");
            }
        }
        tournament.setOrganizer(org);
        tournamentRepository.save(tournament);
    }
    public void deleteTournament(Integer tournamentId, Integer organizerId) {
        // Find the tournament by ID and organizer ID to ensure the authenticated organizer owns it
        Tournament tournament = tournamentRepository.findByTournamentIdAndOrganizerId(tournamentId, organizerId);

        if (tournament == null) {
            throw new ApiException("Tournament not found or you are not authorized to delete it");
        }

        // Proceed with deleting the tournament
        tournamentRepository.delete(tournament);
    }
    public void updateTournament(Integer tournamentId, Tournament updatedTournament, Integer organizerId) {
        // Check organizer authorization
        checkOrganizerAuthorization(tournamentId, organizerId);

        // Find the existing tournament
        Tournament tournament = tournamentRepository.findTournamentById(tournamentId);
        if (tournament == null) {
            throw new ApiException("Tournament not found");
        }

        // Update tournament basic fields
        tournament.setName(updatedTournament.getName());
        tournament.setDescription(updatedTournament.getDescription());
        tournament.setStartDate(updatedTournament.getStartDate());
        tournament.setEndDate(updatedTournament.getEndDate());
        tournament.setStatus(updatedTournament.getStatus());
        tournament.setLocation(updatedTournament.getLocation());
        tournament.setMaxParticipants(updatedTournament.getMaxParticipants());
        tournament.setCurrentParticipants(updatedTournament.getCurrentParticipants());

        // Handle related entities carefully
        // Update participants
        if (updatedTournament.getParticipants() != null) {
            tournament.getParticipants().clear();
            tournament.getParticipants().addAll(updatedTournament.getParticipants());
        }

        // Update matches
        if (updatedTournament.getMatches() != null) {
            tournament.getMatches().clear();
            tournament.getMatches().addAll(updatedTournament.getMatches());
        }

        // Update brackets
        if (updatedTournament.getBrackets() != null) {
            tournament.getBrackets().clear();
            tournament.getBrackets().addAll(updatedTournament.getBrackets());
        }

        // Save the updated tournament
        tournamentRepository.save(tournament);
    }

    public void deleteParticipant(Integer userId, Integer tournamentId) {
        Participant participant = participantRepository.findByUserIdAndTournamentId(userId, tournamentId);

        if (participant == null) {
            throw new ApiException("Participant not found in the specified tournament");
        }

        // Authorization Check: Only the participant themselves or the tournament organizer can delete the participant
        if (!participant.getPlayer().getUser().getId().equals(userId) &&
                !participant.getTournament().getOrganizer().getId().equals(userId)) {
            throw new ApiException("Access denied: You are not authorized to delete this participant.");
        }

        participantRepository.delete(participant);
    }


    public void startTournament(Integer tournamentId, Integer organizerId) {
        // Check if the organizer is authorized to manage the tournament
        checkOrganizerAuthorization(tournamentId, organizerId);

        // Retrieve the tournament from the repository
        Tournament tournament = tournamentRepository.findTournamentById(tournamentId);
        if (tournament == null) {
            throw new ApiException("Tournament not found");
        }

        // Check if the tournament is already active or finished
        if (tournament.getStatus().equals(Tournament.Status.ACTIVE)) {
            throw new ApiException("Tournament is already active");
        } else if (tournament.getStatus().equals(Tournament.Status.FINISHED)) {
            throw new ApiException("Tournament has already been finished");
        }

        // Ensure the tournament start date is today or earlier
        LocalDate today = LocalDate.now();
        if (tournament.getStartDate().isAfter(today)) {
            throw new ApiException("Tournament cannot be started before the start date");
        }

        // Set the tournament status to ACTIVE
        tournament.setStatus(Tournament.Status.ACTIVE);

        // Save the updated tournament to the repository
        tournamentRepository.save(tournament);
    }

    public void checkInParticipant(Integer tournamentId, Integer participantId, Integer organizerId) {
        checkOrganizerAuthorization(tournamentId, organizerId);

        Tournament tournament = tournamentRepository.findTournamentById(tournamentId);
        if (tournament == null) {
            throw new ApiException("Tournament not found");
        }

        Participant participant = participantRepository.findParticipantById(participantId);
        if (participant == null) {
            throw new ApiException("Participant not found");
        }

        if (!participant.getTournament().getId().equals(tournamentId)) {
            throw new ApiException("Participant is not associated with the specified tournament");
        }

        participant.setStatus(Participant.Status.CHECKED_IN);
        participantRepository.save(participant);
    }

    public List<Tournament> getTournamentsByGame(String game) {
        return tournamentRepository.findByGame(game);
    }

    public List<Tournament> getTournamentsByCity(String city) {
        return tournamentRepository.findByCity(city);
    }

    public List<Tournament> getTournamentsByAttendanceTypeOnline() {
        return tournamentRepository.findByAttendanceType("Online");
    }

    public List<Tournament> getTournamentsByAttendanceTypeOnsite() {
        return tournamentRepository.findByAttendanceType("Onsite");
    }

    public List<Tournament> getTournamentsByStatusOngoing() {
        return tournamentRepository.findByStatus(Tournament.Status.OPEN);
    }

    public List<Tournament> getTournamentsByStatusActive() {
        return tournamentRepository.findByStatus(Tournament.Status.ACTIVE);
    }

    public List<Tournament> getTournamentsByStatusClosingSoon() {
        List<Tournament> upcomingTournaments = tournamentRepository.findUpcomingTournaments();
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<Tournament> closingSoonTournaments = new ArrayList<>();

        for (Tournament tournament : upcomingTournaments) {
            if (tournament.getStartDate().equals(tomorrow)) {
                closingSoonTournaments.add(tournament);
            }
        }

        return closingSoonTournaments;
    }

    public List<Tournament> getTournamentsByStatusFinished() {
        return tournamentRepository.findByStatus(Tournament.Status.FINISHED);
    }

    public String getTournamentDescriptionById(Integer id) {
        return tournamentRepository.findTournamentById(id).getDescription();
    }

    public List<Match> getTournamentMatchesById(Integer id) {
        Tournament tournament = tournamentRepository.findTournamentById(id);
        return matchRepository.findByTournament(tournament);
    }

    public Bracket getTournamentBracketById(Integer tournamentId, Integer bracketId) {
        // Use the repository method to find the bracket by its ID and associated tournament ID
        Bracket bracket = bracketRepository.findByIdAndTournamentId(bracketId, tournamentId);
        if (bracket == null) {
            throw new RuntimeException("Bracket not found or not associated with the given tournament");
        }
        return bracket;
    }

    public List<Participant> getTournamentStandingById(Integer id) {
        Tournament tournament = tournamentRepository.findTournamentById(id);
        if (tournament == null) {
            throw new ApiException("Tournament not found");
        }

        return tournament.getParticipants().stream()
                .sorted((p1, p2) -> {
                    if (p1.getSeed() == null && p2.getSeed() == null) {
                        return 0;
                    } else if (p1.getSeed() == null) {
                        return 1;
                    } else if (p2.getSeed() == null) {
                        return -1;
                    } else {
                        return Integer.compare(p2.getSeed(), p1.getSeed());
                    }
                })
                .collect(Collectors.toList());
    }

    public List<Match> getMatchesByStatusCompleted(Integer tournamentId) {
        Tournament tournament = tournamentRepository.findTournamentById(tournamentId);
        if (tournament == null) {
            throw new ApiException("Tournament not found");
        }
        return matchRepository.findByTournamentAndStatus(tournament, "Completed");
    }

    public List<Match> getMatchesByStatusInProgress(Integer tournamentId) {
        Tournament tournament = tournamentRepository.findTournamentById(tournamentId);
        if (tournament == null) {
            throw new ApiException("Tournament not found");
        }
        return matchRepository.findByTournamentAndStatus(tournament, "InProgress");
    }

    public List<Match> getMatchesByStatusNotStarted(Integer tournamentId) {
        Tournament tournament = tournamentRepository.findTournamentById(tournamentId);
        if (tournament == null) {
            throw new ApiException("Tournament not found");
        }
        return matchRepository.findByTournamentAndStatus(tournament, "NotStarted");
    }

    public void finalizeTournament(Integer tournamentId, Integer organizerId) {
        // Ensure the organizer has authorization for the tournament
        checkOrganizerAuthorization(tournamentId, organizerId);

        // Retrieve the tournament
        Tournament tournament = tournamentRepository.findTournamentById(tournamentId);
        if (tournament == null) {
            throw new ApiException("Tournament not found");
        }

        // Check if the tournament has any brackets associated
        if (tournament.getBrackets() == null || tournament.getBrackets().isEmpty()) {
            throw new ApiException("Tournament has no brackets");
        }

        // Retrieve all matches associated with the tournament
        List<Match> matches = matchRepository.findByTournament(tournament);

        // Ensure all matches are completed or completed by a bye
        if (matches.stream().anyMatch(match ->
                !(match.getStatus().equalsIgnoreCase("Completed") || match.getStatus().equalsIgnoreCase("Completed_bye")))) {
            throw new ApiException("All matches must be completed or completed by a bye before finalizing the tournament.");
        }

        // Determine the final ranking by processing matches across all brackets
        List<Participant> sortedParticipants = determineFinalRankingByBracket(matches, tournament);

        // Update participant status and ranking
        for (int i = 0; i < sortedParticipants.size(); i++) {
            Participant participant = sortedParticipants.get(i);
            participant.setSeed(i + 1);
            participant.setStatus(Participant.Status.FINALIZED);
            participantRepository.save(participant);
        }

        // Update the tournament status to finished
        tournament.setStatus(Tournament.Status.FINISHED);
        tournamentRepository.save(tournament);
    }

    private List<Participant> determineFinalRankingByBracket(List<Match> matches, Tournament tournament) {
        Map<Participant, Integer> eliminationRounds = new HashMap<>();
        Participant winner = null;
        Participant runnerUp = null;

        for (Match match : matches) {
            if ("Completed".equalsIgnoreCase(match.getStatus())) {
                int roundNumber = match.getRound().getRoundNumber();
                if (roundNumber == getMaxRoundNumber(matches)) {
                    winner = match.getWinner();
                    runnerUp = match.getLoser();
                } else {
                    eliminationRounds.put(match.getLoser(), roundNumber);
                }
            }
        }

        List<Participant> sortedParticipants = new ArrayList<>();
        if (winner != null) {
            sortedParticipants.add(winner);
        }
        if (runnerUp != null) {
            sortedParticipants.add(runnerUp);
        }

        Map<Integer, List<Participant>> participantsByEliminationRound = new HashMap<>();
        for (Map.Entry<Participant, Integer> entry : eliminationRounds.entrySet()) {
            participantsByEliminationRound
                    .computeIfAbsent(entry.getValue(), k -> new ArrayList<>())
                    .add(entry.getKey());
        }

        List<Integer> sortedRounds = new ArrayList<>(participantsByEliminationRound.keySet());
        sortedRounds.sort(Collections.reverseOrder());

        for (Integer round : sortedRounds) {
            List<Participant> participantsInRound = participantsByEliminationRound.get(round);
            sortedParticipants.addAll(participantsInRound);
        }

        return sortedParticipants;
    }

    private int getMaxRoundNumber(List<Match> matches) {
        return matches.stream()
                .mapToInt(match -> match.getRound().getRoundNumber())
                .max()
                .orElse(0);
    }

    public void checkOrganizerAuthorization(Integer tournamentId, Integer organizerId) {
        Tournament tournament = tournamentRepository.findByTournamentIdAndOrganizerId(tournamentId, organizerId);
        if (tournament == null) {
            throw new ApiException("Unauthorized action: You are not the organizer of this tournament.");
        }
    }

    public List<Match> getTournamentMatchesByStatusCompleted(Integer tournamentId) {
        Tournament tournament = tournamentRepository.findTournamentById(tournamentId);
        if (tournament == null) {
            throw new ApiException("Tournament not found");
        }
        return matchRepository.findByTournamentAndStatus(tournament, "COMPLETED");
    }

    public List<Match> getTournamentMatchesByStatusInProgress(Integer tournamentId) {
        Tournament tournament = tournamentRepository.findTournamentById(tournamentId);
        if (tournament == null) {
            throw new ApiException("Tournament not found");
        }
        return matchRepository.findByTournamentAndStatus(tournament, "IN_PROGRESS");
    }

    public List<Match> getTournamentMatchesByStatusNotStarted(Integer tournamentId) {
        Tournament tournament = tournamentRepository.findTournamentById(tournamentId);
        if (tournament == null) {
            throw new ApiException("Tournament not found");
        }
        return matchRepository.findByTournamentAndStatus(tournament, "NOT_STARTED");
    }


    //////////Mohammed
    public void distributePrizes(Integer tournamentId) {
        Tournament tournament = tournamentRepository.findTournamentById(tournamentId);
        if (tournament == null) {
            throw new ApiException("Tournament not found");
        }

        // Check if the tournament status is "COMPLETED"
        if (tournament.getStatus() != Tournament.Status.FINISHED) {
            throw new ApiException("Prizes can only be distributed for a completed tournament.");
        }

        int participantsPrize = tournament.getParticipantsPrize(); // Number of participants to receive prizes
        List<Participant> participants = participantRepository.findTopParticipantsByTournamentId(tournamentId, participantsPrize);

        double totalPrizePool = tournament.getPrizePool(); // Total prize pool to be distributed

        // Distribute prizes to top participants dynamically
        double[] prizeDistribution = calculatePrizeDistribution(participantsPrize, totalPrizePool);

        for (int i = 0; i < participants.size(); i++) {
            Participant participant = participants.get(i);
            participant.setPrize((int) prizeDistribution[i]); // Set prize to the participant
            participantRepository.save(participant);
        }
    }


    //////////Mohammed

    // Method to calculate prize distribution dynamically
    private double[] calculatePrizeDistribution(int participantsPrize, double prizePool) {
        double[] prizeDistribution = new double[participantsPrize];
        double totalRatio = 0;

        // Calculate the total ratio for prize distribution
        for (int i = 1; i <= participantsPrize; i++) {
            totalRatio += 1.0 / i;
        }

        // Calculate the individual prize for each rank
        for (int i = 1; i <= participantsPrize; i++) {
            prizeDistribution[i - 1] = prizePool * ((1.0 / i) / totalRatio);
        }

        return prizeDistribution;
    }
}
