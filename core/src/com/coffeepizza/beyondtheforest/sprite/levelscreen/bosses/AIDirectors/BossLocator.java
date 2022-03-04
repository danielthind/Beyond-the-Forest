package com.coffeepizza.beyondtheforest.sprite.levelscreen.bosses.AIDirectors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.coffeepizza.beyondtheforest.BeyondManager;
import com.coffeepizza.beyondtheforest.levelscreen.LevelScreen;
import com.coffeepizza.beyondtheforest.sprite.Actor;

public class BossLocator {

    // System
    private boolean debugging = false;
    private String tag = "BossLocator";
    private String message = "";

    // Manager
    private BeyondManager manager;
    private LevelScreen screen;
    private Actor parent;

    // Texture Regions
    private TextureRegion locationSpritesOne;
    private TextureRegion locationSpritesTwo;

    private TextureRegion angleOneEighthPi;
    private TextureRegion angleTwoEighthPi;
    private TextureRegion angleThreeEighthPi;
    private TextureRegion angleFourEighthPi;
    private TextureRegion angleFiveEighthPi;
    private TextureRegion angleSixEighthPi;
    private TextureRegion angleSevenEightPi;
    private TextureRegion angleEightEighthPi;
    private TextureRegion angleNineEighthPi;
    private TextureRegion angleTenEightPi;
    private TextureRegion angleElevenEighthPi;
    private TextureRegion angleTwelveEighthPi;
    private TextureRegion angleThirteenEighthPi;
    private TextureRegion angleFourteenEighthPi;
    private TextureRegion angleFifteenEighthPi;
    private TextureRegion angleSixteenEighthPi;

    private boolean showAngleOneEighthPi = false;
    private boolean showAngleTwoEighthPi = false;
    private boolean showAngleThreeEighthPi = false;
    private boolean showAngleFourEighthPi = false;
    private boolean showAngleFiveEighthPi = false;
    private boolean showAngleSixEighthPi = false;
    private boolean showAngleSevenEightPi = false;
    private boolean showAngleEightEighthPi = false;
    private boolean showAngleNineEighthPi = false;
    private boolean showAngleTenEightPi = false;
    private boolean showAngleElevenEighthPi = false;
    private boolean showAngleTwelveEighthPi = false;
    private boolean showAngleThirteenEighthPi = false;
    private boolean showAngleFourteenEighthPi = false;
    private boolean showAngleFifteenEighthPi = false;
    private boolean showAngleSixteenEighthPi = false;

    // Properties
    private boolean isActive = false;
    private boolean isActiveSave = isActive;

    private Vector2 bossPosition = new Vector2();
    private Vector2 playerPosition = new Vector2();
    private float angle = 0f;

    private float width, height;

    public BossLocator(BeyondManager manager, LevelScreen screen) {
        this.manager = manager;
        this.screen = screen;

        locationSpritesOne = new TextureRegion(manager.getAtlas().findRegion("ui_dark_wolf_location_one"));
        locationSpritesTwo = new TextureRegion(manager.getAtlas().findRegion("ui_dark_wolf_location_two"));

        angleOneEighthPi = new TextureRegion(locationSpritesOne, 0 * 128, 0, 128, 128);
        angleTwoEighthPi = new TextureRegion(locationSpritesOne, 1 * 128, 0, 128, 128);
        angleThreeEighthPi = new TextureRegion(locationSpritesOne, 2 * 128, 0, 128, 128);
        angleFourEighthPi = new TextureRegion(locationSpritesOne, 3 * 128, 0, 128, 128);

        angleFiveEighthPi = new TextureRegion(locationSpritesOne, 4 * 128, 0, 128, 128);
        angleSixEighthPi = new TextureRegion(locationSpritesOne, 5 * 128, 0, 128, 128);
        angleSevenEightPi = new TextureRegion(locationSpritesOne, 6 * 128, 0, 128, 128);
        angleEightEighthPi = new TextureRegion(locationSpritesOne, 7 * 128, 0, 128, 128);

        angleNineEighthPi = new TextureRegion(locationSpritesTwo, 0 * 128, 0, 128, 128);
        angleTenEightPi = new TextureRegion(locationSpritesTwo, 1 * 128, 0, 128, 128);
        angleElevenEighthPi = new TextureRegion(locationSpritesTwo, 2 * 128, 0, 128, 128);
        angleTwelveEighthPi = new TextureRegion(locationSpritesTwo, 3 * 128, 0, 128, 128);

        angleThirteenEighthPi = new TextureRegion(locationSpritesTwo, 4 * 128, 0, 128, 128);
        angleFourteenEighthPi = new TextureRegion(locationSpritesTwo, 5 * 128, 0, 128, 128);
        angleFifteenEighthPi = new TextureRegion(locationSpritesTwo, 6 * 128, 0, 128, 128);
        angleSixteenEighthPi = new TextureRegion(locationSpritesTwo, 7 * 128, 0, 128, 128);

        width = 64 / BeyondManager.PPM;
        height = 64 / BeyondManager.PPM;
    }

