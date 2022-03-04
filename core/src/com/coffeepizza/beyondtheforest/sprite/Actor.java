package com.coffeepizza.beyondtheforest.sprite;


import com.badlogic.gdx.ai.fsm.DefaultStateMachine;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.physics.box2d.*;
import com.coffeepizza.beyondtheforest.BeyondManager;
import com.coffeepizza.beyondtheforest.levelscreen.LevelScreen;
import com.coffeepizza.beyondtheforest.overworld.OverworldScreen;


/** Parent class for all game enemies.
 * Contains
 *      - Creation of a box2d body used for the actors movement (child classes will contain hit/hurt boxes)
 *      - Abstract methods for actor update, movement and hurbox colliders
 *      - Public methods for resolving dash velocities, dash times and jump velocities
 */
public abstract class Actor extends Sprite {

    // System
    private boolean debugging = true;
    private String tag = "Actor";
    private String message = "";

    // Box2d
    protected Object obj;
    protected World world;
    protected Body body;
    protected Shape shape;
//    protected boolean affectedByOneWayPlatforms;

    // Screen
    protected BeyondManager manager;
    protected LevelScreen levelScreen;
    protected OverworldScreen overScreen;

    // Properties
    protected float bodyRadius = 2.0f;
    public float x, wX;
    public float y, wY;
    float gravity = BeyondManager.GRAVITY;
    protected String name;
    protected boolean isDead = false;

    // Timers
    public float elapsedTime = 0;
    public float lockoutTimer = 0;

    // State Machine
    public DefaultStateMachine stateMachine;
    public String lastState = "NONE";
    public String currentState = "NONE";
    public boolean runningRight = true;
    public float stateTimer;
    public boolean attacking;

    // Ray Casting
    private Vector2 rayCollisionP = new Vector2();
    private Vector2 rayNormal = new Vector2();
    private boolean groundCollision;
    private float closestFraction, delta;
    private float maximumDistance = (bodyRadius * 4.0f) / BeyondManager.PPM;
    private Vector2 origin, target;

    /** Constructors */
        // Playscreen Constructor
    public Actor(BeyondManager manager, World world, LevelScreen screen, String region) {
        super(manager.getAtlas().findRegion(region));
        this.manager = manager;
        this.world = world;
        this.levelScreen = screen;
    }
        // Enemy
    public Actor(BeyondManager manager, World world, LevelScreen screen, String region, Object obj) {//float x, float y) {
        super(manager.getAtlas().findRegion(region));
        this.manager = manager;
        this.world = world;
        this.levelScreen = screen;
        this.obj = obj;

        if (obj instanceof Rectangle) {
            /** TODO: Cast obj to its instance type properly */
            this.x = (((Rectangle) obj).getX() + (((Rectangle) obj).getWidth() / 2)) / BeyondManager.PPM;
            this.y = (((Rectangle) obj).getY() + (((Rectangle) obj).getHeight() / 2)) / BeyondManager.PPM;
        }
        else if (obj instanceof Circle) {
        }
        else if (obj instanceof PolygonShape) {
        }
    }
        // Player/Boss
    public Actor(BeyondManager manager, World world, LevelScreen screen, String region, float x, float y) {
        super(manager.getAtlas().findRegion(region));
        this.manager = manager;
        this.world = world;
        this.levelScreen = screen;
        this.x = x;
        this.y = y;
    }
        // OverWorld Constructor - General Sprite
    public Actor(BeyondManager manager, OverworldScreen screen, String region, float x, float y) {
        super(manager.getAtlas().findRegion(region));
        this.manager = manager;
        this.overScreen = screen;
        this.x = x;
        this.y = y;
        positionScaledForOverworld();
    }

    protected void setupStateMachine() {
        stateMachine = new DefaultStateMachine();
        //debugSettings = new DebugSettings();
    }

