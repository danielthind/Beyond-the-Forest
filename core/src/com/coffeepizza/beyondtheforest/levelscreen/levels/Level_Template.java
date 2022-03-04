package com.coffeepizza.beyondtheforest.levelscreen.levels;

import com.badlogic.gdx.Gdx;
import com.coffeepizza.beyondtheforest.BeyondManager;
import com.coffeepizza.beyondtheforest.levelscreen.LevelScreen;

public class Level_Template extends LevelScript {

    // System
    private boolean debugging = false;
    private String tag = "Lvl_Template";
    private String message = "";

    // Manager
    private LevelScreen screen;

    public Level_Template(BeyondManager manager, LevelScreen screen) {
        super(manager, screen);
        this.screen = screen;
        this.thisLevel = tag;
        super.loadSecrets();
        super.level = 0000000000;

        // Check previously played and set start location
        super.checkPreviouslyPlayed(thisLevel);
    }

    @Override
    public void checkpointSetup() {
        if (startCheckpoint == -1) {
            // Triggers
            // Game Mode
            // HUD
            // Camera
            // Sound
            // Player
            if (debugging) { Gdx.app.log(tag, "Completed startCheckpoint trigger " + startCheckpoint); }
        } else if (startCheckpoint == -2) {
            // Triggers
            // Game Mode
            // HUD
            // Camera
            // Sound
            // Player
            if (debugging) { Gdx.app.log(tag, "Completed startCheckpoint trigger " + startCheckpoint); }
        } else if (startCheckpoint == -3) {
            if (debugging) { Gdx.app.log(tag, "Completed startCheckpoint trigger " + startCheckpoint); }
        } else if (startCheckpoint == -4) {
            if (debugging) { Gdx.app.log(tag, "Completed startCheckpoint trigger " + startCheckpoint); }
        }
    }

    @Override
    public void actionTrigger(int trigger) {
        actionPresetTriggers(trigger);

        if (trigger == 1 && currentTrigger(trigger)) {
            // Triggers
            // Game Mode
            // HUD
            // Camera
            // Sound
            // Player
        }
        else if (trigger == 2 && currentTrigger(trigger)) {
            // Triggers
            // Game Mode
            // HUD
            // Camera
            // Sound
            // Player
        }

        /*
        else if (trigger ==  && currentTrigger(trigger)) {
            // Triggers
            // Game Mode
            // HUD
            // Camera
            // Sound
            // Player
        }
        */
    }
}