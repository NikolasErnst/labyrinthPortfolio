package com.example.SEPortfolioLabyrinth.util;

import org.openapitools.client.model.DirectionDto;

public class DirectionHelper {
    public static DirectionDto getDirectionFromIndex(int index) {
        switch(index) {
            case 0: return DirectionDto.UP;
            case 1: return DirectionDto.RIGHT;
            case 2: return DirectionDto.DOWN;
            case 3: return DirectionDto.LEFT;
            default: return null;
        }
    }

    public static int getOppositeDirection(DirectionDto direction) {
        switch (direction) {
            case UP: return 2;
            case RIGHT: return 3;
            case DOWN: return 0;
            case LEFT: return 1;
            default: return -1;
        }
    }
}