    public Body getActorBody() {
        return body;
    }
    protected void defineDynamicBody(boolean affectedByOneWayPlatforms) {
        // Create Basic Body
        BodyDef bdef = new BodyDef();
        bdef.position.set(x, y);
        bdef.type = BodyDef.BodyType.DynamicBody;

        // Crate body in world
        body = world.createBody(bdef);

        // Shapes
        CircleShape cirShape = new CircleShape();
        cirShape.setRadius(bodyRadius / BeyondManager.PPM);
        shape = cirShape;

        // Fixture definition
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.density *= 5;
        fdef.restitution = 0;
        if (affectedByOneWayPlatforms) {
            // Affected by one-way platforms
            body.createFixture(fdef).setUserData("generalPhysicsBody");
        } else {
            // Unaffected by one-way platforms
            body.createFixture(fdef).setUserData("specialPhysicsBody");
        }
    }
    protected void defineKinematicBody(boolean affectedByOneWayPlatforms) {
        // Create Basic Body
        BodyDef bdef = new BodyDef();
        bdef.position.set(x, y);
        bdef.type = BodyDef.BodyType.KinematicBody;

        // Crate body in world
        body = world.createBody(bdef);

        // Shapes
        CircleShape cirShape = new CircleShape();
        cirShape.setRadius(bodyRadius / BeyondManager.PPM);
        shape = cirShape;

        // Fixture definition
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.density *= 5;
        fdef.restitution = 0;
        if (affectedByOneWayPlatforms) {
            // Affected by one-way platforms
            body.createFixture(fdef).setUserData("generalPhysicsBody");
        } else {
            // Unaffected by one-way platforms
            body.createFixture(fdef).setUserData("specialPhysicsBody");
        }
    }

    public abstract void setAnimations();
    public abstract TextureRegion getFrame(float dt);
    public abstract void update(float dt);
    public abstract void defineColliders();
    public abstract TextureRegion setRunningRight(TextureRegion region);
    public abstract boolean getRunningRight();
    public abstract void changeDirection();

    /** Movement equation solvers */
        // Return dash velocity (float) - For a given length and end velocity
    protected float resolveDashVelocity(float dashLength, float endVelocity) {

        //Vector2 velocity;

        float velocityX = 0;
        double desiredLength = (dashLength * 16) / BeyondManager.PPM;

        float finalVelocity = endVelocity;    // Velocity of a run
        float friction = 0.2f;

        float gravity = BeyondManager.GRAVITY;
        double value = (finalVelocity * finalVelocity) - (2 * friction * gravity * desiredLength);
        velocityX = (float) Math.sqrt(value);

        return velocityX;
    }
        // Return dash duration (long) - For a given length and start velocity
    protected float resolveDashTime (float dashVelocity, float dashLength) {
        float time = 0;

        float timeStep = 1 / 60f;               // Time step
        float velocityX = dashVelocity * timeStep;  // Step velocity
        //float finalVelocity = 0;
        double desiredLength = (dashLength * 16) / BeyondManager.PPM;

        //time = (float) ((2 * desiredLength) / (finalVelocity + velocityX));
        time = (float) (desiredLength / velocityX);
        time = (1000 / 60) * time;

        return time;
    }
        // Return jump duration (float) - For a jump of a fixed distance and height
    protected float resolveJumpDuration(float distance, float height) {
        float velocityY = 0;
        //float velocityX = 0;
        //Vector2 velocity;

        double desiredHeight = (height * 16) / BeyondManager.PPM;
        double desiredLength = (distance * 16) / BeyondManager.PPM;

        float timeStep = 1 / 60f;
        //float gravity = LevelScreen.gravity;

        // Quadratic equation
        float a = 1 / (2 * timeStep * timeStep * gravity);
        float b = 0.5f;

        double squaredD = (b * b) - (4 * a * desiredHeight);
        double sqD = Math.sqrt(squaredD);

        double solution1 = (-b - sqD) / (2 * a);
        double solution2 = (-b + sqD) / (2 * a);

        double solution = solution1;
        if (solution1 < 0) { solution = solution2; }

        velocityY = (float) (solution * 60);

        float time = 2 * (- velocityY / gravity);

        return time;
    }
        // Return velocity (Vector2) - For a jump of a fixed distance and height
    protected Vector2 resolveJumpVelocity(float distance, float height) {
        float velocityY = 0;
        float velocityX = 0;
        Vector2 velocity;

        float time = resolveJumpDuration(distance, height);
        double desiredLength = (distance * 16) / BeyondManager.PPM;

        //float time = 2 * (- velocityY / gravity);
        velocityY = gravity * (- time / 2);
        velocityX = (float) (desiredLength / time);

        velocity = new Vector2(velocityX, velocityY);
        return velocity;
    }

