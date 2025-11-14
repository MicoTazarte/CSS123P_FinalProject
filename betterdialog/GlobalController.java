package com.ramolete.betterdialog;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class GlobalController {
    // ==========================================================
    // 1. SINGLETON SETUP (Ensures only ONE instance exists)
    // ==========================================================

    private static GlobalController instance;

    // used for bad guest events
    private final Random random = new Random();

    private final GuestDatabase guestDatabase;

    public GlobalController() {
        // Initialization happens here, once.
        this.guestDatabase = new GuestDatabase();
        // We'll initialize the GuestDatabase here later.
        this.guestList = this.guestDatabase.getGuestList();
    }

    public static GlobalController getInstance() {
        if (instance == null) {
            // If it doesn't exist, create it.
            instance = new GlobalController();
        }
        // Always return the single, existing instance.
        return instance;
    }

    // ==========================================================
    // 2. CORE GAME DATA (This section will be built next)
    // ==========================================================

    // --- GUEST MANAGEMENT ---
    private List<Guest> guestList;                  // The master queue of guests waiting to be checked (Jayk is always first)
    private Guest currentGuest = null;

    // --- PARTY / INVENTORY TRACKING ---
    private final List<Guest> guestsInParty = new ArrayList<>();  // Good guests let inside
    private final List<String> presentList = new ArrayList<>();   // Presents collected (e.g., "Banana", "BigM Snacks")
    private String lastEventMessage = "Someone is at the door..."; // Message shown on the door scene

    // --- DIALOGUE STATE TRACKING (Crucial for the VN flow) ---
    private int dialoguePhase = 0;                  // 0=Intro, 1=Secondary1, 2=Secondary2, 3=FinalChoice
    private boolean isDialogueFinal = false;

    // --- GAME STATE AND SETTINGS ---
    private double playTime = 0.0;                  // Timer for overall playtime
    private boolean isFullscreen = false;           // Display setting
    private boolean inRPGBattle = false;            // Are we in the RPG phase?
    private boolean inDecision = true;
    private boolean isPaused = false;

    // --- PLACEHOLDER SETTINGS ---
    private int volumeLevel = 100;

    // ==========================================================
    // 3. FLOW LOGIC & GETTERS (Will be built in Phase 1.3)
    // ==========================================================

    public void resetDialogueState() {
        this.dialoguePhase = 0;
        this.isDialogueFinal = false;
        //this.lastEventMessage = null;
    }

    public void advanceToNextGuest() {
        if (!guestList.isEmpty()) {
            this.currentGuest = guestList.remove(0); // Take the first guest off the list (Jayk is always first)
            resetDialogueState();
            this.inDecision = true;
            System.out.println("Starting dialogue with: " + this.currentGuest.getName());
        } else {
            // !!!!!!!!!!!!!!!!!!!!!!!!!!!!
            // TODO : End of Decision Phase, transition to next phase
            // !!!!!!!!!!!!!!!!!!!!!!!!!!!!
            this.inDecision = false;
            this.inRPGBattle = true;
            System.out.println("All guests checked. Transitioning to RPG Battle!");
        }
    }

    public List<String> getCurrentDialogueList() {
        if (currentGuest == null) return List.of("ERROR: No guest loaded.");
        if (this.isDialogueFinal) {
            // This is handled by the setFinalDialogue method which returns the list directly.
            // This method should not be called when isDialogueFinal is true.
            return currentGuest.getDialogAccept(); // Default fall back, but indicates flow error.
        }

        // --- Multi-Chunk Dialogue Flow ---
        switch (this.dialoguePhase) {
            case 0:
                return currentGuest.getDialogIntro();
            case 1:
                return currentGuest.getDialogSecondary_1();
            case 2:
                return currentGuest.getDialogSecondary_2();
            case 3:
                return currentGuest.getDialogThird_1(); // Assuming phase 3 uses the third dialogue list
            case 4:
                return currentGuest.getDialogThird_2(); // Assuming phase 4 uses the fourth dialogue list
            default:
                // If phase is beyond what is defined, it means it's time for the FINAL CHOICE
                return currentGuest.getChoices(); // The UI will interpret this as buttons!
        }
    }

    public void advanceDialoguePhase() {
        // We only advance if we haven't hit the phase where the final choice should be made.
        if (this.dialoguePhase < currentGuest.getDialogChunks()) {
            this.dialoguePhase++;
        }
        // If dialoguePhase == dialogChunks, the UI should present the FINAL CHOICE buttons.
    }

    public List<String> setFinalDialogue(boolean accepted) {
        this.isDialogueFinal = true;
        return accepted ? currentGuest.getDialogAccept() : currentGuest.getDialogReject();
    }

    public void acceptCurrentGuest() {
        if (currentGuest == null) return;

        System.out.println(this.currentGuest.getName() + " was ACCEPTED.");

        // --- CHANGE 1: Check isGoodGuest() ---
        if (currentGuest.isGoodGuest()) {
            // Good Guest
            System.out.println(this.currentGuest.getName() + " added to party.");
            this.guestsInParty.add(currentGuest);
            // --- CHANGE 2: Use getGift() ---
            this.presentList.add(currentGuest.getGift());

            this.lastEventMessage = String.format(
                    "%s just gave you a gift: %s.",
                    currentGuest.getName(),
                    currentGuest.getGift() // Use getGift()
            );
        } else {
            // Bad Guest (isGoodGuest() is false)
            System.out.println(this.currentGuest.getName() + " is bad guest.");
            handleBadGuestConsequence(currentGuest);
        }
        this.currentGuest = null;

        advanceToNextGuest();
    }

    public void rejectCurrentGuest() {
        if (currentGuest == null) return;
        System.out.println(this.currentGuest.getName() + " was REJECTED.");
        this.lastEventMessage = String.format("%s was rejected and left the door.", currentGuest.getName());
        this.currentGuest = null;
        advanceToNextGuest();
    }

    private void handleBadGuestConsequence(Guest badGuest) {
        // ... (This method remains the same, as the logic flow is correct) ...

        if (this.guestsInParty.isEmpty()) {
            this.lastEventMessage = badGuest.getName() + " caused a ruckus but found no one to hurt. They left empty-handed.";
            return;
        }

        // 1. Randomly select a victim from the party
        int victimIndex = random.nextInt(this.guestsInParty.size());
        Guest victim = this.guestsInParty.remove(victimIndex);

        // 2. Remove their corresponding present (gift)
        String stolenGift = this.presentList.remove(victimIndex); // It's still a list operation

        // 3. Set the message for the doorScene display
        this.lastEventMessage = String.format(
                "%s sabotaged %s gift! You lost their gift: %s.",
                badGuest.getName(),
                victim.getName(),
                stolenGift // Use the 'stolenGift' name for clarity
        );
    }

    // ==========================================================
    // 4. GETTERS (Accessors for the UI)
    // ==========================================================

    public Guest getCurrentGuest() { return currentGuest; }
    public String getLastEventMessage() { return lastEventMessage; }
    public boolean isInDecision() { return inDecision; }
    public List<Guest> getGuestsInParty() { return guestsInParty; }
    public List<String> getPresentList() { return presentList; }
    public int getDialoguePhase() { return dialoguePhase; }
    public boolean isDialogueFinal() { return isDialogueFinal; }
    public int getDialogChunks() {
        return currentGuest != null ? currentGuest.getDialogChunks() : 0;
    }

    public boolean isPaused() { return isPaused; }
    public void setPaused(boolean paused) { this.isPaused = paused; }

    public boolean toggleFullscreen() {
        this.isFullscreen = !this.isFullscreen;
        return this.isFullscreen;
    }
    public boolean isFullscreen() {
        return isFullscreen;
    }

    // Might remove
    public void setLastEventMessage(String message) {
        this.lastEventMessage = message;
    }

}
