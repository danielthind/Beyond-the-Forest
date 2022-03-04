package com.coffeepizza.beyondtheforest.sprite.util;

import com.coffeepizza.beyondtheforest.levelscreen.levels.LevelScript;

public class GeneralTimer {

    // System
    private boolean debugging = true;
    private String tag = "TriggerTimer";
    private String message = "";

    // Manager

    // Timer
    private float timeLeft;

    // Trigger
    public boolean complete = false;

    public GeneralTimer(float timeLeft) {
        this.timeLeft = timeLeft;
    }

    public void update(float dt) {
        if (!complete) {
            timeLeft -= dt;

            if (timeLeft < 0) {
                complete = true;
            }
        }
    }

    public boolean isCompleted() {
        return complete;
    }
}