    public void activate() {
        isActive = true;
        isActiveSave = isActive;
    }

    public void deactivate() {
        isActive = false;
        isActiveSave = isActive;
    }

    public void update(float dt) {
        isActive = isActiveSave;

        if (isActive) {
            updatePositions(screen.getBossPosition(), screen.getPlayerPosition());
            updateAngle();
            updateLocator();
        }
    }

    private void updatePositions(Vector2 bossPosition, Vector2 playerPosition) {
        this.bossPosition = new Vector2(bossPosition);
        this.playerPosition = new Vector2(playerPosition);
    }

    private void updateAngle() {
        // Deltas
        float deltaX = bossPosition.x - playerPosition.x;
        float deltaY = bossPosition.y - playerPosition.y;

        // Get angle
        angle = (float) Math.toDegrees(Math.atan(deltaY / deltaX));

        // Correct angel
        if (deltaX >= 0 && deltaY >= 0) {
            // Correct angle (0 is vertical not horizontal)
            angle = 90 - angle;
        } else if (deltaX >= 0 && deltaY < 0) {
            // Normalise angle and add 90
            angle = 0 - angle;
            angle += 90;
        } else if (deltaX < 0 && deltaY < 0) {
            // Correct angle (0 is vertical not horizontal)
            angle = 90 - angle;
            angle += 180;
        } else if (deltaX < 0 && deltaY >= 0) {
            // Normalise angle and add 270
            angle = 0 - angle;
            angle += 270;
        } else {
            // Turn off isActive so we dont have some erroneous angle
            isActive = false;
        }
    }

    private void updateLocator() {
        if (angle > 0 && angle <= 22.5) { showAngleOneEighthPi = true; } else { showAngleOneEighthPi = false; }
        if (angle > 22.5 && angle <= 45) { showAngleTwoEighthPi = true; } else { showAngleTwoEighthPi = false; }
        if (angle > 45 && angle <= 67.5) { showAngleThreeEighthPi = true; } else { showAngleThreeEighthPi = false; }
        if (angle > 67.5 && angle <= 90) { showAngleFourEighthPi = true; } else { showAngleFourEighthPi = false; }

        if (angle > 90 && angle <= 112.5) { showAngleFiveEighthPi = true; } else { showAngleFiveEighthPi = false; }
        if (angle > 112.5 && angle <= 135) { showAngleSixEighthPi = true; } else { showAngleSixEighthPi = false; }
        if (angle > 135 && angle <= 157.5) { showAngleSevenEightPi = true; } else { showAngleSevenEightPi = false; }
        if (angle > 157.5 && angle <= 180) { showAngleEightEighthPi = true; } else { showAngleEightEighthPi = false; }

        if (angle > 180 && angle <= 202.5) { showAngleNineEighthPi = true; } else { showAngleNineEighthPi = false; }
        if (angle > 202.5 && angle <= 225) { showAngleTenEightPi = true; } else { showAngleTenEightPi = false; }
        if (angle > 225 && angle <= 247.5) { showAngleElevenEighthPi = true; } else { showAngleElevenEighthPi = false; }
        if (angle > 247.5 && angle <= 270) { showAngleTwelveEighthPi = true; } else { showAngleTwelveEighthPi = false; }

        if (angle > 270 && angle <= 292.5) { showAngleThirteenEighthPi = true; } else { showAngleThirteenEighthPi = false; }
        if (angle > 292.5 && angle <= 315) { showAngleFourteenEighthPi = true; } else { showAngleFourteenEighthPi = false; }
        if (angle > 315 && angle <= 337.5) { showAngleFifteenEighthPi = true; } else { showAngleFifteenEighthPi = false; }
        if (angle > 337.5 && angle <= 360) { showAngleSixteenEighthPi = true; } else { showAngleSixteenEighthPi = false; }
    }

