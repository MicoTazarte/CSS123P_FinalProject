package com.ramolete.betterdialog.rpgbattle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Player {
    // Core Stats
    public int maxHP = 99;
    public int currentHP = 99;
    public int def = 0;
    public int maxSP = 99;
    public int currentSP = 99;
    public int spGain = 10; // Base SP gained per turn

    // Damage Multiplier (Permanent buffs like Cleaver, GooGoo Babies debuff)
    public double dmgMult = 1.0;

    // Defending
    public boolean isDefending = false;
    public int cannotBlockRounds = 0;

    // --- TEMPORARY BUFF TRACKING ---
    public int knowledgeRounds = 0;
    public int beansRounds = 0;
    public double tempDmgMultBonus = 0.0;
    public boolean isConcertActive = false;
    public int concertRounds = 0; // rounds left for Concert effect
    public boolean isTowelActive = false;
    public int towelRounds = 0;   // rounds left for Towel effect

    // Tracks the remaining stock for the Throw Furniture move
    public List<String> furnitureInventory = new ArrayList<>();
    public static final Map<String, Integer> FURNITURE_DAMAGE = Map.of(
            "Picture Frame", 35,
            "Plant", 45,
            "Chair", 75,
            "Knife", 85,
            "Table", 95,
            "Microwave", 120,
            "Television", 130,
            "Bed", 140,
            "Fridge", 150
    );
    public Player() {
        // Default Furniture Inventory (6 Chairs, 1 Table, 2 Knives, etc.)
        furnitureInventory.add("Chair");
        furnitureInventory.add("Chair");
        furnitureInventory.add("Chair");
        furnitureInventory.add("Chair");
        furnitureInventory.add("Chair");
        furnitureInventory.add("Chair");
        furnitureInventory.add("Table");
        furnitureInventory.add("Knife");
        furnitureInventory.add("Knife");
        furnitureInventory.add("Picture Frame");
        furnitureInventory.add("Picture Frame");
        furnitureInventory.add("Fridge");
        furnitureInventory.add("Plant");
        furnitureInventory.add("Plant");
        furnitureInventory.add("Microwave");
        furnitureInventory.add("Television");
        furnitureInventory.add("Bed");
    }

    public double getTotalDmgMult() {
        return dmgMult + tempDmgMultBonus;
    }
}
