package com.ramolete.betterdialog.trash;

import com.ramolete.betterdialog.GlobalController;
import com.ramolete.betterdialog.rpgbattle.GiftTracker;
import com.ramolete.betterdialog.rpgbattle.Miguel;
import com.ramolete.betterdialog.rpgbattle.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * The core logic and state manager for the RPG battle against Miguel.
 * Handles initialization, turn order, action execution, and applying gift effects.
 * All action effects are hardcoded here based on the GiftTracker booleans.
 */
public class BattleSystem {

    // --- Stat Units ---
    private final Player player;
    private final Miguel miguel;

    // --- Utility & Tracking ---
    private final GiftTracker tracker;
    private final GlobalController controller;
    private final Random random = new Random();

    // --- Inventory ---
    // Item Name -> Quantity (Used for the Item button menu)
    private final Map<String, Integer> itemInventory = new HashMap<>();

    // --- TEMPORARY BATTLE EFFECTS ---

    // Tracks the current active defense buff magnitude (e.g., +10 from Melon, +20 from Bicycle)
    private int playerTempDefense = 0;

    // Duration in turns for the active defense buff
    private int defenseBuffDuration = 0;

    // If the player used the Defend button this turn (resets every turn)
    private boolean playerIsDefending = false;

    // Duration in turns Miguel is stunned (0 if not stunned)
    private int miguelStunDuration = 0;

    // --- Battle Flow State ---
    private String battleMessage = "The battle has begun!";
    private int turnCounter = 0;

    // Player's chosen action for the current turn (will be executed during executeTurn)
    private String playerQueuedAction = null;

    // --- Constructor and Initialization ---

    public BattleSystem(GlobalController controller) {
        this.controller = controller;
        this.player = new Player();
        this.miguel = new Miguel();

        // Initialize gift tracker based on collected gifts
        this.tracker = new GiftTracker(controller);

        initializeBattle();
    }

    /**
     * Executes all pre-battle setup: Permanent gift bonuses and item loading.
     */
    private void initializeBattle() {
        System.out.println("Battle Initialization: Applying Permanent Gifts and Loading Items.");

        // 1. Apply Permanent Buffs (Perm category)
        applyPermanentGifts();

        // 2. Load Item Inventory (Item category)
        loadItemInventory();

        // 3. Set the initial message
        this.battleMessage = String.format("Miguel's Max HP: %d. Player's Max HP: %d, Max SP: %d.",
                miguel.maxHP, player.maxHP, player.maxSP);
    }

    /**
     * Applies permanent modifiers to the Player object based on the GiftTracker.
     */
    private void applyPermanentGifts() {
        // --- Hardcoded Permanent Gift Effects ---
        if (tracker.permanent.hasCleaver) {
            player.dmgMult += 0.20;
            battleMessage += "\nPlayer feels the weight of the Cleaver (+20% DMG).";
        }
        if (tracker.permanent.hasPipeBomb) {
            player.maxHP = Math.max(1, player.maxHP - 20);
            player.dmgMult += 0.40;
            battleMessage += "\nPipe Bomb's risk applied (+40% DMG, -20 HP).";
        }
        if (tracker.permanent.hasGooGooBabies) {
            player.spGain += 10;
            battleMessage += "\nGoo-Goo Babies SP bonus applied (+10 SP Gain).";
        }
        if (tracker.permanent.hasCrewmateCostume) {
            player.def += 5;
            battleMessage += "\nCrewmate Costume applied (+5 DEF).";
        }
        if (tracker.permanent.hasObsessed) {
            player.def += 10;
            battleMessage += "\nObsessed passive applied (+10 DEF).";
        }
        if (tracker.permanent.hasCubesBlessing) {
            player.maxSP += 50;
            battleMessage += "\nCube's Blessing applied (+50 Max SP).";
        }

        // Ensure player is at full health/sp after permanent buffs
        player.currentHP = player.maxHP;
        player.currentSP = player.maxSP;
    }

    /**
     * Populates the item inventory based on the Item gifts unlocked.
     */
    private void loadItemInventory() {
        if (tracker.items.hasMiguelsCake) itemInventory.put("Miguel's Cake", 1);
        if (tracker.items.hasBigM) itemInventory.put("Big M’", 1);
        if (tracker.items.hasBanana) itemInventory.put("Banana", 1);
        if (tracker.items.hasCanOfBeans) itemInventory.put("Can of Beans", 1);
        if (tracker.items.hasGulaMelaka) itemInventory.put("Gula Melaka", 1);
        if (tracker.items.hasLenarianMelon) itemInventory.put("Lenarian Melon", 1);
    }

