package com.jkpr.chinesecheckers.client.gui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
/**
  * The {@code LoadingScreen} class represents the loading screen that is displayed when the player is waiting for other players to join the game.
  */
public class LoadingScreen extends VBox {
    private final Label loadingLabel;
    public LoadingScreen() {
        super();
        setAlignment(Pos.CENTER);
        setSpacing(10);
        loadingLabel = new Label("Oczekwianie na reszte graczy...");
        getChildren().add(loadingLabel);
    }

}
