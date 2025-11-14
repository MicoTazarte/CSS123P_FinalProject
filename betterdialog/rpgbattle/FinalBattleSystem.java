package com.ramolete.betterdialog.rpgbattle;

import com.ramolete.betterdialog.GlobalController;
import javafx.animation.PauseTransition;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;

import java.util.*;

import com.ramolete.betterdialog.GlobalController;
import com.ramolete.betterdialog.rpgbattle.GiftTracker;
import com.ramolete.betterdialog.rpgbattle.Miguel;
import com.ramolete.betterdialog.rpgbattle.Player;
import javafx.util.Duration;

import static com.ramolete.betterdialog.DecisionGame.mainStage;

public class FinalBattleSystem {

    // ==========================================================
    // FIELDS
    // ==========================================================

    // --- Scenes ---
    private Stage stage;
    private Scene mainMenuScene;

    // --- Stat Units ---
    private final Player player;
    private final Miguel miguel;

    // --- Utility & Tracking ---
    private final GiftTracker tracker;
    private final GlobalController controller = GlobalController.getInstance();
    private final Random random = new Random();

    // --- Inventory / Action Menu ---
    private final List<String> itemInventory = new ArrayList<>();
    private final List<String> actionMenu = new ArrayList<>();

    // --- Player's Turn State ---
    private boolean isPlayerTurn = false;



    // --- Miguel's Move Pool Definitions (Constants) ---
    private static final List<String> NORMAL_ATTACK_MOVES = Arrays.asList("Strike");
    private static final List<String> ATTACK_ATTACK_MOVES = Arrays.asList("Strike", "Thrash", "Bite");
    private static final List<String> DEFENSE_ATTACK_MOVES = Arrays.asList("Strike", "Bite");

    private static final List<String> NORMAL_DEFENSE_MOVES = Arrays.asList("Stare");
    private static final List<String> ATTACK_DEFENSE_MOVES = Arrays.asList("Stare");
    private static final List<String> DEFENSE_DEFENSE_MOVES = Arrays.asList("Stare", "Block", "Grab");

    // Attack Pattern Sequences: 'A' = Attack List, 'D' = Defense List, 'C' = Charge Attack
    private static final List<String> PATTERN_NORMAL = Arrays.asList("A", "D", "A", "D");
    private static final List<String> PATTERN_ATTACK = Arrays.asList("A", "A", "A", "D");
    private static final List<String> PATTERN_DEFENSE = Arrays.asList("A", "D", "D", "A", "D", "D");
    private static final String CHARGE_ATTACK_NAME = "Ultimate Charge"; // Placeholder <<<<<<<<

    // --- Miguel's Turn State ---
    private String miguelPreparedAction;
    private List<String> miguelPattern; // The current sequence of A/D moves
    private int miguelPatternIndex = 0; // Current position in the sequence
    private boolean isCharging = false; // True if preparing the ultimate move
    private List<String> miguelAttackList = new ArrayList<>(); // Current attack list for mode
    private List<String> miguelDefenseList = new ArrayList<>(); // Current defense list for mode



    // --- INIT UI CREATION ---
    private VBox root;
    private Scene scene;

    // --- TOP UI VAR ---
    private TextArea btlLog = new TextArea();        // Battle log (left)
    private ProgressBar enemyHP;    // Miguel's HP bar
    private Text EHText;

    // --- BATTLE UI VAR ---
    private StackPane btlfield;    // Main battle area
    private VBox itmBox;           // Item menu box
    private VBox actBox;           // Action menu box
    private VBox itmList;          // List of item buttons
    private ScrollPane itmScroll;  // Scrollable container for items
    private static final String WHITE_TEXT_STYLE = "-fx-text-fill: white;";
    private String pendingChoice = null;

    // --- BOTTOM UI VAR ---
        // Player bars and text
    private ProgressBar playerHP;
    private Text PHText;
    private ProgressBar playerSP;
    private Text PSText;
        // Buttons
    private Button actBtn;
    private Button defBtn;
    private Button itmBtn;
        // Transparent button style placeholder
    private static final String TRANSPARENT_BTN_STYLE =
            "-fx-background-color: transparent; -fx-text-fill: white;";

    // ==========================================================
    // CONSTRUCTOR (LOGIC ONLY)
    // ==========================================================
    public FinalBattleSystem(Stage stage, Scene mainMenuScene) {
        this.stage = stage;
        this.mainMenuScene = mainMenuScene;
        this.player = new Player();
        this.miguel = new Miguel();
        this.tracker = new GiftTracker(controller);

        initializeBattle(); // Prepare variables, HP, states, etc.
    }

    // ==========================================================
    // INIT — UI CREATION + STAGE SETUP
    // ==========================================================
    public void init(Stage stage) {

        System.out.println("WELCOME TO RPG BATTLE");
        // 1. Build all UI nodes
        root = createUI();

        // 2. Create the scene
        scene = new Scene(root, 1280, 720); // use your preferred resolution

        root.requestFocus();

        // 3. Add input listeners
        setupControls(scene);

        // 4. Attach scene to the stage
        stage.setScene(scene);
        stage.show(); // OPTIONAL — remove if you want parent to show
    }

    // ==========================================================
    // BATTLE PREPARATION
    // ==========================================================

    private void initializeBattle() {
        System.out.println("Battle Initialization: Applying Permanent Gifts and Loading Items/Actions.");
        applyPermanentGifts();
        loadItemInventory();
        loadActionMenu();
        miguelPattern = PATTERN_NORMAL;
        miguel.currentMode = Miguel.Mode.NORMAL;
        miguelAttackList = NORMAL_ATTACK_MOVES;
        miguelDefenseList = NORMAL_DEFENSE_MOVES;
        updateMiguelMode(miguel.currentMode);
        System.out.println("Initialization complete. Ready for Player input.");

        startBattle();
    }

