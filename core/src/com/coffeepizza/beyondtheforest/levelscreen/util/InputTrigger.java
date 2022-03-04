package com.coffeepizza.beyondtheforest.levelscreen.util;

import com.coffeepizza.beyondtheforest.levelscreen.levels.LevelScript;

public class InputTrigger {

    // System
    private boolean debugging = true;
    private String tag = "TriggerTimer";
    private String message = "";

    // Manager
    private LevelScript screen;

    // Trigger
    public boolean complete = false;
    private int trigger;

    // Type
    public enum inputType {
        DIALOGUE,
        EXAMPLE
    }
    private inputType type;


    public InputTrigger(LevelScript screen, int trigger, inputType type) {
        this.screen = screen;
        this.trigger = trigger;
        this.type = type;
    }

    public void update() {
        if (!complete) {

            if (type == inputType.DIALOGUE && screen.dialogueBoxPressed()) {
                complete = true;
                screen.setQueuedTrigger(trigger);
            }
        }
    }

    public boolean isCompleted() {
        return complete;
    }
}
