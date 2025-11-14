package com.ramolete.betterdialog;

import com.ramolete.betterdialog.rpgbattle.FinalBattleSystem;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.geometry.Pos;
import java.util.List;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import javafx.animation.FadeTransition;
//import javafx.scene.media.MediaPlayer;
//import javafx.scene.media.MediaPlayer;
//import java.io.File;

public class DecisionGame extends Application {

    // --- STATE & CONTROLLERS ---
    private final GlobalController controller = GlobalController.getInstance();
    private Guest currentGuest;
    private int currentLineIndex = 0;
    private List<String> activeDialogueList;

    // ==========================================================
    // AESTHETICS: STYLE CONSTANTS
    // ==========================================================
    private static final String FONT_FAMILY = "Arial";

    // Dialogue Box and Name Tag Styles
    private static final String NAME_BOX_STYLE =
            "-fx-background-color: #2e2e2e; -fx-text-fill: #ffffff; -fx-padding: 3 15 3 15; -fx-background-radius: 15px; -fx-border-radius: 15px;";
    private static final String DIALOGUE_PANE_STYLE =
            "-fx-background-color: #000000; -fx-border-color: #ffffff; -fx-border-width: 6px; -fx-background-radius: 15px; -fx-border-radius: 10px;";

    // Button Base Style (Neon Green)
    private static final String BASE_BUTTON_STYLE =
            // *** CHANGE HERE: Added semi-transparent black background ***
            "-fx-background-color: rgba(0, 0, 0, 0.2);" +
                    // -----------------------------------------------------------
                    "-fx-text-fill: #00ff00; -fx-border-color: #00ff00; -fx-font-size: 20px; -fx-font-family: '" + FONT_FAMILY + "', sans-serif;" +
                    "-fx-background-radius: 30px; -fx-border-radius: 30px; -fx-padding: 15px 30px; -fx-border-width: 3px;" + // Removed the old "transparent" background
                    "-fx-effect: dropshadow(gaussian, #00ff00, 10, 0.0, 0, 0);";

    // Button Hover Style (Inverted/Filled Neon Green)
    // We update this to start with the same base black background, then overwrite the background-color with the neon fill.
    private static final String HOVER_FILL_STYLE =
            BASE_BUTTON_STYLE.replace("-fx-text-fill: #00ff00", "-fx-text-fill: #000000") // Change text color to black
                    // *** CHANGE HERE: Ensure the fill is completely opaque green when hovered ***
                    + " -fx-background-color: #00ff00; -fx-border-color: #00ff00;";

    // --- Core UI Components (References to the styled containers) ---
    private VBox dialogueVBox = createDialogueBox();; // The full, styled VN box container (created in createDialogueBox)
    private VBox decisionVBox = new VBox(20.0F); // The full, styled decision box container (for buttons)
    private Label dialogueLabel; // Reference to the Label INSIDE dialogueVBox
    private Label guestNameLabel; // Reference to the Label INSIDE dialogueVBox

    // --- Scene Elements ---
    public static Stage mainStage;
    private Scene mainMenuScene;
    private final StackPane root = new StackPane();
    private final Label doorSceneMessage = new Label();
    private final ImageView guestImageView = new ImageView();
    private final ImageView doorBackgroundView = new ImageView();

    // --- SCENE CONSTANTS ---
    private static final double SCENE_WIDTH = 1280.0;
    private static final double SCENE_HEIGHT = 720.0;

