package com.ramolete.betterdialog.rpgbattle;

public class Miguel {
    // Core Stats
    public int maxHP = 999;
    public int currentHP = 999;
    public int def = 0; // Defense is 0

    // Damage Multiplier (Buffs/Debuffs)
    public int melonRounds = 0;
    public double dmgMult = 1.0;

    // --- State/Debuffs (Managed by BattleSystem) ---

    // Miguel's current state, determined by HP thresholds, which affects his action choices.
    public enum Mode {
        NORMAL, // HP 999 - 667
        ATTACK, // HP 666 - 334
        DEFENSE // HP 333 - 0
    }
    public Mode currentMode = Mode.NORMAL;

    public Miguel() {
        // Initialization if needed
    }
}