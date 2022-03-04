package com.coffeepizza.beyondtheforest.sprite.levelscreen.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.coffeepizza.beyondtheforest.BeyondManager;
import com.coffeepizza.beyondtheforest.levelscreen.LevelScreen;
import com.coffeepizza.beyondtheforest.sprite.Actor;
import com.coffeepizza.beyondtheforest.sprite.levelscreen.enemies.stateMachines.MaggotState;
import com.coffeepizza.beyondtheforest.sprite.util.UserData;

public class Maggot extends Actor {

    // System
    private boolean debugging = false;
    private String tag = "Maggot";
    private String message = "";

    // Textures
    private TextureRegion maggotTexture;

    // Animations
    private Animation crawlAnimation;
    private Animation fallingAnimation;

    // Properties
    private float crawlSpeed = 0.5f;
//    private boolean crawlRight;

    public Maggot(BeyondManager manager, LevelScreen screen, World world, Vector2 position) {
        super(manager, world, screen, "worm_crawl");
        super.x = position.x;
        super.y = position.y;
        super.defineDynamicBody(false);
        defineColliders();
        super.setupStateMachine();

        maggotTexture = new TextureRegion(this.manager.getAtlas().findRegion("worm_crawl"));
        super.setBounds(0, 0, 48 / BeyondManager.PPM, 48 / BeyondManager.PPM);
        super.setRegion(maggotTexture);

        stateTimer = 0;
        setAnimations();

        stateMachine = new DefaultStateMachine<Maggot, MaggotState>(this, MaggotState.CRAWLING);
        boolean crawlRight = manager.random.nextBoolean();
        this.runningRight = crawlRight;
    }

    @Override
    public void setAnimations() {
        Array<TextureRegion> frames = new Array<TextureRegion>();

        // Crawling
        frames.add(new TextureRegion(maggotTexture, 3 * 48, 0, 48, 48));
        frames.add(new TextureRegion(maggotTexture, 6 * 48, 0, 48, 48));
        frames.add(new TextureRegion(maggotTexture, 4 * 48, 0, 48, 48));
        frames.add(new TextureRegion(maggotTexture, 5 * 48, 0, 48, 48));
        frames.add(new TextureRegion(maggotTexture, 0 * 48, 0, 48, 48));
        frames.add(new TextureRegion(maggotTexture, 1 * 48, 0, 48, 48));
        frames.add(new TextureRegion(maggotTexture, 2 * 48, 0, 48, 48));
        crawlAnimation = new Animation(0.1f, frames);
        frames.clear();

        // Falling
        frames.add(new TextureRegion(maggotTexture, 6 * 48, 0, 48, 48));
        fallingAnimation = new Animation(0.1f, frames);
        frames.clear();
    }

    @Override
    public TextureRegion getFrame(float dt) {
        TextureRegion region;

        switch (stateMachine.getCurrentState().toString()) {
            case "CRAWLING":
                region = (TextureRegion) crawlAnimation.getKeyFrame(stateTimer, true);
                break;
            case "FALLING":
                region = (TextureRegion) fallingAnimation.getKeyFrame(stateTimer, false);
                break;
            default:
                region = (TextureRegion) fallingAnimation.getKeyFrame(stateTimer, false);
                break;
        }

        region = setRunningRight(region);
        if (lastState != currentState) { stateTimer = 0; } else { stateTimer += dt; }
        return region;
    }

    @Override
    public void update(float dt) {
        // Update state machine
        elapsedTime += dt;

        if (super.isDead) { stateMachine.changeState(MaggotState.DYING); }
        stateMachine.update();
        currentState = stateMachine.getCurrentState().toString();

//        if (lastState != currentState) {
//            Gdx.app.log(tag, "" + currentState);
//        }

        /** TODO: Look at moving the below to Actor? */
        // Set sprite position to center of physics body
        super.setPosition(
                body.getPosition().x - getWidth() / 2,
                body.getPosition().y - getHeight() / 2
        );

        TextureRegion currentRegion = getFrame(dt);
        super.setRegion(currentRegion);
        lastState = stateMachine.getCurrentState().toString();
    }

