package com.coffeepizza.beyondtheforest.sprite.levelscreen.enemies;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
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
import com.coffeepizza.beyondtheforest.sprite.Actor;
import com.coffeepizza.beyondtheforest.sprite.levelscreen.enemies.stateMachines.ZombieState;
import com.coffeepizza.beyondtheforest.sprite.util.UserData;

public class Zombie extends Actor {

    // System
    private boolean debugging = false;
    private String tag = "Zombie";
    private String message = "";

    // Properties
    public boolean hobbleRight = true;
    private float leftWaypoint;
    private float rightWaypoint;

    // Textures
    private TextureRegion zombieHobble;
    private TextureRegion zombieIdleRegion;
    private TextureRegion zombieWalkRegion;
    private TextureRegion deadRegion;
    private TextureRegion tutorialDeadRegion;

    // Animations
    private Animation zombieIdle;
    private Animation zombieTurn;
    private Animation zombieWalk;
    private Animation deathAnimation;

    // Timers
    private float turnTime = 0.5f;

    // Tutorial
    private boolean tutorialZombie;

    public Zombie(BeyondManager manager, LevelScreen screen, World world, Rectangle rectangle, boolean tutorialZombie) {
        super(manager, world, screen, "mummy_idle", rectangle);
        super.defineDynamicBody(false);
        super.setupStateMachine();
        this.tutorialZombie = tutorialZombie;
        defineColliders();

        // Set start animation region
        if (this.tutorialZombie) {
            zombieHobble = new TextureRegion(manager.getAtlas().findRegion("mummy_yellow_idle"));
            zombieIdleRegion = new TextureRegion(manager.getAtlas().findRegion("mummy_yellow_idle"));
            zombieWalkRegion = new TextureRegion(manager.getAtlas().findRegion("mummy_yellow_walk"));
            deadRegion = new TextureRegion(manager.getAtlas().findRegion("enemy_death_animation_yellow"));
        } else {
            zombieHobble = new TextureRegion(manager.getAtlas().findRegion("mummy_idle"));
            zombieIdleRegion = new TextureRegion(manager.getAtlas().findRegion("mummy_idle"));
            zombieWalkRegion = new TextureRegion(manager.getAtlas().findRegion("mummy_walk"));
            deadRegion = new TextureRegion(manager.getAtlas().findRegion("enemy_death_animation"));
        }

        super.setBounds(0, 0, 64 / BeyondManager.PPM, 64 / BeyondManager.PPM);
        super.setRegion(zombieHobble);

        stateTimer = 0;
        setAnimations();

        // State Machine
        stateMachine = new DefaultStateMachine<Zombie, ZombieState>(this, ZombieState.HOBBLING_RIGHT);

        // Hobble properties

        // Patrol properties
        leftWaypoint = super.getPatrolLeft();
        rightWaypoint = super.getPatrolRight();
    }

    @Override
    public void setAnimations() {
        Array<TextureRegion> frames = new Array<TextureRegion>();

        // Idle
        for(int i = 0; i < 6; i++) {
            frames.add(new TextureRegion(zombieIdleRegion, i * 64, 0, 64, 64));

        }
        zombieIdle = new Animation(0.2f, frames);
        frames.clear();

        // Turn
        frames.add(new TextureRegion(zombieIdleRegion, 4 * 64, 0, 64, 64));
        frames.add(new TextureRegion(zombieIdleRegion, 3 * 64, 0, 64, 64));
        zombieTurn = new Animation(0.05f, frames);
        frames.clear();

        // Walk
        for(int i = 0; i < 12; i++) {
            frames.add(new TextureRegion(zombieWalkRegion, i * 64, 0, 64, 64));

        }
        zombieWalk = new Animation(0.15f, frames);
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

        if (super.isDead) { stateMachine.changeState(ZombieState.DYING); }
        stateMachine.update();
        currentState = stateMachine.getCurrentState().toString();

        //if (lastState != currentState) {
        //Gdx.app.log(tag, "" + currentState);
        //}

        /** TODO: Look at moving the below to Actor? */
        // Set sprite position to center of physics body
        super.setPosition(
                body.getPosition().x - getWidth() / 2,
                body.getPosition().y - getHeight() / 6// (19 * getHeight() / 48)
        );

        super.checkStateTimerReset();
        super.setRegion(getFrame(dt));
        lastState = stateMachine.getCurrentState().toString();
    }

