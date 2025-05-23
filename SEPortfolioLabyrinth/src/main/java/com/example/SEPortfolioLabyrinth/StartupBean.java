package com.example.SEPortfolioLabyrinth;

import jakarta.annotation.PostConstruct;
import org.openapitools.client.api.DefaultApi;
import org.openapitools.client.model.GameDto;
import org.openapitools.client.model.GameInputDto;

public class StartupBean {

    @PostConstruct
    public void init() {
        DefaultApi defaultApi = new DefaultApi();

        GameInputDto gameInput = new GameInputDto();
        gameInput.setGroupName("Nikolas Ernst");

        GameDto result = defaultApi.gamePost(gameInput);
        System.out.println("Neues Spiel gestartet: " + result);

    }}
