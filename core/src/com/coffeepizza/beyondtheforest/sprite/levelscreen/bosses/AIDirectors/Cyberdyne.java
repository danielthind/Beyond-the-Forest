package com.coffeepizza.beyondtheforest.sprite.levelscreen.bosses.AIDirectors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.coffeepizza.beyondtheforest.BeyondManager;
import com.coffeepizza.beyondtheforest.levelscreen.LevelScreen;
import com.coffeepizza.beyondtheforest.levelscreen.levels.LevelScript;
import com.coffeepizza.beyondtheforest.levelscreen.util.TimerTrigger;
import com.coffeepizza.beyondtheforest.sprite.Actor;
import com.coffeepizza.beyondtheforest.sprite.util.GeneralTimer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public abstract class Cyberdyne {

    // System
    private boolean debugging = false;
    private String tag = "Cyberdyne";
    private String message = "";

    // Manager
    protected BeyondManager manager;
    protected LevelScreen screen;
    protected World world;
    protected LevelScript scriptManager;

    // State Machine
    public DefaultStateMachine stateMachine;
    protected String currentState = "NONE";
    protected String lastState = "NONE";

    // Trigger
    protected TreeMap<Integer, Vector2> bossTriggers = new TreeMap<Integer, Vector2>();

    // Timers
    private List<TimerTrigger> timersList = new ArrayList<>();

    // Minions
    public static List<Actor> minionList = new ArrayList<Actor>();

    public Cyberdyne(BeyondManager manager, LevelScreen screen, World world, LevelScript scriptManager, HashMap unsortedTriggers) {
        this.manager = manager;
        this.screen = screen;
        this.world = world;
        this.scriptManager = scriptManager;

        // Sort triggers into TreeMap
        bossTriggers.putAll(unsortedTriggers);
    }

    public abstract void setCyberdyneLive();
    public abstract void update(float dt);

    public abstract void damageCyberdyne();
    public abstract int getBossHealth();
    public abstract Vector2 getBossPosition();
    public abstract void actionCyberdyneTrigger(int trigger);
    public abstract void draw(Batch batch);

    // Update
    protected void updateCyberdyne(float dt) {
        // State machine
        stateMachine.update();

        // Timers
        updateTimers(dt);
    }

    // Timers
    public void setTimer(float time, int trigger) {
        timersList.add(new TimerTrigger(this, time, trigger));
    }
    protected void updateTimers(float dt) {
        for (TimerTrigger t : timersList) {
            t.update(dt);
        }

        removeOldTriggers();
    }
    protected void removeOldTriggers() {
        boolean goodToClear;

        // Timers
        goodToClear = true;
        for (TimerTrigger t : timersList) {
            if (!t.isCompleted()) {
                goodToClear = t.isCompleted();
            }
        }
        if (goodToClear) { timersList.clear(); }
    }

    // LevelScript Triggers
    public void actionScreenTrigger(int trigger) {
        screen.scriptManager.actionTrigger(trigger);
    }
}