    @Override
    public TextureRegion getFrame(float dt) {
        TextureRegion region;

        switch (stateMachine.getCurrentState().toString()) {
            case "HOBBLING_LEFT":
                region = (TextureRegion) zombieWalk.getKeyFrame(stateTimer, true);
                break;
            case "HOBBLING_RIGHT":
                region = (TextureRegion) zombieWalk.getKeyFrame(stateTimer, true);
                break;
            case "TURN_AROUND":
                region = (TextureRegion) zombieTurn.getKeyFrame(stateTimer, false);
                break;
            case "DYING":
                region = (TextureRegion) deathAnimation.getKeyFrame(stateTimer, false);
                break;
            default:
                region = (TextureRegion) zombieIdle.getKeyFrame(stateTimer, true);
                break;

        }

        region = setRunningRight(region);
        if (lastState != currentState) { stateTimer = 0; } else { stateTimer += dt; }
        //stateTimer += dt;

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
    public void changeDirection() { }

    @Override
    public boolean getRunningRight() {
        return runningRight;
    }

    @Override
    public void defineColliders() {
        /** TODO:  Do this method in actor? */

        // Shapes
        PolygonShape polygon = new PolygonShape();

        /** TODO: Base collider radius on sprite dimensions? */
        float colliderWidth = super.bodyRadius + 4;
        float colliderHeight = 30f;
        Vector2 bottomLeft = new Vector2(-colliderWidth / BeyondManager.PPM, 0);
        Vector2 bottomRight = new Vector2(colliderWidth / BeyondManager.PPM, 0);
        Vector2 topLeft = new Vector2(-colliderWidth / BeyondManager.PPM, colliderHeight / BeyondManager.PPM);
        Vector2 topRight = new Vector2(colliderWidth / BeyondManager.PPM, colliderHeight / BeyondManager.PPM);

        Vector2[] verts = new Vector2[] {
                bottomLeft, bottomRight, topRight, topLeft
        };

        polygon.set(verts);
        shape = polygon;

        // Fixture definition
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.isSensor = true;
        if(tutorialZombie) {
            super.body.createFixture(fdef).setUserData(new UserData("tutorialCollider", this, 0));
        } else {
            super.body.createFixture(fdef).setUserData(new UserData("enemyCollider", this, 0));
        }
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public StateMachine<Zombie, ZombieState> getStateMachine () {
        return stateMachine;
    }

    public boolean isPastRightWaypoint() {
        boolean isPast = false;

        if (body.getPosition().x > rightWaypoint) { isPast = true; }

        return isPast;
    }
    public boolean isPastLeftWaypoint() {
        boolean isPast = false;

        if (body.getPosition().x < leftWaypoint) { isPast = true; }

        return isPast;
    }
    public void hobble() {
        if (stateMachine.getCurrentState() == ZombieState.HOBBLING_RIGHT) {
            //body.applyLinearImpulse(new Vector2(0.05f, 0), body.getWorldCenter(), true);
            //body.applyLinearImpulse(new Vector2(0.05f, 0), body.getWorldCenter(), true);
            body.setLinearVelocity(0.15f,0);
        }
        else if (stateMachine.getCurrentState() == ZombieState.HOBBLING_LEFT) {
            //body.applyLinearImpulse(new Vector2(- 0.05f, 0), body.getWorldCenter(), true);
            body.setLinearVelocity(-0.15f,0);
        }
    }
    public void turn() {
        halfVelocities();
        hobbleRight = !hobbleRight;
        elapsedTime = 0;
        lockoutTimer = turnTime;
    }

    /** Draw */
    public void draw(Batch batch) {
        super.draw(batch);
    }
}