    private void applyPermanentGifts() {
        System.out.println("Applying Permanent Gifts...");
        GiftTracker.Permanent p = tracker.permanent;

        if (p.hasCleaver) {
            player.dmgMult += 0.3;
        }
        if (p.hasPipeBomb) {
            player.currentHP -= 20;
            if (player.currentHP < 0) player.currentHP = 1;
        }
        if (p.hasGooGooBabies) {
            player.def -= 5;
            player.dmgMult -= 0.1;
        }
        if (p.hasCrewmateCostume) {
            player.def += 5;
        }
        if (p.hasObsessed) {
            player.maxSP -= 24;
            if (player.currentSP > player.maxSP) player.currentSP = player.maxSP;
        }
        if (p.hasCubesBlessing) {
            player.spGain += 5;
        }

        System.out.println("Permanent Gifts application complete. Player's new stats:");
    }
    private void loadItemInventory() {
        System.out.println("Loading Item Inventory...");
        GiftTracker.Items i = tracker.items;

        // DEBUG ZONE ====================================
//        itemInventory.add("Miguel's Cake");
//        itemInventory.add("Big M'");
//        itemInventory.add("Banana");
//        itemInventory.add("Can of Beans");
//        itemInventory.add("Gula Melaka");
//        itemInventory.add("Lenarian Melon");
        // DEBUG ZONE ====================================

        if (i.hasMiguelsCake) itemInventory.add("Miguel's Cake");
        if (i.hasBigM) itemInventory.add("Big M'");
        if (i.hasBanana) itemInventory.add("Banana");
        if (i.hasCanOfBeans) itemInventory.add("Can of Beans");
        if (i.hasGulaMelaka) itemInventory.add("Gula Melaka");
        if (i.hasLenarianMelon) itemInventory.add("Lenarian Melon");

        System.out.println("Item Inventory loaded. Total items: " + itemInventory.size());
    }
    private void loadActionMenu() {
        System.out.println("Loading Player Action Menu...");

        // 1. Add Default Actions (Always available)
        actionMenu.add("Punch");
        actionMenu.add("Throw Furniture"); // Relies on the unique 'furnitureInventory' in Player

        // 2. Add Unlocked Actions (Based on gifts)
        GiftTracker.Actions a = tracker.actions;

        // DEBUG ZONE ====================================
//        actionMenu.add("Knowledge");
//        actionMenu.add("Thunderous Cross Cut");
//        actionMenu.add("Concert");
//        actionMenu.add("Towel");
//        actionMenu.add("Bicycle");
//        actionMenu.add("Hack Away");
//        actionMenu.add("Stare");
        // DEBUG ZONE ====================================

        if (a.hasKnowledge) actionMenu.add("Knowledge");
        if (a.hasHamonHairband) actionMenu.add("Thunderous Cross Cut");
        if (a.hasConcert) actionMenu.add("Concert");
        if (a.hasTowel) actionMenu.add("Towel");
        if (a.hasBicycle) actionMenu.add("Bicycle");
        if (a.hasSteakKnife) actionMenu.add("Hack Away");
        if (a.hasStare) actionMenu.add("Stare");

        System.out.println("Action Menu loaded. Total actions: " + actionMenu.size());
        System.out.println("  Actions: " + actionMenu);
        System.out.println("---");
    }

    // ==========================================================
    // BATTLE LOOP
    // ==========================================================

    public void startBattle() {
        System.out.println("\n--- THE BATTLE BEGINS! ---");
        System.out.printf("Player HP: %d/%d | Miguel HP: %d/%d%n",
                player.currentHP, player.maxHP, miguel.currentHP, miguel.maxHP);

        startNextRound(); // Start first round
    }

    private void startNextRound() {
        if (isBattleOver()) {
            endBattleSummary();
            return;
        }

        System.out.println("\n--- ROUND START ---");

        // Step 1: Miguel prepares an action
        miguelPreparedAction = miguelChooseMove();
        System.out.println("Miguel is preparing: " + miguelPreparedAction);

        // Step 2: Player turn — wait asynchronously
        playerTurn();
    }

    // --- CHECK IF BATTLE OVER ---
    private boolean isBattleOver() {
        return player.currentHP <= 0 || miguel.currentHP <= 0;
    }

    // ==========================================================
    // STEP 1: MIGUEL'S PREPARATION
    // ==========================================================

    private String miguelChooseMove() {
        // 1. Mode check
        Miguel.Mode newMode = checkMiguelMode();
        if (newMode != miguel.currentMode) {
            System.out.println("Mode Change: " + miguel.currentMode + " -> " + newMode);
            updateMiguelMode(newMode);
        }

        // 2. Charge check
        if (isCharging) {
            isCharging = false;
            return CHARGE_ATTACK_NAME;
        }

        // 3. Pattern exhausted check
        if (miguelPatternIndex >= miguelPattern.size()) {
            System.out.println("Pattern exhausted. Miguel is charging!");
            miguelPatternIndex = 0;
            isCharging = true;

            Collections.shuffle(miguelPattern);
            return "Charging...";
        }

        // 4. Pick next move
        String moveType = miguelPattern.get(miguelPatternIndex);
        String selectedMove;
        if ("A".equals(moveType)) {
            selectedMove = miguelAttackList.isEmpty() ? "Error: No Attack Move"
                    : miguelAttackList.get(random.nextInt(miguelAttackList.size()));
        } else { // "D"
            selectedMove = miguelDefenseList.isEmpty() ? "Error: No Defense Move"
                    : miguelDefenseList.get(random.nextInt(miguelDefenseList.size()));
        }

        miguelPatternIndex++;
        miguelPreparedAction = selectedMove; // store for later
        return selectedMove;
    }

    private Miguel.Mode checkMiguelMode() {
        if (miguel.currentHP > 666) {
            return Miguel.Mode.NORMAL;
        } else if (miguel.currentHP > 333) {
            return Miguel.Mode.ATTACK;
        } else {
            return Miguel.Mode.DEFENSE;
        }
    }

    private void updateMiguelMode(Miguel.Mode newMode) {
        if (miguel.currentMode == newMode) return; // No change

        System.out.println("Updating Miguel mode: " + miguel.currentMode + " -> " + newMode);

        miguel.currentMode = newMode;
        miguelPatternIndex = 0;  // Reset pattern index
        isCharging = false;      // Cancel charge if mode changes

            // Reset base stats
        //miguel.def = 0;
        //miguel.dmgMult = 1.0;

        // Assign pattern and move lists based on mode
        switch (newMode) {
            case NORMAL:
                miguelPattern = new ArrayList<>(PATTERN_NORMAL);
                miguelAttackList = NORMAL_ATTACK_MOVES;
                miguelDefenseList = NORMAL_DEFENSE_MOVES;
                break;
            case ATTACK:
                miguelPattern = new ArrayList<>(PATTERN_ATTACK);
                miguelAttackList = ATTACK_ATTACK_MOVES;
                miguelDefenseList = ATTACK_DEFENSE_MOVES;
                miguel.dmgMult += 0.3;
                break;
            case DEFENSE:
                miguelPattern = new ArrayList<>(PATTERN_DEFENSE);
                miguelAttackList = DEFENSE_ATTACK_MOVES;
                miguelDefenseList = DEFENSE_DEFENSE_MOVES;
                miguel.dmgMult -= 0.4;
                miguel.def += 20;
                break;
        }

        // Shuffle pattern so next cycle is less predictable
        Collections.shuffle(miguelPattern);

        System.out.println("Miguel's mode updated. Next pattern: " + miguelPattern);
    }