    @Override
    public void defineColliders() {
        /** TODO:  Do this method in actor? */
        // Box2d Body
        PolygonShape polygon = new PolygonShape();
        FixtureDef fdef = new FixtureDef();

        float colliderWidth = super.bodyRadius * 6;
        float colliderHeight = super.bodyRadius * 3;
        float sideSensorMaxWidth = colliderWidth + 3;
        float sideSensorMinWidth = 4;
        float sideSensorTop = colliderHeight + 2;
        float sideSensorBottom = colliderHeight - 1;

        /** Enemy side sensors */
        Vector2 bottomLeft = new Vector2(-sideSensorMaxWidth / BeyondManager.PPM, sideSensorBottom / BeyondManager.PPM);
        Vector2 bottomRight = new Vector2(-sideSensorMinWidth / BeyondManager.PPM, sideSensorBottom / BeyondManager.PPM);
        Vector2 topLeft = new Vector2(-sideSensorMaxWidth / BeyondManager.PPM, sideSensorTop / BeyondManager.PPM);
        Vector2 topRight = new Vector2(-sideSensorMinWidth / BeyondManager.PPM, sideSensorTop / BeyondManager.PPM);

        Vector2[] verts = new Vector2[] {
                bottomLeft, bottomRight, topRight, topLeft
        };

        polygon.set(verts);
        shape = polygon;
        fdef.shape = shape;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData(new UserData("enemySide", this, 0));

        bottomLeft = new Vector2(sideSensorMinWidth / BeyondManager.PPM, sideSensorBottom / BeyondManager.PPM);
        bottomRight = new Vector2(sideSensorMaxWidth / BeyondManager.PPM, sideSensorBottom / BeyondManager.PPM);
        topLeft = new Vector2(sideSensorMinWidth / BeyondManager.PPM, sideSensorTop / BeyondManager.PPM);
        topRight = new Vector2(sideSensorMaxWidth / BeyondManager.PPM, sideSensorTop / BeyondManager.PPM);

        verts = new Vector2[] {
                bottomLeft, bottomRight, topRight, topLeft
        };

        polygon.set(verts);
        shape = polygon;
        fdef.shape = shape;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData(new UserData("enemySide", this, 0));

        /** Main collider */
        // Shapes
        polygon = new PolygonShape();

        /** TODO: Base collider radius on sprite dimensions? */
        bottomLeft = new Vector2(-colliderWidth / BeyondManager.PPM, -colliderHeight / BeyondManager.PPM);
        bottomRight = new Vector2(colliderWidth / BeyondManager.PPM, -colliderHeight / BeyondManager.PPM);
        topLeft = new Vector2(-colliderWidth / BeyondManager.PPM, colliderHeight / BeyondManager.PPM);
        topRight = new Vector2(colliderWidth / BeyondManager.PPM, colliderHeight / BeyondManager.PPM);

        verts = new Vector2[] {
                bottomLeft, bottomRight, topRight, topLeft
        };

        polygon.set(verts);
        shape = polygon;

        // Fixture definition
        fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.isSensor = true;
        super.body.createFixture(fdef).setUserData(new UserData("enemyCollider", this, 0));
    }

    @Override
    public TextureRegion setRunningRight(TextureRegion region) {
        TextureRegion orientatedRegion = region;

        if (body.getLinearVelocity().x < 0 && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;
        } else if (body.getLinearVelocity().x > 0 && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }
        return orientatedRegion;
    }

    @Override
    public boolean getRunningRight() { return runningRight; }

    @Override
    public void changeDirection() {
        runningRight = !runningRight;
    }

    // Movement methods
    public void crawlPhysics() {
        /** Crude check to change direction */
//        if (body.getLinearVelocity().x < 0.1f) { crawlRight = !crawlRight; }

        if (runningRight) { //crawlRight) {
            body.setLinearVelocity(crawlSpeed, body.getLinearVelocity().y);
        } else {
            body.setLinearVelocity(-crawlSpeed, body.getLinearVelocity().y);
        }
    }

    @Override
    public void setName(String name) { }
}