    // ==========================================================
    // CORE TURN FLOW
    // ==========================================================

    /**
     * Executes the turn order: Player Action -> Miguel Action -> End of Round Cleanup.
     * @param miguelMoveName The name of Miguel's pre-determined move.
     * @return The complete turn summary message.
     */
    public String executeTurn(String miguelMoveName) {
        if (playerQueuedAction == null) {
            return "ERROR: Player action was not queued. Skipping turn.";
        }

        turnCounter++;
        battleMessage = String.format("--- Turn %d ---", turnCounter);

        // --- 1. Player Turn ---
        String playerResult = executePlayerAction();
        battleMessage += "\n" + playerResult;

        if (miguel.currentHP <= 0) {
            battleMessage += "\nMIGUEL DEFEATED! You win!";
            return battleMessage;
        }

        // --- 2. Miguel Turn ---
        String miguelResult = executeMiguelAction(miguelMoveName);
        battleMessage += "\n" + miguelResult;

        if (player.currentHP <= 0) {
            battleMessage += "\nPLAYER DEFEATED! Game Over!";
            return battleMessage;
        }

        // --- 3. End of Round Phase ---
        endTurnCleanup();

        // Reset queued action
        playerQueuedAction = null;

        return battleMessage;
    }

    /**
     * Executes the player's queued Action (Punch, Throw Furniture, or Skill)
     * or processes the Defend/Item action name.
     */
    private String executePlayerAction() {
        String actionName = playerQueuedAction;

        // Item effects are handled immediately in queueItem(), Defend is handled by playerIsDefending flag.
        if (actionName.equals("Defend") || actionName.equals("Pass")) {
            return actionName.equals("Defend") ?
                    "Player held a defensive stance." :
                    "Player takes no further action this turn.";
        }

        // Execute Action/Skill
        switch (actionName) {
            case "Punch":
                // Default action 1: Low base damage (20)
                return dealDamage(20, player.dmgMult, "Punch");
            case "Throw Furniture":
                // Default action 2: Moderate base damage (35)
                return dealDamage(35, player.dmgMult, "Throw Furniture");
            case "Knowledge":
                return handleSkill_Knowledge();
            case "Hamon Hairband":
                return handleSkill_HamonHairband();
            case "Concert":
                return handleSkill_Concert();
            case "Towel":
                return handleSkill_Towel();
            case "Bicycle":
                return handleSkill_Bicycle();
            case "Steak Knife":
                return handleSkill_SteakKnife();
            case "Stare":
                return handleSkill_Stare();
            default:
                return "Unknown action executed: " + actionName;
        }
    }

    /**
     * Executes Miguel's attack, checking for stun and player defense.
     */
    private String executeMiguelAction(String miguelMoveName) {
        // 1. Check for Stun
        if (miguelStunDuration > 0) {
            return "Miguel is stunned and cannot move this turn!";
        }

        // 2. Determine base damage and message (Placeholder values for Miguel's moves)
        int baseDamage = 0;
        String message;

        switch (miguelMoveName) {
            case "Stare Down":
                baseDamage = 20;
                message = "Miguel gives you a piercing stare. ";
                break;
            case "Smash":
                baseDamage = 40;
                message = "Miguel smashes the ground, sending debris flying. ";
                break;
            case "Ultimate":
                baseDamage = 70;
                message = "Miguel unleashes his true power! ";
                break;
            default:
                baseDamage = 10;
                message = "Miguel hesitates. ";
                break;
        }

        // 3. Apply Damage Calculation
        double totalDamageMult = miguel.dmgMult;
        int rawDamage = (int) (baseDamage * totalDamageMult);
        int finalDefense = player.def;

        // Apply temporary defense buff (from items/skills)
        finalDefense += playerTempDefense;

        // Apply Defend stance (halves the incoming damage before defense reduction)
        if (playerIsDefending) {
            rawDamage /= 2;
        }

        // Calculate final damage dealt
        int damageDealt = Math.max(0, rawDamage - finalDefense);

        // 4. Apply damage to Player
        player.currentHP -= damageDealt;

        return message + String.format("You took %d damage!", damageDealt);
    }

