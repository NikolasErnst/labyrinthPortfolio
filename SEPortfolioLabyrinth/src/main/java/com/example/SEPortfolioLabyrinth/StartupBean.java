package com.example.SEPortfolioLabyrinth;

import jakarta.annotation.PostConstruct;
import org.openapitools.client.api.DefaultApi;
import org.openapitools.client.model.GameDto;
import org.openapitools.client.model.GameInputDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StartupBean {

    @Autowired
    MazeRunner mazeRunner;

    @PostConstruct
    public void init() {
        mazeRunner.solveMaze();
    }
}
