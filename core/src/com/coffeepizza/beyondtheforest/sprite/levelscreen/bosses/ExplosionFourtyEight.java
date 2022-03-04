package com.coffeepizza.beyondtheforest.sprite.levelscreen.bosses;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.coffeepizza.beyondtheforest.BeyondManager;
import com.coffeepizza.beyondtheforest.levelscreen.LevelScreen;
import com.coffeepizza.beyondtheforest.sprite.Actor;

public class ExplosionFourtyEight extends Actor {

    // System
    private boolean debugging = false;
    private String tag = "ExplosionFourtyEight";
    private String message = "";

    // Properties
    private float frameTime;
    private Vector2 position;
    private float width, widthInGame;

    // Textures
    private TextureRegion explosionFourtyEightRegion;
    private TextureRegion currentRegion;

    // Animations
    private Animation explosionFourtyEight;

    public ExplosionFourtyEight(BeyondManager manager, LevelScreen screen, World world, Vector2 position, float frameTime) {
        super(manager, world, screen, "48_explosion");
        //super.defineDynamicBody(false);
        //super.setPosition(position.x, position.y);
        this.position = new Vector2(position);
        this.frameTime = frameTime;

        width = 48;
        widthInGame = width / BeyondManager.PPM;

        // Textures
        explosionFourtyEightRegion = new TextureRegion(manager.getAtlas().findRegion("48_explosion"));

        super.setBounds(0, 0, width / BeyondManager.PPM, width / BeyondManager.PPM);
        //super.setRegion(explosionSixtyFourRegion);
        setAnimations();

        currentRegion = getFrame(0);
        elapsedTime = 0;
    }

    @Override
    public void setAnimations() {
        Array<TextureRegion> frames = new Array<TextureRegion>();

        // 48
        int width = 48;
        for(int i = 0; i < 15; i++) {
            frames.add(new TextureRegion(explosionFourtyEightRegion, i * width, 0, width, width));

        }
        explosionFourtyEight = new Animation(frameTime, frames);
        frames.clear();
    }

    @Override
    public TextureRegion getFrame(float dt) {
        TextureRegion region;

        region = (TextureRegion) explosionFourtyEight.getKeyFrame(elapsedTime, true);

        return region;
    }

    @Override
    public void update(float dt) {
        elapsedTime += dt;

        currentRegion = getFrame(dt);
        //super.setRegion(currentRegion);
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
    @Override
    public void draw(Batch batch) {
            batch.draw(currentRegion,
                    position.x,
                    position.y,
                    widthInGame,
                    widthInGame
            );
    }

}