    // ==========================================================
    // STEP 2: PLAYER'S TURN
    // ==========================================================

    private void playerTurn() {
        System.out.println("\n--- PLAYER TURN ---");

        isPlayerTurn = true;   // Unlock menus/buttons
        //lockMenus(false);      // Allow player interaction

        btlLog.appendText("\nYour turn! Choose an Action (Z), Defend (X), or Item (C).");
    }

    private void handlePlayerChoice(String choice) {
        if (!isPlayerTurn) return;

        System.out.println("Player chose: " + choice);

        // SP Costs
        int cost = getSPCost(choice);

        if (player.currentSP < cost) {
            btlLog.appendText("\nNot enough SP!");
            return; // stay in player turn
        }

        // Deduct SP
        player.currentSP -= cost;
        updatePlayerSPBar();

        // Decide what type of thing this is:
        if (actionMenu.contains(choice)) {
            executeAction(choice);

        } else if (itemInventory.contains(choice)) {
            executeItem(choice);

        } else if (choice.equals("DEFEND")) {
            // Check if player can block
            if (player.cannotBlockRounds > 0) {
                btlLog.appendText("\nToo tired to Defend this round!");
                return; // stays in player turn, prevents blocking
            }
            executeDefend();

        } else {
            System.out.println("ERROR: Unknown player choice: " + choice);
        }

        // --- End Player Turn ---
        isPlayerTurn = false;
        //lockMenus(true);

        // Check if Miguel was defeated
        if (isBattleOver()) {
            endBattleSummary();
            return; // Stop here; Miguel won't act
        }

        waitThen(() -> {
            miguelTurn();      // Start enemy AI
        }, 0.7);
    }

    private int getSPCost(String action) {
        return switch (action) {
            case "Punch" -> 0;
            case "Throw Furniture" -> 25;
            case "Knowledge" -> 55;
            case "Thunderous Cross Cut" -> 45;
            case "Concert" -> 30;
            case "Towel" -> 30;
            case "Bicycle" -> 30;
            case "Hack Away" -> 30;
            case "Stare" -> 45;
            default -> 0;
        };
    }

    private void executeAction(String actionName) {
        System.out.println("Executing Action: " + actionName);

        switch (actionName) {

            case "Punch" -> {
                int baseDamage = 50;
                double multiplier = player.getTotalDmgMult();
                int finalDamage = (int)(baseDamage * multiplier) - miguel.def;
                if (finalDamage < 0) finalDamage = 0;

                applyDamageToMiguel(finalDamage);

                btlLog.appendText("\nYou punched Miguel for " + finalDamage + " damage!");
            }

            case "Throw Furniture" -> {
                if (player.furnitureInventory.isEmpty()) {
                    btlLog.appendText("\nYou have no furniture left to throw!");
                    return; // stay in player action phase
                }

                // Pick a random piece
                Random rand = new Random();
                int index = rand.nextInt(player.furnitureInventory.size());
                String chosenFurniture = player.furnitureInventory.get(index);

                // Remove from inventory
                player.furnitureInventory.remove(index);

                // Get damage from map
                int furnitureDamage = Player.FURNITURE_DAMAGE.getOrDefault(chosenFurniture, 0);

                // Apply damage formula
                double multiplier = player.getTotalDmgMult();
                int finalDamage = (int)(furnitureDamage * multiplier) - miguel.def;
                if (finalDamage < 0) finalDamage = 0;
                applyDamageToMiguel(finalDamage);

                btlLog.appendText("\nYou threw a " + chosenFurniture + "!");
                btlLog.appendText("\nIt dealt " + finalDamage + " damage!");

                updateMiguelHPBar();
            }

            case "Knowledge" -> {
                player.tempDmgMultBonus += 1.5;  // +150% damage for 2 rounds
                player.knowledgeRounds = 2;     // duration including this turn
                btlLog.appendText("\nThe secrets of the universe! Damage doubled for your next turn.");
            }
            case "Thunderous Cross Cut" -> {
                int baseDamage = 140;
                double multiplier = player.getTotalDmgMult();
                int finalDamage = (int)(baseDamage * multiplier) - miguel.def;
                if (finalDamage < 0) finalDamage = 0;

                applyDamageToMiguel(finalDamage);

                btlLog.appendText("\nHamon surges through you! You dealt " + finalDamage + " damage!");
            }
            case "Concert" -> {
                player.concertRounds = 4; // lasts 3 rounds
                btlLog.appendText("\nYou performed Concert with Miku! SP gain doubled for 3 rounds!");
            }
            case "Towel" -> {
                player.towelRounds = 3; // lasts 3 rounds
                btlLog.appendText("\nYou wrapped yourself in a cozy Towel! +10 DEF for 3 rounds!");
            }
            case "Bicycle" -> {
                int baseDamage = 110;
                double multiplier = player.getTotalDmgMult();
                int finalDamage = (int)(baseDamage * multiplier) - miguel.def;
                if (finalDamage < 0) finalDamage = 0;

                applyDamageToPlayer(10);
                btlLog.appendText("\nYou materialized a bicycle with your mind...");

                applyDamageToMiguel(finalDamage);

                btlLog.appendText("\nYou crashed the bicycle into Miguel for " + finalDamage + " damage! You got hurt from the recoil.");
            }
            case "Hack Away" -> {
                int baseDamage = 120;
                double multiplier = player.getTotalDmgMult();
                int finalDamage = (int)(baseDamage * multiplier) - miguel.def;
                if (finalDamage < 0) finalDamage = 0;

                player.cannotBlockRounds = 2;

                btlLog.appendText("\nYou slashed all over the place, it left you tired...");

                applyDamageToMiguel(finalDamage);

                btlLog.appendText("\nYou cut Miguel for " + finalDamage + " damage!");
            }
            case "Stare" -> {

                miguel.def -= 5;
                miguel.dmgMult -= 0.1;

                btlLog.appendText("\nYour Stare pierces Miguel's Defense.");
            }
        }

        actBox.setVisible(false);
    }

