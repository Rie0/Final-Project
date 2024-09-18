package org.twspring.noob.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.twspring.noob.Api.ApiException;
import org.twspring.noob.Model.League;
import org.twspring.noob.Model.Participant;
import org.twspring.noob.Model.Player;
import org.twspring.noob.Model.Tournament;
import org.twspring.noob.Repository.LeagueRepository;
import org.twspring.noob.Repository.ParticipantRepository;
import org.twspring.noob.Repository.PlayerRepository;
import org.twspring.noob.Repository.TournamentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor

//Hussam
public class ParticipantService {
    private final ParticipantRepository participantRepository;
    private final TournamentRepository tournamentRepository;
    private final PlayerRepository playerRepository;
    private final LeagueRepository leagueRepository;

    public List<Participant> getParticipants() {
        return participantRepository.findAll();
    }

    public Participant getParticipantById(Integer id) {
        return participantRepository.findParticipantById(id);
    }

    public void participateInTournament(Integer playerId, Integer tournamentId, String name) {
        // Find the player and tournament using their respective repositories
        Player player = playerRepository.findPlayerById(playerId);
        Tournament tournament = tournamentRepository.findTournamentById(tournamentId);
        // Check if the tournament exists
        if (tournament == null) {
            throw new ApiException("Tournament not found");
        }
        // Check if the player is already registered in the tournament
        Participant existingParticipant = participantRepository.findParticipantByPlayerIdAndTournamentId(playerId, tournamentId);
        if (existingParticipant != null) {
            throw new ApiException("Participant is already registered for this tournament");
        }

        // Check if the tournament is full
        if (tournament.getCurrentParticipants() >= tournament.getMaxParticipants()) {
            throw new ApiException("Tournament has reached its maximum number of participants");
        }

        // Create a new participant
        Participant participant = new Participant();
        participant.setName(name);
        participant.setPlayer(player);
        participant.setTournament(tournament);
        participant.setStatus(Participant.Status.REGISTERED);

        // Save the new participant
        participantRepository.save(participant);

        // Update the number of current participants in the tournament
        tournament.setCurrentParticipants(tournament.getCurrentParticipants() + 1);

        // Update the status of the tournament to FULL if the maximum number of participants is reached
        if (tournament.getCurrentParticipants() >= tournament.getMaxParticipants()) {
            tournament.setStatus(Tournament.Status.FULL);
        }
        tournament.getParticipants().add(participant);
        tournamentRepository.save(tournament);


    }


    public void updateParticipant(Integer participantId, Participant updatedParticipant) {
        Participant participant = participantRepository.findParticipantById(participantId);
        if (participant != null) {
            participant.setName(updatedParticipant.getName());
            participant.setSeed(updatedParticipant.getSeed());
            participant.setStatus(updatedParticipant.getStatus());
            participant.setPlayer(updatedParticipant.getPlayer());
            participantRepository.save(participant);
        }
    }

    public void deleteParticipant(Integer participantId, Integer tournamentId) {
        // Find the participant by ID
        Participant participant = participantRepository.findParticipantById(participantId);
        if (participant == null) {
            throw new ApiException("Participant not found");
        }

        // Check if the participant is associated with the given tournament
        if (!participant.getTournament().getId().equals(tournamentId)) {
            throw new ApiException("Participant is not associated with the specified tournament");
        }

        // Proceed with deletion
        participantRepository.deleteById(participantId);
    }



}
