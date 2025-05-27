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
    @Setter
    private DirectionDto lastSuccessfulMove = null;

    public void addFailedDirection(int direction) {
        failedDirections.add(direction);
    }

    public void clearFailedDirections() {
        failedDirections.clear();
    }

}
