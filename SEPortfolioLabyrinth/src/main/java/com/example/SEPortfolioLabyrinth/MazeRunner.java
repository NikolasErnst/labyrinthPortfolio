package com.example.SEPortfolioLabyrinth;
import org.openapitools.client.api.DefaultApi;
import org.openapitools.client.model.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.openapitools.client.model.MoveStatusDto.*;

@Component
public class MazeRunner {

    final DefaultApi defaultApi = new DefaultApi();

    private Set<Integer> failedDirections = new HashSet<>();
    private DirectionDto lastSuccessfulMove = null;

    void solveMaze() {
        GameDto game = startGame();
        BigDecimal gameId = game.getGameId();

        while (!gameErfolg(gameId)) {
            int[] directionIndex = {0, 1, 2, 3};

            for (int i = 0; i < directionIndex.length; i++) {

                if (lastSuccessfulMove != null && directionIndex[i] == getOppositeDirection(lastSuccessfulMove)) {
                    continue;
                }

                System.out.println("Try Move: " + getDirectionFromIndex(directionIndex[i]));

                MoveStatusDto currentMove = move(gameId, directionIndex[i]);

                System.out.println("Result: " + currentMove);

                if (currentMove.equals(MOVED)) {
                    lastSuccessfulMove = getDirectionFromIndex(directionIndex[i]);
                    failedDirections.clear();
                    break;
                }

                if (currentMove.equals(BLOCKED)) {
                    System.out.println("Direction blocked, try next direction");
                    continue;
                }

                if (currentMove.equals(FAILED)) {
                    System.out.println("Move Failed. Create new game and go to last valid status");

                    failedDirections.add(directionIndex[i]);

                    BigDecimal oldGameId = gameId;
                    gameId = startGame().getGameId();

                    moveSuccessfulOldMoves(gameId, oldGameId);
                    lastSuccessfulMove = getLastMoveDirection(gameId);

                    gameId = tryAllDirectionsExceptOppositeAndFailed(gameId, lastSuccessfulMove, oldGameId);
                    break;
                }
            }
        }
        System.out.println("Finished maze. This is your move history");
        System.out.println(getMoveHistory(gameId));
        System.out.println("Game Finished, Thanks for playing!");
    }

    private DirectionDto getLastMoveDirection(BigDecimal gameId) {
        List<MoveDto> moveHistory = getMoveHistory(gameId);
        if (!moveHistory.isEmpty()) {
            return moveHistory.get(moveHistory.size() - 1).getDirection();
        }
        return null;
    }

    private DirectionDto getDirectionFromIndex(int index) {
        switch(index) {
            case 0: return DirectionDto.UP;
            case 1: return DirectionDto.RIGHT;
            case 2: return DirectionDto.DOWN;
            case 3: return DirectionDto.LEFT;
            default: return null;
        }
    }

    private BigDecimal tryAllDirectionsExceptOppositeAndFailed(BigDecimal gameId, DirectionDto lastDirection, BigDecimal oldGameId) {
        int oppositeDirection = getOppositeDirection(lastDirection);

        for (int directionIndex = 0; directionIndex < 4; directionIndex++) {
            if (directionIndex == oppositeDirection || failedDirections.contains(directionIndex)) {
                continue;
            }

            System.out.println("Try Move: " + getDirectionFromIndex(directionIndex));

            MoveStatusDto moveResult = move(gameId, directionIndex);

            System.out.println("Result: " + moveResult);

            if (moveResult.equals(MOVED)) {

                lastSuccessfulMove = getDirectionFromIndex(directionIndex);
                failedDirections.clear();
                return gameId;
            }

            if (moveResult.equals(BLOCKED)) {
                System.out.println("Direction blocked, try next direction.");
                continue;
            }

            if (moveResult.equals(FAILED)) {
                System.out.println("Move Failed. Create new game and go to last valid status");

                failedDirections.add(directionIndex);

                gameId = startGame().getGameId();

                moveSuccessfulOldMoves(gameId, oldGameId);

                lastSuccessfulMove = getLastMoveDirection(gameId);

                return tryAllDirectionsExceptOppositeAndFailed(gameId, lastDirection, oldGameId);
            }
        }
        return gameId;
    }

    private int getOppositeDirection(DirectionDto direction) {
        switch (direction) {
            case UP: return 2;
            case RIGHT: return 3;
            case DOWN: return 0;
            case LEFT: return 1;
            default: return -1;
        }
    }

    GameDto startGame() {
        GameInputDto gameInput = new GameInputDto();
        gameInput.setGroupName("Niklas Neuweiler, Nikolas Ernst");

        GameDto result = defaultApi.gamePost(gameInput);
        System.out.println("New game started with ID: " + result.getGameId());

        return result;
    }

    MoveStatusDto move(BigDecimal gameID, int directionIndex) {
        DirectionDto direction = getDirectionFromIndex(directionIndex);

        MoveInputDto moveInput = new MoveInputDto();
        moveInput.direction(direction);

        MoveDto moveOutput = defaultApi.gameGameIdMovePost(gameID, moveInput);

        return moveOutput.getMoveStatus();
    }

    void moveSuccessfulOldMoves(BigDecimal newGameId, BigDecimal oldGameId) {
        List<MoveDto> oldMoves = getMoveHistory(oldGameId);

        if (oldMoves.isEmpty() || oldMoves.size() == 1) {
            return;
        }

        for (MoveDto move : oldMoves) {
            if (move.getMoveStatus().equals(MoveStatusDto.MOVED)) {

                DirectionDto direction = move.getDirection();
                MoveInputDto moveInput = new MoveInputDto();
                moveInput.direction(direction);

                MoveDto track = defaultApi.gameGameIdMovePost(newGameId, moveInput);

                if (track.getMoveStatus().equals(MoveStatusDto.MOVED)) {
                    lastSuccessfulMove = direction;
                }
            }
        }
    }

    boolean gameErfolg(BigDecimal gameId) {
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