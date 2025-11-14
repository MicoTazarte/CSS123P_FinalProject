package com.ramolete.betterdialog.rpgbattle;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class LoseScreen {

    public static Parent create() {

        VBox root = new VBox(30);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: black;");

        Label title = new Label("GAME OVER");
        title.setStyle("-fx-text-fill: red; -fx-font-size: 48px;");

        Label msg = new Label("Miguel has defeated you...");
        msg.setStyle("-fx-text-fill: white; -fx-font-size: 22px;");

        Button retryBtn = new Button("Retry");
        retryBtn.setOnAction(e -> {
            // TODO: Start battle again
        });

        Button backBtn = new Button("Return to Menu");
        backBtn.setOnAction(e -> {
            // TODO: Load menu
        });

        root.getChildren().addAll(title, msg, retryBtn, backBtn);

        return root;
    }
}

