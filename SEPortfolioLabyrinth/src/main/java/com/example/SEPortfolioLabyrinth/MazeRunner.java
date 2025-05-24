package com.example.SEPortfolioLabyrinth;
import org.openapitools.client.api.DefaultApi;
import org.openapitools.client.model.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.openapitools.client.model.MoveStatusDto.BLOCKED;
import static org.openapitools.client.model.MoveStatusDto.FAILED;

@Component
public class MazeRunner {

    final DefaultApi defaultApi = new DefaultApi();


    //@Autowired
    //Direction direction;

    void solveMaze() {
        int directionIndex;

        GameDto game = startGame();
        BigDecimal gameId = game.getGameId();

       while (!gameErfolg(gameId)) {
           directionIndex = 0;
           MoveStatusDto prevMove = move(gameId,directionIndex);

           if(prevMove.equals(BLOCKED)){
               move(gameId, ++directionIndex);
           }

           if(prevMove.equals(FAILED)){
               BigDecimal oldGameId = gameId;

               gameId = startGame().getGameId();

               moveSuccessfulOldMoves(oldGameId);
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

    void moveSuccessfulOldMoves(BigDecimal oldGameId) {

    }

    boolean getMoveErfolg(){
        return false;
    }

    boolean gameErfolg(BigDecimal gameId ) {
        GameDto currentGame = defaultApi.gameGameIdGet(gameId);
        return (currentGame.getStatus() == GameStatusDto.SUCCESS);
    }

    public List<MoveDto> getMoveHistory(BigDecimal gameId) {
        if (gameId != null) {
            return defaultApi.gameGameIdMoveGet(gameId);
        }
        return new ArrayList<>();
    }
}