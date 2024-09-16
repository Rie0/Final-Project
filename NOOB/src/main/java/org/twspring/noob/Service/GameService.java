package org.twspring.noob.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.twspring.noob.Api.ApiException;
import org.twspring.noob.Model.Game;
import org.twspring.noob.Model.PC;
import org.twspring.noob.Repository.GameRepository;
import org.twspring.noob.Repository.PcRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameService {
    private final GameRepository gameRepository;
private final PcRepository pcRepository;

    public List<Game> getAllGame() {
        return gameRepository.findAll();
    }


    public void addGame(Game game,Integer pcId) {
        PC pc= pcRepository.findPCById(pcId);
        if(pc==null) {
            throw new ApiException("PC not found");
        }
        gameRepository.save(game);

    }

    public void updateGame(Integer id, Game game) {
        Game game1 = gameRepository.findGameById(id);
        if (game1 == null) {
            throw new ApiException("game not found");
        }
        game1.setGameGenres(game.getGameGenres());
game1.setGameName(game.getGameName());
        gameRepository.save(game1);
    }

    public void deleteGame(Integer id) {
        Game game = gameRepository.findGameById(id);
        if (game == null) {
            throw new ApiException("game not found");
        }
        gameRepository.delete(game);
    }




}
