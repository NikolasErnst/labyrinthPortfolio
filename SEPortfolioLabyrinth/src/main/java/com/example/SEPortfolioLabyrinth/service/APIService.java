package com.example.SEPortfolioLabyrinth.service;

import org.openapitools.client.api.DefaultApi;
import org.openapitools.client.model.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.example.SEPortfolioLabyrinth.util.DirectionHelper.getDirectionFromIndex;

@Service
public class APIService {
    final DefaultApi defaultApi = new DefaultApi();

    public GameDto startGame() {
        GameInputDto gameInput = new GameInputDto();
        gameInput.setGroupName("Niklas Neuweiler, Nikolas Ernst, Hanna Huber");

        GameDto result = defaultApi.gamePost(gameInput);
        System.out.println("New game started with ID: " + result.getGameId());

        return result;
    }

    public MoveStatusDto move(BigDecimal gameID, int directionIndex) {
        DirectionDto direction = getDirectionFromIndex(directionIndex);

        MoveInputDto moveInput = new MoveInputDto();
        moveInput.direction(direction);

        MoveDto moveOutput = defaultApi.gameGameIdMovePost(gameID, moveInput);

        return moveOutput.getMoveStatus();
    }

    public boolean gameErfolg(BigDecimal gameId) {
        GameDto currentGame = defaultApi.gameGameIdGet(gameId);
        System.out.println(currentGame.toString());
        return (currentGame.getStatus() == GameStatusDto.SUCCESS);
    }

    public List<MoveDto> getMoveHistory(BigDecimal oldGameId) {
        if (oldGameId != null) {
            return defaultApi.gameGameIdMoveGet(oldGameId);
        }
        return new ArrayList<>();
    }
}
