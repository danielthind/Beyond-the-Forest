package com.coffeepizza.beyondtheforest.sprite;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.coffeepizza.beyondtheforest.BeyondManager;
import com.coffeepizza.beyondtheforest.levelscreen.LevelScreen;
import com.coffeepizza.beyondtheforest.sprite.util.UserData;

public class Secret extends Actor {

    // System
    private boolean debugging = false;
    private String tag = "Secret";
    private String message = "";

    // Animations
    private TextureRegion bone;
    private Animation boneAnimation;
    private Animation foundAnimation;
    public int spriteWidth = 32;

    public int secretNumber;

    public Secret(BeyondManager manager, World world, LevelScreen screen, Rectangle rectangle, int number) {
        super(manager, world, screen, "items_bone", rectangle);
        this.secretNumber = number;
        super.defineDynamicBody(false);
        body.setGravityScale(0);
        body.getFixtureList().first().isSensor();

        defineColliders();

        // Set start animation region
        bone = new TextureRegion(manager.getAtlas().findRegion("items_bone"));
        super.setBounds(0, 0, spriteWidth * 1 / BeyondManager.PPM, spriteWidth * 1 / BeyondManager.PPM);
        super.setRegion(bone);
        setAnimations();

        elapsedTime = 0;
    }

    @Override
    public void setAnimations() {
        Array<TextureRegion> frames = new Array<TextureRegion>();

        // Normal
        for(int i = 0; i < 8; i++) {
            frames.add(new TextureRegion(bone, i * 32, 0, 32, 32));

        }
        boneAnimation = new Animation(0.15f, frames);
        frames.clear();

        // Found
        frames.add(new TextureRegion(bone, 8 * 32, 0, 32, 32));
        foundAnimation = new Animation(0.15f, frames);
        frames.clear();

    }

    @Override
    public TextureRegion getFrame(float dt) {
        TextureRegion region;
        if (!isDead) {
            region = (TextureRegion) boneAnimation.getKeyFrame(elapsedTime, true);
        } else {
            region = (TextureRegion) foundAnimation.getKeyFrame(elapsedTime, true);
        }

        return region;
    }

    @Override
    public void update(float dt) {
        elapsedTime += dt;

        super.setPosition(
                body.getPosition().x - getWidth() / 2,
                body.getPosition().y - getHeight() / 2
        );

        super.setRegion(getFrame(dt));
    }

    @Override
    public void defineColliders() {
        // Shapes
        PolygonShape polygon = new PolygonShape();

        /** TODO: Base collider radius on sprite dimensions? */
        float colliderWidth = super.bodyRadius * 8;
        float colliderHeight = colliderWidth;

        Vector2 top = new Vector2(0, colliderHeight / BeyondManager.PPM);
        Vector2 right = new Vector2(colliderWidth / BeyondManager.PPM, 0);
        Vector2 bottom = new Vector2(0, -colliderHeight / BeyondManager.PPM);
        Vector2 left = new Vector2(-colliderWidth / BeyondManager.PPM, 0);

        Vector2[] verts = new Vector2[] {
                top, right, bottom, left
        };

        polygon.set(verts);
        shape = polygon;

        // Fixture definition
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.isSensor = true;
        super.body.createFixture(fdef).setUserData(new UserData("secret", this, secretNumber));
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
