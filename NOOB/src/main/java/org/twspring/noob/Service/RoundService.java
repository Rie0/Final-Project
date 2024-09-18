package org.twspring.noob.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.twspring.noob.Model.Round;
import org.twspring.noob.Repository.RoundRepository;

import java.util.List;

@Service
@RequiredArgsConstructor

//Hussam
public class RoundService {
    private final RoundRepository roundRepository;

    public List<Round> getRounds() {
        return roundRepository.findAll();
    }

    public Round getRoundById(Integer id) {
        return roundRepository.findRoundById(id);
    }

    public Round saveRound(Round round) {
        return roundRepository.save(round);
    }

    public void updateRound(Integer roundId, Round updatedRound) {
        Round round = roundRepository.findRoundById(roundId);
        if (round != null) {
            round.setRoundNumber(updatedRound.getRoundNumber());
            round.setBracket(updatedRound.getBracket());
            roundRepository.save(round);
        }
    }

    public void deleteRound(Integer roundId) {
        roundRepository.deleteById(roundId);
    }
}