    @Override
    public void start(Stage primaryStage) {
        mainStage = primaryStage;

        // === 1️⃣ CREATE MAIN MENU ===
        AnchorPane startRoot = new AnchorPane();
        startRoot.setStyle("-fx-background-color: #f0f0f0;");

        Button newGameBtn = new Button("NEW GAME");
        Button continueBtn = new Button("CONTINUE");
        Button optionsBtn = new Button("OPTIONS");

        String btnStyle = "-fx-background-color: black; " +
                "-fx-text-fill: white; " +
                "-fx-font-family: 'Consolas'; " +
                "-fx-font-size: 16px; " +
                "-fx-border-color: white; " +
                "-fx-border-width: 2px; " +
                "-fx-padding: 8 16 8 16;";
        newGameBtn.setStyle(btnStyle);
        continueBtn.setStyle(btnStyle);
        optionsBtn.setStyle(btnStyle);

        HBox buttonBox = new HBox(30, newGameBtn, continueBtn, optionsBtn);
        buttonBox.setAlignment(Pos.CENTER);

        // Use AnchorPane to keep buttons at bottom
        startRoot.getChildren().add(buttonBox);
        AnchorPane.setBottomAnchor(buttonBox, 80.0);
        AnchorPane.setLeftAnchor(buttonBox, 0.0);
        AnchorPane.setRightAnchor(buttonBox, 0.0);

        optionsPane = createOptionsPane(mainStage);
        startRoot.getChildren().add(optionsPane);

        startRoot.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                if (optionsPane != null && optionsPane.isVisible()) {
                    optionsPane.setVisible(false); // Hide options
                } else if (pausePane != null) {
                    pausePane.setVisible(!pausePane.isVisible()); // Toggle pause
                }
            }
        });

        mainMenuScene = new Scene(startRoot, SCENE_WIDTH, SCENE_HEIGHT);
        mainStage.setTitle("Main Menu");
        mainStage.setScene(mainMenuScene);
        mainStage.show();

        // --- NEW GAME BUTTON ---
        newGameBtn.setOnAction(e -> setupGameScene());

        // --- CONTINUE / OPTIONS PLACEHOLDERS ---
        continueBtn.setOnAction(e -> System.out.println("Continue clicked - placeholder"));
        optionsBtn.setOnAction(e -> optionsPane.setVisible(!optionsPane.isVisible()));
    }

    // === GAME SCENE SETUP ===
    private void setupGameScene() {
        // Clear previous children
        root.getChildren().clear();

        // Initialize dialogue box if needed
        if (dialogueVBox.getChildren().isEmpty()) {
            dialogueVBox = createDialogueBox();
        }

        // Ensure decisionVBox uses same instance
        decisionVBox.getChildren().clear();
        decisionVBox.setAlignment(Pos.TOP_RIGHT);
        decisionVBox.setPadding(new Insets(50, 50, 0, 0));

        // Add all elements to the root StackPane
        root.getChildren().addAll(doorBackgroundView, guestImageView, dialogueVBox, decisionVBox, doorSceneMessage);

        // Configure background image sizing
        doorBackgroundView.fitWidthProperty().bind(root.widthProperty());
        doorBackgroundView.fitHeightProperty().bind(root.heightProperty());
        doorBackgroundView.setPreserveRatio(false);

        // Position dialogue at bottom center
        StackPane.setAlignment(dialogueVBox, Pos.BOTTOM_CENTER);

        // Position decision buttons at top-right
        StackPane.setAlignment(decisionVBox, Pos.TOP_RIGHT);

        // Start game flow
        controller.advanceToNextGuest();
        showDoorScene();

        // Create pause pane only once
        if (pausePane == null) {
            pausePane = createPausePane();
            root.getChildren().add(pausePane);
        }

        optionsPane = createOptionsPane(mainStage);
        root.getChildren().add(optionsPane);

        // Fullscreen toggle
        Scene gameScene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
        gameScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.F11) {
                boolean newState = controller.toggleFullscreen();
                mainStage.setFullScreen(newState);
                System.out.println("Fullscreen Toggled: " + newState);
                event.consume();
            } else if (event.getCode() == KeyCode.ESCAPE) {
                if (pausePane != null) {
                    pausePane.setVisible(!pausePane.isVisible());
                }
            }
        });
        gameScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                if (optionsPane != null && optionsPane.isVisible()) {
                    optionsPane.setVisible(false); // Hide options
                } else if (pausePane != null) {
                    pausePane.setVisible(!pausePane.isVisible()); // Toggle pause
                }
            }
        });

        mainStage.setScene(gameScene);
        mainStage.show();
    }

    // ==========================================================
    // SCENE MANAGEMENT & TRANSITIONS
    // ==========================================================

    private void showDoorScene() {
        // --- VISUALS: Door Scene Setup (Consistent between phases) ---
        doorSceneMessage.setVisible(false);
        decisionVBox.setVisible(false);
        guestImageView.setVisible(false);
        guestNameLabel.setVisible(false);

        // --- Door ---
        doorBackgroundView.setVisible(true);
        Image doorImage = new Image(DecisionGame.class.getResourceAsStream("/images/dg_door.png"));
        doorBackgroundView.setImage(doorImage);

        String imagePath = DecisionGame.class.getResource("/images/dg_door.png").toExternalForm();
        root.setStyle("-fx-background-image: url('" + imagePath + "'); " +
                "-fx-background-size: cover;");

        // --- LOGIC ---
        String currentMessage = controller.getLastEventMessage();

        // Check if a consequence message needs to be shown (i.e., this is NOT a fresh knock scene)
        boolean consequencePending = (currentMessage != null && !currentMessage.equals("Someone is at the door..."));

        if (consequencePending) {
            // --- PHASE 1: SHOWING CONSEQUENCE MESSAGE ---
            dialogueVBox.setVisible(true);
            dialogueLabel.setText(currentMessage);

            // Clicks will clear the message and reload the scene to start the knock sequence.
            root.setOnMouseClicked(e -> {
                // Clear the consequence state
                controller.setLastEventMessage("Someone is at the door...");
                //controller.setCurrentGuest(null);
                showDoorScene(); // Recursively call to transition to the knock sequence
            });

        } else {
            // --- PHASE 2: KNOCK/TRANSITION SEQUENCE ---

            // 1. Hide the dialogue box first for the silent door view
            dialogueVBox.setVisible(false);
            root.setOnMouseClicked(null); // Disable clicks during the pause

            PauseTransition pause = new PauseTransition(Duration.seconds(2.5)); // Wait 2.5 seconds

            pause.setOnFinished(e -> {
                // ⭐ Play Knock Sound Effect HERE ⭐
                System.out.println("[SFX: KNOCK-KNOCK]"); // Placeholder for sound

                // 2. Display the 'Someone is at the door' message
                dialogueVBox.setVisible(true);
                dialogueLabel.setText(controller.getLastEventMessage());

                // 3. Re-enable clicks to start the dialogue flow
                root.setOnMouseClicked(clickEvent -> {
                    if (controller.isInDecision()) {
                        runFadeTransition(() -> {
                            guestNameLabel.setVisible(true);
                            showDoorEyeHole();
                        });
                    } else {
                        // Game Over or RPG Battle transition
                        root.setOnMouseClicked(null); // Stop input immediately
                        dialogueVBox.setVisible(false); // Hide dialogue box during fade

                        runFadeTransition(() -> {
                            // This is where you would call the RPG battle scene initializer.
                            // For now, we'll just show the final message clearly after the fade.
                            dialogueVBox.setVisible(true);
                            dialogueLabel.setText("All guests checked! Transitioning to RPG Battle!");

                            FinalBattleSystem battle = new FinalBattleSystem(mainStage, mainMenuScene);
                            battle.init(mainStage);
                        });
                    }
                });
            });

            pause.play();
        }
    }

    private void showDoorEyeHole() {
        currentGuest = controller.getCurrentGuest();

        // --- VISUALS: Eyehole Scene Setup ---
        doorSceneMessage.setVisible(false);
        dialogueVBox.setVisible(true);      // Show the entire styled dialogue box
        decisionVBox.setVisible(false);
        guestImageView.setVisible(true);

        // Set guest image
        guestImageView.setImage(currentGuest.getGuestImage());
        guestImageView.fitWidthProperty().bind(root.widthProperty());
        guestImageView.setPreserveRatio(true);
        guestNameLabel.setText(currentGuest.getName());

        // --- DIALOGUE START ---
        activeDialogueList = controller.getCurrentDialogueList(); // Phase 0 (Intro)
        currentLineIndex = 0;

        root.setOnMouseClicked(e -> advanceDialogue());

        advanceDialogue(); // Show the very first line
    }

    // ==========================================================
    // DIALOGUE CORE LOGIC
    // ==========================================================

    private void advanceDialogue() {
        if (activeDialogueList == null) return;

        // 1. Display next line
        if (currentLineIndex < activeDialogueList.size()) {
            dialogueLabel.setText(activeDialogueList.get(currentLineIndex));
            currentLineIndex++;

        } else {
            // 2. Out of lines in the current list
            root.setOnMouseClicked(null); // Stop advancing on click

            if (controller.isDialogueFinal()) {
                // The final Accept/Reject dialogue has finished. This click should not happen.
                // It means the consequence was already processed in the previous step.
                // This is a safety check.
                showDoorScene();

            } else if (controller.getDialoguePhase() < controller.getDialogChunks()) {
                // Current phase dialogue (Intro, Secondary, etc.) has finished.
                // Advance the phase state and show the next set of buttons.
                controller.advanceDialoguePhase();
                showPhaseButtons();

            } else {
                // The final dialogue list (Secondary_2 or Third_2) has finished.
                // It is time for the final Accept/Reject choice.
                showFinalChoiceButtons();
            }
        }
    }

    // ==========================================================
    // CHOICE / BUTTON LOGIC (Multi-Chunk Implementation)
    // ==========================================================

    private void showPhaseButtons() {
        // This handles intermediate choices for Chunks 2 and 3.

        decisionVBox.setVisible(true);

        final int phase = controller.getDialoguePhase();
        final int chunks = controller.getDialogChunks();

        if (phase >= chunks) {
            showFinalChoiceButtons();
            return;
        }

        final int startIndex = (phase - 1) * 2; // Phase 1 starts at index 0, Phase 2 starts at index 2

        // Note: GlobalController.advanceDialoguePhase() was called in advanceDialogue()
        // which moved us to phase 1 (secondary), phase 2 (third), etc.

        List<String> allChoices = controller.getCurrentGuest().getChoices();

        // Create Button 1 (Choice A)
        Button choiceA = createStyledButton(allChoices.get(startIndex));
        choiceA.setOnAction(e -> handlePhaseChoice(0));

        // Create Button 2 (Choice B)
        Button choiceB = createStyledButton(allChoices.get(startIndex + 1));
        choiceB.setOnAction(e -> handlePhaseChoice(1));

        // DecisionVBox (the container) now holds the actual choice buttons
        decisionVBox.getChildren().clear();
        decisionVBox.getChildren().addAll(choiceA, choiceB);
    }

    private void handlePhaseChoice(int choiceIndex) {
        decisionVBox.setVisible(false);

        final int nextPhase = controller.getDialoguePhase();

        // 1. Determine the next dialogue list based on phase and choice
        if (nextPhase == 1) {
            // Load Secondary_1 or Secondary_2
            activeDialogueList = (choiceIndex == 0)
                    ? controller.getCurrentGuest().getDialogSecondary_1()
                    : controller.getCurrentGuest().getDialogSecondary_2();

        } else if (nextPhase == 2) {
            // Load Third_1 or Third_2
            activeDialogueList = (choiceIndex == 0)
                    ? controller.getCurrentGuest().getDialogThird_1()
                    : controller.getCurrentGuest().getDialogThird_2();

        } else {
            // Fallback for safety
            showFinalChoiceButtons();
            return;
        }

        // 2. Restart dialogue reading
        currentLineIndex = 0;
        root.setOnMouseClicked(e -> advanceDialogue());
        advanceDialogue();
    }

    // --- Final Decision ---

    private void showFinalChoiceButtons() {
        // Displays the two final buttons which always lead to Accept or Reject consequence.

        // 1. Get the full list of choice strings from the current guest
        List<String> allChoices = controller.getCurrentGuest().getChoices();
        int size = allChoices.size();

        // 2. Dynamically get the button text
        // The second-to-last index is for the ACCEPT path
        String acceptText = allChoices.get(size - 2);
        // The last index is for the REJECT path
        String rejectText = allChoices.get(size - 1);

        Button acceptButton = createStyledButton(acceptText);
        Button rejectButton = createStyledButton(rejectText);

        acceptButton.setOnAction(e -> handleFinalChoice(true));
        rejectButton.setOnAction(e -> handleFinalChoice(false));

        decisionVBox.getChildren().clear();
        decisionVBox.getChildren().addAll(acceptButton, rejectButton);
        decisionVBox.setVisible(true);
    }

    private void handleFinalChoice(boolean accepted) {
        decisionVBox.setVisible(false);

        // 1. Get the final dialogue list from the Controller and set the flag
        activeDialogueList = controller.setFinalDialogue(accepted);
        currentLineIndex = 0;

        // 2. Set the click handler to advance the final lines and then trigger consequence
        root.setOnMouseClicked(e -> {
            if (currentLineIndex < activeDialogueList.size()) {
                // Still reading the final lines
                advanceDialogue();
            } else {
                // Final line is done, process the consequence and transition.
                root.setOnMouseClicked(null);
                if (accepted) {
                    controller.acceptCurrentGuest();
                } else {
                    controller.rejectCurrentGuest();
                }

                runFadeTransition(() -> {
                    showDoorScene();
                });
            }
        });

        // Display the first line of the final dialogue immediately
        advanceDialogue();
    }

    // ======
    // RC
    // ======

    private VBox createDialogueBox() {
        this.guestNameLabel = new Label();
        this.guestNameLabel.setStyle(NAME_BOX_STYLE);
        this.guestNameLabel.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 16.0F));
        this.guestNameLabel.setTextFill(Color.WHITE);

        // Name Container (The VBox that gives the name tag its bottom-left offset)
        VBox nameContainer = new VBox(this.guestNameLabel);
        nameContainer.setAlignment(Pos.BOTTOM_LEFT);
        nameContainer.setPadding(new Insets(0.0F, 0.0F, 8.0F, 40.0F));

        // --- FIX IS HERE: Initialize dialogueLabel BEFORE use ---
        this.dialogueLabel = new Label(); // <--- MOVE THIS LINE UP
        this.dialogueLabel.setWrapText(true);
        this.dialogueLabel.setFont(Font.font(FONT_FAMILY, 16.0F));
        this.dialogueLabel.setTextFill(Color.WHITE);
        this.dialogueLabel.setMaxHeight(Double.MAX_VALUE);
        this.dialogueLabel.setAlignment(Pos.CENTER_LEFT);

        // Dialogue Content (The black box that holds the text)
        VBox dialogueContent = new VBox(10.0F, this.dialogueLabel); // This now adds a non-null Label
        dialogueContent.setStyle(DIALOGUE_PANE_STYLE);
        dialogueContent.setMaxWidth(Double.MAX_VALUE);
        dialogueContent.setPadding(new Insets(60.0F, 50.0F, 60.0F, 50.0F));
        dialogueContent.setAlignment(Pos.CENTER_LEFT);

        VBox finalBox = new VBox(nameContainer, dialogueContent);

        finalBox.setMaxWidth(SCENE_WIDTH * 0.95);
        finalBox.setMaxHeight(SCENE_HEIGHT * 0.30);

        return finalBox;
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle(BASE_BUTTON_STYLE);

        button.setWrapText(true);

        // Set the fixed width from your friend's aesthetic
        button.setPrefWidth(280.0F);

        // Apply the HOVER logic using the pre-defined constants
        button.setOnMouseEntered((e) -> button.setStyle(HOVER_FILL_STYLE));
        button.setOnMouseExited((e) -> button.setStyle(BASE_BUTTON_STYLE));

        return button;
    }

    // =================================
    // FADE TRANSITION
    // =================================

    private void runFadeTransition(Runnable action) {
        // 1. Create a black overlay pane that covers the whole screen
        StackPane blackOverlay = new StackPane();
        blackOverlay.setStyle("-fx-background-color: black;");
        blackOverlay.setOpacity(0.0); // Start transparent

        // Add the overlay to the root. It must be on top of everything.
        root.getChildren().add(blackOverlay);

        // --- PHASE 1: FADE OUT (Screen goes black) ---
        FadeTransition fadeOut = new FadeTransition(Duration.millis(600), blackOverlay);
        fadeOut.setToValue(1.0); // Fully opaque black

        fadeOut.setOnFinished(e -> {
            // --- PHASE 2: Execute the scene change in the dark ---
            action.run();

            // --- PHASE 3: FADE IN (Screen returns from black) ---
            FadeTransition fadeIn = new FadeTransition(Duration.millis(600), blackOverlay);
            fadeIn.setToValue(0.0); // Fully transparent

            fadeIn.setOnFinished(f -> {
                // Cleanup: remove the temporary black pane from the scene
                root.getChildren().remove(blackOverlay);
            });

            fadeIn.play();
        });

        fadeOut.play();
    }

    // ===========================
    // PAUSE
    // ===========================

    // Class-level variable so it’s accessible anywhere
    private StackPane pausePane;

    private StackPane createPausePane() {
        // StackPane so it can overlay everything
        StackPane pausePane = new StackPane();
        pausePane.setStyle("-fx-background-color: black;"); // semi-transparent overlay
        pausePane.setVisible(false); // start hidden
        pausePane.setPrefSize(SCENE_WIDTH, SCENE_HEIGHT);

        // --- Set Style ---
        String BUTTON_STYLE = "-fx-background-color: black; " +
                "-fx-text-fill: white; " +
                "-fx-font-family: 'Consolas'; " +
                "-fx-font-size: 16px; " +
                "-fx-border-color: white; " +
                "-fx-border-width: 2px; " +
                "-fx-padding: 8 16 8 16;";

        String BARRIER_STYLE = "-fx-background-color: black;" +
                "-fx-background-radius: 10;" +
                "-fx-padding: 10;";

        // --- Inner VBox for your custom pause UI ---
        VBox pauseUI = new VBox(20);
        pauseUI.setAlignment(Pos.BOTTOM_CENTER);
        pauseUI.setPadding(new Insets(0, 0, 50, 0));

        // Title
        Label titleLabel = new Label("Hello Miguel!");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        titleLabel.setTextFill(Color.WHITE);

        // Buttons
        Button saveBtn = new Button("SAVE");
        Button loadBtn = new Button("LOAD");
        Button optionsBtn = new Button("OPTIONS");
        Button quitBtn = new Button("QUIT");

        double fixedButtonWidth = 150;
        saveBtn.setStyle(BUTTON_STYLE);
        saveBtn.setPrefWidth(fixedButtonWidth);
        saveBtn.setOnAction(e -> System.out.println("Save clicked!"));

        loadBtn.setStyle(BUTTON_STYLE);
        loadBtn.setPrefWidth(fixedButtonWidth);
        loadBtn.setOnAction(e -> System.out.println("Load clicked!"));

        optionsBtn.setStyle(BUTTON_STYLE);
        optionsBtn.setPrefWidth(fixedButtonWidth);
        optionsBtn.setOnAction(e -> optionsPane.setVisible(!optionsPane.isVisible()));

        quitBtn.setStyle(BUTTON_STYLE);
        quitBtn.setPrefWidth(fixedButtonWidth);
        quitBtn.setOnAction(e -> Platform.exit()); // Quit closes the game

        // Button container
        VBox buttonContainer = new VBox(15, saveBtn, loadBtn, optionsBtn, quitBtn);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setPadding(new Insets(15));
        buttonContainer.setStyle(BARRIER_STYLE); // optional border/background style

        // Center wrapper for layout
        HBox centerWrapper = new HBox(buttonContainer);
        centerWrapper.setAlignment(Pos.CENTER);

        pauseUI.getChildren().addAll(titleLabel, centerWrapper);

        // Add your VBox pause UI to the StackPane overlay
        pausePane.getChildren().add(pauseUI);

        return pausePane;
    }

    // ===========================
    // OPTIONS
    // ===========================

    private VBox optionsPane;

    private VBox createOptionsPane(Stage primaryStage) {
        VBox root = new VBox(40);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: black;");
        root.setPadding(new Insets(50));

        // --- Title ---
        Label titleLabel = new Label("Options");
        titleLabel.setFont(Font.font("Consolas", FontWeight.BOLD, 36));
        titleLabel.setTextFill(Color.WHITE);

        // --- Settings Grid ---
        GridPane settingsGrid = new GridPane();
        settingsGrid.setAlignment(Pos.CENTER);
        settingsGrid.setHgap(50);
        settingsGrid.setVgap(30);

        String labelStyle = "-fx-font-family: 'Consolas'; -fx-font-size: 20px; -fx-text-fill: white;";
        String ARROW_BUTTON_STYLE = "-fx-background-color: black; " +
                "-fx-text-fill: white; " +
                "-fx-font-family: 'Consolas'; " +
                "-fx-font-size: 16px; " +
                "-fx-border-color: white; " +
                "-fx-border-width: 2px; " +
                "-fx-padding: 8 10 8 10;";

        // --- Fullscreen Toggle ---
        Label fullScreenLabel = new Label("Full Screen");
        fullScreenLabel.setStyle(labelStyle);

        CheckBox fullScreenToggle = new CheckBox();
        // Set initial state based on controller
        fullScreenToggle.setSelected(controller.isFullscreen());
        fullScreenToggle.setText(fullScreenToggle.isSelected() ? "ON" : "OFF");

        // Toggle fullscreen on change
        fullScreenToggle.selectedProperty().addListener((obs, oldVal, newVal) -> {
            fullScreenToggle.setText(newVal ? "ON" : "OFF");
            controller.toggleFullscreen();         // flips controller state
            primaryStage.setFullScreen(newVal);    // applies fullscreen
        });

        settingsGrid.add(fullScreenLabel, 0, 0);
        settingsGrid.add(fullScreenToggle, 1, 0);

        // --- Volume Controls ---
        String[] volumeNames = {"Master Volume", "BGM Volume", "SFX Volume"};
        int[] volumeValues = {100, 80, 80};

        for (int i = 0; i < volumeNames.length; i++) {
            int index = i;
            Label nameLabel = new Label(volumeNames[i]);
            nameLabel.setStyle(labelStyle);

            Label percentLabel = new Label(volumeValues[i] + "%");
            percentLabel.setStyle(labelStyle);
            percentLabel.setMinWidth(65);
            percentLabel.setAlignment(Pos.CENTER);

            Button leftBtn = new Button("<");
            leftBtn.setStyle(ARROW_BUTTON_STYLE);
            leftBtn.setOnAction(e -> {
                volumeValues[index] = Math.max(0, volumeValues[index] - 10);
                percentLabel.setText(volumeValues[index] + "%");
            });

            Button rightBtn = new Button(">");
            rightBtn.setStyle(ARROW_BUTTON_STYLE);
            rightBtn.setOnAction(e -> {
                volumeValues[index] = Math.min(100, volumeValues[index] + 10);
                percentLabel.setText(volumeValues[index] + "%");
            });

            HBox controlBox = new HBox(5, leftBtn, percentLabel, rightBtn);
            controlBox.setAlignment(Pos.CENTER_LEFT);
            settingsGrid.add(nameLabel, 0, i + 1);
            settingsGrid.add(controlBox, 1, i + 1);
        }

        root.getChildren().addAll(titleLabel, settingsGrid);

        // Hidden by default
        root.setVisible(false);

        return root;
    }


}