    /** Conversions */
    protected float convertScalledGlobalLengthToTileUnits(float f) {
        float r;
        r = (f / 0.16f);

        return r;
    }
        // Overworld
    protected void positionScaledForOverworld() {
        //Gdx.app.log(tag, "(x, y: " + x + ", " + y
                //+ ") (w, h: " + super.getWidth() + ", " + super.getHeight() + ")");
        //wX = (x - super.getWidth() / 2) / GameManager.PPM;
        //wY = (y - super.getHeight() / 2) / GameManager.PPM;
        //wX = (super.getWidth() * GameManager.PPM) / 2;
        //wY = (super.getWidth() * GameManager.PPM) / 2;
        wX = x / BeyondManager.PPM;
        wY = y / BeyondManager.PPM;

        //wX = (x / (2 * GameManager.PPM)) + super.getWidth();
        //wY = (y / (2 * GameManager.PPM)) + super.getHeight();
    }

    /** Patrol methods - Get bounding creation box bounds */
        // Return maximum height of patrol path
    protected float getPatrolTop() {
        float top = 0.0f;

        if (obj instanceof Rectangle) {
            top = (((Rectangle) obj).getY() + ((Rectangle) obj).getHeight()) / BeyondManager.PPM;
        }
        else if (obj instanceof Circle) {

        }
        else if (obj instanceof PolygonShape) {

        }
        return top;
    }
        // Return minimum height of patrol path
    protected float getPatrolBottom() {
        float bottom = 0.0f;

        if (obj instanceof Rectangle) {
            bottom = ((Rectangle) obj).getY() / BeyondManager.PPM;
        }
        else if (obj instanceof Circle) {

        }
        else if (obj instanceof PolygonShape) {

        }
        return bottom;
    }
        // Return right height of patrol path
    protected float getPatrolRight() {
        float right = 0.0f;

        if (obj instanceof Rectangle) {
            right = (((Rectangle) obj).getX() + ((Rectangle) obj).getWidth()) / BeyondManager.PPM;
        }
        else if (obj instanceof Circle) {

        }
        else if (obj instanceof PolygonShape) {

        }
        return right;
    }
        // Return left of patrol path
    protected float getPatrolLeft() {
        float left = 0.0f;

        if (obj instanceof Rectangle) {
            left = ((Rectangle) obj).getX() / BeyondManager.PPM;
        }
        else if (obj instanceof Circle) {

        }
        else if (obj instanceof PolygonShape) {

        }
        return left;
    }

