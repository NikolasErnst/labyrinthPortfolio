package com.example.SEPortfolioLabyrinth.service;

import org.openapitools.client.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MoveHistoryService {

    @Autowired
    private APIService apiService;

    public DirectionDto moveSuccessfulOldMoves(BigDecimal newGameId, BigDecimal oldGameId) {
        List<MoveDto> oldMoves = apiService.getMoveHistory(oldGameId);
        DirectionDto lastSuccessfulMove = null;

        if (oldMoves.isEmpty() || oldMoves.size() == 1) {
            return lastSuccessfulMove;
        }

        for (MoveDto move : oldMoves) {
            if (move.getMoveStatus().equals(MoveStatusDto.MOVED)) {

                DirectionDto direction = move.getDirection();
                MoveInputDto moveInput = new MoveInputDto();
                moveInput.direction(direction);

                MoveDto track = apiService.defaultApi.gameGameIdMovePost(newGameId, moveInput);

                if (track.getMoveStatus().equals(MoveStatusDto.MOVED)) {
                    lastSuccessfulMove = direction;
                }
            }
        }

        return lastSuccessfulMove;
    }

    public DirectionDto getLastMoveDirection(BigDecimal gameId) {
        List<MoveDto> moveHistory = apiService.getMoveHistory(gameId);
        if (!moveHistory.isEmpty()) {
            return moveHistory.get(moveHistory.size() - 1).getDirection();
        }
        return null;
    }
}
