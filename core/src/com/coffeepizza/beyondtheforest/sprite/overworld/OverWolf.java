package com.coffeepizza.beyondtheforest.sprite.overworld;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.coffeepizza.beyondtheforest.BeyondManager;
import com.coffeepizza.beyondtheforest.overworld.OverworldScreen;
import com.coffeepizza.beyondtheforest.sprite.Actor;

public class OverWolf extends Actor {

    // System
    private boolean debugging = false;
    private String tag = "PlayerOverworldAvatar";
    private String message = "";

    // Timers
    private float movementTime = 1f;
    private float moveInterval = 0.1f;

    // Attributes
    private int currentLevel, currentPositionMarker;
    private int moveToLevel, moveToPosition;

    // Animation
    private Animation idleAnimation;
    private TextureRegion wolfRegion;
    private boolean runningRight = true;

    // State
    private boolean moving = false;

    // Converted coordinates
    //public float wX, wY;

    public OverWolf(BeyondManager manager, OverworldScreen screen, int level, int positionCount, Vector2 point) {
        super(manager, screen, "ow_wolf", point.x, point.y);
        this.currentLevel = level;
        this.currentPositionMarker = positionCount;

        // Texture regions and Animations
        wolfRegion = screen.getAtlas().findRegion("ow_wolf");
        super.setBounds(0, 0, 16 / BeyondManager.PPM, 16 / BeyondManager.PPM);
        super.setRegion(wolfRegion);
        super.positionScaledForOverworld();
        setAnimations();

        // Timers
        stateTimer = 0;
        elapsedTime = 0;
    }

    public void reset(int level, int positionCount, Vector2 point) {
        super.x = point.x;
        super.y = point.y;

        this.currentLevel = level;
        this.currentPositionMarker = positionCount;

        super.positionScaledForOverworld();

        // Timers
        stateTimer = 0;
        elapsedTime = 0;
    }

    public void move(int moveToLvl) {
        // Update markers
        moveToLevel = moveToLvl;
        moveToPosition = overScreen.getLevelPositionCount(moveToLevel);
        moving = true;
        elapsedTime = 0;

        // Work out move time
        int diff = 1;
        if (moveToPosition > currentPositionMarker) {
            diff = moveToPosition - currentPositionMarker;
        } else {
            diff = currentPositionMarker - moveToPosition;
        }

        if (diff != 0) {
            moveInterval = movementTime / diff;
        } else {
            moveInterval = 0.1f;
        }

        //Gdx.app.log(tag, "Current level, position: " + currentLevel + ", " + currentPositionMarker);
        //Gdx.app.log(tag, "Move to level, position: " + moveToLevel + ", " + moveToPosition);
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    private void updateCurrentLevel() {

    }

    @Override
    public void setAnimations() {
        Array<TextureRegion> frames = new Array<TextureRegion>();

        // Idle
        for(int i = 0; i < 1; i++) {
            frames.add(new TextureRegion(wolfRegion, i * 16, 0, 16, 16));
        }
        idleAnimation = new Animation(0.15f, frames);
        frames.clear();
    }

    @Override
    public TextureRegion getFrame(float dt) {
        TextureRegion region;

        region = (TextureRegion) idleAnimation.getKeyFrame(stateTimer, true);

        stateTimer += dt;
        //region = setRunningRight(region);
        return region;
    }

    @Override
    public void update(float dt) {
        elapsedTime += dt;

        if (moving && elapsedTime > moveInterval) {
            /** Test if we made it to the right position */
            if (moveToPosition == currentPositionMarker) {
                int getLevel = overScreen.getLevelOfThisPosition(currentPositionMarker);
                if (getLevel != -1) {
                    currentLevel = getLevel;
                }

                moveInterval = 0.1f;
                moving = false;
            }

            /** If not move to new position */
            if (moveToPosition > currentPositionMarker) {
                currentPositionMarker++;
            } if (moveToPosition < currentPositionMarker) {
                currentPositionMarker--;
            }
            // Move to new position
            Vector2 point = overScreen.getPositionPoint(currentPositionMarker, "OverWolf");
            if (point != null) {
                x = point.x;
                y = point.y;

                // Reset elapsedTime
                elapsedTime = 0;
            }
        }

        // Update and set position
        super.positionScaledForOverworld();
        super.setPosition(
                wX, wY
        );
        super.setRegion(wolfRegion);
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
        return runningRight;
    }

    @Override
    public void changeDirection() { }

    @Override
    public void setName(String name) {

    }

    public void draw(Batch batch) {
        super.draw(batch);
    }
}