    private void executeItem(String itemName) {
        System.out.println("Using Item: " + itemName);

        switch (itemName) {

            case "Miguel's Cake" -> {
                player.currentHP += 100;
                if (player.currentHP > player.maxHP) player.currentHP = player.maxHP;

                updatePlayerHPBar();

                btlLog.appendText("\nYou ate Miguel’s Cake. You monster! Restored ful HP!");
            }

            case "Banana" -> {
                player.currentHP += 45;
                if (player.currentHP > player.maxHP) player.currentHP = player.maxHP;

                updatePlayerHPBar();

                btlLog.appendText("\nYou ate the Banana and restored some HP!");
            }

            case "Can of Beans" -> {
                player.tempDmgMultBonus += 1.0;   // +100% damage
                player.beansRounds = 3;          // lasts 3 turns including this one
                btlLog.appendText("\nYou ate a Can of Beans! Damage doubled for 2 rounds. What's in this stuff?");
            }
            case "Big M'" -> {
                player.currentHP += 75;
                if (player.currentHP > player.maxHP) player.currentHP = player.maxHP;

                updatePlayerHPBar();

                btlLog.appendText("\nYou ate the Big M' and restored a lot of HP! Sorry Martin...");
            }
            case "Gula Melaka" -> {
                player.currentSP += 55;
                if (player.currentSP > player.maxSP) player.currentSP = player.maxSP;

                btlLog.appendText("\nYou drank Gula Melaka and restored SP! How refreshing!");
            }
            case "Lenarian Melon" -> {// store debuff separately
                miguel.melonRounds = 4;          // lasts 4 turns
                miguel.def -= 15;                // apply immediately
                btlLog.appendText("\nThe Lenarian Melon hit Miguel! His Def decreased by 15 for 3 rounds.");
            }
        }

        // Remove item after use
        itemInventory.remove(itemName);
        refreshItemMenu();

        itmBox.setVisible(false);
    }

    private void executeDefend() {
        System.out.println("Player is defending!");

        player.isDefending = true; // or a DEF buff for 1 turn

        btlLog.appendText("\nYou brace yourself for incoming damage!");
    }

    // ==========================================================
    // STEP 3: MIGUEL'S TURN
    // ==========================================================

    private void miguelTurn() {
        System.out.println("\n--- MIGUEL TURN ---");
        System.out.println("Miguel uses: " + miguelPreparedAction);

        String action = miguelPreparedAction;

        // Example: handle different moves
        switch (action) {
            case "Strike" -> {
                int baseDamage = 20; // example value
                int finalDamage = (int) (baseDamage * miguel.dmgMult) - player.def;
                if (finalDamage < 0) finalDamage = 0;

                applyDamageToPlayer(finalDamage);

                btlLog.appendText("\nMiguel Strikes for " + finalDamage + " damage!");
            }

            case "Thrash" -> {
                int baseDamage = 30; // example value
                int finalDamage = (int) (baseDamage * miguel.dmgMult) - player.def;
                if (finalDamage < 0) finalDamage = 0;

                applyDamageToPlayer(finalDamage);

                btlLog.appendText("\nMiguel Thrashes for " + finalDamage + " damage!");
            }

            case "Bite" -> {
                int baseDamage = 15;
                int finalDamage = (int) (baseDamage * miguel.dmgMult) - player.def;
                if (finalDamage < 0) finalDamage = 0;

                applyDamageToPlayer(finalDamage);
                player.maxHP -= 10;
                player.maxSP -= 10;
                updatePlayerHPBar();
                updatePlayerSPBar();

                btlLog.appendText("\nMiguel takes a Bite out of you. Lost Max HP and SP!");
                btlLog.appendText("\nMiguel Bites for " + finalDamage + " damage!");
            }

            case "Stare" -> { //
                // Could reduce player SP or apply debuff

                btlLog.appendText("\nMiguel Stares! Player you feel tense.");
            }

            case "Block" -> {
                btlLog.appendText("\nMiguel put up his defenses!"); // change to blank
            }

            case "Grab" -> {
                int baseDamage = 15; // example value
                int finalDamage = (int) (baseDamage * miguel.dmgMult) - player.def;
                if (finalDamage < 0) finalDamage = 0;

                applyDamageToPlayer(finalDamage);
                player.dmgMult -= 0.1;

                btlLog.appendText("\nIt's hard to move. You lose some dmgMult!");
                btlLog.appendText("\nMiguel Grabs you for " + finalDamage + " damage!");
            }

            case CHARGE_ATTACK_NAME -> {
                int baseDamage = 50;
                int finalDamage = (int) (baseDamage * miguel.dmgMult) - player.def;
                if (finalDamage < 0) finalDamage = 0;

                applyDamageToPlayer(finalDamage);
                updatePlayerHPBar();

                btlLog.appendText("\n*** CHARGE ATTACK! Miguel hits for " + finalDamage + " damage! ***");
            }

            case "Charging..." -> {
                btlLog.appendText("\n!!! Miguel is charging a powerful attack !!!");
            }

            default -> {
                btlLog.appendText("\nMiguel hesitates...");
            }
        }

        // --- End Miguel Turn ---
        // Optional: update cooldowns, timers, etc.
        // Then proceed to end-of-round cleanup

        // Check if Player was defeated
        if (isBattleOver()) {
            endBattleSummary();
            return; // Stop here; no next round
        }

        waitThen(() -> {
            endRoundCleanup();
        }, 0.9);
    }

    // ==========================================================
    // STEP 4: CLEANUP
    // ==========================================================

