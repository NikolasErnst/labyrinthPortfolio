package com.example.SEPortfolioLabyrinth;
import org.openapitools.client.api.DefaultApi;
import org.openapitools.client.model.GameDto;
import org.openapitools.client.model.GameInputDto;
import org.openapitools.client.model.GameStatusDto;
import org.openapitools.client.model.MoveDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

@Component
public class MazeRunner {

    //@Autowired
    //Direction direction;

    void solveMaze() {
        DefaultApi defaultApi = new DefaultApi();
        int directionIndex;

        startGame(defaultApi);
        BigDecimal gameId =BigDecimal.valueOf(0);

        //getMoveHistory(BigDecimal.valueOf(700),defaultApi);

       while (!gameErfolg(gameId,defaultApi)) {
           directionIndex = 0;
           move(directionIndex);

           if(!getMoveErfolg()){
               moveSuccessfulOldMoves();
               move(++directionIndex);
           }
       }
    }

    void startGame(DefaultApi defaultApi) {
        GameInputDto gameInput = new GameInputDto();
        gameInput.setGroupName("Niklas Neuweiler, Nikolas Ernst");

        GameDto result = defaultApi.gamePost(gameInput);
    }

    void move(int direction) {

    }

    void moveSuccessfulOldMoves() {
        //hole alle alten steps
        //falls alle alten steps nur 1 nichts
        // gehe alle alten steps -1
    }

    boolean getMoveErfolg(){
        return false;
    }

    boolean gameErfolg(BigDecimal gameId, DefaultApi defaultApi) {
        GameDto currentGame = defaultApi.gameGameIdGet(gameId);
        return (currentGame.getStatus() == GameStatusDto.SUCCESS);
    }

    public List<MoveDto> getMoveHistory(BigDecimal gameId, DefaultApi defaultApi) {
        if (gameId != null) {
            return defaultApi.gameGameIdMoveGet(gameId);
        }
        return new ArrayList<>();
    }
}