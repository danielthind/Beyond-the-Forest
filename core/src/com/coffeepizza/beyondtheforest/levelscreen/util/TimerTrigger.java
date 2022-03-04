package com.coffeepizza.beyondtheforest.levelscreen.util;

import com.badlogic.gdx.Gdx;
import com.coffeepizza.beyondtheforest.levelscreen.levels.LevelScript;
import com.coffeepizza.beyondtheforest.sprite.levelscreen.bosses.AIDirectors.Cyberdyne;

public class TimerTrigger {

    // System
    private boolean debugging = true;
    private String tag = "TriggerTimer";
    private String message = "";

    // Manager
    private LevelScript levelScript;
    private Cyberdyne cyberdyne;
    private boolean isLevelScreen;

    // Timer
    private float timeLeft;

    // Trigger
    public boolean complete = false;
    private int trigger;

    public TimerTrigger(LevelScript screen, float timeLeft, int trigger) {
        this.levelScript = screen;
        this.timeLeft = timeLeft;
        this.trigger = trigger;
        this.isLevelScreen = true;
    }

    public TimerTrigger(Cyberdyne cyberdyne, float timeLeft, int trigger) {
        this.cyberdyne = cyberdyne;
        this.timeLeft = timeLeft;
        this.trigger = trigger;
        this.isLevelScreen = false;
    }

    public void update(float dt) {
        if (!complete) {
            timeLeft -= dt;

            if (timeLeft < 0) {
                complete = true;

                // Action trigger in level or cyberdyne
                if(isLevelScreen) {
                    levelScript.setQueuedTrigger(trigger);
                } else {
                    cyberdyne.actionCyberdyneTrigger(trigger);
                }
            }
        }
    }

    public boolean isCompleted() {
        return complete;
    }
}
