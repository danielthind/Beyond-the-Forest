package com.coffeepizza.beyondtheforest.levelscreen.levels;

import com.badlogic.gdx.Gdx;
import com.coffeepizza.beyondtheforest.BeyondManager;
import com.coffeepizza.beyondtheforest.levelscreen.GameCamera;
import com.coffeepizza.beyondtheforest.levelscreen.LevelScreen;
import com.coffeepizza.beyondtheforest.levelscreen.util.HUD;

public class Level_Zero_Eight extends LevelScript {

    // System
    private boolean debugging = false;
    private String tag = "Lvl_08";
    private String message = "";

    // Manager
    private LevelScreen screen;

    public Level_Zero_Eight(BeyondManager manager, LevelScreen screen) {
        super(manager, screen);
        this.screen = screen;
        this.thisLevel = tag;
        super.loadSecrets();
        super.level = 8;
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
            cameraSetZoomDistance(GameCamera.ZOOM_DISTANCE.NEAR);

            // Sound
            // Player
            setPlayerAutorunDirection(true, true);
            setPlayerRun();

            if (debugging) { Gdx.app.log(tag, "Completed startCheckpoint trigger " + startCheckpoint); }
        } else if (startCheckpoint == -2) {
            // Triggers
            trigger = 50;
            currentTrigger = trigger;
            // Triggers
            // Triggers
            createTimerTrigger(1f, 50);

            // Game Mode
            initGameplay(true);

            // HUD
            fadeSetScreenToOpaque();
            fadeOpaqueToVisible();

            // Camera
            // Sound
            // Player
            // Golem
            setGolemRise();

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
            cameraSetZoomRate(GameCamera.ZOOM_RATE.SLOW);
            cameraSetZoomDistanceTarget(GameCamera.ZOOM_DISTANCE.NORMAL);

            // Sound
            // Player

            // Debug
        }
        if (trigger == 2 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createTimerTrigger(2.0f, currentTrigger);

            // Game Mode
            // HUD
            // Camera
            cameraShake(0.25f, GameCamera.CAMERA_SHAKE.EXTRA_LIGHT);

            // Sound
            // Player
            setPlayerAutorunDirection(true, false);
            setPlayerIdle();
        }
        if (trigger == 3 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();

            // Game Mode
            // HUD
            // Camera
            // Sound
            // Player
            setPlayerAutorunDirection(true, true);
            setPlayerRun();
        }
        if (trigger == 4 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createTimerTrigger(2.0f, currentTrigger);

            // Game Mode
            // HUD
            // Camera
            cameraShake(1.0f, GameCamera.CAMERA_SHAKE.EXTRA_LIGHT);

            // Sound
            // Player
            setPlayerAutorunDirection(true, false);
            setPlayerIdle();
        }
        if (trigger == 5 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createTimerTrigger(2.0f, currentTrigger);

            // Game Mode
            // HUD
            // Camera
            cameraShake(1.0f, GameCamera.CAMERA_SHAKE.EXTRA_LIGHT);

            // Sound
            // Player
            cameraSetLerpAlpha(GameCamera.LERP_ALPHA.LERP_SLOWEST);
            cameraSetYOffset(GameCamera.POSITION_OFFSET.OFFSET_NONE);
            cameraSetTargetting(GameCamera.camTargets.GOLEM);
        }
        if (trigger == 6 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createTimerTrigger(4.0f, currentTrigger);

            // Game Mode
            // HUD
            // Camera
            cameraShake(1.0f, GameCamera.CAMERA_SHAKE.EXTRA_LIGHT);

            // Sound
            // Player
        }
        if (trigger == 7 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createTimerTrigger(3.0f, currentTrigger);

            // Game Mode
            // HUD
            // Camera
            cameraShake(5.0f, GameCamera.CAMERA_SHAKE.EXTRA_LIGHT);

            // Sound
            // Player
            // Boss / Golem
            setGolemRise();
        }
        if (trigger == 8 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createTimerTrigger(3.0f, currentTrigger);

            // Game Mode
            // HUD
            displayTextPrompt(localisedText.getText(1),
                    HUD.SPEAKER.DEKU
            );

            // Camera
            // Sound
            // Player
            // Boss / Golem
        }
        if (trigger == 9 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createTimerTrigger(3.0f, currentTrigger);

            // Game Mode
            // HUD
            updateTextPrompt(localisedText.getText(2),
                    HUD.SPEAKER.DEKU
            );

            // Camera
            // Sound
            // Player
            // Boss / Golem
        }
        if (trigger == 10 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createTimerTrigger(2.0f, currentTrigger);

            // Game Mode
            // HUD
            updateTextPrompt(localisedText.getText(3),
                    HUD.SPEAKER.DEKU
            );

            // Camera
            // Sound
            // Player
            // Boss / Golem
        }
        if (trigger == 11 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createTimerTrigger(3.0f, currentTrigger);

            // Game Mode
            // HUD
            updateTextPrompt(localisedText.getText(4),
                    HUD.SPEAKER.DEKU
            );

            // Camera
            // Sound
            // Player
            // Boss / Golem
        }
        if (trigger == 12 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createTimerTrigger(4.0f, currentTrigger);

            // Game Mode
            // HUD
            updateTextPrompt(localisedText.getText(5),
                    HUD.SPEAKER.DEKU
            );

            // Camera
            // Sound
            // Player
            // Boss / Golem
        }
        if (trigger == 13 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createTimerTrigger(6.0f, currentTrigger);

            // Game Mode
            // HUD
            updateTextPrompt(localisedText.getText(6),
                    HUD.SPEAKER.DEKU
            );

            // Camera
            // Sound
            // Player
            // Boss / Golem
        }
        if (trigger == 14 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createTimerTrigger(6.0f, currentTrigger);

            // Game Mode
            // HUD
            updateTextPrompt(localisedText.getText(7),
                    HUD.SPEAKER.DEKU
            );

            // Camera
            // Sound
            // Player
            // Boss / Golem
        }
        if (trigger == 15 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createTimerTrigger(6.0f, currentTrigger);

            // Game Mode
            // HUD
            updateTextPrompt(localisedText.getText(8),
                    HUD.SPEAKER.DEKU
            );

            // Camera
            // Sound
            // Player
            // Boss / Golem
        }
        if (trigger == 16 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createTimerTrigger(6.0f, currentTrigger);

            // Game Mode
            // HUD
            updateTextPrompt(localisedText.getText(9),
                    HUD.SPEAKER.DEKU
            );

            // Camera
            // Sound
            // Player
            // Boss / Golem
        }
        if (trigger == 17 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createTimerTrigger(2.0f, currentTrigger);

            // Game Mode
            // HUD
            updateTextPrompt(localisedText.getText(10),
                    HUD.SPEAKER.DEKU
            );

            // Camera
            // Sound
            // Player
            // Boss / Golem
        }
        if (trigger == 18 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createTimerTrigger(6.0f, currentTrigger);

            // Game Mode
            // HUD
            updateTextPrompt(localisedText.getText(11),
                    HUD.SPEAKER.DEKU
            );

            // Camera
            // Sound
            // Player
            // Boss / Golem
        }
        if (trigger == 19 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createTimerTrigger(6.0f, currentTrigger);

            // Game Mode
            // HUD
            updateTextPrompt(localisedText.getText(12),
                    HUD.SPEAKER.DEKU
            );

            // Camera
            // Sound
            // Player
            // Boss / Golem
        }
        if (trigger == 20 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createTimerTrigger(2.0f, currentTrigger);

            // Game Mode
            // HUD
            updateTextPrompt(localisedText.getText(13),
                    HUD.SPEAKER.DEKU
            );

            // Camera
            // Sound
            // Player
            // Boss / Golem
        }
        if (trigger == 21 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createTimerTrigger(6.0f, currentTrigger);

            // Game Mode
            // HUD
            updateTextPrompt(localisedText.getText(14),
                    HUD.SPEAKER.DEKU
            );

            // Camera
            // Sound
            // Player
            // Boss / Golem
        }
        if (trigger == 22 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createTimerTrigger(6f, 23);

            // Game Mode
            // HUD
            updateTextPrompt(localisedText.getText(15),
                    HUD.SPEAKER.DEKU
            );

            // Camera
            // Sound
            // Player
            // Boss / Golem
        }
        if (trigger == 23 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createTimerTrigger(6f, 24);


            // Game Mode
            // HUD
            updateTextPrompt(localisedText.getText(16),
                    HUD.SPEAKER.DEKU
            );

            // Camera
            // Sound
            // Player
            // Boss / Golem
        }
        if (trigger == 24 && currentTrigger(trigger)) {
            // Triggers
            currentTrigger = 50;
            createTimerTrigger(1f, 50);


            // Game Mode
            // HUD
            removeTextPrompt();

            // Camera
            // Sound
            // Player
            // Boss / Golem
        }

