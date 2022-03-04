package com.coffeepizza.beyondtheforest.sprite.levelscreen.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.coffeepizza.beyondtheforest.BeyondManager;
import com.coffeepizza.beyondtheforest.levelscreen.LevelScreen;
import com.coffeepizza.beyondtheforest.sprite.Actor;
import com.coffeepizza.beyondtheforest.sprite.levelscreen.enemies.stateMachines.SpiderState;
import com.coffeepizza.beyondtheforest.sprite.util.UserData;

public class Spider extends Actor {

    // System
    private boolean debugging = false;
    private String tag = "Shelob";
    private String message = "";
    //private long clock = System.currentTimeMillis();  - in super

    // Textures
    public TextureRegion spiderHanging;
    private TextureRegion deadRegion;

    // Animations
    private Animation spiderClimbing;
    private Animation spiderDescending;
    private Animation spiderWeb;
    private Animation deathAnimation;

    // Properties
    private float webTopPoint;
    private float topWaypoint;
    private float bottomWaypoint;
    //private float elapsedTime;
    private float nextFlap;
    private Vector2 flapVelocity;
    private float standardFlapDuration;
    private float flyingUpFlapDuration;
    private float flyingDownFlapDuration;

    public Spider(BeyondManager manager, LevelScreen screen, World world, Rectangle rectangle) {
        super(manager, world, screen, "spider_hanging", rectangle);
        super.defineDynamicBody(false);
        super.setupStateMachine();
        defineColliders();

        // Set start animation region
        spiderHanging = new TextureRegion(manager.getAtlas().findRegion("spider_hanging"));
        deadRegion = new TextureRegion(manager.getAtlas().findRegion("enemy_death_animation"));
        super.setBounds(0, 0, 64 / BeyondManager.PPM, 64 / BeyondManager.PPM);
        super.setRegion(spiderHanging);

        stateTimer = 0;
        setAnimations();

        // State machine
        stateMachine = new DefaultStateMachine<Spider, SpiderState>(this, SpiderState.CLIMPING);

        // Flap properties
        flapVelocity = super.resolveJumpVelocity(0, 1f);
        standardFlapDuration = super.resolveJumpDuration(0, 1f);
        flyingUpFlapDuration = standardFlapDuration * 0.75f;
        flyingDownFlapDuration = standardFlapDuration * 1.5f;//1.05f;

        // Patrol properties
//            // Webbing
//        webTopPoint = super.getPatrolTop()
//                - ((0f*16) / BeyondManager.PPM);
            // Bottom patrol location
        bottomWaypoint = super.getPatrolBottom()
                - ((0.5f*16) / BeyondManager.PPM);
            // Top Patrol location
        topWaypoint = bottomWaypoint
                + ((7*16) / BeyondManager.PPM);

        elapsedTime = 0;
        nextFlap = elapsedTime - 1;
    }

    @Override
    public void setAnimations() {
        Array<TextureRegion> frames = new Array<TextureRegion>();

        // Climbing
        frames.add(new TextureRegion(spiderHanging, 0 * 64, 0, 64, 64));
        frames.add(new TextureRegion(spiderHanging, 1 * 64, 0, 64, 64));
        spiderClimbing = new Animation(0.15f, frames);
        frames.clear();

        // Decending
        frames.add(new TextureRegion(spiderHanging, 0 * 64, 0, 64, 64));
        spiderDescending = new Animation(0.15f, frames);
        frames.clear();

        // Web
        frames.add(new TextureRegion(spiderHanging, 3 * 64, 0, 64, 64));
        spiderWeb = new Animation(0.15f, frames);
        frames.clear();

        // Death
        for(int i = 0; i < 8; i++) {
            frames.add(new TextureRegion(deadRegion, i * 64, 0, 64, 64));

        }
        deathAnimation = new Animation(0.05f, frames);
        frames.clear();
    }

    @Override
    public void update(float dt) {
        // Update state machine
        elapsedTime += dt;

        if (super.isDead) { stateMachine.changeState(SpiderState.DYING); }
        stateMachine.update();
        currentState = stateMachine.getCurrentState().toString();

        /** TODO: Look at moving the below to Actor? */
        // Set sprite position to center of physics body
        super.setPosition(
                body.getPosition().x - getWidth() / 2,
                body.getPosition().y - getHeight() / 2// (19 * getHeight() / 48)
        );

        super.checkStateTimerReset();
        super.setRegion(getFrame(dt));
        lastState = stateMachine.getCurrentState().toString();
    }

    @Override
    public TextureRegion getFrame(float dt) {
        TextureRegion region;

        switch (stateMachine.getCurrentState().toString()) {
            case "CLIMPING":
                region = (TextureRegion) spiderClimbing.getKeyFrame(stateTimer, true);
                break;
            case "DESCENDING":
                region = (TextureRegion) spiderDescending.getKeyFrame(stateTimer, true);
                break;
            case "DYING":
                region = (TextureRegion) deathAnimation.getKeyFrame(stateTimer, false);
                break;
            default:
                region = (TextureRegion) spiderDescending.getKeyFrame(stateTimer, true);
                break;

        }

        region = setRunningRight(region);
        if (lastState != currentState) { stateTimer = 0; } else { stateTimer += dt; }

        return region;
    }

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
    public void changeDirection() { }

    @Override
    public void defineColliders() {
        /** TODO:  Do this method in actor? */

        // Shapes
        PolygonShape polygon = new PolygonShape();

        /** TODO: Base collider radius on sprite dimensions? */
        float colliderRadius = super.bodyRadius * 8;

        // Shapes
        CircleShape circle = new CircleShape();
        circle.setRadius(colliderRadius / BeyondManager.PPM);
        shape = circle;

        // Fixture definition
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.isSensor = true;
        super.body.createFixture(fdef).setUserData(new UserData("enemyCollider", this, 0));
    }

    public StateMachine<Spider, SpiderState> getStateMachine () {
        return stateMachine;
    }

    public boolean timeToFlap() {
        boolean flap = false;

        if (elapsedTime > nextFlap) { flap = true; }

        return flap;
    }
    public boolean isAboveTopWaypoint() {
        boolean isAbove = false;

        if (body.getPosition().y > topWaypoint) { isAbove = true; }

        return isAbove;
    }
    public boolean isBelowBottomWaypoint() {
        boolean isBelow = false;

        if (body.getPosition().y < bottomWaypoint) { isBelow = true; }

        return isBelow;
    }
    public void setUpFlap() {
        nextFlap = flyingUpFlapDuration;
    }
    public void setDownFlap() {
        nextFlap = flyingDownFlapDuration;
    }
    public void resetElapsedTimed() {
        elapsedTime = 0;
    }
    public void flap() {
        super.halfVelocities();
        body.setLinearVelocity(0.0f, flapVelocity.y);

        if (debugging) { Gdx.app.log(tag, "flapping: " + flapVelocity.y); }
    }

    public void draw(Batch batch) {
        super.draw(batch);
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}