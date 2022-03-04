package com.coffeepizza.beyondtheforest.levelscreen.levels;

import com.badlogic.gdx.Gdx;
import com.coffeepizza.beyondtheforest.BeyondManager;
import com.coffeepizza.beyondtheforest.levelscreen.GameCamera;
import com.coffeepizza.beyondtheforest.levelscreen.util.HUD;
import com.coffeepizza.beyondtheforest.levelscreen.util.InputTrigger;
import com.coffeepizza.beyondtheforest.levelscreen.LevelScreen;

public class Level_Zero_One extends LevelScript {

    // System
    private boolean debugging = false;
    private String tag = "Lvl_01";
    private String message = "";

    // Manager
//    private int level = 1;
    private LevelScreen screen;

    public Level_Zero_One(BeyondManager manager, LevelScreen screen) {
        super(manager, screen);
        this.screen = screen;
        this.thisLevel = tag;
        super.loadSecrets();
        super.level = 1;
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
            createTimerTrigger(1.0f, 1);

            // Game Mode
            initCutscene(true);

            // HUD
            fadeSetScreenToOpaque();

            // Camera
            // Target
            cameraSetTargetAndPosition(GameCamera.camTargets.CARROT);
            cameraSetLerpAlpha(GameCamera.LERP_ALPHA.LERP_SLOW);
            // Zoom
            cameraSetZoomDistance(GameCamera.ZOOM_DISTANCE.CLOSEUP);
            cameraSetZoomRate(GameCamera.ZOOM_RATE.NORMAL);

            // Sound
            // Player
            setPlayerAutorunDirection(true, false);
            setPlayerSleep();

            if (debugging) { Gdx.app.log(tag, "Completed startCheckpoint trigger " + startCheckpoint); }
        } else if (startCheckpoint == -2) {
            // Triggers
            trigger = 20;
            currentTrigger = trigger;
            // Triggers
            createTimerTrigger(2.0f, 20);

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
            createTimerTrigger(2.0f, trigger + 1);

            // Game Mode
            // HUD
            displayTextPrompt(localisedText.getText(1),
                    HUD.SPEAKER.DEKU);
            // Camera
            // Sound
            // Player
            if (debugging) { Gdx.app.log(tag, "Completed trigger " + trigger); }
        }
        else if (trigger == 2 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createTimerTrigger(4.0f, trigger + 1);

            // Game Mode
            // HUD
            // Camera
            fadeOpaqueToVisible();

            // Sound
            // Player
        }
        else if (trigger == 3 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createTimerTrigger(6.0f, trigger + 1);

            // Game Mode
            // HUD
            updateTextPrompt(localisedText.getText(2),
                    HUD.SPEAKER.DEKU);

            // Camera
            cameraSetTargetting(GameCamera.camTargets.PLAYER);

            // Sound
            // Player
        }
        else if (trigger == 4 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createTimerTrigger(6.0f, trigger + 1);

            // Game Mode
            // HUD
            updateTextPrompt(localisedText.getText(3),
                    HUD.SPEAKER.DEKU);

            // Camera
            cameraSetZoomRate(GameCamera.ZOOM_RATE.SLOW);
            cameraSetZoomDistanceTarget(GameCamera.ZOOM_DISTANCE.NEAR);
            // Sound
            // Player
        }
        else if (trigger == 5 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createTimerTrigger(6.0f, trigger + 1);

            // Game Mode
            // HUD
            updateTextPrompt(localisedText.getText(4),
                    HUD.SPEAKER.DEKU);

            // Camera
            // Sound
            // Player
        }
        else if (trigger == 6 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createTimerTrigger(6.0f, trigger + 1);

            // Game Mode
            // HUD
            updateTextPrompt(localisedText.getText(5),
                    HUD.SPEAKER.DEKU);

            // Camera
            // Sound
            // Player
        }
        else if (trigger == 7 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createTimerTrigger(0.25f, trigger + 1);

            // Game Mode
            // HUD
            updateTextPrompt(localisedText.getText(6),
                    HUD.SPEAKER.DEKU);

            // Camera
            cameraShake(0.5f, GameCamera.CAMERA_SHAKE.LIGHT);

            // Sound
            // Player
        }
        else if (trigger == 8 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createTimerTrigger(1.75f, trigger + 1);

            // Game Mode
            // HUD
            // Camera
            // Sound
            // Player
            setPlayerAutorunDirection(true, false);
            setPlayerIdle();
        }
        else if (trigger == 9 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createTimerTrigger(2.0f, trigger + 1);

            // Game Mode
            // HUD
            updateTextPrompt(localisedText.getText(7),
                    HUD.SPEAKER.DEKU);
            // Camera
            // Sound
            // Player
        }
        else if (trigger == 10 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();

            // Game Mode
            initGameplay(true);

            // HUD
            removeTextPrompt();

            // Camera
            // Sound
            // Player
        }
        else if (trigger == 11) {
            // Triggers
            // Game Mode
            // HUD
            displayTextPrompt(localisedText.getText(50),
                    HUD.SPEAKER.NARRATOR
            );
            flashChangeDir();

            // Camera
            // Sound
            // Player
        }


        else if (trigger == 12) {
            // Triggers
            // Game Mode
            // HUD
            displayTextPrompt(localisedText.getText(51),
                    HUD.SPEAKER.NARRATOR
            );
            flashJump();

            // Camera
            // Sound
            // Player
        }
        else if (trigger == 13) {
            // Triggers
            // Game Mode
            // HUD
            displayTextPrompt(localisedText.getText(52),
                    HUD.SPEAKER.NARRATOR
            );
            flashAttack();

            // Camera
            // Sound
            // Player
        }
        else if (trigger == 14) {
            // Triggers
            // Game Mode
            // HUD
            displayTextPrompt(localisedText.getText(53),
                    HUD.SPEAKER.NARRATOR
            );
            flashMenu();

            // Camera
            // Sound
            // Player
        }

        if (trigger == 20 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createInputTrigger(21, InputTrigger.inputType.DIALOGUE);

            // Game Mode
            // HUD
            displayTextPrompt(localisedText.getText(-100),
                    HUD.SPEAKER.NARRATOR
            );

            // Camera
            // Sound
            // Player
            setPlayerIdle();
        }
        else if (trigger == 21 && currentTrigger(trigger)) {
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