package com.coffeepizza.beyondtheforest.levelscreen.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.coffeepizza.beyondtheforest.BeyondManager;
import com.coffeepizza.beyondtheforest.levelscreen.*;
import com.coffeepizza.beyondtheforest.levelscreen.util.HUD;
import com.coffeepizza.beyondtheforest.levelscreen.util.InputTrigger;
import com.coffeepizza.beyondtheforest.levelscreen.util.TimerTrigger;
import com.coffeepizza.beyondtheforest.sprite.levelscreen.player.stateMachines.PlayerStateMachine;

import java.util.ArrayList;
import java.util.List;

public abstract class LevelScript {

    // System
    private boolean debugging = false;
    private String tag = "";
    private String message = "";

    // Manager and Save
    protected int level;
    protected BeyondManager manager;
    private LevelScreen screen;
    protected Preferences prefs;
    protected String thisLevel;
    protected LocalisedText localisedText;

    // Save Data
    protected int startCheckpoint;
//    private int farthestCheckoint;

    // Triggers
    protected int trigger;
    protected int tapTrigger = -100;    // -100 is a junk trigger
    protected int currentTrigger;

    // Timer Triggers
    private List<TimerTrigger> timerList = new ArrayList<>();
    private List<InputTrigger> inputList = new ArrayList<>();

    // Queued Traiiger
    private int queuedTrigger = -100;   // -100 is a junk trigger
    private boolean queueIsEmpty = true;

    public LevelScript (BeyondManager manager, LevelScreen screen) {
        this.manager = manager;
        this.screen = screen;

        // Load preferences
        prefs = manager.prefs;
        localisedText = new LocalisedText(manager, screen);

//        loadSecrets();
//        prefs =  Gdx.app.getPreferences("Beyond Data");
    }

    /** Load and Save */
    protected void checkPreviouslyPlayed(String levelString) {
        boolean playedBefore = prefs.getBoolean(levelString + "_playedBefore", false);

        if (debugging) { Gdx.app.log(this.tag, "playedBefore: " + playedBefore); }

        if (playedBefore) {
            startCheckpoint = prefs.getInteger(levelString + "_start");
        } else {
            startCheckpoint = -1;
//            farthestCheckoint = -1;

            prefs.putBoolean(levelString + "_playedBefore", true);
            prefs.putInteger(levelString + "_start", startCheckpoint);
//            prefs.putInteger(levelString + "_farthest", farthestCheckoint);
        }
        // Persist prefs
        prefs.flush();
    }
        /** End level save */
    protected void levelCompleted(String levelString) {
        if (debugging) { manager.printPrefs("Before reset:" + levelString); }

        // Update unlocked levels
        int unlocked = prefs.getInteger("unlockedLevels");
        Gdx.app.log(tag, "--- unlocked levels information ---\n"
                + "unlocked levels in save: " + unlocked + "\n"
                + "new number of unlocked levels: " + (level + 1) + "\n"
                + "levelString: " + levelString + "\n"
                + "--- ---"
        );
        if (unlocked == level) {
            prefs.putInteger("unlockedLevels", level + 1);
        }
//        prefs.putInteger("unlockedLevels", prefs.getInteger("unlockedLevels") + 1);

        // Reset checkpoint
        prefs.putBoolean(levelString + "_playedBefore", true);
        prefs.putInteger(levelString + "_start", -1);
//        prefs.putInteger(levelString + "_farthest", -1);

        // Persist prefs
        prefs.flush();

        if (debugging) { manager.printPrefs("After reset:" + levelString); }
    }
    protected void setupLocalisedText() {
        localisedText.setLevelAndInitTexts(level);

        if (debugging) { Gdx.app.log(tag, "Loaclised text language level set to : " + level); }
    }

        /** Mid level save */
    protected void saveLevel(String levelString) {
        // Save data
        // Reset checkpoint
        prefs.putBoolean(levelString + "_playedBefore", true);
        prefs.putInteger(levelString + "_start", startCheckpoint);
//        prefs.putInteger(levelString + "_farthest", farthestCheckoint);

        // Persist prefs
        prefs.flush();
    }

    protected void gameCompleted() { }

