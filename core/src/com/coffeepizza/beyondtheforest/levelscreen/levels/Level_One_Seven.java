package com.coffeepizza.beyondtheforest.levelscreen.levels;

import com.badlogic.gdx.Gdx;
import com.coffeepizza.beyondtheforest.BeyondManager;
import com.coffeepizza.beyondtheforest.levelscreen.GameCamera;
import com.coffeepizza.beyondtheforest.levelscreen.LevelScreen;
import com.coffeepizza.beyondtheforest.levelscreen.util.HUD;
import com.coffeepizza.beyondtheforest.levelscreen.util.InputTrigger;

public class Level_One_Seven extends LevelScript {

    // System
    private boolean debugging = false;
    private String tag = "Lvl_17";
    private String message = "";

    // Manager
    private LevelScreen screen;

    public Level_One_Seven(BeyondManager manager, LevelScreen screen) {
        super(manager, screen);
        this.screen = screen;
        this.thisLevel = tag;
        super.loadSecrets();
        super.level = 17;
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
            setBlackFade(false);
            fadeSetScreenToOpaque();
            fadeOpaqueToVisible();

            // Camera
            cameraSetZoomDistance(GameCamera.ZOOM_DISTANCE.CLOSEUP);
            cameraSetZoomRate(GameCamera.ZOOM_RATE.SLOW);
            cameraSetZoomDistanceTarget(GameCamera.ZOOM_DISTANCE.NORMAL);
            cameraSetYOffset(GameCamera.POSITION_OFFSET.OFFSET_NORMAL);

            // Target
            // Zoom
            // Sound
            // Player
            setPlayerAutorunDirection(true, true);
            setPlayerRun();

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
            iterateTrigger();

            // Game Mode
            // HUD
            // Camera
            // Sound
            // Player
            setPlayerAutorunDirection(false, true);
        }
        else if (trigger == 2 && currentTrigger(trigger)) {
            // Triggers
            // Triggers
            iterateTrigger();

            // Game Mode
            // HUD
            // Camera
            // Sound
            // Player
            setPlayerAutorunDirection(true, true);
        }
        else if (trigger == 3 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            //createTimerTrigger(10f, 4);

            // Game Mode
            // HUD
            // Camera
            // Sound
            // Player
            setPlayerIdle();
        }

        else if (trigger == 10) {
            // Triggers
            // Game Mode
            // HUD
            displayTextPrompt(localisedText.getText(1),
                    HUD.SPEAKER.NARRATOR);

            // Camera
            // Sound
            // Player
        }
        else if (trigger == 11) {
            // Triggers
            // Game Mode
            // HUD
            updateTextPrompt(localisedText.getText(2),
                    HUD.SPEAKER.NARRATOR);

            // Camera
            // Sound
            // Player
        }
        else if (trigger == 12) {
            // Triggers
            // Game Mode
            // HUD
            updateTextPrompt(localisedText.getText(3),
                    HUD.SPEAKER.NARRATOR);

            // Camera
            // Sound
            // Player
        }
        else if (trigger == 13) {
            // Triggers
            // Game Mode
            // HUD
            updateTextPrompt(localisedText.getText(4),
                    HUD.SPEAKER.NARRATOR);

            // Camera
            // Sound
            // Player
        }
        else if (trigger == 14) {
            // Triggers
            createTimerTrigger(4f, 15);
            // Game Mode
            // HUD
            updateTextPrompt(localisedText.getText(5),
                    HUD.SPEAKER.NARRATOR);

            // Camera
            // Sound
            // Player
        }
        else if (trigger == 15) {
            // Triggers
            createTimerTrigger(7f, 16);
            // Game Mode
            // HUD
            removeTextPrompt();

            // Camera
            // Sound
            // Player
            setPlayerSleep();
        }
        else if (trigger == 16) {
            // Triggers
            createTimerTrigger(4f, 17);
            // Game Mode
            // HUD
            setBlackFade(true);
            fadeVisibleToOpaque();

            // Camera
            // Sound
            // Player
        }
        else if (trigger == 17) {
            // Triggers
            // Game Mode
            prefs.putInteger("currentLevel" , 17);
            prefs.putInteger("unlockedLevels" , 17);

            prefs.flush();

            //levelCompleted(thisLevel);
            manager.loadNewScreen(BeyondManager.OVERWORLD);

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