    private void endRoundCleanup() {
        System.out.println("\n--- END OF ROUND ---");

        // 1. Reset player defending status
        player.isDefending = false;

        // HACK AWAY
        // Decrement cannot block counter
        if (player.cannotBlockRounds > 0) {
            player.cannotBlockRounds--;
        }

        // Apply Towel
        if (player.towelRounds > 0) {
            if (!player.isTowelActive) {      // Apply bonus only once
                player.def += 10;
                player.isTowelActive = true;
            }

            player.towelRounds--;             // Decrement rounds
            if (player.towelRounds == 0) {   // Remove bonus when rounds end
                player.def -= 10;
                player.isTowelActive = false;
                btlLog.appendText("\nTowel effect has worn off.");
            }
        }

        // Apply Concert
        if (player.concertRounds > 0) {
            if (!player.isConcertActive) {    // Apply multiplier only once
                player.spGain *= 2;
                player.isConcertActive = true;
            }

            player.concertRounds--;           // Decrement rounds
            if (player.concertRounds == 0) { // Remove multiplier when rounds end
                player.spGain /= 2;
                player.isConcertActive = false;
                btlLog.appendText("\nConcert effect has worn off.");
            }
        }
        updatePlayerSPBar();

        // 2. Recover SP based on player's SP gain stat
        if (!"Stare".equals(miguelPreparedAction)) {
            player.currentSP += player.spGain;
            if (player.currentSP > player.maxSP) {
                player.currentSP = player.maxSP;
            }
        }
        updatePlayerSPBar();

        // Knowledge
        if (player.knowledgeRounds > 0) {
            player.knowledgeRounds--;      // count down

            if (player.knowledgeRounds == 0) {
                player.tempDmgMultBonus -= 1.5;   // remove bonus
                btlLog.appendText("\nKnowledge effect has worn off.");
            }
        }

        // Can of Beans
        if (player.beansRounds > 0) {
            player.beansRounds--;
            if (player.beansRounds == 0) {
                player.tempDmgMultBonus -= 1.0; // remove effect
                btlLog.appendText("\nCan of Beans effect has worn off.");
            }
        }

        // Miguel Melon
        if (miguel.melonRounds > 0) {
            miguel.melonRounds--;
            if (miguel.melonRounds == 0) {
                miguel.def += 15;  // restore DEF
                btlLog.appendText("\nLenarian Melon effect has worn off.");
            }
        }

        System.out.printf("Player SP: %d/%d | Miguel HP: %d/%d%n",
                player.currentSP, player.maxSP, miguel.currentHP, miguel.maxHP);

        // 5. Start next round
        waitThen(() -> {
            startNextRound();
        }, 0.5);
    }



//    private void lockMenus(boolean lock) {
//        actBtn.setDisable(lock);
//        defBtn.setDisable(lock);
//        itmBtn.setDisable(lock);
//
//        // Hide action/item boxes if locked
//        if (lock) {
//            actBox.setVisible(false);
//            itmBox.setVisible(false);
//        }
//    }

    // ==========================================================
    // UI CREATION
    // ==========================================================
    private VBox createUI() {
        // Main root container
        VBox root = new VBox();
        root.setSpacing(10);
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color: black;"); // Background color

        // Top UI: Player/Miguel HP, names, etc.
        HBox topUi = createTopUi();

        // Battle area: Enemy and player sprites, effects
        StackPane battleArea = createBattleArea();

        // Bottom controls: Action menu, item buttons, etc.
        VBox bottomControls = createBottomControls();

        // Make battle area expand vertically
        VBox.setVgrow(battleArea, Priority.ALWAYS);

        // Add all sections to root
        root.getChildren().addAll(topUi, battleArea, bottomControls);

        return root;
    }

    // --- TOP UI ---
    private HBox createTopUi() {
        // Main container
        HBox topUi = new HBox();
        topUi.setAlignment(Pos.CENTER_LEFT);
        topUi.setSpacing(40);
        topUi.setPadding(new Insets(0, 0, 10, 0)); // bottom margin

        // 1️⃣ Battle Log
        //btlLog = new TextArea();
        btlLog.setEditable(false);
        btlLog.setPromptText("...");
        btlLog.setPrefHeight(100);
        btlLog.setWrapText(true);
        btlLog.setStyle(
                "-fx-control-inner-background: black;" +
                        "-fx-text-fill: white;" +
                        "-fx-border-color: white;" +
                        "-fx-border-width: 5px;" +
                        "-fx-border-radius: 5px;" +
                        "-fx-font-size: 14px;"
        );
        HBox.setHgrow(btlLog, Priority.ALWAYS);

        // 2️⃣ Spacer
        Pane spacer = new Pane();
        spacer.setPrefWidth(150);

        // 3️⃣ Enemy Stats VBox
        VBox enemyStats = new VBox();
        enemyStats.setAlignment(Pos.CENTER_RIGHT);
        enemyStats.setSpacing(5);
        enemyStats.setPadding(new Insets(10));
        enemyStats.setStyle(
                "-fx-border-color: white;" +
                        "-fx-border-width: 5px;" +
                        "-fx-border-radius: 5px;"
        );

        // Enemy Name
        Text enemyName = new Text("Miguel");
        enemyName.setFill(Color.WHITE);
        enemyName.setFont(Font.font("System", FontWeight.BOLD, 18));

        // Enemy HP bar
        enemyHP = new ProgressBar(1.0); // full initially
        enemyHP.setPrefWidth(250);

        // HP text
        EHText = new Text("HP: 999"); // placeholder value
        EHText.setFill(Color.WHITE);

        // Add name + bar + text to enemyStats VBox
        enemyStats.getChildren().addAll(enemyName, enemyHP, EHText);

        // Add battle log, spacer, and enemyStats to topUi
        topUi.getChildren().addAll(btlLog, spacer, enemyStats);

        return topUi;
    }

    private StackPane createBattleArea() {

        // Main Battle Field
        btlfield = new StackPane();
        btlfield.setAlignment(Pos.CENTER);
        btlfield.setStyle("-fx-background-color: #1e1e1e;");

        // Background rectangle
        Rectangle backgroundRect = new Rectangle(1280, 400);
        backgroundRect.setArcWidth(5.0);
        backgroundRect.setArcHeight(5.0);
        backgroundRect.setFill(Color.DARKSLATEGRAY);
        backgroundRect.setStroke(Color.BLACK);
        backgroundRect.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);

        btlfield.getChildren().add(backgroundRect);

        // Container for menus (items + actions)
        HBox menuContainer = new HBox();
        menuContainer.setSpacing(40);
        menuContainer.setAlignment(Pos.CENTER);
        menuContainer.setPadding(new Insets(10));

        // --- Item Box (itmBox) ---
        itmList = new VBox();
        itmList.setSpacing(5);
        itmList.setStyle("-fx-background-color: transparent;");

        itmScroll = new ScrollPane(itmList);
        itmScroll.setFitToWidth(true);
        itmScroll.setPrefHeight(150);
        itmScroll.setStyle("-fx-background-color: transparent;");