    public void draw(Batch batch) {
        if (isActive) {
            if (showAngleOneEighthPi) { batch.draw(angleOneEighthPi, playerPosition.x - (width / 2), playerPosition.y - (height / 3), width, height);}
            if (showAngleTwoEighthPi) { batch.draw(angleTwoEighthPi, playerPosition.x - (width / 2), playerPosition.y - (height / 3), width, height);}
            if (showAngleThreeEighthPi) { batch.draw(angleThreeEighthPi, playerPosition.x - (width / 2), playerPosition.y - (height / 3), width, height);}
            if (showAngleFourEighthPi) { batch.draw(angleFourEighthPi, playerPosition.x - (width / 2), playerPosition.y - (height / 3), width, height);}
            if (showAngleFiveEighthPi) { batch.draw(angleFiveEighthPi, playerPosition.x - (width / 2), playerPosition.y - (height / 3), width, height);}
            if (showAngleSixEighthPi) { batch.draw(angleSixEighthPi, playerPosition.x - (width / 2), playerPosition.y - (height / 3), width, height);}
            if (showAngleSevenEightPi) { batch.draw(angleSevenEightPi, playerPosition.x - (width / 2), playerPosition.y - (height / 3), width, height);}
            if (showAngleEightEighthPi) { batch.draw(angleEightEighthPi, playerPosition.x - (width / 2), playerPosition.y - (height / 3), width, height);}
            if (showAngleNineEighthPi) { batch.draw(angleNineEighthPi, playerPosition.x - (width / 2), playerPosition.y - (height / 3), width, height);}
            if (showAngleTenEightPi) { batch.draw(angleTenEightPi, playerPosition.x - (width / 2), playerPosition.y - (height / 3), width, height);}
            if (showAngleElevenEighthPi) { batch.draw(angleElevenEighthPi, playerPosition.x - (width / 2), playerPosition.y - (height / 3), width, height);}
            if (showAngleTwelveEighthPi) { batch.draw(angleTwelveEighthPi, playerPosition.x - (width / 2), playerPosition.y - (height / 3), width, height);}
            if (showAngleThirteenEighthPi) { batch.draw(angleThirteenEighthPi, playerPosition.x - (width / 2), playerPosition.y - (height / 3), width, height);}
            if (showAngleFourteenEighthPi) { batch.draw(angleFourteenEighthPi, playerPosition.x - (width / 2), playerPosition.y - (height / 3), width, height);}
            if (showAngleFifteenEighthPi) { batch.draw(angleFifteenEighthPi, playerPosition.x - (width / 2), playerPosition.y - (height / 3), width, height);}
            if (showAngleSixteenEighthPi) { batch.draw(angleSixteenEighthPi, playerPosition.x - (width / 2), playerPosition.y - (height / 3), width, height);}
        }
    }
}
