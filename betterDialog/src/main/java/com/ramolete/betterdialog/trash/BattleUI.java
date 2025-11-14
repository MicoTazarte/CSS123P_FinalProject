package com.ramolete.betterdialog.trash;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class BattleUI extends Application {

    // --- UI Component Fields ---
    // Top UI
    private TextArea btlLog;
    private ProgressBar enemyHP;
    private Text EHText;
    private ProgressBar enemySP;
    private Text ESText;

    // Battle Area
    private StackPane btlfield;
    private VBox itmBox;
    private ScrollPane itmScroll;
    private VBox itmList;
    private VBox actBox;

    // Bottom Controls
    private ProgressBar playerHP;
    private Text PHText;
    private ProgressBar playerSP;
    private Text PSText;
    private Button actBtn;
    private Button defBtn;
    private Button itmBtn;

    // --- Styling Constants ---
    private static final String TRANSPARENT_BTN_STYLE =
            "-fx-background-color: transparent;" +
                    "-fx-text-fill: white;" +
                    "-fx-border-color: #3f3f3f;" +
                    "-fx-border-width: 2px;" +
                    "-fx-border-radius: 3px;";

    private static final String MENU_BOX_STYLE =
            "-fx-background-color: #333333;" +
                    "-fx-border-color: #777777;" +
                    "-fx-border-width: 2px;" +
                    "-fx-padding: 10px;";

    // --- Application Entry Point ---

    @Override
    public void start(Stage primaryStage) {
        VBox root = createUI();

        // Define a reasonable size for the initial application window
        Scene scene = new Scene(root, 1280, 720);

        // Load custom fonts or apply base text styles here if needed.
        // For basic visibility, we'll set a default font style for all Text and Labels.
        String defaultTextStyle = "-fx-fill: white; -fx-font-size: 14px;";
        scene.getStylesheets().add(this.getClass().getResource("battle_ui.css").toExternalForm());

        primaryStage.setTitle("RPG Battle UI - JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    // --- UI Construction Methods ---

    /**
     * UI Construction Entry Point
     */
    private VBox createUI() {
        VBox root = new VBox();
        root.setSpacing(10);
        root.setStyle("-fx-background-color: black;");
        root.setPadding(new Insets(10));

        HBox topUi = createTopUi();
        StackPane battleArea = createBattleArea();
        VBox bottomControls = createBottomControls();

        // Ensure the battle area expands to fill the available vertical space
        VBox.setVgrow(battleArea, Priority.ALWAYS);

        root.getChildren().addAll(topUi, battleArea, bottomControls);
        return root;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Top UI Section: Log and Enemy Stats
     */
    private HBox createTopUi() {
        HBox topUi = new HBox();
        topUi.setAlignment(Pos.CENTER_LEFT);
        topUi.setSpacing(40);
        topUi.setPadding(new Insets(0, 0, 10, 0)); // Bottom margin

        // 1. Battle Log (btlLog)
        btlLog = new TextArea();
        btlLog.setEditable(false);
        btlLog.setPromptText("...");
        btlLog.setPrefHeight(100);
        btlLog.setFocusTraversable(false); // Prevent the log from taking focus
        btlLog.setStyle("-fx-control-inner-background: #1a1a1a; -fx-text-fill: #90ee90; -fx-font-family: 'Monospaced'; -fx-font-size: 12px; -fx-border-color: #3f3f3f; -fx-border-width: 1px;");
        HBox.setHgrow(btlLog, Priority.ALWAYS);

        // 2. Spacer Pane
        Pane spacer = new Pane();
        spacer.setPrefWidth(150);

        // 3. Enemy Stats VBox
        VBox enemyStats = new VBox();
        enemyStats.setAlignment(Pos.CENTER_RIGHT);
        enemyStats.setPadding(new Insets(5));
        enemyStats.setStyle("-fx-background-color: #1a1a1a; -fx-border-color: #555555; -fx-border-width: 1px;");

        Text enemyName = new Text("Miguel");
        enemyName.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        enemyName.setFill(Color.LIGHTBLUE);

        enemyHP = new ProgressBar(1.0);
        enemyHP.setPrefWidth(250);
        enemyHP.setStyle("-fx-accent: red;");
        EHText = new Text("HP: 100");
        EHText.setFill(Color.RED);

        enemySP = new ProgressBar(1.0);
        enemySP.setPrefWidth(250);
        enemySP.setStyle("-fx-accent: blue;");
        ESText = new Text("SP: 100");
        ESText.setFill(Color.BLUE);

        // Add a simple log message to see the log area
        btlLog.appendText("A wild Miguel appeared!");

        enemyStats.getChildren().addAll(enemyName, enemyHP, EHText, enemySP, ESText);
        topUi.getChildren().addAll(btlLog, spacer, enemyStats);
        return topUi;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Battle Area Section: Battlefield and Menus
     */
    private StackPane createBattleArea() {
        btlfield = new StackPane();
        btlfield.setAlignment(Pos.CENTER);
        btlfield.setStyle("-fx-background-color: #1e1e1e; -fx-border-color: #3f3f3f; -fx-border-width: 2px;");

        // Background Rectangle (Visual element in the StackPane)
        Rectangle backgroundRect = new Rectangle();
        backgroundRect.widthProperty().bind(btlfield.widthProperty());
        backgroundRect.heightProperty().bind(btlfield.heightProperty());
        backgroundRect.setFill(Color.web("#1e1e1e"));

        // Container for ItmBox and ActBox
        HBox menuContainer = new HBox();
        menuContainer.setSpacing(40);
        menuContainer.setAlignment(Pos.CENTER);
        menuContainer.setPadding(new Insets(10));

        // --- Item Box (itmBox) ---
        itmList = new VBox(); // Container for item buttons
        itmList.setSpacing(5);
        itmList.setStyle("-fx-background-color: #333333;");

        // Add a few placeholder items for visual context
        itmList.getChildren().addAll(
                new Button("Potion (x3)"),
                new Button("Mega Potion (x1)"),
                new Button("Elixir (x1)")
        );
        itmList.getChildren().forEach(node -> node.setStyle(TRANSPARENT_BTN_STYLE));


        itmScroll = new ScrollPane(itmList);
        itmScroll.setFitToWidth(true);
        itmScroll.setPrefHeight(150);
        itmScroll.setStyle("-fx-background-color: #333333; -fx-border-color: transparent;");

        itmBox = new VBox(); // The actual Item Menu box
        itmBox.setVisible(false); // Initially hidden
        itmBox.setAlignment(Pos.TOP_CENTER);
        itmBox.setPrefSize(200, 200);
        itmBox.setStyle(MENU_BOX_STYLE);
        Label itemLabel = new Label("Inventory");
        itemLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        itmBox.getChildren().addAll(itemLabel, itmScroll);

        // --- Action Box (actBox) ---
        actBox = new VBox(); // The Action Menu box
        actBox.setVisible(false); // Initially hidden
        actBox.setAlignment(Pos.TOP_CENTER);
        actBox.setPrefSize(200, 200);
        actBox.setSpacing(5);
        actBox.setStyle(MENU_BOX_STYLE);
        Label actionLabel = new Label("Choose Action");
        actionLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        // Default 'Basic Jab' button (Skills are added in initializeController)
        Button batkBtn = new Button("Basic Jab");
        batkBtn.setStyle(TRANSPARENT_BTN_STYLE);
        batkBtn.setPrefWidth(150);

        // Add a few placeholder skills for visual context
        Button skill1 = new Button("Fireball");
        skill1.setStyle(TRANSPARENT_BTN_STYLE);
        skill1.setPrefWidth(150);

        Button skill2 = new Button("Heal");
        skill2.setStyle(TRANSPARENT_BTN_STYLE);
        skill2.setPrefWidth(150);

        actBox.getChildren().addAll(actionLabel, batkBtn, skill1, skill2);

        menuContainer.getChildren().addAll(itmBox, actBox);

        // Add all elements to the battle area
        btlfield.getChildren().addAll(backgroundRect, menuContainer);

        return btlfield;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Bottom Controls Section: Player Stats and Main Buttons
     */
    private VBox createBottomControls() {
        VBox bottomVBox = new VBox();
        bottomVBox.setSpacing(10);

        HBox controlsHBox = new HBox();
        controlsHBox.setAlignment(Pos.CENTER);
        controlsHBox.setSpacing(50); // Increased spacing for better separation

        // 1. Player Stats VBox
        VBox playerStats = new VBox();
        playerStats.setAlignment(Pos.CENTER_LEFT);
        playerStats.setPadding(new Insets(5));
        playerStats.setStyle("-fx-background-color: #1a1a1a; -fx-border-color: #555555; -fx-border-width: 1px;");


        Text playerName = new Text("You");
        playerName.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        playerName.setFill(Color.LIGHTGREEN);

        playerHP = new ProgressBar(1.0);
        playerHP.setPrefWidth(250);
        playerHP.setStyle("-fx-accent: green;");
        PHText = new Text("HP: 100");
        PHText.setFill(Color.GREEN);

        playerSP = new ProgressBar(1.0);
        playerSP.setPrefWidth(250);
        playerSP.setStyle("-fx-accent: cyan;");
        PSText = new Text("SP: 100");
        PSText.setFill(Color.CYAN);

        playerStats.getChildren().addAll(playerName, playerHP, PHText, playerSP, PSText);

        // 2. Action Buttons VBox (for alignment)
        VBox actionButtonsVBox = new VBox();
        actionButtonsVBox.setAlignment(Pos.CENTER);
        actionButtonsVBox.setSpacing(10);
        actionButtonsVBox.setPadding(new Insets(10));

        String btnStyle = TRANSPARENT_BTN_STYLE + "-fx-border-width: 3px; -fx-border-radius: 5px; -fx-font-size: 16px;";

        actBtn = new Button("ACTION (Z)");
        actBtn.setStyle(btnStyle + "-fx-border-color: #3cb371;"); // MediumSeaGreen border
        actBtn.setPrefWidth(120);

        defBtn = new Button("DEFEND (X)");
        defBtn.setStyle(btnStyle + "-fx-border-color: #daa520;"); // Goldenrod border
        defBtn.setPrefWidth(120);

        itmBtn = new Button("ITEM (C)");
        itmBtn.setStyle(btnStyle + "-fx-border-color: #4682b4;"); // SteelBlue border
        itmBtn.setPrefWidth(120);

        actionButtonsVBox.getChildren().addAll(actBtn, defBtn, itmBtn);

        // 3. Spacer Pane (to push buttons towards the center)
        Pane bottomSpacer = new Pane();
        HBox.setHgrow(bottomSpacer, Priority.ALWAYS);

        controlsHBox.getChildren().addAll(playerStats, bottomSpacer, actionButtonsVBox);
        bottomVBox.getChildren().add(controlsHBox);
        return bottomVBox;
    }

}
