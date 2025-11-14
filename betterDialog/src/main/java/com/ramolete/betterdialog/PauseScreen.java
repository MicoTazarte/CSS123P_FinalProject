package com.ramolete.betterdialog;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.List;

/**
 * Represents the full-screen Pause Menu, including the Menu buttons (Yellow)
 * and the Player Stats display (Blue).
 * * This panel is added directly to the main scene's root when the game is paused.
 */
public class PauseScreen extends StackPane {

    private final GlobalController controller = GlobalController.getInstance();
    private static final String FONT_FAMILY = "monospaced"; // Using a pixel-art friendly font
    private static final String BORDER_COLOR = "#99004d"; // Dark magenta/red border from the image

    // References for dynamic updates (though initially static for a pause screen)
    private Label partySizeLabel;
    private VBox statsDisplayVBox;
    private ImageView playerImageView;
    private Label playerNameLabel;
    private Label playerLevelLabel;

    public PauseScreen() {
        // --- 1. RED Panel (Full Screen Background) ---
        // The background is solid black, filling the 1280x720 area.
        this.setStyle("-fx-background-color: black;");
        this.setPrefSize(1280, 720);

        // The entire content layout is centered
        HBox mainLayout = new HBox(20);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(50)); // Padding around the edges

        // --- 2. Build the YELLOW Panel (Menu Buttons) ---
        VBox menuVBox = createMenuPanel();

        // --- 3. Build the BLUE Panel (Stats & Info) ---
        VBox statsVBox = createStatsPanel();

        // Add panels to the main layout
        mainLayout.getChildren().addAll(menuVBox, statsVBox);
        this.getChildren().add(mainLayout);