    /** Raycasting */
    private boolean rayFrontVerticalGrounded(float rayLengthMultiplier) {
        // Set default value
        boolean grounded = false;
        groundCollision = false;
        closestFraction = 1f;

        float distanceCheck = maximumDistance * rayLengthMultiplier;

        rayCollisionP = new Vector2();
        closestFraction = 1f;
        delta = 10;             // i.e. big gap, i.e. airborne

        // Points
        if (runningRight) {
            origin = new Vector2(getActorBody().getPosition().x + ((bodyRadius / 6f) / BeyondManager.PPM),
                    getActorBody().getPosition().y);
            target =  new Vector2(getActorBody().getPosition().x + ((bodyRadius / 6f) / BeyondManager.PPM),
                    getActorBody().getPosition().y - distanceCheck
            );
        } else {
            origin = new Vector2(getActorBody().getPosition().x - ((bodyRadius / 6f) / BeyondManager.PPM),
                    getActorBody().getPosition().y);
            target =  new Vector2(getActorBody().getPosition().x - ((bodyRadius / 6f) / BeyondManager.PPM),
                    getActorBody().getPosition().y - distanceCheck
            );
        }

        // Cast Ray
        RayCastCallback callbackForNormal = new RayCastCallback() {
            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                if (fixture.getUserData() != null) {
                    if (fraction < closestFraction && (fixture.getUserData().equals("ground"))) {
                        rayCollisionP.set(point);
                        //rayNormal.set(normal);           //.add(point);       // Add collision point as normal is relative to collosion point
                        closestFraction = fraction;
                        groundCollision = true;
                    }
                }
                return fraction;        // Clips ray to closet hit
                // return 0;            // Stop casting after this 0
                // return 1;            // Gets information, now continue
                // return -1;           // ignore all fixtures after this -1
            }
        };
        world.rayCast(callbackForNormal, origin, target);

        if (groundCollision) {
            delta = Math.abs(origin.y - rayCollisionP.y);

            if (delta < distanceCheck) {
                grounded = true;
            }
        }

