package com.ramolete.betterdialog.rpgbattle;

import com.ramolete.betterdialog.GlobalController;
import java.util.List;

/**
 * Tracks which combat rewards (Perm, Item, Action) the player has unlocked
 * based on the collected gift names in the GlobalController.
 * This class uses simple boolean flags for quick lookup in the BattleSystem.
 */
public class GiftTracker {

    private final List<String> presentList;

    // Inner classes to logically group the unlockable categories
    public final Permanent permanent = new Permanent();
    public final Items items = new Items();
    public final Actions actions = new Actions();

    /**
     * Initializes the GiftTracker by checking the GlobalController's present list.
     * All boolean flags are set here.
     */
    public GiftTracker(GlobalController controller) {
        this.presentList = controller.getPresentList();

        // --- Process All Gifts ---
        permanent.checkGifts(presentList);
        items.checkGifts(presentList);
        actions.checkGifts(presentList);
    }

    // ==========================================================
    // INNER CLASS 1: PERMANENT BONUSES (Perm)
    // Applied at the start of battle to Player's maxHP, def, etc.
    // ==========================================================
    public class Permanent {
        public boolean hasCleaver = false;
        public boolean hasPipeBomb = false;
        public boolean hasGooGooBabies = false;
        public boolean hasCrewmateCostume = false;
        public boolean hasObsessed = false;
        public boolean hasCubesBlessing = false;

        private void checkGifts(List<String> list) {
            hasCleaver = list.contains("Cleaver");
            hasPipeBomb = list.contains("Pipe Bomb");
            hasGooGooBabies = list.contains("Goo-Goo Babies");
            hasCrewmateCostume = list.contains("Crewmate Costume");
            hasObsessed = list.contains("Obsessed");
            hasCubesBlessing = list.contains("Cube's Blessing");
        }
    }

    // ==========================================================
    // INNER CLASS 2: CONSUMABLE ITEMS (Item)
    // Grants an item to the player's inventory for use in battle.
    // ==========================================================
    public class Items {
        public boolean hasMiguelsCake = false;
        public boolean hasBigM = false;
        public boolean hasBanana = false;
        public boolean hasCanOfBeans = false;
        public boolean hasGulaMelaka = false;
        public boolean hasLenarianMelon = false;

        private void checkGifts(List<String> list) {
            hasMiguelsCake = list.contains("Miguel's Cake");
            hasBigM = list.contains("Big M’");
            hasBanana = list.contains("Banana");
            hasCanOfBeans = list.contains("Can of Beans");
            hasGulaMelaka = list.contains("Gula Melaka");
            hasLenarianMelon = list.contains("Lenarian Melon");
        }
    }

    // ==========================================================
    // INNER CLASS 3: SPECIAL SKILLS (Actions)
    // Unlocks a special move available in the battle menu.
    // ==========================================================
    public class Actions {
        public boolean hasKnowledge = false;
        public boolean hasHamonHairband = false;
        public boolean hasConcert = false;
        public boolean hasTowel = false;
        public boolean hasBicycle = false;
        public boolean hasSteakKnife = false;
        public boolean hasStare = false;

        private void checkGifts(List<String> list) {
            hasKnowledge = list.contains("Knowledge");
            hasHamonHairband = list.contains("Hamon Hairband");
            hasConcert = list.contains("Concert");
            hasTowel = list.contains("Towel");
            hasBicycle = list.contains("Bicycle");
            hasSteakKnife = list.contains("Steak Knife");
            hasStare = list.contains("Stare");
        }
    }
}