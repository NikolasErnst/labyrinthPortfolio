package com.example.SEPortfolioLabyrinth;

import com.example.SEPortfolioLabyrinth.core.MazeSolver;
import com.example.SEPortfolioLabyrinth.core.MazeStateManager;
import com.example.SEPortfolioLabyrinth.service.APIService;
import com.example.SEPortfolioLabyrinth.service.MoveHistoryService;
import org.openapitools.client.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static com.example.SEPortfolioLabyrinth.util.DirectionHelper.getDirectionFromIndex;
import static com.example.SEPortfolioLabyrinth.util.DirectionHelper.getOppositeDirection;
import static org.openapitools.client.model.MoveStatusDto.*;

@Component
public class MazeRunner {

    @Autowired
    private APIService apiService;

    @Autowired
    private MoveHistoryService moveHistoryService;

    @Autowired
    private MazeStateManager stateManager;

    @Autowired
    private MazeSolver mazeSolver;

    void solveMaze() {
        GameDto game = apiService.startGame();
        BigDecimal gameId = game.getGameId();

        while (!apiService.gameErfolg(gameId)) {
            int[] directionIndex = {0, 1, 2, 3};

            for (int i = 0; i < directionIndex.length; i++) {

                if (stateManager.getLastSuccessfulMove() != null && directionIndex[i] == getOppositeDirection(stateManager.getLastSuccessfulMove())) {
                    continue;
                }

                System.out.println("Try Move: " + getDirectionFromIndex(directionIndex[i]));

                MoveStatusDto currentMove = apiService.move(gameId, directionIndex[i]);

                System.out.println("Result: " + currentMove);

                if (currentMove.equals(MOVED)) {
                    stateManager.setLastSuccessfulMove(getDirectionFromIndex(directionIndex[i]));
                    stateManager.clearFailedDirections();
                    break;
                }

                if (currentMove.equals(BLOCKED)) {
                    System.out.println("Direction blocked, try next direction");
                    continue;
                }

                if (currentMove.equals(FAILED)) {
                    System.out.println("Move Failed. Create new game and go to last valid status");

                    stateManager.addFailedDirection(directionIndex[i]);

                    BigDecimal oldGameId = gameId;
                    gameId = apiService.startGame().getGameId();

                    moveHistoryService.moveSuccessfulOldMoves(gameId, oldGameId);
                    DirectionDto lastMove = moveHistoryService.getLastMoveDirection(gameId);
                    stateManager.setLastSuccessfulMove(lastMove);

                    gameId = mazeSolver.tryAllDirectionsExceptOppositeAndFailed(gameId, stateManager.getLastSuccessfulMove(), oldGameId);
                    break;
                }
            }
        }
        System.out.println("Finished maze. This is your move history");
        System.out.println(apiService.getMoveHistory(gameId));
        System.out.println("Game Finished, Thanks for playing!");
    }
}