        /** Tutorial */
        else if (trigger == 50 && currentTrigger(trigger)) {
            // Triggers

            // Game Mode
            initGameplay(true);

            // HUD
            // Camera
            // Sound
            // Player
            // Golem
            currentTrigger = 51;
        }
        else if (trigger == 51) {
            // Triggers
            currentTrigger = 52;

            // Game Mode
            // HUD
            stopFlashingControls();
            removeTextPrompt();

            // Camera
            // Sound
            // Player
            // Golem

        }
        else if (trigger == 52) {
            // Triggers
            currentTrigger = 53;

            // Game Mode
            // HUD
            displayTextPrompt(localisedText.getText(52),
                    HUD.SPEAKER.NARRATOR);
            stopFlashingControls();

            // Camera
            // Sound
            // Player
            // Golem
        }
        else if (trigger == 53) {
            // Triggers
            // Game Mode
            // HUD
            stopFlashingControls();
            flashJump();

            // Camera
            // Sound
            // Player
            // Golem
        }
        else if (trigger == 54) {
            // Triggers
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
            currentTrigger = 63;

            // Game Mode
            // HUD
            displayTextPrompt(localisedText.getText(62),
                    HUD.SPEAKER.NARRATOR);
            stopFlashingControls();
            flashAttack();

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
        else if (trigger == 65) {
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
        if (trigger == 1 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createInputTrigger(2, InputTrigger.inputType.DIALOGUE);

            // Game Mode
            // HUD
            displayTextPrompt("TAP TO START",
                    HUD.SPEAKER.NARATOR
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
            // Golem
        }
        */
    }
}