        itmBox = new VBox();
        itmBox.setVisible(false); // initially hidden
        itmBox.setAlignment(Pos.CENTER);
        itmBox.setSpacing(5);
        itmBox.setPrefWidth(200);
        itmBox.setPrefHeight(250);
        itmBox.setStyle(
                "-fx-background-color: black;" +
                        "-fx-padding: 10;" +
                        "-fx-border-color: white;" +
                        "-fx-border-width: 2;"
        );

        Label itemLabel = new Label("Inventory");
        itemLabel.setStyle("-fx-font-size: 16px; " + WHITE_TEXT_STYLE);

        itmBox.getChildren().addAll(itemLabel, itmScroll);

        // --- Action Box (actBox) ---
        actBox = new VBox();
        actBox.setVisible(false); // initially hidden
        actBox.setAlignment(Pos.CENTER);
        actBox.setSpacing(5);
        actBox.setPrefWidth(200);
        actBox.setPrefHeight(250);
        actBox.setStyle(
                "-fx-background-color: black;" +
                        "-fx-padding: 10;" +
                        "-fx-border-color: white;" +
                        "-fx-border-width: 2;"
        );

        Label actionLabel = new Label("Choose Action");
        actionLabel.setStyle("-fx-font-size: 16px; " + WHITE_TEXT_STYLE);

        actBox.getChildren().add(actionLabel);

        itmBox.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        actBox.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        // --- Populate Item Menu ---
        itmList.getChildren().clear(); // Clear in case of reload
        for (String itemName : itemInventory) {
            Button itemBtn = new Button(itemName);
            itemBtn.setStyle("-fx-font-size: 14px; -fx-background-color: #222; -fx-text-fill: white;");
            itemBtn.setPrefWidth(180);

            // When clicked, send the item name to the player handler
            //itemBtn.setOnAction(e -> handlePlayerChoice(itemName));
            itemBtn.setOnAction(e -> handleMenuSelection(itemName, "ITEM"));

            itmList.getChildren().add(itemBtn);
        }

        // --- Populate Action Menu ---
        actBox.getChildren().clear(); // Clear label and buttons
        for (String actionName : actionMenu) {
            Button actionBtn = new Button(actionName);
            actionBtn.setStyle("-fx-font-size: 14px; -fx-background-color: #222; -fx-text-fill: white;");
            actionBtn.setPrefWidth(180);

            // When clicked, send the action name to the player handler
            //actionBtn.setOnAction(e -> handlePlayerChoice(actionName));
            actionBtn.setOnAction(e -> handleMenuSelection(actionName, "ACTION"));

            actBox.getChildren().add(actionBtn);
        }

        // Add menus to battle field
        btlfield.getChildren().addAll(itmBox, actBox);

