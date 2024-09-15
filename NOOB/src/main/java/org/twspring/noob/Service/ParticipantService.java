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

    public void saveParticipant(Participant participant, Integer tournamentId , Integer playerId) {
        Tournament tournament = tournamentRepository.findTournamentById(tournamentId);
        if (tournament == null) {
            throw new ApiException("Tournament not found");
        }

        Player player = playerRepository.findPlayerById(playerId);
        if (player == null) {
            throw new ApiException("Player not found");
        }

        // Check if the player is already registered in the tournament
        Participant existingParticipant = participantRepository.findParticipantByPlayerIdAndTournamentId(playerId, tournamentId);
        System.out.println(existingParticipant);

        if (existingParticipant != null) {
            throw new ApiException("Player is already registered for this tournament");
        }


        tournament.setCurrentParticipants(tournament.getCurrentParticipants() + 1);

        participant.setPlayer(player);
        participant.setTournament(tournament);
        participantRepository.save(participant);

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

    public void deleteParticipant(Integer participantId) {
        Participant participant = participantRepository.findParticipantById(participantId);
        if (participant == null) {
            throw new ApiException("Participant not found");
        }
        participantRepository.deleteById(participantId);
    }

}
