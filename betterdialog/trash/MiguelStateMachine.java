package com.ramolete.betterdialog.trash;

import com.ramolete.betterdialog.rpgbattle.Miguel;

import java.util.*;

public class MiguelStateMachine {

    private final Miguel miguel;
    private final Random random = new Random();

    // --- Core State Variables ---
    private enum Mode { NORMAL, ATTACK, DEFENSE }
    private Mode currentMode;

    // Tracks how many moves have occurred since the last Charge Attack.
    private int moveCounter = 0;

    // The specific pattern for the current set (e.g., ["A", "D", "A", "D"]). Shuffled on load.
    private List<String> roundSet = new ArrayList<>();

    // Index inside the current shuffled roundSet.
    private int setIndex = 0;

    // Telegraph Flag: True when the boss is preparing the charge attack
    private boolean charging = false;

    // --- Fixed Cycle Lengths ---
    private static final int NORMAL_CYCLE_LENGTH = 4; // [A, D, A, D]
    private static final int ATTACK_CYCLE_LENGTH = 4; // [A, A, A, D]
    private static final int DEFENSE_CYCLE_LENGTH = 6; // [A, A, D, A, A, D]

    // --- Move Pools (These strings are passed to BattleSystem for effect lookup) ---
    private final List<String> normalAttackPool = List.of("Basic Punch");
    private final List<String> attackAttackPool = List.of("Basic Punch", "Thrash", "Bite");
    private final List<String> defenseAttackPool = List.of("Basic Punch", "Bite");

    private final List<String> normalDefensePool = List.of("Stare");
    private final List<String> attackDefensePool = List.of("Stare");
    private final List<String> defenseDefensePool = List.of("Stare", "Block", "Grab");


    public MiguelStateMachine(Miguel miguel) {
        this.miguel = miguel;
        // Initialize the first mode and move set (forceShuffle = true)
        updateMode(true);
    }

    /**
     * Updates mode based on current HP and applies mode-specific stat changes.
     */
    private void updateMode(boolean forceShuffle) {
        Mode newMode;
        List<String> baseRoundSet;

        // 1. Determine the Mode based on HP and define the base set
        if (miguel.currentHP > 666) {
            newMode = Mode.NORMAL;
            baseRoundSet = new ArrayList<>(List.of("A", "D", "A", "D"));
        } else if (miguel.currentHP > 333) {
            newMode = Mode.ATTACK;
            baseRoundSet = new ArrayList<>(List.of("A", "A", "A", "D"));
        } else {
            newMode = Mode.DEFENSE;
            baseRoundSet = new ArrayList<>(List.of("A", "A", "D", "A", "A", "D"));
        }

        // 2. Transition and Apply Stats ONLY if the Mode has changed
        if (newMode != currentMode) {
            currentMode = newMode;
            setIndex = 0;
            moveCounter = 0;
            forceShuffle = true; // Force reload on mode change

            // Apply Stat Modifiers (Resets and applies new base)
            miguel.dmgMult = 1.0;
            miguel.def = 0;
            switch (currentMode) {
                case ATTACK: miguel.dmgMult = 1.3; break;
                case DEFENSE: miguel.dmgMult = 0.6; miguel.def = 20; break;
                default: // Normal mode is base stats
            }
        }

        // 3. Load and Shuffle the roundSet if starting a new cycle
        if (setIndex == 0 && forceShuffle) {
            roundSet = baseRoundSet;
            Collections.shuffle(roundSet); // RANDOM ORDER IMPLEMENTED HERE
        }
    }

    /** * Returns the name of the next specific move Miguel will perform (e.g., "Thrash", "Stare").
     * This handles the core logic of the cycle, randomization, and charging.
     */
    public String nextMove() {
        updateMode(false); // Check mode, but don't force a shuffle

        // Get current cycle length based on mode
        int cycleLength = switch(currentMode) {
            case NORMAL -> NORMAL_CYCLE_LENGTH;
            case ATTACK -> ATTACK_CYCLE_LENGTH;
            case DEFENSE -> DEFENSE_CYCLE_LENGTH;
        };

        // --- 1. Handle Charge Attack EXECUTION ---
        if (charging) {
            charging = false;
            String chargeMove = switch (currentMode) {
                case NORMAL -> "Charge Normal";
                case ATTACK -> "Charge Attack";
                case DEFENSE -> "Charge Defense";
            };
            setIndex = 0; // Reset index for the new cycle
            moveCounter = 0; // Reset counter
            updateMode(true); // Force shuffle the new set
            return chargeMove;
        }

        // --- 2. Check if the cycle is complete (Time to Charge) ---
        if (moveCounter >= cycleLength) {
            charging = true;
            return "Telegraph";
        }

        // --- 3. Normal Move Selection ---
        String currentMoveType = roundSet.get(setIndex);
        String chosenMoveName;

        if (currentMoveType.equals("A")) {
            chosenMoveName = getRandomMoveFromPool(getAttackPool());
        } else { // currentMoveType.equals("D")
            chosenMoveName = getRandomMoveFromPool(getDefensePool());
        }

        // --- 4. Advance Pointers ---
        setIndex++;
        moveCounter++;

        return chosenMoveName;
    }

    /** Calculate damage based on the specific move name and mode's multiplier */
    public int performMove() {
        String moveName = nextMove();
        int baseDamage = 0;

        // --- Base Damage Lookup Table (Must match names above) ---
        switch (moveName) {
            // Attack Moves (Ranged Damage)
            case "Basic Punch": baseDamage = random.nextInt(15) + 10; break;
            case "Thrash": baseDamage = random.nextInt(25) + 20; break;
            case "Bite": baseDamage = random.nextInt(15) + 5; break;

            // Defense/Buff Moves (No Damage)
            case "Stare":
            case "Block":
            case "Grab":
            case "Telegraph": baseDamage = 0; break;

            // Charged Attacks (Fixed Damage)
            case "Charge Normal": baseDamage = 30; break;
            case "Charge Attack": baseDamage = 50; break;
            case "Charge Defense": baseDamage = 40; break;
        }

        return (int)(baseDamage * miguel.dmgMult);
    }

    // --- Helper Methods ---

    /** Selects a random move from the available pool. */
    private String getRandomMoveFromPool(List<String> availableMoves) {
        return availableMoves.get(random.nextInt(availableMoves.size()));
    }

    private List<String> getAttackPool() {
        return switch (currentMode) {
            case NORMAL -> normalAttackPool;
            case ATTACK -> attackAttackPool;
            case DEFENSE -> defenseAttackPool;
        };
    }

    private List<String> getDefensePool() {
        return switch (currentMode) {
            case NORMAL -> normalDefensePool;
            case ATTACK -> attackDefensePool;
            case DEFENSE -> defenseDefensePool;
        };
    }
}