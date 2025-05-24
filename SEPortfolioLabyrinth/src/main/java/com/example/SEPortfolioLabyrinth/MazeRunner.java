package com.example.SEPortfolioLabyrinth;
import org.openapitools.client.api.DefaultApi;
import org.openapitools.client.model.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.openapitools.client.model.MoveStatusDto.*;

@Component
public class MazeRunner {

    final DefaultApi defaultApi = new DefaultApi();

    //@Autowired
    //Direction direction;

    void solveMaze() {

        GameDto game = startGame();
        BigDecimal gameId = game.getGameId();

        List<Integer> moveHistory = new ArrayList<>();

       while (!gameErfolg(gameId)) {

           int[] directionIndex = {0, 1, 2, 3};
           MoveStatusDto currentMove = move(gameId,directionIndex);

           if(currentMove.equals(MOVED)){
                directionIndex = 0;
           }

           if(currentMove.equals(BLOCKED)){
               move(gameId, ++directionIndex);
           }

           if(currentMove.equals(FAILED)){
               BigDecimal oldGameId = gameId;

               gameId = startGame().getGameId();

               moveSuccessfulOldMoves(gameId,oldGameId);
               move(gameId, ++directionIndex);
           }
       }
    }

    GameDto startGame() {
        GameInputDto gameInput = new GameInputDto();
        gameInput.setGroupName("Niklas Neuweiler, Nikolas Ernst");

        GameDto result = defaultApi.gamePost(gameInput);

        return result;
    }

    MoveStatusDto move(BigDecimal gameID, int directionIndex) {

        DirectionDto direction = null;

        switch(directionIndex){
            case 0:
                direction = direction.UP;
                break;
            case 1:
                direction = direction.RIGHT;
                break;
            case 2:
                direction = direction.DOWN;
                break;
            case 3:
                direction = direction.LEFT;
                break;
        }

        MoveInputDto moveInput = new MoveInputDto();
        moveInput.direction(direction);

        MoveDto moveOutput = defaultApi.gameGameIdMovePost(gameID, moveInput);

        System.out.println(moveOutput.getMoveStatus());

        return moveOutput.getMoveStatus();
    }

    void moveSuccessfulOldMoves(BigDecimal newGameId, BigDecimal oldGameId) {

        List<MoveDto> oldMoves = getMoveHistory(oldGameId);

        if(oldMoves.isEmpty() || oldMoves.size() == 1){
            return;
        }

        for (MoveDto move : oldMoves) {
            if(move.getMoveStatus().equals(MOVED)){
                MoveInputDto moveInput = new MoveInputDto();
                moveInput.direction(move.getDirection());

                defaultApi.gameGameIdMovePost(newGameId, moveInput);
            }
        }
    }

    boolean gameErfolg(BigDecimal gameId ) {
        GameDto currentGame = defaultApi.gameGameIdGet(gameId);
        return (currentGame.getStatus() == GameStatusDto.SUCCESS);
    }

    public List<MoveDto> getMoveHistory(BigDecimal oldGameId) {
        if (oldGameId != null) {
            return defaultApi.gameGameIdMoveGet(oldGameId);
        }
        return new ArrayList<>();
    }
}