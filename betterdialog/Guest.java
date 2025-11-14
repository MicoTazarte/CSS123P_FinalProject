package com.ramolete.betterdialog;

import javafx.scene.image.Image;
import java.util.List;

public class Guest {
    private final String name;
    private final String nameID;
    private final Image guestImage;
    private final String gift;
    private final boolean isGoodGuest;

    // =======================
    // DIALOG INITIALIZE
    // =======================

    private final int dialogChunks; // 1, 2, or 3
    private final List<String> choices; // Name of Choices [s1, s2, t1, t2, a, r]

    // Level 1: Always plays
    private final List<String> dialogIntro;

    // Level END (Final): Always plays at the end
    private final List<String> dialogAccept;
    private final List<String> dialogReject;

    // Level 2 (Conditional Branching)
    private final List<String> dialogSecondary_1;
    private final List<String> dialogSecondary_2;

    // Level 3 (Deeper Conditional Branching)
    private final List<String> dialogThird_1;
    private final List<String> dialogThird_2;

    // =======================
    // CONSTRUCTOR
    // =======================

    public Guest(String name, String nameID, Image guestImage, String gift, boolean isGoodGuest,
                 int dialogChunks, List<String> choices, List<String> dialogIntro,
                 List<String> dialogAccept, List<String> dialogReject,
                 List<String> dialogSecondary_1, List<String> dialogSecondary_2,
                 List<String> dialogThird_1, List<String> dialogThird_2) {

        // --- 1. Validate 'dialogChunks' ---
        if (dialogChunks < 1 || dialogChunks > 3) {
            throw new IllegalArgumentException(
                    "Guest '" + name + "' error: dialogChunks must be 1, 2, or 3. Found: " + dialogChunks);
        }

        // --- 2. Validate 'choices' List Size based on 'dialogChunks' ---
        int expectedChoices = dialogChunks * 2;
        if (choices == null || choices.size() != expectedChoices) {
            String errorMessage = String.format(
                    "Guest '%s' error: dialogChunks=%d requires exactly %d choices. Found: %d",
                    name, dialogChunks, expectedChoices, (choices == null ? 0 : choices.size()));
            throw new IllegalArgumentException(errorMessage);
        }

        // --- 3. Validate Presence of Necessary Secondary/Third Dialogs ---

        if (dialogChunks < 2 && (dialogSecondary_1 != null || dialogSecondary_2 != null)) {
            // Optional: Prevent data being present when it shouldn't be
            System.err.println("Warning: dialogChunks < 2, but Secondary dialogs are present for " + name);
        }

        if (dialogChunks >= 2) {
            if (dialogSecondary_1 == null || dialogSecondary_2 == null) {
                throw new IllegalArgumentException(
                        "Guest '" + name + "' error: dialogChunks >= 2 requires dialogSecondary_1 and _2 to be non-null.");
            }
        }

        if (dialogChunks == 3) {
            if (dialogThird_1 == null || dialogThird_2 == null) {
                throw new IllegalArgumentException(
                        "Guest '" + name + "' error: dialogChunks = 3 requires dialogThird_1 and _2 to be non-null.");
            }
        }

        // --- 4. Assignment (Only runs if no exceptions were thrown) ---

        this.name = name;
        this.nameID = nameID;
        this.guestImage = guestImage;
        this.gift = gift;
        this.isGoodGuest = isGoodGuest;

        // Dialogue fields
        this.dialogChunks = dialogChunks;
        this.choices = choices;
        this.dialogIntro = dialogIntro;
        this.dialogAccept = dialogAccept;
        this.dialogReject = dialogReject;
        this.dialogSecondary_1 = dialogSecondary_1;
        this.dialogSecondary_2 = dialogSecondary_2;
        this.dialogThird_1 = dialogThird_1;
        this.dialogThird_2 = dialogThird_2;
    }

    // =======================
    // GETTERS
    // =======================

    public String getName() { return name; }
    public String getNameID() { return nameID; }
    public Image getGuestImage() { return guestImage; }
    public String getGift() { return gift; }
    public boolean isGoodGuest() { return isGoodGuest; }

    // --- Dialogue Getters (The meat of the problem) ---

    public int getDialogChunks() { return dialogChunks; }
    public List<String> getChoices() { return choices; }
    public List<String> getDialogIntro() { return dialogIntro; }
    public List<String> getDialogAccept() { return dialogAccept; }
    public List<String> getDialogReject() { return dialogReject; }

    // Conditional branches
    public List<String> getDialogSecondary_1() { return dialogSecondary_1; }
    public List<String> getDialogSecondary_2() { return dialogSecondary_2; }
    public List<String> getDialogThird_1() { return dialogThird_1; }
    public List<String> getDialogThird_2() { return dialogThird_2; }
}
