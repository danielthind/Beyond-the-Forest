package com.coffeepizza.beyondtheforest.sprite.levelscreen.npcs;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.coffeepizza.beyondtheforest.BeyondManager;
import com.coffeepizza.beyondtheforest.levelscreen.LevelScreen;
import com.coffeepizza.beyondtheforest.sprite.Actor;
import com.coffeepizza.beyondtheforest.sprite.levelscreen.enemies.Ghost;
import com.coffeepizza.beyondtheforest.sprite.levelscreen.enemies.stateMachines.GhostState;
import com.coffeepizza.beyondtheforest.sprite.levelscreen.npcs.stateMachines.GolemState;

public class Golem extends Actor {

    // System
    private boolean debugging = false;
    private String tag = "Zombie";
    private String message = "";

    // Properties
    private boolean rising = false;

    // Textures
    private TextureRegion golemRiseRegion;
    private TextureRegion golemIdleRegion;

    // Animations
    private Animation golemHidden;
    private Animation golemRise;
    private Animation golemIdle;

    // Timers
    private float riseFrameDuration = 0.2f;
    private float riseTimer = 0;

    public Golem(BeyondManager manager, LevelScreen screen, World world, Rectangle rectangle) {
        super(manager, world, screen, "golem_rise", rectangle);
        //super.defineDynamicBody(false);
        super.defineKinematicBody(false);
        super.setupStateMachine();

        // Set start animation region
        golemRiseRegion = new TextureRegion(manager.getAtlas().findRegion("golem_rise"));
        golemIdleRegion = new TextureRegion(manager.getAtlas().findRegion("golem_idle"));
        super.setBounds(0, 0, 74 / BeyondManager.PPM, 57 / BeyondManager.PPM);
        super.setRegion(golemIdleRegion);

        stateTimer = 0;
        setAnimations();

        // State machine
        stateMachine = new DefaultStateMachine<Golem, GolemState>(this, GolemState.HIDDEN);

        elapsedTime = 0;
        runningRight = true;
    }

    @Override
    public void setAnimations() {
        Array<TextureRegion> frames = new Array<TextureRegion>();

        // Hidden
        frames.add(new TextureRegion(golemRiseRegion, 0 * 74, 0, 74, 57));
        golemHidden = new Animation(0.15f, frames);

        // Rise
        frames.add(new TextureRegion(golemRiseRegion, 0 * 74, 0, 74, 57));
        frames.add(new TextureRegion(golemRiseRegion, 1 * 74, 0, 74, 57));
        frames.add(new TextureRegion(golemRiseRegion, 0 * 74, 0, 74, 57));
        frames.add(new TextureRegion(golemRiseRegion, 1 * 74, 0, 74, 57));
        frames.add(new TextureRegion(golemRiseRegion, 2 * 74, 0, 74, 57));
        frames.add(new TextureRegion(golemRiseRegion, 1 * 74, 0, 74, 57));
        frames.add(new TextureRegion(golemRiseRegion, 2 * 74, 0, 74, 57));
        frames.add(new TextureRegion(golemRiseRegion, 1 * 74, 0, 74, 57));
        frames.add(new TextureRegion(golemRiseRegion, 2 * 74, 0, 74, 57));
        frames.add(new TextureRegion(golemRiseRegion, 1 * 74, 0, 74, 57));
        frames.add(new TextureRegion(golemRiseRegion, 2 * 74, 0, 74, 57));
        frames.add(new TextureRegion(golemRiseRegion, 3 * 74, 0, 74, 57));
        frames.add(new TextureRegion(golemRiseRegion, 2 * 74, 0, 74, 57));
        frames.add(new TextureRegion(golemRiseRegion, 1 * 74, 0, 74, 57));
        frames.add(new TextureRegion(golemRiseRegion, 2 * 74, 0, 74, 57));
        frames.add(new TextureRegion(golemRiseRegion, 3 * 74, 0, 74, 57));
        frames.add(new TextureRegion(golemRiseRegion, 2 * 74, 0, 74, 57));
        frames.add(new TextureRegion(golemRiseRegion, 3 * 74, 0, 74, 57));
        frames.add(new TextureRegion(golemRiseRegion, 4 * 74, 0, 74, 57));
        frames.add(new TextureRegion(golemRiseRegion, 3 * 74, 0, 74, 57));
        frames.add(new TextureRegion(golemRiseRegion, 4 * 74, 0, 74, 57));
        frames.add(new TextureRegion(golemRiseRegion, 5 * 74, 0, 74, 57));
        frames.add(new TextureRegion(golemRiseRegion, 4 * 74, 0, 74, 57));
        frames.add(new TextureRegion(golemRiseRegion, 5 * 74, 0, 74, 57));
        frames.add(new TextureRegion(golemRiseRegion, 6 * 74, 0, 74, 57));
        frames.add(new TextureRegion(golemRiseRegion, 7 * 74, 0, 74, 57));
        //frames.add(new TextureRegion(golemRiseRegion, 8 * 74, 0, 74, 57));
        golemRise = new Animation(riseFrameDuration, frames);
        frames.clear();

        // Idle
        for(int i = 0; i < 5; i++) {
            frames.add(new TextureRegion(golemIdleRegion, i * 74, 0, 74, 57));
        }
        golemIdle = new Animation(0.2f, frames);
        frames.clear();
    }

    @Override
    public TextureRegion getFrame(float dt) {
        TextureRegion region;

        switch (stateMachine.getCurrentState().toString()) {
            case "HIDDEN":
                region = (TextureRegion) golemHidden.getKeyFrame(stateTimer, true);
                break;
            case "RISE":
                region = (TextureRegion) golemRise.getKeyFrame(stateTimer, false);
                break;
            case "IDLE":
                region = (TextureRegion) golemIdle.getKeyFrame(stateTimer, true);
                break;
            default:
                region = (TextureRegion) golemHidden.getKeyFrame(stateTimer, true);
                break;

        }

        if (lastState != currentState) { stateTimer = 0; } else { stateTimer += dt; }
        return region;
    }

    @Override
    public void update(float dt) {
        // Update state machine
        elapsedTime += dt;

        if (rising) {
            if (riseTimer > 30 * riseFrameDuration) {
                stateMachine.changeState(GolemState.IDLE);
            } else {
                riseTimer += dt;
            }
        }

        stateMachine.update();
        currentState = stateMachine.getCurrentState().toString();

        /** TODO: Look at moving the below to Actor? */
        // Set sprite position to center of physics body
        super.setPosition(
                body.getPosition().x - getWidth() / 2,
                body.getPosition().y - (32 / BeyondManager.PPM)// - getHeight()  // / 2// (19 * getHeight() / 48)
        );

        super.checkStateTimerReset();
        super.setRegion(getFrame(dt));
        lastState = stateMachine.getCurrentState().toString();
    }

    public void golemRise() {
        stateMachine.changeState(GolemState.RISE);
        riseTimer = 0;
        rising = true;
    }

    @Override
    public void defineColliders() {

    }

    @Override
    public TextureRegion setRunningRight(TextureRegion region) {
        return null;
    }

    @Override
    public boolean getRunningRight() {
        return false;
    }

    @Override
    public void changeDirection() {

    }

    @Override
    public void setName(String name) {

    }

    /** Draw */
    public void draw(Batch batch) {
        super.draw(batch);
    }
}
