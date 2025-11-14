package com.ramolete.betterdialog.rpgbattle;

import com.ramolete.betterdialog.GlobalController;
import com.ramolete.betterdialog.rpgbattle.GiftTracker;
import com.ramolete.betterdialog.rpgbattle.Miguel;
import com.ramolete.betterdialog.rpgbattle.Player;
import javafx.stage.Stage;

import java.util.*;

public class BattleSystem {

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
    private static final String CHARGE_ATTACK_NAME = "Ultimate Charge"; // Placeholder <<<<<<<< name

    // --- Stat Units ---
    private final Player player;
    private final Miguel miguel;

    // --- Utility & Tracking ---
    private final GiftTracker tracker;
    private final GlobalController controller;
    private final Random random = new Random();

    // --- Inventory / Action Menu ---
    private final List<String> itemInventory = new ArrayList<>();
    private final List<String> actionMenu = new ArrayList<>();

    // --- Miguel's Turn State ---
    private String miguelPreparedAction;
    private List<String> miguelPattern; // The current sequence of A/D moves
    private int miguelPatternIndex = 0; // Current position in the sequence
    private boolean isCharging = false; // True if preparing the ultimate move
    private List<String> miguelAttackList = new ArrayList<>(); // Current attack list for mode
    private List<String> miguelDefenseList = new ArrayList<>(); // Current defense list for mode

    // ==========================================================
    // CONSTRUCTOR
    // ==========================================================
    public BattleSystem(GlobalController controller) {
        this.controller = controller;
        this.player = new Player();
        this.miguel = new Miguel();
        this.tracker = new GiftTracker(controller);

        initializeBattle();
    }

    // ==========================================================
    // INIT — UI INITIALIZATION WITH STAGE
    // ==========================================================
//    public void init(Stage stage) {
//
//        // Build UI structure
//        root = createUI();  // <-- You will define this later
//
//        // Create Scene
//        scene = new Scene(root, 1280, 720);
//
//        // Connect scene to stage
//        stage.setScene(scene);
//
//        // Add input handlers
//        setupControls(scene); // <-- You add this later
//
//        // Show (optional — if parent wants to control showing, remove this)
//        stage.show();
//    }

    // ==========================================================
    // INITIALIZATION METHODS
    // ==========================================================

    private void initializeBattle() {
        System.out.println("Battle Initialization: Applying Permanent Gifts and Loading Items/Actions.");
        applyPermanentGifts();
        loadItemInventory();
        loadActionMenu();
        updateMiguelMode(Miguel.Mode.NORMAL);
        System.out.println("Initialization complete. Ready for Player input.");
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

        while (!isBattleOver()) {
            System.out.println("\n--- ROUND START ---");

            // 1. Miguel will choose a move (Preparation)
            miguelPreparedAction = miguelChooseMove();
            System.out.println("Miguel is preparing to use: " + miguelPreparedAction);

            // 2. Player will choose a move and will execute it
            playerTurn();

            // Check win/loss after player's action
            if (isBattleOver()) break;

            // 3. Miguel will execute his move
            miguelTurn();

            // Check win/loss after Miguel's action
            if (isBattleOver()) break;

            // 4. End round, player recover sp, other timers count down.
            endRoundCleanup();
        }

        // Battle End Summary <<<<<<<<<<<<<<<<<<<<
        System.out.println("\n--- BATTLE END ---");
        if (player.currentHP <= 0) {
            System.out.println("*** GAME OVER! The Player was defeated. ***");
        } else if (miguel.currentHP <= 0) {
            System.out.println("*** VICTORY! Miguel was defeated! ***");
        } else {
            System.out.println("Battle concluded.");
        }
    }

    private boolean isBattleOver() {
        return player.currentHP <= 0 || miguel.currentHP <= 0;
    }

    // ==========================================================
    // STEP 1: MIGUEL'S PREPARATION
    // ==========================================================

    /**
     * Miguel selects his move for the current turn based on his current Mode and HP.
     * This method will need to be complex later.
     */
    private String miguelChooseMove() {
        // 1. Check and Set Mode (Highest priority)
        Miguel.Mode newMode = checkMiguelMode();
        if (newMode != miguel.currentMode) {
            System.out.println("Mode Change Detected: " + miguel.currentMode + " -> " + newMode);
            updateMiguelMode(newMode);
        }

        // --- 2. Determine Action ---

        if (isCharging) {
            // Miguel is in the telegraphed charge phase. Next turn is the attack.
            isCharging = false;
            return CHARGE_ATTACK_NAME; // Executes the move immediately next turn
        }

        // Check if the current pattern is exhausted (time for a Charge)
        if (miguelPatternIndex >= miguelPattern.size()) {
            System.out.println("Pattern exhausted. Miguel is starting to charge!");
            miguelPatternIndex = 0; // Reset index for the next cycle
            isCharging = true; // Sets up the CHARGE_ATTACK_NAME for the next turn
            return "Charging..."; // Placeholder action string for the current round
        }

        // Get the next move type from the pattern
        String moveType = miguelPattern.get(miguelPatternIndex);
        String selectedMove = "Error: No Move";

        if ("A".equals(moveType)) {
            // Pick a random move from the Attack list
            if (!miguelAttackList.isEmpty()) {
                selectedMove = miguelAttackList.get(random.nextInt(miguelAttackList.size()));
            }
        } else if ("D".equals(moveType)) {
            // Pick a random move from the Defense list
            if (!miguelDefenseList.isEmpty()) {
                selectedMove = miguelDefenseList.get(random.nextInt(miguelDefenseList.size()));
            }
        }

        // Advance pattern index
        miguelPatternIndex++;

        return selectedMove;
    }