    /** Secrets */
    public void loadSecrets() {
        boolean found;
        String s = thisLevel + "_secret1";
        found  = prefs.getBoolean(thisLevel + "_secret1", false);
        screen.secretBone1 = found;

        found  = prefs.getBoolean(thisLevel + "_secret2", false);
        screen.secretBone2 = found;

        found  = prefs.getBoolean(thisLevel + "_secret3", false);
        screen.secretBone3 = found;

        found  = prefs.getBoolean(thisLevel + "_secret4", false);
        screen.secretBone4 = found;
    }
    public void saveSecret(int secretNumber) {
        if (debugging) { manager.printPrefs("Secret found BEFORE"); }

        prefs.putBoolean(thisLevel + "_secret" + secretNumber, true);
        prefs.flush();

        if (debugging) { manager.printPrefs("Secret found AFTER"); }
    }

    /** Checkpoint setting */
    public void setStartLocation() {
        // Get start checkpoint location
        float x = screen.checkpointLocations.get(startCheckpoint).x;
        float y = screen.checkpointLocations.get(startCheckpoint).y;

        // Set palyers start location
        screen.setStartCoordinates(x, y);
    }
    public void recordCheckpoint(int checkpoint) {
        int farthestCheckoint = prefs.getInteger(thisLevel + "_start");

        // Check if this is a farther checkpoint but not lower than -4 (checkpoint limit)
        if (checkpoint < farthestCheckoint && checkpoint > -5) {
            prefs.putInteger(thisLevel + "_start", checkpoint);
        }

        prefs.flush();
    }
    public abstract void checkpointSetup();

    public abstract void actionTrigger(int trigger);

    public void update(float dt) {
        // Timers Triggers
        for (TimerTrigger t : timerList) {
            t.update(dt);
        }
        if (!queueIsEmpty) { actionQueuedTriggers(); }

        // Input Triggers
        for (InputTrigger i : inputList) {
            i.update();
        }
        if (!queueIsEmpty) { actionQueuedTriggers(); }

        // Clear old triggers
        removeOldTriggers();
    }

    public void setQueuedTrigger(int t) {
        queueIsEmpty = false;
        queuedTrigger = t;
    }
    public void actionQueuedTriggers() {
        actionTrigger(queuedTrigger);
        queuedTrigger = -100;
        queueIsEmpty = true;
    }
    protected void actionPresetTriggers(int trigger) {
        /** TODO: Decided to use this or not */
        /*
            NEUTRAL
            STOPPED
            SLEEP
            GROWL
            RUN
            JUMP_CHARGE
            JUMP_ASCEND
            JUMP_DESCEND
            DOUBLE_JUMP_ACCEND
            DOUBLE_JUMP_DECEND
            JUMP_LANDING
            ATTACK_DASH
            LONGJUMP_ACCENDING
            ATTACK_JUMP_ACCEND
            ATTACK_JUMP_DECEND
            ATTACK_JUMP_LANDING
            ATTACK_DIVE
            DAMAGE_KNOCKBACK
            DAMAGE_LANDING
            DEATH_LANDING
         */
        /** Player States */
        if (trigger == -50) {
            // Stopped
            setPlayerJump();
        }
        else if (trigger == -49) {
            setPlayerAttackDash();
        }
        else if (trigger == -39) {
            setPlayerIdle();
        }
        else if (trigger == -38) {
            setPlayerChangeDirection();
        }
        else if (trigger == -29) {
            setPlayerFrozen();
        }

        /** UI */
        if (trigger == -11) {
            // Triggers
            // Game Mode
            // HUD
            removeTextPrompt();
            stopFlashingControls();

            // Camera
            // Sound
            // Player
        }

        /** End Level */
        else if (trigger == -5) {
            /** End Level - run right */
            // Triggers
            createTimerTrigger(2.0f, -8);

            // Game Mode
            initCutscene(true);

            // HUD
            fadeVisibleToOpaque();

            // Camera
            // Sound
            // Player
            setPlayerAutorunDirection(true, true);
            setPlayerRun();
        }
        else if (trigger == -6) {
            /** End Level - run left */
            // Triggers
            createTimerTrigger(2.0f, -8);

            // Game Mode
            initCutscene(false);

            // HUD
            fadeVisibleToOpaque();

            // Camera
            // Sound
            // Player
            setPlayerAutorunDirection(false, true);
            setPlayerRun();
        }
        else if (trigger == -8) {
            /** Level Complete */
            // Triggers
            // Game Mode
            levelCompleted(thisLevel);
//            manager.setOverworld();
            manager.loadNewScreen(BeyondManager.OVERWORLD);

            // HUD
            // Camera
            // Sound
            // Player
        }
        else if (trigger == -9) {
            /** Game Complete */
            /*
            // Triggers
            // Game Mode
            // HUD
            displayTextPrompt("TAP TO START",
                    HUD.SPEAKER.NARATOR
            );
            flashAttack();
            flashJump();

            // Camera
            // Sound
            // Player
            */

            manager.restartLevelThroughOverworld(17);
        }

        /** Death */
        else if (trigger == -100) {
            // Triggers
            createTimerTrigger(1.0f, -101);

            // Game Mode
            initCutscene(true);
        }
        else if (trigger == -101) {
            // Triggers
            createTimerTrigger(2.0f, -102);

            // HUD
            fadeVisibleToOpaque();
        }
        else if (trigger == -102) {
            if (debugging) {
                Gdx.app.log(tag, "Level: " + level);
                manager.printPrefs("Death save");
            }

            manager.restartLevelThroughOverworld(level);
        }

        /** Damage */
        else if (trigger == -110) {
            /** Spike damage */

            if (screen.player.runningRight) {
                screen.player.enemyRight = true;
            } else {
                screen.player.enemyRight = false;
            }

            screen.player.triggerDamage(-1);

            // Shake camera
            screen.camera.shakeCamera(1.0f, GameCamera.CAMERA_SHAKE.LIGHT);
        }
    }

