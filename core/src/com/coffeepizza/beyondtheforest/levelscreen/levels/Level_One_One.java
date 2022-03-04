package com.coffeepizza.beyondtheforest.levelscreen.levels;

import com.badlogic.gdx.Gdx;
import com.coffeepizza.beyondtheforest.BeyondManager;
import com.coffeepizza.beyondtheforest.levelscreen.LevelScreen;
import com.coffeepizza.beyondtheforest.levelscreen.util.HUD;
import com.coffeepizza.beyondtheforest.levelscreen.util.InputTrigger;

public class Level_One_One extends LevelScript {

    // System
    private boolean debugging = false;
    private String tag = "Lvl_11";
    private String message = "";

    // Manager
    private LevelScreen screen;

    public Level_One_One(BeyondManager manager, LevelScreen screen) {
        super(manager, screen);
        this.screen = screen;
        this.thisLevel = tag;
        super.loadSecrets();
        super.level = 11;
        super.setupLocalisedText();

        // Check previously played and set start location
        super.checkPreviouslyPlayed(thisLevel);
    }

    @Override
    public void checkpointSetup() {
        if (startCheckpoint == -1) {
            // Triggers
            trigger = 1;
            currentTrigger = trigger;
            // Triggers
            createTimerTrigger(2.0f, 1);

            // Game Mode
            initCutscene(true);

            // HUD
            fadeSetScreenToOpaque();
            fadeOpaqueToVisible();

            // Camera
            // Target
            // Zoom
            // Sound
            // Player
            setPlayerAutorunDirection(true, false);
            setPlayerIdle();

            if (debugging) { Gdx.app.log(tag, "Completed startCheckpoint trigger " + startCheckpoint); }
        } else if (startCheckpoint == -2) {
            // Triggers
            trigger = 1;
            currentTrigger = trigger;
            // Triggers
            createTimerTrigger(2.0f, 1);

            // Game Mode
            initCutscene(true);

            // HUD
            fadeSetScreenToOpaque();
            fadeOpaqueToVisible();

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
            iterateTrigger();
            createInputTrigger(2, InputTrigger.inputType.DIALOGUE);

            // Game Mode
            // HUD
            displayTextPrompt("TAP TO START",
                    HUD.SPEAKER.NARRATOR
            );

            // Camera
            // Sound
            // Player
            setPlayerIdle();
        }
        else if (trigger == 2 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();

            // Game Mode
            initGameplay(true);

            // HUD
            // HUD
            removeTextPrompt();
            stopFlashingControls();

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