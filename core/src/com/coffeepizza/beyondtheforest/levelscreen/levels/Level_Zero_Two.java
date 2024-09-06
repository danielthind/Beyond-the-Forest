package com.coffeepizza.beyondtheforest.levelscreen.levels;

import com.badlogic.gdx.Gdx;
import com.coffeepizza.beyondtheforest.BeyondManager;
import com.coffeepizza.beyondtheforest.levelscreen.GameCamera;
import com.coffeepizza.beyondtheforest.levelscreen.util.HUD;
import com.coffeepizza.beyondtheforest.levelscreen.util.InputTrigger;
import com.coffeepizza.beyondtheforest.levelscreen.LevelScreen;

public class Level_Zero_Two extends LevelScript {

    // System
    private boolean debugging = false;
    private String tag = "Lvl_02";
    private String message = "";

    // Manager
//    private int level = 2;
    private LevelScreen screen;

    public Level_Zero_Two(BeyondManager manager, LevelScreen screen) {
        super(manager, screen);
        this.screen = screen;
        this.thisLevel = tag;
        super.loadSecrets();
        super.level = 2;
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

            // Game Mode
            initCutscene(true);

            // HUD
            fadeSetScreenToOpaque();
            fadeOpaqueToVisible();

            // Camera
            // Target
            // Zoom
            cameraSetZoomDistance(GameCamera.ZOOM_DISTANCE.CLOSEUP);

            // Sound
            // Player
            setPlayerAutorunDirection(true, true);
            setPlayerRun();

            if (debugging) { Gdx.app.log(tag, "Completed startCheckpoint trigger " + startCheckpoint); }
        } else if (startCheckpoint == -2) {
            // Triggers
            trigger = 2;
            currentTrigger = trigger;
            // Triggers
            createTimerTrigger(2.0f, 2);

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

            // Game Mode
            // HUD
            // Cameraa
            cameraSetZoomRate(GameCamera.ZOOM_RATE.SLOW);
            cameraSetZoomDistanceTarget(GameCamera.ZOOM_DISTANCE.NORMAL);

            // Sound
            // Player
        }
        else if (trigger == 2 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createInputTrigger(3, InputTrigger.inputType.DIALOGUE);

            // Game Mode
            // HUD
            displayTextPrompt(localisedText.getText(-100), //"TAP TO START",
                    HUD.SPEAKER.NARRATOR
            );
            flashAttack();
            flashJump();
            flashChangeDir();

            // Camera
            // Sound
            // Player
            setPlayerIdle();
        }
        else if (trigger == 3 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();

            // Game Mode
            initGameplay(true);

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