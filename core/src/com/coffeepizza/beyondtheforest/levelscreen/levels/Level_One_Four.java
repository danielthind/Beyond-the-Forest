package com.coffeepizza.beyondtheforest.levelscreen.levels;

import com.badlogic.gdx.Gdx;
import com.coffeepizza.beyondtheforest.BeyondManager;
import com.coffeepizza.beyondtheforest.levelscreen.GameCamera;
import com.coffeepizza.beyondtheforest.levelscreen.LevelScreen;
import com.coffeepizza.beyondtheforest.levelscreen.util.HUD;
import com.coffeepizza.beyondtheforest.levelscreen.util.InputTrigger;

public class Level_One_Four extends LevelScript {

    // System
    private boolean debugging = true;
    private String tag = "Lvl_14";
    private String message = "";

    // Manager
    private LevelScreen screen;

    public Level_One_Four(BeyondManager manager, LevelScreen screen) {
        super(manager, screen);
        this.screen = screen;
        this.thisLevel = tag;
        super.loadSecrets();
        super.level = 14;
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
            // Game Mode
            initCutscene(false);

            // HUD
            fadeSetScreenToOpaque();
            fadeOpaqueToVisible();

            // Camera
            // Target
            // Zoom
            // Sound
            // Player
            setPlayerAutorunDirection(false, true);
            setPlayerRun();

            if (debugging) { Gdx.app.log(tag, "Completed startCheckpoint trigger " + startCheckpoint); }
        } else if (startCheckpoint == -2) {
            // Triggers
            trigger = 1;
            currentTrigger = trigger;

            // Triggers
            // Game Mode
            initCutscene(false);

            // HUD
            fadeSetScreenToOpaque();
            fadeOpaqueToVisible();

            // Camera
            // Target
            // Zoom
            // Sound
            // Player
            setPlayerAutorunDirection(false, true);
            setPlayerRun();

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
            // Camera
            // Sound
            // Player
            setPlayerAutorunDirection(true, true);
            setPlayerRun();

            // Golem
        }
        else if (trigger == 2 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createTimerTrigger(2f, 3);

            // Game Mode
            // HUD
            // Camera
            cameraShake(0.5f, GameCamera.CAMERA_SHAKE.LIGHT);

            // Sound
            // Player
            setPlayerAutorunDirection(true, false);
            setPlayerIdle();

            // Golem
        }
        else if (trigger == 3 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createTimerTrigger(4f, 4);

            // Game Mode
            // HUD
            // Camera
            cameraShake(5f, GameCamera.CAMERA_SHAKE.EXTRA_LIGHT);
            cameraSetTargetting(GameCamera.camTargets.GOLEM);
            cameraSetLerpAlpha(GameCamera.LERP_ALPHA.LERP_SLOW);

            // Sound
            // Player
            // Golem
            setGolemRise();
        }
        else if (trigger == 4 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createTimerTrigger(4f, 5);

            // Game Mode
            // HUD
            displayTextPrompt(localisedText.getText(1),
                    HUD.SPEAKER.DEKU);

            // Camera
            // Sound
            // Player
            // Golem
        }
        else if (trigger == 5 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createTimerTrigger(4f, 6);

            // Game Mode
            // HUD
            updateTextPrompt(localisedText.getText(2),
                    HUD.SPEAKER.DEKU);

            // Camera
            // Sound
            // Player
            // Golem
        }
        else if (trigger == 6 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createTimerTrigger(4f, 7);

            // Game Mode
            // HUD
            updateTextPrompt(localisedText.getText(3),
                    HUD.SPEAKER.DEKU);

            // Camera
            // Sound
            // Player
            // Golem
        }
        else if (trigger == 7 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createTimerTrigger(4f, 8);

            // Game Mode
            // HUD
            updateTextPrompt(localisedText.getText(4),
                    HUD.SPEAKER.DEKU);

            // Camera
            // Sound
            // Player
            // Golem
        }
        else if (trigger == 8 && currentTrigger(trigger)) {
            // Triggers
            currentTrigger = 50;
            createTimerTrigger(6f, 50);

            // Game Mode
            // HUD
            updateTextPrompt(localisedText.getText(5),
                    HUD.SPEAKER.DEKU);

            // Camera
            // Sound
            // Player
            // Golem
        }

        /** Start tutorial */
        else if (trigger == 50 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();

            // Game Mode
            initGameplay(true);

            // HUD
            removeTextPrompt();

            // Camera
            // Sound
            // Player
            // Golem
        }

        /** Tutorial */
        else if (trigger == 51) {
            // Triggers
            currentTrigger = 52;

            // Game Mode
            // HUD
            removeTextPrompt();
            stopFlashingControls();

            // Camera
            // Sound
            // Player
            // Golem
        }
        else if (trigger == 52 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();

            // Game Mode
            // HUD
            displayTextPrompt(localisedText.getText(52),
                    HUD.SPEAKER.NARRATOR);
            stopFlashingControls();
            flashAttack();

            // Camera
            // Sound
            // Player
            // Golem
        }
        else if (trigger == 53 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();

            // Game Mode
            // HUD
            stopFlashingControls();

            // Camera
            // Sound
            // Player
            // Golem
        }
        else if (trigger == 54 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();

            // Game Mode
            // HUD
            stopFlashingControls();
            flashAttack();

            // Camera
            // Sound
            // Player
            // Golem
        }
        else if (trigger == 55) {
            // Triggers
            currentTrigger = 52;

            // Game Mode
            // HUD
            removeTextPrompt();
            stopFlashingControls();

            // Camera
            // Sound
            // Player
            // Golem
        }

        else if (trigger == 61) {
            // Triggers
            currentTrigger = 62;

            // Game Mode
            // HUD
            removeTextPrompt();
            stopFlashingControls();

            // Camera
            // Sound
            // Player
            // Golem
        }
        else if (trigger == 62 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();

            // Game Mode
            // HUD
            displayTextPrompt(localisedText.getText(62),
                    HUD.SPEAKER.NARRATOR);
            stopFlashingControls();
            flashJump();

            // Camera
            // Sound
            // Player
            // Golem
        }
        else if (trigger == 63 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();

            // Game Mode
            // HUD
            stopFlashingControls();

            // Camera
            // Sound
            // Player
            // Golem
        }
        else if (trigger == 64 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();

            // Game Mode
            // HUD
            stopFlashingControls();
            flashJump();

            // Camera
            // Sound
            // Player
            // Golem
        }
        else if (trigger == 65 && currentTrigger(trigger)) {
            // Triggers
            currentTrigger = 62;

            // Game Mode
            // HUD
            removeTextPrompt();
            stopFlashingControls();

            // Camera
            // Sound
            // Player
            // Golem
        }


        /*
        else if (trigger ==  && currentTrigger(trigger)) {
            // Triggers
            // Game Mode
            // HUD
            // Camera
            // Sound
            // Player
            // Golem
        }
        */
    }
}