    /** Setup */
    protected void initCutscene(boolean runningRight) {
        // Game Mode
        screen.gameMode = LevelScreen.GameMode.CUTSCENE;                            // Gameplay mode

        // HUD
        screen.hud.showLetterboxing();

        //Camera
        cameraSetYOffset(GameCamera.POSITION_OFFSET.OFFSET_NONE);

        // Sound
        // Player
    }
    protected void initGameplay(boolean runninRight) {
        // Game Mode
        screen.gameMode = LevelScreen.GameMode.PLAY;                                // Gameplay mode

        // HUD
        screen.hud.hideLetterboxing();

        // Camera
            // Targetting
        cameraSetTargetting(GameCamera.camTargets.PLAYER);
        cameraSetYOffset(GameCamera.POSITION_OFFSET.OFFSET_NORMAL);
        cameraSetLerpAlpha(GameCamera.LERP_ALPHA.LERP_NORMAL);
            // Zoom
        cameraSetZoomDistanceTarget(GameCamera.ZOOM_DISTANCE.NORMAL);
        cameraSetZoomRate(GameCamera.ZOOM_RATE.NORMAL);

        // Sound
        // Player
        setPlayerAutorunDirection(runninRight, true);
        setPlayerRun();
    }
    protected void iterateTrigger() {
        currentTrigger++;
    }

    /** Input Checks */
    public boolean dialogueBoxPressed() {
        return screen.dialogueBoxPressed();
    }

    /** Checks */
    protected boolean currentTrigger(int i) {
        boolean isCurrentTrigger = false;

        if (i == currentTrigger) {
            isCurrentTrigger = true;
        }

        return isCurrentTrigger;
    }

    /** Triggers */
        // Timers
    protected void createTimerTrigger(float timeLeft, int trigger) {
        timerList.add(new TimerTrigger(this, timeLeft, trigger));
    }
        // Input
    protected void createInputTrigger(int trigger, InputTrigger.inputType type) {
        inputList.add((new InputTrigger(this, trigger, type)));
    }
        // Clear
    private void removeOldTriggers() {
        boolean goodToClear;

        // Timers
        goodToClear = true;
        for (TimerTrigger t : timerList) {
            if (!t.isCompleted()) {
                goodToClear = t.isCompleted();
            }
        }
        if (goodToClear) { timerList.clear(); }

        // Inputs
        goodToClear = true;
        for (InputTrigger i : inputList) {
            if (!i.isCompleted()) {
                goodToClear = i.isCompleted();
            }
        }
        if (goodToClear) { inputList.clear(); }
    }

