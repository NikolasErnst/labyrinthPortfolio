package com.example.SEPortfolioLabyrinth.core;

import com.example.SEPortfolioLabyrinth.service.APIService;
import com.example.SEPortfolioLabyrinth.service.MoveHistoryService;
import org.openapitools.client.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.example.SEPortfolioLabyrinth.util.DirectionHelper.getDirectionFromIndex;
import static com.example.SEPortfolioLabyrinth.util.DirectionHelper.getOppositeDirection;
import static org.openapitools.client.model.MoveStatusDto.*;

@Service
public class MazeSolver {
    @Autowired
    private APIService apiService;

    @Autowired
    private MoveHistoryService moveHistoryService;

    @Autowired
    private MazeStateManager stateManager;

    public BigDecimal tryAllDirectionsExceptOppositeAndFailed(BigDecimal gameId, DirectionDto lastDirection, BigDecimal oldGameId) {
        int oppositeDirection = getOppositeDirection(lastDirection);

        for (int directionIndex = 0; directionIndex < 4; directionIndex++) {
            if (directionIndex == oppositeDirection || stateManager.getFailedDirections().contains(directionIndex)) {
                continue;
            }

            System.out.println("Try Move: " + getDirectionFromIndex(directionIndex));

            MoveStatusDto moveResult = apiService.move(gameId, directionIndex);

            System.out.println("Result: " + moveResult);

            if (moveResult.equals(MOVED)) {
                stateManager.setLastSuccessfulMove(getDirectionFromIndex(directionIndex));
                stateManager.clearFailedDirections();
                return gameId;
            }

            if (moveResult.equals(BLOCKED)) {
                System.out.println("Direction blocked, try next direction.");
                continue;
            }

            if (moveResult.equals(FAILED)) {
                System.out.println("Move Failed. Create new game and go to last valid status");

                stateManager.addFailedDirection(directionIndex);

                gameId = apiService.startGame().getGameId();

                DirectionDto lastMove = moveHistoryService.moveSuccessfulOldMoves(gameId, oldGameId);
                stateManager.setLastSuccessfulMove(lastMove);

                return tryAllDirectionsExceptOppositeAndFailed(gameId, lastDirection, oldGameId);
            }
        }
        return gameId;
    }
}
