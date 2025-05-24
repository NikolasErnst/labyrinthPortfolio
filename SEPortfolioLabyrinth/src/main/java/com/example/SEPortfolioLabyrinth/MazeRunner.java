package com.example.SEPortfolioLabyrinth;
import org.openapitools.client.api.DefaultApi;
import org.openapitools.client.model.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class MazeRunner {

    final DefaultApi defaultApi = new DefaultApi();


    //@Autowired
    //Direction direction;

    void solveMaze() {
        int directionIndex;

        startGame();
        BigDecimal gameId =BigDecimal.valueOf(0);

        //getMoveHistory(BigDecimal.valueOf(700),defaultApi);

       while (!gameErfolg(gameId)) {
           directionIndex = 0;
           move(directionIndex);

           if(!getMoveErfolg()){
               moveSuccessfulOldMoves();
               move(++directionIndex);
           }
       }
    }

    void startGame() {
        GameInputDto gameInput = new GameInputDto();
        gameInput.setGroupName("Niklas Neuweiler, Nikolas Ernst");

        GameDto result = defaultApi.gamePost(gameInput);
    }

    boolean move(BigDecimal gameID, int directionIndex) {

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

        return moveOutput.getMoveStatus().equals(MoveStatusDto.MOVED);
    }

    void moveSuccessfulOldMoves() {
        //hole alle alten steps
        //falls alle alten steps nur 1 nichts
        // gehe alle alten steps -1
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