    /** Camera */
        // Position
    protected void cameraSetTargetAndPosition(GameCamera.camTargets target) { screen.camera.setTargetAndPosition(target); }
    protected void cameraSetTargetting(GameCamera.camTargets target) {
        screen.camera.setTargetting(target);
    }
    protected void cameraSetLerpAlpha(GameCamera.LERP_ALPHA alpha) { screen.camera.setLerpAlpha(alpha);}
    protected void cameraSetYOffset(GameCamera.POSITION_OFFSET offset) {
        screen.camera.setOffset(offset);
    }
        // Zoom
    protected void cameraSetZoomDistance(GameCamera.ZOOM_DISTANCE zoom) {
        screen.camera.setZoom(zoom);
    }
    protected void cameraSetZoomDistanceTarget(GameCamera.ZOOM_DISTANCE targetZoom) {
        screen.camera.setZoomTarget(targetZoom);
    }
    protected void cameraSetZoomRate(GameCamera.ZOOM_RATE rate) {
        screen.camera.setZoomRate(rate);
    }
        // Shake
    protected void cameraShake(float duration, GameCamera.CAMERA_SHAKE magnitude) {
        screen.camera.shakeCamera(duration, magnitude);
    }

    /** Text */
    /**
     * @param text - 45 characters per line, up to 7 lines
     * @param speaker - set to colour based on speaker
     */
    protected void displayTextPrompt(String text, HUD.SPEAKER speaker) {
        screen.hud.promptBoxOpen(text, speaker);
    }
    protected void updateTextPrompt(String text, HUD.SPEAKER speaker) {
        screen.hud.promptBoxUpdateText(text, speaker);
    }
    protected void removeTextPrompt() { screen.hud.promptBoxClose(); }

    /** UI */
        // Controls
    protected void stopFlashingControls() { screen.hud.stopFlashingControls(); }
    protected void flashJump() { screen.hud.flashJump(); }
    protected void flashAttack() { screen.hud.flashAttack(); }
    protected void flashChangeDir() { screen.hud.flashChangeDir(); }
    protected void flashMenu() { screen.hud.flashMenu(); }
        // Fades
    protected void fadeOpaqueToVisible() {
        screen.hud.fadeOpaqueToVisible();
    }
    protected void fadeVisibleToOpaque() {
        screen.hud.fadeVisibleToOpaque();
    }
    protected void fadeSetScreenToOpaque() { screen.hud.fadeSetScreenToOpaque(); }
    protected void fadeSetScreenToVisible() { screen.hud.fadeSetScreenToVisible(); }
    protected void setBlackFade(boolean blackFade) { screen.hud.setBlackFade(blackFade);}


    /** Player Methods */
    protected void setPlayerSleep() {
        screen.player.stateMachine.changeState(PlayerStateMachine.SLEEP);
    }
    protected void setPlayerIdle() {
        screen.player.stateMachine.changeState(PlayerStateMachine.NEUTRAL);
    }
    protected void setPlayerFrozen() {
        //if (!screen.player.isFrozen) {
            screen.player.stateMachine.changeState(PlayerStateMachine.FROZEN);
        //}
    }
    protected void setPlayerGrowl ()  {
        screen.player.stateMachine.changeState(PlayerStateMachine.GROWL);
    }
    protected void setPlayerStopped() {
        /** TODO: Use this for freeze pads */
    }
    protected void setPlayerRun() {
        screen.player.stateMachine.changeState(PlayerStateMachine.RUN);
    }
    protected void setPlayerJump() {
        screen.player.stateMachine.changeState(PlayerStateMachine.JUMP_CHARGE);
    }
    protected void setPlayerAttackDash() {
        screen.player.stateMachine.changeState(PlayerStateMachine.ATTACK_DASH);
    }
    protected void setPlayerLongJump() {
        screen.player.stateMachine.changeState(PlayerStateMachine.LONGJUMP_ASCEND);
    }
    protected void setPlayerDiveAttack() {
        screen.player.stateMachine.changeState(PlayerStateMachine.ATTACK_DIVE);
    }
    protected void setPlayerChangeDirection() {
        screen.player.changeDirection();
    }
    protected void setPlayerDeath() {
        /** TODO: this */
    }
    protected void setPlayerAutorunDirection(boolean runningRight, boolean autoRunning) {
        screen.player.setRunningRight(runningRight, autoRunning);
        screen.player.directHitCollider();
    }

    /** Golem Methods */
    protected void setGolemRise() { screen.setGolemRise(); }

    /** Boss Methods */
    protected void setCyberdyneLive() { screen.cyberdyne.setCyberdyneLive(); }
    protected void actionCyberdyneTrigger(int triggerNo) { screen.cyberdyne.actionCyberdyneTrigger(triggerNo); }

    /** Bouncers or Freezers */
        /** TODO: For freeze pads use player state STOPPED */
    protected void hitFreezerBouncer() {}
    protected void hitUpBouncer() {}
    protected void hitDownBouncer() {}
}
