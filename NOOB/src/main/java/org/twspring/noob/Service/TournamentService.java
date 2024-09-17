package org.twspring.noob.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.twspring.noob.Api.ApiException;
import org.twspring.noob.Model.*;
import org.twspring.noob.Repository.MatchRepository;
import org.twspring.noob.Repository.OrganizerRepository;
import org.twspring.noob.Repository.ParticipantRepository;
import org.twspring.noob.Repository.TournamentRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TournamentService {
    private final OrganizerRepository organizerRepository;
    private final TournamentRepository tournamentRepository;
    private final MatchRepository matchRepository;
    private final ParticipantRepository participantRepository;

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
        tournament.setOrganizer(org);
        tournamentRepository.save(tournament);
    }

    public void updateTournament(Integer tournamentId, Tournament updatedTournament, Integer organizerId) {
        checkOrganizerAuthorization(tournamentId, organizerId);
        Tournament tournament = tournamentRepository.findTournamentById(tournamentId);
        if (tournament != null) {
            tournament.setName(updatedTournament.getName());
            tournament.setDescription(updatedTournament.getDescription());
            tournament.setStartDate(updatedTournament.getStartDate());
            tournament.setEndDate(updatedTournament.getEndDate());
            tournament.setStatus(updatedTournament.getStatus());
            tournament.setLocation(updatedTournament.getLocation());
            tournament.setMaxParticipants(updatedTournament.getMaxParticipants());
            tournament.setCurrentParticipants(updatedTournament.getCurrentParticipants());
            tournament.setParticipants(updatedTournament.getParticipants());
            tournament.setMatches(updatedTournament.getMatches());
            tournament.setBracket(updatedTournament.getBracket());
            tournamentRepository.save(tournament);
        }
    }

    public void deleteTournament(Integer tournamentId, Integer organizerId) {
        checkOrganizerAuthorization(tournamentId, organizerId);
        Tournament tournament = tournamentRepository.findByTournamentIdAndOrganizerId(tournamentId, organizerId);
        if (tournament == null) {
            throw new ApiException("Tournament not found or not authorized to delete.");
        }
        tournamentRepository.deleteById(tournamentId);
    }

    public void startTournament(Integer tournamentId, Integer organizerId) {
        checkOrganizerAuthorization(tournamentId, organizerId);
        Tournament tournament = tournamentRepository.findTournamentById(tournamentId);

        if (tournament == null) {
            throw new ApiException("Tournament not found");
        }

        if (tournament.getStatus().equals(Tournament.Status.ACTIVE)) {
            throw new ApiException("Tournament is already active");
        } else if (tournament.getStatus().equals(Tournament.Status.FINISHED)) {
            throw new ApiException("Tournament has already been finished");
        }

        LocalDate today = LocalDate.now();
        if (tournament.getStartDate().isAfter(today)) {
            throw new ApiException("Tournament cannot be started before the start date");
        }

        tournament.setStatus(Tournament.Status.ACTIVE);
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
        return tournamentRepository.findByStatus("OPEN");
    }

    public List<Tournament> getTournamentsByStatusActive() {
        return tournamentRepository.findByStatus("ACTIVE");
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
        return tournamentRepository.findByStatus("FINISHED");
    }

    public String getTournamentDescriptionById(Integer id) {
        return tournamentRepository.findTournamentById(id).getDescription();
    }

    public List<Match> getTournamentMatchesById(Integer id) {
        Tournament tournament = tournamentRepository.findTournamentById(id);
        return matchRepository.findByTournament(tournament);
    }

    public Bracket getTournamentBracketById(Integer id) {
        Tournament tournament = tournamentRepository.findTournamentById(id);
        return tournament.getBracket();
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
        checkOrganizerAuthorization(tournamentId, organizerId);
        Tournament tournament = tournamentRepository.findTournamentById(tournamentId);
        if (tournament == null) {
            throw new ApiException("Tournament not found");
        }
        if (tournament.getBracket() == null) {
            throw new ApiException("Tournament has no bracket");
        }

        List<Match> matches = matchRepository.findByTournament(tournament);

        if (matches.stream().anyMatch(match ->
                !(match.getStatus().equalsIgnoreCase("Completed") || match.getStatus().equalsIgnoreCase("Completed_bye")))) {
            throw new ApiException("All matches must be completed or completed by a bye before finalizing the tournament.");
        }

        List<Participant> sortedParticipants = determineFinalRankingByBracket(matches, tournament);

        for (int i = 0; i < sortedParticipants.size(); i++) {
            Participant participant = sortedParticipants.get(i);
            participant.setSeed(i + 1);
            participant.setStatus(Participant.Status.FINALIZED);
            participantRepository.save(participant);
        }

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
}