    /**
     * SP recovery and buff/debuff duration check.
     */
    private void endTurnCleanup() {
        // --- 1. Player SP Gain ---
        int spRecovered = player.spGain;
        player.currentSP = Math.min(player.maxSP, player.currentSP + spRecovered);
        battleMessage += String.format("\nPlayer recovers %d SP.", spRecovered);

        // --- 2. Resolve Buff/Debuff Durations ---

        // Resolve Player Defense Buff (from Item or Skill)
        if (defenseBuffDuration > 0) {
            defenseBuffDuration--;
            if (defenseBuffDuration == 0) {
                // Revert the defense increase
                player.def -= playerTempDefense;
                playerTempDefense = 0;
                battleMessage += "\nPlayer's defense buff wore off.";
            }
        }

        // Reset one-turn Defend stance
        playerIsDefending = false;

        // Resolve Miguel Stun
        if (miguelStunDuration > 0) {
            miguelStunDuration--;
            if (miguelStunDuration == 0) {
                battleMessage += "\nMiguel recovered from stun.";
            }
        }

        // Ensure stats don't fall out of bounds
        player.currentHP = Math.max(0, player.currentHP);
        player.currentSP = Math.max(0, player.currentSP);
        miguel.currentHP = Math.max(0, miguel.currentHP);
    }

    // ==========================================================
    // PLAYER INPUT HANDLERS (QUEUES ACTION)
    // ==========================================================

    /**
     * Queues an Action (Punch, Throw Furniture, or Skill).
     */
    public String queueAction(String actionName) {
        if (!isActionAvailable(actionName)) {
            return "You do not have the gift required for " + actionName + ".";
        }

        int spCost = getSkillSpCost(actionName);
        if (player.currentSP < spCost) {
            return actionName + " requires " + spCost + " SP. Not enough SP!";
        }

        player.currentSP -= spCost;
        playerQueuedAction = actionName;
        return actionName + " queued. Press [Execute] to proceed.";
    }

    /**
     * Queues the Defend action.
     */
    public String queueDefend() {
        // Set the flag that halves incoming damage for this turn
        playerIsDefending = true;
        playerQueuedAction = "Defend";
        return "Defend queued. Incoming damage will be halved this turn. Press [Execute] to proceed.";
    }

    /**
     * Queues an Item use and executes the item effect immediately.
     */
    public String queueItem(String itemName) {
        if (!itemInventory.containsKey(itemName) || itemInventory.get(itemName) <= 0) {
            return "You do not have any " + itemName + " left.";
        }

        itemInventory.put(itemName, itemInventory.get(itemName) - 1);

        // Execute item effect immediately
        String result = executeItemEffect(itemName);

        // Queue "Pass" to signify the player's turn is done.
        playerQueuedAction = "Pass";

        return result + ". Press [Execute] to proceed to Miguel's move.";
    }

    // ==========================================================
    // HARDCODED EFFECT EXECUTION
    // ==========================================================

    /**
     * Calculates damage dealt to Miguel based on base damage and player multipliers.
     */
    private String dealDamage(int baseDamage, double damageMultiplier, String actionName) {
        int damage = (int) (baseDamage * damageMultiplier);

        // Steak Knife bonus check is handled in its specific handler

        // Apply damage to Miguel
        miguel.currentHP -= damage;

        return String.format("You used %s, dealing %d damage to Miguel!", actionName, damage);
    }

    // --- SKILL / ACTION HARDCODE ---
    private String handleSkill_Knowledge() {
        return "You use Knowledge. You feel aware of Miguel's next intent (Look at the UI).";
    }

    private String handleSkill_HamonHairband() {
        // High base damage (50)
        return dealDamage(50, player.dmgMult, "Hamon Hairband");
    }

    private String handleSkill_Concert() {
        // Deal 10 damage and restore 15 HP.
        player.currentHP = Math.min(player.maxHP, player.currentHP + 15);
        String damageResult = dealDamage(10, player.dmgMult, "Concert");
        return damageResult + " The concert invigorates you and heals 15 HP.";
    }

    private String handleSkill_Towel() {
        // Heals 20 HP.
        player.currentHP = Math.min(player.maxHP, player.currentHP + 20);
        return "You used the Towel! Feeling refreshed and healed 20 HP.";
    }

    private String handleSkill_Bicycle() {
        // Grants +20 DEF for 1 turn.
        // First, revert any previous temporary buff if it exists (though EndOfTurn should clear it)
        if (defenseBuffDuration > 0) {
            player.def -= playerTempDefense;
            playerTempDefense = 0;
            defenseBuffDuration = 0;
        }

        playerTempDefense = 20;
        defenseBuffDuration = 1; // Lasts for the remainder of this turn, removed at cleanup
        player.def += 20;

        return "You rode the Bicycle! Player Defense is greatly boosted this turn (+20 DEF).";
    }

    private String handleSkill_SteakKnife() {
        int baseDamage = 40;
        String actionName = "Steak Knife";
        int damage = (int) (baseDamage * player.dmgMult);

        // Steak Knife bonus check: +50% damage if Miguel is over 50% HP
        double hpPercent = (double) miguel.currentHP / miguel.maxHP;
        if (hpPercent > 0.5) {
            damage = (int) (damage * 1.5);
            actionName += " (Critical Bonus)";
        }

        miguel.currentHP -= damage;
        return String.format("You used %s, dealing %d damage to Miguel!", actionName, damage);
    }

