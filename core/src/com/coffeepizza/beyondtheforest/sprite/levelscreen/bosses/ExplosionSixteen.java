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

public class ExplosionSixteen extends Actor {

    // System
    private boolean debugging = false;
    private String tag = "ExplosionSixtyFour";
    private String message = "";

    // Properties
    private float frameTime;
    private Vector2 position;
    private float width, widthInGame;

    // Textures
    private TextureRegion explosionSixteenRegion;
    private TextureRegion currentRegion;

    // Animations
    private Animation explosionSixteen;

    public ExplosionSixteen(BeyondManager manager, LevelScreen screen, World world, Vector2 position, float frameTime) {
        super(manager, world, screen, "64_explosion");
        //super.defineDynamicBody(false);
        //super.setPosition(position.x, position.y);
        this.position = new Vector2(position);
        this.frameTime = frameTime;

        width = 16;
        widthInGame = width / BeyondManager.PPM;

        // Textures
        explosionSixteenRegion = new TextureRegion(manager.getAtlas().findRegion("16_explosion"));

        super.setBounds(0, 0, width / BeyondManager.PPM, width / BeyondManager.PPM);
        //super.setRegion(explosionSixtyFourRegion);
        setAnimations();

        currentRegion = getFrame(0);
        elapsedTime = 0;
    }

    @Override
    public void setAnimations() {
        Array<TextureRegion> frames = new Array<TextureRegion>();

        // 16
        int width = 16;
        for(int i = 0; i < 15; i++) {
            frames.add(new TextureRegion(explosionSixteenRegion, i * width, 0, width, width));

        }
        explosionSixteen = new Animation(frameTime, frames);
        frames.clear();
    }

    @Override
    public TextureRegion getFrame(float dt) {
        TextureRegion region;

        region = (TextureRegion) explosionSixteen.getKeyFrame(elapsedTime, true);

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
