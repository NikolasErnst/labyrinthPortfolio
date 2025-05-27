package com.example.SEPortfolioLabyrinth.core;

import lombok.Getter;
import lombok.Setter;
import org.openapitools.client.model.DirectionDto;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Getter
@Component
public class MazeStateManager {
    private Set<Integer> failedDirections = new HashSet<>();
    private DirectionDto lastSuccessfulMove = null;

    public Set<Integer> getFailedDirections() {
        return failedDirections;
    }

    public DirectionDto getLastSuccessfulMove() {
        return lastSuccessfulMove;
    }

    public void setLastSuccessfulMove(DirectionDto lastSuccessfulMove) {
        this.lastSuccessfulMove = lastSuccessfulMove;
    }

    public void addFailedDirection(int direction) {
        failedDirections.add(direction);
    }

    public void clearFailedDirections() {
        failedDirections.clear();
    }
}
