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
import com.coffeepizza.beyondtheforest.sprite.levelscreen.enemies.stateMachines.GhostState;
import com.coffeepizza.beyondtheforest.sprite.util.UserData;

public class Ghost extends Actor {

    // System
    private boolean debugging = false;
    private String tag = "Ghost";
    private String message = "";

    // Textures
    public TextureRegion ghostFloating;
    private TextureRegion deadRegion;

    // Animations
    private Animation ghostMoving;
    private Animation deathAnimation;

    // Properties
    private float topWaypoint;
    private float bottomWaypoint;
    private float leftWaypoint;
    private float rightWaypoint;
    private float nextFlap;
    private Vector2 flapUpVelocity;
    private float standardFlapDuration;
    private float flyingUpFlapDuration;
    private float flyingDownFlapDuration;

    public Ghost (BeyondManager manager, LevelScreen screen, World world, Rectangle rectangle) {
        super(manager, world, screen, "dampe_idle", rectangle);
        super.defineDynamicBody(false);
        super.setupStateMachine();
        defineColliders();

        // Set start animation region
        ghostFloating = new TextureRegion(manager.getAtlas().findRegion("dampe_idle"));
        deadRegion = new TextureRegion(manager.getAtlas().findRegion("enemy_death_animation"));
        super.setBounds(0, 0, 64 / BeyondManager.PPM, 64 / BeyondManager.PPM);
        super.setRegion(ghostFloating);

        stateTimer = 0;
        setAnimations();

        // State machine
        stateMachine = new DefaultStateMachine<Ghost, GhostState>(this, GhostState.ASCENDING);

        // Flap properties
        flapUpVelocity = super.resolveJumpVelocity(0.75f, 1f);
        standardFlapDuration = super.resolveJumpDuration(0, 1f);
        flyingUpFlapDuration = standardFlapDuration * 0.75f;
        flyingDownFlapDuration = standardFlapDuration * 1.05f;//1.5f;//

        // Patrol properties
        bottomWaypoint = super.getPatrolBottom()
                - ((0.25f*16) / BeyondManager.PPM);
        topWaypoint = super.getPatrolTop()
                + ((0.25f*16) / BeyondManager.PPM);
        leftWaypoint = super.getPatrolLeft()
                + ((0.25f*16) / BeyondManager.PPM);
        rightWaypoint = super.getPatrolRight()
                - ((0.25f*16) / BeyondManager.PPM);

        elapsedTime = 0;
        nextFlap = elapsedTime - 1;
        runningRight = true;
    }

    @Override
    public void setAnimations() {
        Array<TextureRegion> frames = new Array<TextureRegion>();

        // Climbing
        for(int i = 0; i < 4; i++) {
            frames.add(new TextureRegion(ghostFloating, i * 64, 0, 64, 64));

        }
        ghostMoving = new Animation(0.2f, frames);
        frames.clear();

        // Death
        for(int i = 0; i < 8; i++) {
            frames.add(new TextureRegion(deadRegion, i * 64, 0, 64, 64));

        }
        deathAnimation = new Animation(0.05f, frames);
        frames.clear();
    }

    @Override
    public TextureRegion getFrame(float dt) {
        TextureRegion region;

        switch (stateMachine.getCurrentState().toString()) {
            /*
            case "ASCENDING":
                region = (TextureRegion) ghostMoving.getKeyFrame(stateTimer, true);
                break;
            case "DESCENDING":
                region = (TextureRegion) ghostMoving.getKeyFrame(stateTimer, true);
                break;
             */
            case "DYING":
                region = (TextureRegion) deathAnimation.getKeyFrame(stateTimer, false);
                break;
            default:
                region = (TextureRegion) ghostMoving.getKeyFrame(stateTimer, true);
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

        if (super.isDead) { stateMachine.changeState(GhostState.DYING); }
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
    public void defineColliders() {
        /** TODO:  Do this method in actor? */

        // Shapes
        PolygonShape polygon = new PolygonShape();

        /** TODO: Base collider radius on sprite dimensions? */
        float colliderWidth = super.bodyRadius + 5;
        float colliderHeight = 40f;
        Vector2 bottomLeft = new Vector2(-colliderWidth / BeyondManager.PPM, -(colliderHeight / BeyondManager.PPM / 2));
        Vector2 bottomRight = new Vector2(colliderWidth / BeyondManager.PPM, -(colliderHeight / BeyondManager.PPM / 2));
        Vector2 topLeft = new Vector2(-colliderWidth / BeyondManager.PPM, (colliderHeight / BeyondManager.PPM / 2));
        Vector2 topRight = new Vector2(colliderWidth / BeyondManager.PPM, (colliderHeight / BeyondManager.PPM / 2));

        Vector2[] verts = new Vector2[] {
                bottomLeft, bottomRight, topRight, topLeft
        };

        polygon.set(verts);
        shape = polygon;

        // Fixture definition
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.isSensor = true;

        /*
        // Shapes
        PolygonShape polygon = new PolygonShape();

        /** TODO: Base collider radius on sprite dimensions? *
        float colliderRadius = super.bodyRadius * 6;

        // Shapes
        CircleShape circle = new CircleShape();
        circle.setRadius(colliderRadius / BeyondManager.PPM);
        shape = circle;

        // Fixture definition
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.isSensor = true;
        */
        super.body.createFixture(fdef).setUserData(new UserData("enemyCollider", this, 0));
    }

    @Override
    public TextureRegion setRunningRight(TextureRegion region) {
        TextureRegion orientatedRegion = region;

        if (runningRight && region.isFlipX()) {
            region.flip(true, false);
        } else if (!runningRight && !region.isFlipX()) {
            region.flip(true, false);
        }
        return orientatedRegion;
    }

    @Override
    public boolean getRunningRight() {
        return runningRight;
    }

    @Override
    public void changeDirection() {

    }

    public StateMachine<Ghost, GhostState> getStateMachine () {
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

        // X direction check and velocties
        if (runningRight) {
            body.setLinearVelocity(flapUpVelocity.x, flapUpVelocity.y);
            if (body.getPosition().x > rightWaypoint) { runningRight = false; }
        } else {
            body.setLinearVelocity(-flapUpVelocity.x, flapUpVelocity.y);
            if (body.getPosition().x < leftWaypoint) { runningRight = true; }
        }

        if (debugging) { Gdx.app.log(tag, "flapping: " + flapUpVelocity.y); }
    }

    public void draw(Batch batch) {
        super.draw(batch);
    }

    @Override
    public void setName(String name) {

    }
}
