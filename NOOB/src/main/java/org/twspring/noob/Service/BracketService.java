package org.twspring.noob.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.twspring.noob.Model.Bracket;
import org.twspring.noob.Repository.BracketRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BracketService {
    private final BracketRepository bracketRepository;

    public List<Bracket> getBrackets() {
        return bracketRepository.findAll();
    }

    public Bracket getBracketById(Integer id) {
        return bracketRepository.findBracketById(id);
    }

    public Bracket saveBracket(Bracket bracket) {
        return bracketRepository.save(bracket);
    }

    public void updateBracket(Integer bracketId, Bracket updatedBracket) {
        Bracket bracket = bracketRepository.findBracketById(bracketId);
        if (bracket != null) {
            bracket.setBracketType(updatedBracket.getBracketType());
            bracket.setRounds(updatedBracket.getRounds());
            bracket.setTournament(updatedBracket.getTournament());
            bracketRepository.save(bracket);
        }
    }

    public void deleteBracket(Integer bracketId) {
        bracketRepository.deleteById(bracketId);
    }
}