        return btlfield;
    }

    private VBox createBottomControls() {

        VBox bottomVBox = new VBox();
        bottomVBox.setSpacing(10);

        HBox controlsHBox = new HBox();
        controlsHBox.setAlignment(javafx.geometry.Pos.CENTER);
        controlsHBox.setSpacing(50); // separation between player stats & buttons

        // 1️⃣ Player Stats VBox
        VBox playerStats = new VBox();
        playerStats.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        playerStats.setSpacing(5);
        playerStats.setPadding(new Insets(10));
        playerStats.setStyle(
                "-fx-border-color: white;" +
                        "-fx-border-width: 5px;" +
                        "-fx-border-radius: 5px;"
        );

        Text playerName = new Text("You");
        playerName.setFill(javafx.scene.paint.Color.WHITE);
        playerName.setFont(Font.font("System", FontWeight.BOLD, 18));

        playerHP = new ProgressBar(1.0);
        playerHP.setPrefWidth(250);
        PHText = new Text("HP: 99");
        PHText.setFill(javafx.scene.paint.Color.WHITE);

        playerSP = new ProgressBar(1.0);
        playerSP.setPrefWidth(250);
        PSText = new Text("SP: 99");
        PSText.setFill(javafx.scene.paint.Color.WHITE);

        playerStats.getChildren().addAll(playerName, playerHP, PHText, playerSP, PSText);

        // 2️⃣ Action Buttons VBox
        VBox actionButtonsVBox = new VBox();
        actionButtonsVBox.setAlignment(javafx.geometry.Pos.CENTER);
        actionButtonsVBox.setSpacing(10);
        actionButtonsVBox.setPadding(new Insets(10));

        String btnStyle = TRANSPARENT_BTN_STYLE + "-fx-border-width: 3px; -fx-border-radius: 5px; -fx-font-size: 16px;";

        actBtn = new Button("ACTION (Z)");
        actBtn.setStyle(btnStyle);
        actBtn.setPrefWidth(120);

        defBtn = new Button("DEFEND (X)");
        defBtn.setStyle(btnStyle);
        defBtn.setPrefWidth(120);

        itmBtn = new Button("ITEM (C)");
        itmBtn.setStyle(btnStyle);
        itmBtn.setPrefWidth(120);

        actionButtonsVBox.getChildren().addAll(actBtn, defBtn, itmBtn);

        // Combine player stats and buttons in HBox
        controlsHBox.getChildren().addAll(playerStats, actionButtonsVBox);

        bottomVBox.getChildren().add(controlsHBox);

        updatePlayerSPBar();
        updatePlayerHPBar();

        return bottomVBox;
    }

    // =========================
    // UPDATE BARS
    // =========================

    // --- UPDATE MIGUEL HP BAR ---
    private void applyDamageToMiguel(int dmg) {
        int finalDamage = dmg;

        if ("Block".equals(miguelPreparedAction)) {
            finalDamage = dmg / 2;
        }

        miguel.currentHP -= finalDamage;
        if (miguel.currentHP < 0) miguel.currentHP = 0;

        updateMiguelHPBar();
    }

    private void updateMiguelHPBar() {
        double percent = (double) miguel.currentHP / miguel.maxHP;
        enemyHP.setProgress(percent);     // actual bar
        EHText.setText("HP: " + miguel.currentHP);  // HP text
    }

    // --- UPDATE PLAYER HP BAR ---

    private void applyDamageToPlayer(int damage) {
        int finalDamage = damage;

        if (player.isDefending == true) {
            finalDamage = damage / 2;
        }

        player.currentHP -= finalDamage;
        if (player.currentHP < 0) player.currentHP = 0;

        updatePlayerHPBar();
    }

    private void updatePlayerHPBar() {
        double percent = (double) player.currentHP / player.maxHP;

        if (percent < 0) percent = 0;
        if (percent > 1) percent = 1;

        playerHP.setProgress(percent);  // ProgressBar
        PHText.setText("HP: " + player.currentHP);
    }

    // --- UPDATE PLAYER SP BAR ---

    private void updatePlayerSPBar() {
        double percent = (double) player.currentSP / player.maxSP;
        if (percent < 0) percent = 0;
        if (percent > 1) percent = 1;

        playerSP.setProgress(percent);
        PSText.setText("SP: " + player.currentSP);
    }

    // --- UPDATE ITEM MENU ---
    private void refreshItemMenu() {
        itmBox.setStyle(
                "-fx-background-color: black;" +
                        "-fx-padding: 10;" +
                        "-fx-border-color: white;" +
                        "-fx-border-width: 2;"
        );

        itmList.getChildren().clear();  // Remove all old buttons

        for (String item : itemInventory) {
            Button itemBtn = new Button(item);
            itemBtn.setPrefWidth(160);

            // --- Reapply any button style you had originally ---
            itemBtn.setStyle(
                    "-fx-background-color: #222;" + // dark button
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 14px;" +
                            "-fx-border-color: white;" +
                            "-fx-border-width: 1;" +
                            "-fx-border-radius: 3;" +
                            "-fx-background-radius: 3;"
            );
            itemBtn.setPadding(new Insets(5, 10, 5, 10));

            itemBtn.setOnAction(e -> handlePlayerChoice(item));
            itmList.getChildren().add(itemBtn);
        }

    }

    // ==========================================================
    // INPUT HANDLERS (WILL ADD MENU NAVIGATION LATER)
    // ==========================================================

    private void setupControls(Scene scene) {

        // --- BUTTONS ---
        actBtn.setOnAction(e -> { /* toggle actBox */ });
        itmBtn.setOnAction(e -> { /* toggle itmBox */ });
        defBtn.setOnAction(e -> { /* defend */ });

        // --- KEYBOARD ---
        scene.setOnKeyPressed(e -> {
            switch(e.getCode()) {
                case Z -> {
                    actBox.setVisible(!actBox.isVisible());
                    itmBox.setVisible(false);
                }
                case C -> {
                    itmBox.setVisible(!itmBox.isVisible());
                    actBox.setVisible(false);
                }
                case X -> {
                    if (actBox.isVisible() || itmBox.isVisible()) {
                        actBox.setVisible(false);
                        itmBox.setVisible(false);
                    } else {
                        handlePlayerChoice("DEFEND");
                        actBox.setVisible(false);
                        itmBox.setVisible(false);
                    }
                }
            }
        });

        // ACTION button → shows action menu
        actBtn.setOnAction(e -> {
            actBox.setVisible(!actBox.isVisible()); // toggle visibility
            itmBox.setVisible(false);              // hide item menu
        });

        // ITEM button → shows item menu
        itmBtn.setOnAction(e -> {
            itmBox.setVisible(!itmBox.isVisible()); // toggle visibility
            actBox.setVisible(false);               // hide action menu
        });

        // DEFEND button → placeholder for now
        defBtn.setOnAction(e -> {
            handlePlayerChoice("DEFEND");
            actBox.setVisible(false);
            itmBox.setVisible(false);
        });
    }

    // ======================
    // DESCRIPTIONS
    // =====================

    private void handleMenuSelection(String name, String type) {

        // ---- FIRST PRESS: show description ----
        if (pendingChoice == null || !pendingChoice.equals(name)) {
            pendingChoice = name;

            String desc = getDescription(name);
            btlLog.appendText("\n" + desc);
            return; // Do NOT execute yet
        }

        // ---- SECOND PRESS: confirm action ----
        pendingChoice = null; // reset

        handlePlayerChoice(name); // continue your normal flow
    }

    private String getDescription(String name) {
        switch (name) {

            // ===== ACTIONS =====
            case "Punch":
                return "A basic punch. Low damage. no SP cost";

            case "Throw Furniture":
                return "Throw a random object from your surroundings. Damage varies depending on the object. 25 SP cost";

            case "Knowledge":
                return "Sharpen your mind. +150% DMG multiplier for the next round. 55 SP cost";

            case "Thunderous Cross Cut":
                return "A powerful finishing technique. High damage at 45 SP cost";

            case "Concert":
                return "Concert with Miku. SP gain doubled for 3 rounds. 30 SP cost";

            case "Towel":
                return "Wrap yourself up. +10 DEF for 3 rounds. 30 SP cost";

            case "Bicycle":
                return "A reckless attack. High damage but you hurt yourself slightly. 30 SP cost";

            case "Hack Away":
                return "Wild slashes. High damage but you cannot block for the next round. 30 SP cost";

            case "Stare":
                return "Lower Miguel’s guard by staring deeply. Weakens his dmgMult and Def. 45 SP cost";

            // ===== ITEMS =====
            case "Miguel's Cake":
                return "A precious cake for Miguel. Restores full HP.";

            case "Banana":
                return "Heals a small amount of HP.";

            case "Can of Beans":
                return "Protein! Doubles your damage for the next 2 rounds.";

            case "Big M'":
                return "Martin's favorite burger. Restores huge HP.";

            case "Gula Melaka":
                return "A mysteriously delicious drink. Restore half of your SP.";

            case "Lenarian Melon":
                return "Melon from Lenaria. Turns Miguel into a nerd. -15 DEF for the next 3 rounds.";

            default:
                return "No description available.";
        }
    }

    // =========================
    // LOSE / WIN SCREENS
    // =========================

    private void endBattleSummary() {
        System.out.println("\n--- BATTLE END ---");

        if (player.currentHP <= 0) {
            System.out.println("*** GAME OVER! The Player was defeated. ***");
            createLoseScreen((Stage) btlLog.getScene().getWindow());  // Your friend’s Lose screen method
        } else if (miguel.currentHP <= 0) {
            if (itemInventory.contains("Miguel's Cake")) {
                System.out.println("*** TRUE END achieved! ***");
                createTrueEndScreen((Stage) btlLog.getScene().getWindow()); // Your True End
            } else {
                System.out.println("*** VICTORY! Miguel was defeated! ***");
                createWinScreen((Stage) btlLog.getScene().getWindow());  // Your normal Win screen
            }
        } else {
            System.out.println("Battle concluded.");
            return; // or show neutral screen
        }
    }

    // --- WIN SCREEN TEMPLATE ---
    private Scene createWinScreen(Stage primaryStage) {
        // --- Root Container: VBox ---
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: black;");

        // --- 1. VICTORY Title ---
        Label titleLabel = new Label("VICTORY!");
        titleLabel.setFont(Font.font("Arial Black", FontWeight.BOLD, 100));
        titleLabel.setTextFill(Color.LIMEGREEN);

        // --- 2. Win Message ---
        Label messageLabel = new Label(
                "You have defeated the 'concept' of Miguel.\n" +
                        "Congratulations on your hard-earned victory.\n" +
                        "However, the fate of the real Miguel remains unknown..."
        );
        messageLabel.setFont(Font.font("Arial", 24));
        messageLabel.setTextFill(Color.WHITE);
        messageLabel.setWrapText(true);
        messageLabel.setTextAlignment(TextAlignment.CENTER);

        // --- 3. Return Button ---
        Button returnBtn = new Button("Goodbye!");
        String btnStyle = "-fx-background-color: black; " +
                "-fx-text-fill: white; " +
                "-fx-font-family: 'Consolas'; " +
                "-fx-font-size: 16px; " +
                "-fx-border-color: white; " +
                "-fx-border-width: 2px; " +
                "-fx-padding: 8 16 8 16;";
        returnBtn.setStyle(btnStyle);

        returnBtn.setOnAction(e -> {
            System.out.println("No clicked. Exiting...");
            primaryStage.close(); // Exit game
        });

        HBox buttonBox = new HBox(returnBtn);
        buttonBox.setAlignment(Pos.CENTER);

        root.getChildren().addAll(titleLabel, messageLabel, buttonBox);

        Scene scene = new Scene(root, 1280, 720);
        primaryStage.setTitle("You Win!");
        primaryStage.setScene(scene);
        primaryStage.show();

        return scene;
    }

    // --- LOSE SCREEN TEMPLATE ---
    private Scene createLoseScreen(Stage primaryStage) {
        // --- Root Container: VBox ---
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: black;");

        // --- 1. GAME OVER Title ---
        Label titleLabel = new Label("GAME OVER");
        titleLabel.setFont(Font.font("Arial Black", FontWeight.BOLD, 120));
        titleLabel.setTextFill(Color.WHITE);

        // --- 2. Retry Question ---
        Label questionLabel = new Label("Do you want to try again?");
        questionLabel.setFont(Font.font("Arial", 24));
        questionLabel.setTextFill(Color.WHITE);

        // --- 3. Yes / No Buttons ---
        Button yesBtn = new Button("YES");
        Button noBtn = new Button("NO");

        String btnStyle = "-fx-background-color: black; " +
                "-fx-text-fill: white; " +
                "-fx-font-family: 'Consolas'; " +
                "-fx-font-size: 16px; " +
                "-fx-border-color: white; " +
                "-fx-border-width: 2px; " +
                "-fx-padding: 8 16 8 16;";

        yesBtn.setStyle(btnStyle);
        noBtn.setStyle(btnStyle);

        // --- Button functionality placeholders ---
        yesBtn.setOnAction(e -> {
            // Get the current stage from any node in the scene
            Stage stage = (Stage) yesBtn.getScene().getWindow();

            // Create a new instance of the battle system to reset everything
            FinalBattleSystem newBattle = new FinalBattleSystem(mainStage, mainMenuScene);

            // Re-initialize the UI and start the battle on the same stage
            newBattle.init(stage);
        });

        noBtn.setOnAction(e -> {
            System.out.println("No clicked. Exiting...");
            primaryStage.close(); // Exit game
        });

        // --- Button container ---
        HBox buttonBox = new HBox(40, yesBtn, noBtn);
        buttonBox.setAlignment(Pos.CENTER);

        // --- Add all elements to the root VBox ---
        root.getChildren().addAll(titleLabel, questionLabel, buttonBox);

        // --- Scene setup ---
        Scene scene = new Scene(root, 1280, 720);
        primaryStage.setTitle("Game Over Screen");
        primaryStage.setScene(scene);
        primaryStage.show();

        return scene; // return for reference if needed
    }

    // --- TRUE END SCREEN TEMPLATE ---
    private Scene createTrueEndScreen(Stage primaryStage) {
        // --- Root Container: VBox ---
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: black;");

        // --- 1. TRUE ENDING Title ---
        Label titleLabel = new Label("TRUE ENDING");
        titleLabel.setFont(Font.font("Arial Black", FontWeight.BOLD, 100));
        titleLabel.setTextFill(Color.GOLD);

        // --- 2. True Ending Message ---
        Label messageLabel = new Label(
                "Congratulations! You discovered the True Ending.\n" +
                        "You still don't know where the real Miguel is...\n" +
                        "But hey. At least you got some cake, and the guests are happy.\n" +
                        "Thank you for playing!"
        );
        messageLabel.setFont(Font.font("Arial", 24));
        messageLabel.setTextFill(Color.WHITE);
        messageLabel.setWrapText(true);
        messageLabel.setTextAlignment(TextAlignment.CENTER);

        // --- 3. Return Button ---
        Button returnBtn = new Button("Goodbye!");
        String btnStyle = "-fx-background-color: black; " +
                "-fx-text-fill: white; " +
                "-fx-font-family: 'Consolas'; " +
                "-fx-font-size: 16px; " +
                "-fx-border-color: white; " +
                "-fx-border-width: 2px; " +
                "-fx-padding: 8 16 8 16;";
        returnBtn.setStyle(btnStyle);

        // --- Button functionality ---
        returnBtn.setOnAction(e -> {
            System.out.println("Exiting...");
            primaryStage.close();
        });

        // --- Button container (optional centering) ---
        HBox buttonBox = new HBox(returnBtn);
        buttonBox.setAlignment(Pos.CENTER);

        // --- Add all elements to root ---
        root.getChildren().addAll(titleLabel, messageLabel, buttonBox);

        // --- Scene setup ---
        Scene scene = new Scene(root, 1280, 720);
        primaryStage.setTitle("True Ending");
        primaryStage.setScene(scene);
        primaryStage.show();

        return scene;
    }

    // =========================
    // SLOW DOWN GAME
    // =========================

    private void waitThen(Runnable action, double seconds) {
        PauseTransition pause = new PauseTransition(Duration.seconds(seconds));
        pause.setOnFinished(e -> action.run());
        pause.play();
    }

}
