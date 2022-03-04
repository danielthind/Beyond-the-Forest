package com.coffeepizza.beyondtheforest.levelscreen.levels;

import com.badlogic.gdx.Gdx;
import com.coffeepizza.beyondtheforest.BeyondManager;
import com.coffeepizza.beyondtheforest.levelscreen.GameCamera;
import com.coffeepizza.beyondtheforest.levelscreen.LevelScreen;
import com.coffeepizza.beyondtheforest.levelscreen.util.HUD;

public class Level_One_Six extends LevelScript {

    // System
    private boolean debugging = false;
    private String tag = "Lvl_16";
    private String message = "";

    // Manager
    private LevelScreen screen;

    public Level_One_Six(BeyondManager manager, LevelScreen screen) {
        super(manager, screen);
        this.screen = screen;
        this.thisLevel = tag;
        super.loadSecrets();
        super.level = 16;
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
            initCutscene(true);

            // HUD
            fadeSetScreenToOpaque();
            fadeOpaqueToVisible();

            // Camera
            cameraSetZoomDistanceTarget(GameCamera.ZOOM_DISTANCE.NEAR);
            cameraSetZoomDistance(GameCamera.ZOOM_DISTANCE.NEAR);

            // Target
            // Zoom
            // Sound
            // Player
            setPlayerAutorunDirection(true, true);
            setPlayerRun();

            screen.updateBossHealth();

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

        /** Opening Cutscene
         *      - Wolf enters
         *      - Ground shakes
         *      - Camera pans to Shelob entrance
         *      - Shelob fires egg, camera follows
         *      - Egg lands and maggot hatches
         *      - Timed pause
         *          - Then starts boss fight
         *              - Shelob in inital attack phase
         */
        if (trigger == 1 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createTimerTrigger(1f, 2);

            // Game Mode
            // HUD
            // Camera
            cameraShake(0.25f, GameCamera.CAMERA_SHAKE.LIGHT);

            // Sound
            // Player
            setPlayerGrowl();
        }
        else if (trigger == 2 && currentTrigger(trigger)) {
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
        else if (trigger == 3 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createTimerTrigger(1f, 4);
            // Game Mode
            // HUD
            // Camera
            cameraShake(0.25f, GameCamera.CAMERA_SHAKE.MEDIUM);

            // Sound
            // Player
            setPlayerGrowl();
        }
        else if (trigger == 4 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createTimerTrigger(0.1f, 5);

            // Game Mode
            // HUD
            // Camera
            // Sound
            // Player
            setPlayerRun();
            setPlayerAutorunDirection(true, true);
        }
        else if (trigger == 5 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createTimerTrigger(1f, 6);

            // Game Mode
            // HUD
            // Camera
            cameraShake(0.25f, GameCamera.CAMERA_SHAKE.MEDIUM);

            // Sound
            // Player
            setPlayerGrowl();
        }
        else if (trigger == 6 && currentTrigger(trigger)) {
            // Triggers
            currentTrigger = 50;
            createTimerTrigger(1f, 50);

            // Game Mode
            // HUD
            // Camera
            cameraShake(0.25f, GameCamera.CAMERA_SHAKE.MEDIUM);

            // Sound
            // Player
            // Boss
            setCyberdyneLive();
        }

        /** Shelob enters and begins dialogue */
        else if (trigger == 50 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();

            // Game Mode
            // HUD
            // Camera
            cameraSetTargetting(GameCamera.camTargets.BOSS);
            cameraSetZoomDistanceTarget(GameCamera.ZOOM_DISTANCE.FAR);
            cameraSetLerpAlpha(GameCamera.LERP_ALPHA.LERP_SLOW);

            // Sound
            // Player
            // Boss
        }

        /** Intro dialogue - triggered from Sdyne */
        else if (trigger == 51 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createTimerTrigger(4f, 52);

            // Game Mode
            // HUD
            displayTextPrompt(localisedText.getText(1),
                    HUD.SPEAKER.VILLIAN);

            // Camera
            // Sound
            // Player
            // Boss
        }
        else if (trigger == 52 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createTimerTrigger(4f, 53);

            // Game Mode
            // HUD
            updateTextPrompt(localisedText.getText(2),
                    HUD.SPEAKER.VILLIAN);

            // Camera
            // Sound
            // Player
            // Boss
        }
        else if (trigger == 53 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createTimerTrigger(2f, 54);

            // Game Mode
            // HUD
            updateTextPrompt(localisedText.getText(3),
                    HUD.SPEAKER.VILLIAN);

            // Camera
            // Sound
            // Player
            // Boss
        }
        else if (trigger == 54 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createTimerTrigger(4f, 55);

            // Game Mode
            // HUD
            updateTextPrompt(localisedText.getText(4),
                    HUD.SPEAKER.VILLIAN);

            // Camera
            // Sound
            // Player
            // Boss
        }
        else if (trigger == 55 && currentTrigger(trigger)) {
            // Triggers
            currentTrigger = 100;
            createTimerTrigger(2f, 100);

            // Game Mode
            // HUD
            updateTextPrompt(localisedText.getText(5),
                    HUD.SPEAKER.VILLIAN);

            // Camera
            // Sound
            // Player
            // Boss
        }



        /** Shelob begins attack cycle */
        else if (trigger == 100 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createTimerTrigger(3f, 101); // time to get to player change from 5f

            // Game Mode
            // HUD
            // Camera
            cameraSetTargetting(GameCamera.camTargets.PLAYER);

            // Sound
            // Player
            // Boss
        }
        else if (trigger == 101 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();

            // Game Mode
            initGameplay(true);

            // HUD
            removeTextPrompt();

            // Camera
            cameraSetZoomDistanceTarget(GameCamera.ZOOM_DISTANCE.FAR);
            cameraSetZoomDistance(GameCamera.ZOOM_DISTANCE.FAR);

            // Sound
            // Player
            // Boss
            actionCyberdyneTrigger(2);

            /** Debugging flame */
            //cameraSetZoomRate(GameCamera.ZOOM_RATE.SLOW);
            //actionCyberdyneTrigger(98);
            //cameraSetZoomDistanceTarget(GameCamera.ZOOM_DISTANCE.EXTRA_FAR);
            //cameraSetZoomDistance(GameCamera.ZOOM_DISTANCE.EXTRA_FAR);
        }





        /** Upon Shelob defeat */
        else if (trigger == 200) {
            // Triggers
            currentTrigger = 201;
            createTimerTrigger(1f, 201);

            // Game Mode
            initCutscene(true);

            // HUD
            // Camera
            cameraSetTargetting(GameCamera.camTargets.BOSS);
            cameraShake(15f, GameCamera.CAMERA_SHAKE.HEAVY);

            // Sound
            // Player
            // Boss
        }
        else if (trigger == 201 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createTimerTrigger(6f, 202);

            // Game Mode
            // HUD
            displayTextPrompt(localisedText.getText(50),
                    HUD.SPEAKER.VILLIAN);

            // Camera
            cameraSetZoomDistanceTarget(GameCamera.ZOOM_DISTANCE.NEAR);
            cameraSetZoomRate(GameCamera.ZOOM_RATE.SLOW);
            cameraSetYOffset(GameCamera.POSITION_OFFSET.OFFSET_NEGATIVE_NORMAL);

            // Sound
            // Player
            // Boss
        }
        else if (trigger == 202 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createTimerTrigger(2f, 203);

            // Game Mode
            // HUD
            setBlackFade(false);
            fadeVisibleToOpaque();

            // Camera
            // Sound
            // Player
            // Boss
        }
        else if (trigger == 203 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createTimerTrigger(2f, 204);

            // Game Mode
            // HUD
            removeTextPrompt();

            // Camera
            // Sound
            // Player
            // Boss
        }
        else if (trigger == 204 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createTimerTrigger(4f, 205);

            // Game Mode
            // HUD
            displayTextPrompt(localisedText.getText(75),
                    HUD.SPEAKER.DEKU);

            // Camera
            // Sound
            // Player
            // Boss
        }
        else if (trigger == 205 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createTimerTrigger(1f, 206);

            // Game Mode
            // HUD
            removeTextPrompt();

            // Camera
            // Sound
            // Player
            // Boss
        }
        else if (trigger == 206 && currentTrigger(trigger)) {
            // Triggers
            //iterateTrigger();
            createTimerTrigger(1f, -8);

            //manager.loadCreditsScreen();

            // Game Mode
            //levelCompleted(thisLevel);
            //manager.setCurrentLevel(17);
            //manager.loadNewLevel(17);

            // HUD
            // Camera
            // Sound
            // Player
            // Boss
        }

        /*
        else if (trigger == 51 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createTimerTrigger(1f, 52);

            // Game Mode
            // HUD
            // Camera
            cameraSetLerpAlpha(GameCamera.LERP_ALPHA.LERP_INSTANT);
            cameraSetZoomDistanceTarget(GameCamera.ZOOM_DISTANCE.FAR);

            // Sound
            // Player
        }
        else if (trigger == 52 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createTimerTrigger(4f, 53);

            // Game Mode
            // HUD
            // Camera
            // Sound
            // Player
        }
        else if (trigger == 53 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();

            // Game Mode
            initGameplay(true);
            cameraSetZoomDistanceTarget(GameCamera.ZOOM_DISTANCE.FAR);

            // HUD
            // Camera
            // Sound
            // Player
            // Boss
            //actionCyberdyneTrigger(2);
        }
        */

        /** Upon Shelob defeat *
        else if (trigger == 75) {
            // Triggers
            currentTrigger = 76;
            createTimerTrigger(3f, 76);

            // Game Mode
            initCutscene(true);

            // HUD
            // Camera
            cameraSetTargetting(GameCamera.camTargets.BOSS);
            cameraSetLerpAlpha(GameCamera.LERP_ALPHA.LERP_SLOW);
            cameraSetZoomDistanceTarget(GameCamera.ZOOM_DISTANCE.NORMAL);
            cameraSetZoomRate(GameCamera.ZOOM_RATE.SLOW);

            // Sound
            // Player
            setPlayerAutorunDirection(true, false);
            setPlayerGrowl();

            // Boss
        }
        else if (trigger == 76 && currentTrigger(trigger)) {
            // Triggers
            iterateTrigger();
            createTimerTrigger(4f, 77);

            // Game Mode
            // HUD
            displayTextPrompt("FAILED MY MASTER I HAVE...\n\n" +
                    "SOMEONE ELSE WILL GET YOU\n\n" +
                    "PLACEHOLDER TEXT.. É"
                    ,
                    HUD.SPEAKER.VILLIAN);

            // Camera
            cameraShake(2.00f, GameCamera.CAMERA_SHAKE.EXTRA_LIGHT);

            // Sound
            // Player
            // Boss
        }

        /** End level *
        else if (trigger == 77 && currentTrigger(trigger)) {
            // Triggers
            createTimerTrigger(1f, -5);

            // Game Mode
            // HUD
            removeTextPrompt();

            // Camera
            cameraSetTargetting(GameCamera.camTargets.PLAYER);
            cameraSetLerpAlpha(GameCamera.LERP_ALPHA.LERP_NORMAL);
            cameraSetZoomDistanceTarget(GameCamera.ZOOM_DISTANCE.NORMAL);
            cameraSetZoomRate(GameCamera.ZOOM_RATE.NORMAL);

            // Sound
            // Player
            setPlayerAutorunDirection(true, true);
            setPlayerRun();

            // Boss
        }

        /** Boss Fight - General triggers *
        else if (trigger == 90) {
            // Triggers
            // Game Mode
            // HUD
            // Camera
            cameraShake(2.00f, GameCamera.CAMERA_SHAKE.MEDIUM);

            // Sound
            // Player
            // Boss
        }

        else if (trigger == 20 && currentTrigger(trigger)) {
//            // Triggers
//            iterateTrigger();
//            createInputTrigger(2, InputTrigger.inputType.DIALOGUE);
//
//            // Game Mode
//            // HUD
//            displayTextPrompt("TAP TO START",
//                    HUD.SPEAKER.NARATOR
//            );
//
//            // Camera
//            // Sound
//            // Player
//            setPlayerIdle();
            // Boss
        }
        else if (trigger == 21 && currentTrigger(trigger)) {
//            // Triggers
//            iterateTrigger();
//
//            // Game Mode
//            initGameplay(true);
//
//            // HUD
//            removeTextPrompt();
//            stopFlashingControls();
//
//            // Camera
//            cameraSetZoomDistanceTarget(GameCamera.ZOOM_DISTANCE.FAR);
//            // Sound
//            // Player
            // Boss
        }
        */

        /*
        else if (trigger ==  && currentTrigger(trigger)) {
            // Triggers
            // Game Mode
            // HUD
            // Camera
            // Sound
            // Player
            // Boss
        }
        */
    }
}