    /**
     * Checks Miguel's HP and returns the appropriate Mode.
     */
    private Miguel.Mode checkMiguelMode() {
        if (miguel.currentHP > 666) {
            return Miguel.Mode.NORMAL;
        } else if (miguel.currentHP > 333) {
            return Miguel.Mode.ATTACK;
        } else {
            return Miguel.Mode.DEFENSE;
        }
    }

    /**
     * Updates Miguel's mode, stats, and action pools.
     * @param newMode The mode to switch to.
     */
    private void updateMiguelMode(Miguel.Mode newMode) {
        miguel.currentMode = newMode;
        miguelPatternIndex = 0; // Reset pattern index on mode change
        isCharging = false; // Cancel charge on mode change

        // Reset stats to base values first (DEF=0, DMG=1.0)
        miguel.def = 0;
        miguel.dmgMult = 1.0;

        switch (newMode) {
            case NORMAL:
                miguelPattern = PATTERN_NORMAL;
                miguelAttackList = NORMAL_ATTACK_MOVES;
                miguelDefenseList = NORMAL_DEFENSE_MOVES;
                // Stats: def=0, dmgMult=1.0 (default)
                break;
            case ATTACK:
                miguelPattern = PATTERN_ATTACK;
                miguelAttackList = ATTACK_ATTACK_MOVES;
                miguelDefenseList = ATTACK_DEFENSE_MOVES;
                miguel.dmgMult += 0.3; // +0.3 dmg mult
                break;
            case DEFENSE:
                miguelPattern = PATTERN_DEFENSE;
                miguelAttackList = DEFENSE_ATTACK_MOVES;
                miguelDefenseList = DEFENSE_DEFENSE_MOVES;
                miguel.dmgMult -= 0.4; // -0.4 dmg mult (0.6 total)
                miguel.def += 20;    // +20 Def
                break;
        }

        System.out.printf("-> Miguel Stats updated: DMG Mult: %.1f, DEF: %d%n", miguel.dmgMult, miguel.def);
    }

    // ==========================================================
    // STEP 2: PLAYER'S TURN
    // ==========================================================

    /**
     * Player is presented with options and executes their chosen action.
     */
    private void playerTurn() {
        // Will be implemented later
        System.out.println("PLAYER'S TURN: Executing action...");
        // Placeholder damage to move the system forward:
        int damage = 10;
        miguel.currentHP -= damage;
        System.out.printf("Player deals %d damage! Miguel HP remaining: %d/%d%n", damage, miguel.currentHP, miguel.maxHP);
    }

    // ==========================================================
    // STEP 3: MIGUEL'S TURN
    // ==========================================================

    /**
     * Miguel executes the action he chose in the preparation phase.
     */
    private void miguelTurn() {
        // [To be implemented after playerTurn is ready]
        System.out.println("MIGUEL'S TURN: Executing " + miguelPreparedAction);

        if ("Charging...".equals(miguelPreparedAction)) {
            System.out.println("Miguel finishes charging and prepares for the attack next round!");
            return;
        }

        // Placeholder damage based on prepared action
        int baseDamage = CHARGE_ATTACK_NAME.equals(miguelPreparedAction) ? 50 : 5;

        // Apply damage logic placeholder:
        int damage = (int) (baseDamage * miguel.dmgMult);
        player.currentHP -= damage;
        System.out.printf("Miguel executes %s, dealing %d damage! Player HP remaining: %d/%d%n", miguelPreparedAction, damage, player.currentHP, player.maxHP);
    }

    // ==========================================================
    // STEP 4: CLEANUP
    // ==========================================================

    /**
     * Handles end-of-round logic (SP recovery, debuff timers, etc.).
     */
    private void endRoundCleanup() {
        // Will be implemented later
        player.currentSP += player.spGain;
        if (player.currentSP > player.maxSP) {
            player.currentSP = player.maxSP;
        }
    }

    // ==========================================================
    // UI SEGMENT
    // ==========================================================



}