    private String handleSkill_Stare() {
        // 75% chance to Stun Miguel for 1 turn.
        if (random.nextDouble() <= 0.75) {
            miguelStunDuration = 1;
            return "You Stared down Miguel! He is stunned and cannot move next turn!";
        } else {
            return "You Stared down Miguel! But he resisted the effect.";
        }
    }

    // --- ITEM EXECUTION (Immediate effect) ---
    private String executeItemEffect(String itemName) {
        String message = "You used " + itemName + ". ";
        switch (itemName) {
            case "Miguel's Cake":
                player.currentHP = player.maxHP;
                player.currentSP = player.maxSP;
                return message + "You feel great! Max HP and SP restored!";
            case "Big M’":
                player.currentHP = Math.min(player.maxHP, player.currentHP + 50);
                return message + "Heals 50 HP.";
            case "Banana":
                player.currentHP = Math.min(player.maxHP, player.currentHP + 25);
                return message + "Heals 25 HP.";
            case "Can of Beans":
                player.currentSP = Math.min(player.maxSP, player.currentSP + 50);
                return message + "Restores 50 SP.";
            case "Gula Melaka":
                player.currentSP = Math.min(player.maxSP, player.currentSP + 25);
                return message + "Restores 25 SP.";
            case "Lenarian Melon":
                // Grants +10 Def for 2 turns.

                // Clear and reset previous temporary buff if it exists
                if (defenseBuffDuration > 0) {
                    player.def -= playerTempDefense;
                }

                playerTempDefense = 10;
                defenseBuffDuration = 2; // Lasts for 2 turns
                player.def += 10;
                return message + "Player Defense boosted by 10 for 2 turns!";
            default:
                return "Unknown item: " + itemName + ". Nothing happened.";
        }
    }

    // ==========================================================
    // UTILITY & GETTERS
    // ==========================================================

    /**
     * Helper to check if an action is unlocked. Defaults (Punch/Throw Furniture) are always available.
     */
    private boolean isActionAvailable(String actionName) {
        if (actionName.equals("Punch") || actionName.equals("Throw Furniture")) {
            return true; // Always available
        }

        switch (actionName) {
            case "Knowledge": return tracker.actions.hasKnowledge;
            case "Hamon Hairband": return tracker.actions.hasHamonHairband;
            case "Concert": return tracker.actions.hasConcert;
            case "Towel": return tracker.actions.hasTowel;
            case "Bicycle": return tracker.actions.hasBicycle;
            case "Steak Knife": return tracker.actions.hasSteakKnife;
            case "Stare": return tracker.actions.hasStare;
            default: return false;
        }
    }

    /**
     * Helper to get the SP cost of an action.
     */
    private int getSkillSpCost(String actionName) {
        switch (actionName) {
            case "Punch": return 0;
            case "Throw Furniture": return 10;
            case "Knowledge": return 0;
            case "Hamon Hairband": return 30;
            case "Concert": return 15;
            case "Towel": return 10;
            case "Bicycle": return 10;
            case "Steak Knife": return 20;
            case "Stare": return 25;
            default: return 0;
        }
    }

    // --- Accessors for UI / State Machine ---

    public Player getPlayer() { return player; }
    public Miguel getMiguel() { return miguel; }
    public String getBattleMessage() { return battleMessage; }
    public Map<String, Integer> getItemInventory() { return itemInventory; }
    public GiftTracker getGiftTracker() { return tracker; }
    public boolean getPlayerIsDefending() { return playerIsDefending; }
    public int getMiguelStunDuration() { return miguelStunDuration; }


    /**
     * Returns a list of all unlocked action names (skills + defaults)
     * for the Action menu in the UI.
     */
    public List<String> getUnlockedActions() {
        List<String> actions = new ArrayList<>();
        actions.add("Punch"); // Default
        actions.add("Throw Furniture"); // Default

        if (tracker.actions.hasKnowledge) actions.add("Knowledge");
        if (tracker.actions.hasHamonHairband) actions.add("Hamon Hairband");
        if (tracker.actions.hasConcert) actions.add("Concert");
        if (tracker.actions.hasTowel) actions.add("Towel");
        if (tracker.actions.hasBicycle) actions.add("Bicycle");
        if (tracker.actions.hasSteakKnife) actions.add("Steak Knife");
        if (tracker.actions.hasStare) actions.add("Stare");

        return actions;
    }
}