        return grounded;
    }
    private boolean rayBackVerticalGrounded(float rayLengthMultiplier) {
        // Set default value
        boolean grounded = false;
        groundCollision = false;
        closestFraction = 1f;

        float distanceCheck = maximumDistance * rayLengthMultiplier;

        rayCollisionP = new Vector2();
        closestFraction = 1f;
        delta = 10;             // i.e. big gap, i.e. airborne

        // Points
        if (runningRight) {
            origin = new Vector2(getActorBody().getPosition().x - ((bodyRadius / 6f) / BeyondManager.PPM),
                    getActorBody().getPosition().y);
            target =  new Vector2(getActorBody().getPosition().x - ((bodyRadius / 6f) / BeyondManager.PPM),
                    getActorBody().getPosition().y - distanceCheck
            );
        } else {
            origin = new Vector2(getActorBody().getPosition().x + ((bodyRadius / 6f) / BeyondManager.PPM),
                    getActorBody().getPosition().y);
            target =  new Vector2(getActorBody().getPosition().x + ((bodyRadius / 6f) / BeyondManager.PPM),
                    getActorBody().getPosition().y - distanceCheck
            );
        }

        // Cast Ray
        RayCastCallback callbackForNormal = new RayCastCallback() {
            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                if (fixture.getUserData() != null) {
                    if (fraction < closestFraction && (fixture.getUserData().equals("ground"))) {
                        rayCollisionP.set(point);
                        //rayNormal.set(normal);           //.add(point);       // Add collision point as normal is relative to collosion point
                        closestFraction = fraction;
                        groundCollision = true;
                    }
                }
                return fraction;        // Clips ray to closet hit
                // return 0;            // Stop casting after this 0
                // return 1;            // Gets information, now continue
                // return -1;           // ignore all fixtures after this -1
            }
        };
        world.rayCast(callbackForNormal, origin, target);

        if (groundCollision) {
            delta = Math.abs(origin.y - rayCollisionP.y);

            if (delta < distanceCheck) {
                grounded = true;
            }
        }

        return grounded;
    }
    private boolean rayMiddleVerticalGrounded(float rayLengthMultiplier) {
        // Set default value
        boolean grounded = false;
        groundCollision = false;
        closestFraction = 1f;

        float distanceCheck = maximumDistance * rayLengthMultiplier;

        rayCollisionP = new Vector2();
        rayNormal = new Vector2();
        closestFraction = 1f;
        delta = 10;             // i.e. big gap, i.e. airborne

        // Points
        origin = new Vector2(getActorBody().getPosition().x,
                getActorBody().getPosition().y);
        target =  new Vector2(getActorBody().getPosition().x,
                getActorBody().getPosition().y - distanceCheck
        );

        // Cast Ray
        RayCastCallback callbackForNormal = new RayCastCallback() {
            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                if (fixture.getUserData() != null) {
                    if (fraction < closestFraction && (fixture.getUserData().equals("ground"))) {
                        rayCollisionP.set(point);
                        rayNormal.set(normal);           //.add(point);       // Add collision point as normal is relative to collosion point
                        closestFraction = fraction;
                        groundCollision = true;
                    }
                }
                return fraction;        // Clips ray to closet hit
                // return 0;            // Stop casting after this 0
                // return 1;            // Gets information, now continue
                // return -1;           // ignore all fixtures after this -1
            }
        };
        world.rayCast(callbackForNormal, origin, target);

        if (groundCollision) {
            delta = Math.abs(origin.y - rayCollisionP.y);

            if (delta < distanceCheck) {
                grounded = true;
            }
        }

        return grounded;
    }

    public Vector2 getGroundedRayNormal() {
        return rayNormal;
    }

    /** State checks */
        // Grounded
    public boolean isGroundedNormal() {
        /** Normal (middle & back) check with an normal length ray */
        return (rayBackVerticalGrounded(1.0f) && rayMiddleVerticalGrounded(1.0f));
    }
    public boolean isGroundedExtended() {
        /** Normal (middle & back) check with an extended ray (to be used when moving quickly) */
        return (rayBackVerticalGrounded(2.0f) && rayMiddleVerticalGrounded(2.0f));
    }
    public boolean isFullyGrounded() {
        /** Full (front, middle & back) check with an normal ray */
        return (rayFrontVerticalGrounded(1.0f)
                && rayBackVerticalGrounded(1.0f)
                && rayMiddleVerticalGrounded(1.0f)
        );
    }
        // Descenging
    public boolean isDecending() {
        boolean decending = false;
        if (body.getLinearVelocity().y < 0) { decending = true; }
        return decending;
    }

    /** Lockout timer - setter and checker */
    public void setLockout(float duration) {
        elapsedTime = 0;
        lockoutTimer = duration;
    }
    public boolean freeFromLockout() {
        boolean isFree = false;
        // Checks if player is no longer locked out from state changes, etc..
        if (elapsedTime > lockoutTimer) {
            isFree = true;
        }
        return isFree;
    }
    //public void resetStartTimer() { stateTimer = 0; }

    /** Remove actor */
    public void damageActor() {
        removeActorFromLevelScreen();
    }
    protected void removeActorFromLevelScreen() {
        levelScreen.enemiesToRemove.add(this);
        isDead = true;
    }

    /** Stops movement */
    public void halfVelocities() {
        //body.setLinearVelocity(0.0f, 0.0f);
        body.setLinearVelocity(body.getLinearVelocity().x * 0.5f, body.getLinearVelocity().y * 0.5f);
    }
    public void halfXVel() {
        //body.setLinearVelocity(0.0f, body.getLinearVelocity().y);
        body.setLinearVelocity(body.getLinearVelocity().x * 0.5f, body.getLinearVelocity().y);
    }
    public void zeroXVel() {
        //body.setLinearVelocity(0.0f, body.getLinearVelocity().y);
        body.setLinearVelocity(0, body.getLinearVelocity().y);
    }
    public void zeroYVel() { body.setLinearVelocity(body.getLinearVelocity().x, 0.0f); }

    /** Set physics body start position */
    public void setPosition (float x, float y) {
        super.setPosition(x, y);
    }
    public Vector2 getPosition () { return body.getPosition(); }

    /** Name */
    public abstract void setName(String name);
    public String getName() {
        return this.name;
    }

    /** Draw current sprite */
        // Animation and region
    public void checkStateTimerReset() {
        if (!lastState.equals(currentState)) {
            stateTimer = 0;
        }
    }
    public void setRegion(TextureRegion region) {
        super.setRegion(region);
    }
        // Draw
    public void draw(Batch batch) {
        super.draw(batch);
    }
}