        // Initial data load
        updateStatsDisplay();
    }

    /**
     * Creates the menu panel (Yellow section in the diagram).
     */
    private VBox createMenuPanel() {
        // Yellow Panel Container: Bordered VBox
        VBox menuPanel = new VBox(5); // Spacing between buttons
        menuPanel.setPrefWidth(200);
        menuPanel.setPadding(new Insets(10, 5, 10, 10)); // Top, Right, Bottom, Left
        menuPanel.setStyle(
                "-fx-background-color: #2b2b2b;" + // Dark internal background
                        "-fx-border-color: " + BORDER_COLOR + ";" +
                        "-fx-border-width: 3px;" +
                        "-fx-border-style: solid;");

        // --- Buttons (SAVE, LOAD, QUIT) ---
        Button saveBtn = createMenuButton("SAVE");
        Button loadBtn = createMenuButton("LOAD");
        // We will repurpose the 'Effect' button from the image to 'QUIT'
        Button quitBtn = createMenuButton("QUIT GAME");

        // Placeholder actions - these will be implemented later in DecisionGame
        saveBtn.setOnAction(e -> System.out.println("Save functionality placeholder."));
        loadBtn.setOnAction(e -> System.out.println("Load functionality placeholder."));
        quitBtn.setOnAction(e -> System.out.println("Quit functionality placeholder."));

        menuPanel.getChildren().addAll(saveBtn, loadBtn, quitBtn);
        return menuPanel;
    }

    /**
     * Creates a styled button for the menu panel.
     */
    private Button createMenuButton(String text) {
        Button button = new Button(text);
        button.setPrefWidth(Double.MAX_VALUE);
        // Pixel-art font style matching the image
        button.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-family: '" + FONT_FAMILY + "';" +
                        "-fx-font-size: 16px;" +
                        "-fx-background-color: transparent;" +
                        "-fx-padding: 5 10 5 10;");

        // Hover effect for interaction feedback
        button.setOnMouseEntered(e -> button.setStyle(button.getStyle() + "-fx-background-color: " + BORDER_COLOR + ";"));
        button.setOnMouseExited(e -> button.setStyle(button.getStyle().replace("-fx-background-color: " + BORDER_COLOR + ";", "-fx-background-color: transparent;")));

        return button;
    }

    /**
     * Creates the stats panel (Blue section in the diagram).
     */
    private VBox createStatsPanel() {
        // Blue Panel Container: Large Bordered VBox
        VBox statsPanel = new VBox(20);
        statsPanel.setPrefSize(700, 600);
        statsPanel.setPadding(new Insets(15));
        statsPanel.setStyle(
                "-fx-background-color: #000000;" + // True black internal background
                        "-fx-border-color: " + BORDER_COLOR + ";" +
                        "-fx-border-width: 3px;" +
                        "-fx-border-style: solid;");

        // --- Top Header: Character Image, Name, Level ---
        HBox header = new HBox(20);
        header.setAlignment(Pos.TOP_LEFT);

        // Placeholder for Player Image (Using a small, bordered box similar to the image)
        // We assume the player character image is available at this path.
        String imageUrl = DecisionGame.class.getResource("/images/player_icon.png").toExternalForm();
        playerImageView = new ImageView();
        try {
            playerImageView.setImage(new Image(imageUrl));
        } catch (Exception e) {
            // Placeholder if image doesn't load
            System.err.println("Player image not found for pause screen.");
        }
        playerImageView.setFitWidth(64);
        playerImageView.setFitHeight(64);
        playerImageView.setStyle("-fx-border-color: " + BORDER_COLOR + "; -fx-border-width: 3px; -fx-background-color: #333333;");

        // Name and Level container
        VBox nameLevelVBox = new VBox(5);
        playerNameLabel = createStatLabel("Mado-chan", true); // True for bold
        playerLevelLabel = createStatLabel("E 1", false);
        nameLevelVBox.getChildren().addAll(playerNameLabel, playerLevelLabel);

        header.getChildren().addAll(playerImageView, nameLevelVBox);

        // --- Stats Area (HP, Attack, etc. - based on image) ---
        statsDisplayVBox = new VBox(10);
        statsDisplayVBox.setPadding(new Insets(20, 0, 0, 0));

        // Initial setup for the stats display
        statsPanel.getChildren().addAll(header, statsDisplayVBox);
        return statsPanel;
    }

    /**
     * Creates a styled label for displaying stats.
     */
    private Label createStatLabel(String text, boolean isBold) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: white;" + "-fx-font-family: '" + FONT_FAMILY + "';" + "-fx-font-size: 18px;");
        if (isBold) {
            label.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 18));
        }
        return label;
    }

    /**
     * Fetches current data from the GlobalController and updates the stats panel.
     */
    public void updateStatsDisplay() {
        // *** STATS ARE DYNAMICALLY UPDATED HERE ***

        statsDisplayVBox.getChildren().clear();

        // 1. Placeholder Player Name/Level (We'll update GlobalController for real data later)
        // Since we don't have a Player class yet, we use fixed text.
        playerNameLabel.setText("Decision Maker");
        playerLevelLabel.setText("Lvl: " + controller.getGuestsInParty().size());

        // 2. Dynamic Guest/Party Count
        partySizeLabel = createStatLabel("Party Members: " + controller.getGuestsInParty().size(), false);

        // 3. Dynamic Present/Inventory Display
        List<String> presents = controller.getPresentList();
        Label inventoryLabel = createStatLabel("Inventory:", true);

        VBox inventoryVBox = new VBox(5);
        if (presents.isEmpty()) {
            inventoryVBox.getChildren().add(createStatLabel(" (No gifts yet)", false));
        } else {
            for (String present : presents) {
                // Display the gifts collected (presents)
                inventoryVBox.getChildren().add(createStatLabel(" - " + present, false));
            }
        }

        // Add all sections to the display
        statsDisplayVBox.getChildren().addAll(partySizeLabel, inventoryLabel, inventoryVBox);
    }

    // You'll need a method to set the callback for the buttons if they need to interact
    // directly with the DecisionGame class (e.g., to unpause or quit).
    // For now, they print to the console.
}
