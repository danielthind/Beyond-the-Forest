package com.coffeepizza.beyondtheforest.sprite.levelscreen.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.coffeepizza.beyondtheforest.BeyondManager;
import com.coffeepizza.beyondtheforest.levelscreen.LevelScreen;
import com.coffeepizza.beyondtheforest.sprite.Actor;
import com.coffeepizza.beyondtheforest.sprite.levelscreen.player.stateMachines.PlayerStateMachine;
import com.coffeepizza.beyondtheforest.sprite.util.AttackAnimator;

public class Player extends Actor {

    // System
    private Preferences prefs;
    private boolean debugging = false;
    public Vector2 debugSP, debugPoint, p3;
    private String tag = "PlayerObject";
    private String message = "";

    // Velocities
    public float runVMax = 2f;//1.5f;
    public float fallXMax = runVMax * 2f;
    public float walkThreshold = 0.5f;
    private float fallDampeningFactor = 0.95f;
//    public float runIR = 0.3f;
//    public float runIL = -runIR;
    public Vector2 damageVR;
    private Vector2 damageVL;
    public Vector2 jumpVR;
    private Vector2 jumpVL;
    private float slideV;
    private Vector2 slideVR;
    private Vector2 slideVL;
    private Vector2 slideKickVR;
    private Vector2 slideKickVL;
    private Vector2 londJumpVR;
    private Vector2 londJumpVL;
    private Vector2 diveKickImp;
    private Vector2 rDiveKickImp;

    private Vector2 miniJumpR;
    private Vector2 miniJumpL;

    // Speed Recording
    private float trackInterval = 0.02f;
    private float trackTime = 0.02f;
    private Vector2 pastSpeed1 = new Vector2(0, 0);
    private Vector2 pastSpeed2 = new Vector2(0, 0);
    private Vector2 pastSpeed3 = new Vector2(0, 0);
    private Vector2 pastSpeed4 = new Vector2(0, 0);
    private Vector2 pastSpeed5 = new Vector2(0, 0);
    public Vector2 pastLocation1 = new Vector2(0, 0);
    public Vector2 pastLocation2 = new Vector2(0, 0);
    public Vector2 pastLocation3 = new Vector2(0, 0);
    //public Vector2 pastLocation4 = new Vector2(0, 0);
    //public Vector2 pastLocation5 = new Vector2(0, 0);

        // Timers - dt ~ 0.015 - 0.020
    //public float elapsedTime;
    private float inputTimer = 0;
    //public float lockoutTimer = 0.1f;
    private float idleTimer = 5.0f;
    public float slideTimer;
    public float halfSlideKickTimer;
    public float damageTimer = 1.0f;
    public float jumpLockoutTimer = 0.2f;       // Fixes state changing to running  (grounded) before leaving the ground
    public float jumpChargeTimer = 0.05f;
    public float jumpLandingTimer = 0.5f;
    private int continueInterval = 3;       // Jump continue method triggers every interval no. of updates
    private int jumpContinueCount = continueInterval;
    private int diveContinueCount = continueInterval;

    // Unstick physics
    private float unstickTimer = 0f;
    private float unstickTimeToCheck = 0.5f;
    private float minSpeed = 0.1f;

    // Textures
    private TextureRegion wolfIdleRegion;
    private TextureRegion wolfSleepRegion;
    private TextureRegion wolfGrowlRegion;
    private TextureRegion wolfRunRegion;
    private TextureRegion wolfJumpRegion;
    private TextureRegion wolfSlideRegion;
    private TextureRegion wolfSlideKickRegion;
    private TextureRegion wolfDiveKickingRegion;
    private TextureRegion wolfDamageRegion;
    private TextureRegion wolfDeathRegion;

    // Animations
    private Animation wolfIdle;
    private Animation wolfSleep;
    private Animation wolfGrowl;
    private Animation wolfRun;
    private Animation wolfJumpCharge;
    private Animation wolfJumpAscend;
    private Animation wolfJumpDescend;
    private Animation wolfJumpLanding;
    private Animation woldDoubleJumpAscend;
    private Animation woldDoubleJumpDescend;
    private Animation wolfAttackDash;
    private Animation wolfLongJumpAccend;
    private Animation wolfAttackJumpAccend;
    private Animation wolfAttackJumpDecend;
    private Animation wolfAttackJumpLanding;
    private Animation wolfAttackDive;
    private Animation wolfDamage;
    private Animation wolfDamageLanding;
    private Animation wolfDeathLanding;

    // States
    public boolean autoRun = false;
    public boolean savedRunningRight = runningRight;
    public boolean enemyRight = true;
    public boolean invincible = false;
    public boolean isFrozen = false;
    public int health = 3;

    public enum InputLevel {
        BASIC,
        INTERMEDIATE,
        ADVANCED
    }
    public InputLevel currentInputLevel;

    // Colliders
    public Fixture rightHitCollider;
    public Fixture leftHitCollider;

    // Attack Animator
    private AttackAnimator attackAnimator;
    private boolean attackingDebug = false;
//    private TextureRegion attFrame;

    // Prefs
    private boolean controlsLRInsteadOfCD;//, controlsYFlipped;

    public Player(BeyondManager manager, World world, LevelScreen screen, float startX, float startY, int inputLevel) {
        super(manager, world, screen, "wolf_idle", startX, startY);
        this.prefs = manager.prefs;
        super.defineDynamicBody(true);
        super.setupStateMachine();
        defineColliders();
        super.levelScreen = screen;
        this.attackAnimator = new AttackAnimator(manager, screen, this);

        // Textures
        wolfIdleRegion = manager.getAtlas().findRegion("wolf_idle");
        wolfSleepRegion = manager.getAtlas().findRegion("wolf_sleep");
        wolfGrowlRegion = manager.getAtlas().findRegion("wolf_growl");
        wolfRunRegion = manager.getAtlas().findRegion("wolf_run");
        wolfJumpRegion = manager.getAtlas().findRegion("wolf_jump");
        wolfSlideRegion = manager.getAtlas().findRegion("wolf_slide");
        wolfSlideKickRegion = manager.getAtlas().findRegion("wolf_slidekick");
        wolfDiveKickingRegion = manager.getAtlas().findRegion("wolf_slidekick");       /** TODO: Change this to divekicking */
        wolfDamageRegion = manager.getAtlas().findRegion("wolf_damage");
        wolfDeathRegion = manager.getAtlas().findRegion("wolf_death");

//        attFrame = manager.getAtlas().findRegion("spider_hanging");

        stateTimer = 0;
        setAnimations();

        // Buonds and animation region
        this.setBounds(0,0, 64 / BeyondManager.PPM, 64 / BeyondManager.PPM); // So it knows how large to draw sprite
        this.setRegion(wolfIdleRegion);

        // State machine
        stateMachine = new DefaultStateMachine<Player, PlayerStateMachine>(this, PlayerStateMachine.NEUTRAL);
        currentState = stateMachine.getCurrentState().toString();
        lastState = currentState;

        // Set movement velocities
        /** Note:
         *      Actor (super) methods resolve time in milliseconds
         *      Update (dt) use seconds
         *          i.e. divide by 1000
         */
        damageVR = new Vector2(resolveJumpVelocity(5f, 5f));
        damageVL = new Vector2(resolveJumpVelocity(-5f, 5f));

        // Basic controls
        jumpVR = new Vector2(resolveJumpVelocity(8.0f, 5f));
        jumpVL = new Vector2(resolveJumpVelocity(-8.0f, 5f));
        slideV = resolveDashVelocity(10, 2 * runVMax);
        slideVR = new Vector2(slideV, 0);
        slideVL = new Vector2(-slideV, 0);
        slideTimer = resolveDashTime(slideV, 10) / 1000f;

        // Physics bug
        miniJumpR = new Vector2(jumpVR.x / 3, jumpVR.y / 3);
        miniJumpL = new Vector2(jumpVL.x / 3, jumpVL.y / 3);

        // Intermediate controls
        londJumpVR = new Vector2(resolveJumpVelocity(17f, 3f));
        londJumpVL = new Vector2(resolveJumpVelocity(-17f,3f));
        diveKickImp = new Vector2(3f, -3f);
        rDiveKickImp = new Vector2(-3f, -3f);

        // Advanced controls
        slideKickVR = new Vector2(resolveJumpVelocity(10f,2f));
        slideKickVL = new Vector2(resolveJumpVelocity(-10f,2f));
        halfSlideKickTimer = resolveJumpDuration(10f, 2f) / 2;
        //Gdx.app.log(tag, "--------- SLIDE KICK TIMER: " + halfSlideKickTimer + "-----------");

        // Input and State Machines
        if (inputLevel == 0) {
            currentInputLevel = InputLevel.BASIC;
        } else if (inputLevel == 1) {
            currentInputLevel = InputLevel.INTERMEDIATE;
        } else if (inputLevel == 2) {
            currentInputLevel = InputLevel.ADVANCED;
        }
        //setupStateMachine();

        getPrefs();
    }

    public void getPrefs() {
        //controlsYFlipped = prefs.getBoolean("controlsYFlipped");
        controlsLRInsteadOfCD = prefs.getBoolean("controlsLRInsteadOfCD");
    }

    @Override
    public void setAnimations() {
        Array<TextureRegion> frames = new Array<TextureRegion>();

        // Idle
        for(int i = 0; i < 8; i++) {
            frames.add(new TextureRegion(wolfIdleRegion, i * 64, 0, 64, 64));

        }
        wolfIdle = new Animation(0.15f, frames);
        frames.clear();

        // Sleep
        for(int i = 0; i < 4; i++) {
            frames.add(new TextureRegion(wolfSleepRegion, i * 64, 0, 64, 64));
        }
        frames.add(new TextureRegion(wolfSleepRegion, 3 * 64, 0, 64, 64));
        frames.add(new TextureRegion(wolfSleepRegion, 3 * 64, 0, 64, 64));
        frames.add(new TextureRegion(wolfSleepRegion, 2 * 64, 0, 64, 64));
        frames.add(new TextureRegion(wolfSleepRegion, 1 * 64, 0, 64, 64));
        frames.add(new TextureRegion(wolfSleepRegion, 0 * 64, 0, 64, 64));
        wolfSleep = new Animation(0.25f, frames);
        frames.clear();

        // Growl
        for(int i = 0; i < 2; i++) {
            frames.add(new TextureRegion(wolfGrowlRegion, i * 64, 0, 64, 64));
        }
        wolfGrowl = new Animation(0.15f, frames);
        frames.clear();

        // Run
        for(int i = 0; i < 6; i++) {
            frames.add(new TextureRegion(wolfRunRegion, i * 64, 0, 64, 64));
        }
        wolfRun = new Animation(0.1f, frames);
        frames.clear();

        // Jump
        frames.add(new TextureRegion(wolfJumpRegion, 0 * 64, 0, 64, 64));
        wolfJumpCharge = new Animation(0.1f, frames);
        frames.clear();

        frames.add(new TextureRegion(wolfJumpRegion, 1 * 64, 0, 64, 64));
        wolfJumpAscend = new Animation(0.1f, frames);
        frames.clear();


        frames.add(new TextureRegion(wolfJumpRegion, 2 * 64, 0, 64, 64));
        wolfJumpDescend = new Animation(0.2f, frames);
        frames.clear();

        for(int i = 3; i < 4; i++) {
            frames.add(new TextureRegion(wolfJumpRegion, i * 64, 0, 64, 64));
        }
        wolfJumpLanding = new Animation(0.1f, frames);
        frames.clear();

        woldDoubleJumpAscend = wolfJumpAscend;
        woldDoubleJumpDescend = wolfJumpDescend;

        // Slide
        for(int i = 0; i < 6; i++) {
            frames.add(new TextureRegion(wolfSlideRegion, i * 64, 0, 64, 64));
        }
        wolfAttackDash = new Animation(0.1f, frames);
        frames.clear();

        // LongJump
        wolfLongJumpAccend = wolfJumpAscend;

        // SlideKick
        for(int i = 0; i < 1; i++) {
            frames.add(new TextureRegion(wolfSlideKickRegion, i * 64, 0, 64, 64));
        }
        wolfAttackJumpAccend = new Animation(0.1f, frames);
        frames.clear();

        for(int i = 1; i < 2; i++) {
            frames.add(new TextureRegion(wolfSlideKickRegion, i * 64, 0, 64, 64));
        }
        wolfAttackJumpDecend = new Animation(0.1f, frames);
        frames.clear();

        for(int i = 2; i < 4; i++) {
            frames.add(new TextureRegion(wolfSlideKickRegion, i * 64, 0, 64, 64));
        }
        wolfAttackJumpLanding = new Animation(0.1f, frames);
        frames.clear();

        // DiveKick
        wolfAttackDive = wolfAttackJumpDecend;
        //wolfDiveKickLanding = wolfSlideKickLanding;

        // Damage
        for(int i = 0; i < 2; i++) {
            frames.add(new TextureRegion(wolfDamageRegion, i * 64, 0, 64, 64));
        }
        wolfDamage = new Animation(0.1f, frames);
        frames.clear();

        for(int i = 2; i < 4; i++) {
            frames.add(new TextureRegion(wolfDamageRegion, i * 64, 0, 64, 64));
        }
        wolfDamageLanding = new Animation(0.1f, frames);
        frames.clear();

        // Death
        //wolfDeath = wolfDamage;

        for(int i = 2; i < 7; i++) {
            frames.add(new TextureRegion(wolfDeathRegion, i * 64, 0, 64, 64));
        }
        wolfDeathLanding = new Animation(0.1f, frames);
        frames.clear();
    }

    @Override
    public void update(float dt) {
        /** dt: on average is 0.015 */
        elapsedTime += dt;
        recordSpeed(dt);

        stateMachine.update();
        currentState = stateMachine.getCurrentState().toString();
        if (debugging) {
            if (lastState != currentState) {
                Gdx.app.log(tag, currentState);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
            autoRun = !autoRun;
        }

            // Position, state and attack animation
        Vector2 pos = new Vector2(
                body.getPosition().x - getWidth() / 2,    // 96 / 2 = 48
                body.getPosition().y - getHeight() / 5);
        super.setPosition(pos.x, pos.y);

        super.checkStateTimerReset();

        TextureRegion currentRegion = getFrame(dt);
        super.setRegion(currentRegion);

        attackAnimator.update(dt, new TextureRegion(currentRegion), pos);
        if (debugging) {
            if (attacking != attackingDebug) {
                Gdx.app.log(tag, "Attacking: " + attacking);
            }
            attackingDebug = attacking;
        }

//        attFrame = new TextureRegion(currentRegion);

        lastState = stateMachine.getCurrentState().toString();

        /** Physics bug */
        if (currentState == "JUMP_DESCEND") {
            // If we've not moves much while still descending, add to unstick timer
            if (velocityIsSmall(pastSpeed5)
                    && velocityIsSmall(pastSpeed4)
                    && velocityIsSmall(pastSpeed3)
                    && velocityIsSmall(pastSpeed2)
                    && velocityIsSmall(pastSpeed1)
            ) {
                unstickTimer += dt;

                // when unstick timer gets too big, checkUnstuck happens in the statemachine
            }

        } else if (unstickTimer != 0f) {
            unstickTimer = 0f;
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)) {
            Gdx.app.log(tag,
                    "*** Player ***\n"
                            + "velocity: " + body.getLinearVelocity() + "\n"
                            + "currentState, lastState: " + currentState + ", " + lastState + "\n"
                            + "pastSpeed5: " + pastSpeed5 + "\n"
                            + "pastSpeed4: " + pastSpeed4 + "\n"
                            + "pastSpeed3: " + pastSpeed3 + "\n"
                            + "pastSpeed2: " + pastSpeed2 + "\n"
                            + "pastSpeed1: " + pastSpeed1 + "\n"
                    );
        }
    }

    @Override
    public void defineColliders() {
        // Box2d Body
        FixtureDef fdef = new FixtureDef();

        /** Player Sensors */
        // Sides
        EdgeShape sideL = new EdgeShape();
        sideL.set(new Vector2(-7 / BeyondManager.PPM, 4 / BeyondManager.PPM),
                new Vector2(-7 / BeyondManager.PPM, 0 / BeyondManager.PPM));
        fdef.shape = sideL;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData("sideLeft");

        EdgeShape sideR = new EdgeShape();
        sideR.set(new Vector2(7 / BeyondManager.PPM, 4 / BeyondManager.PPM),
                new Vector2(7 / BeyondManager.PPM, 0 / BeyondManager.PPM));
        fdef.shape = sideR;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData("sideRight");

        // CutScene Body Sensor
        CircleShape cir = new CircleShape();
        cir.setRadius((bodyRadius + 2) / BeyondManager.PPM);
        fdef.shape = cir;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData("playerCutSceneCollider");


        /** Colliders */
        /** TODO: Base collider radius on sprite dimensions? */
        float colliderWidth;
        float colliderHeight;
        float colliderWidthRight;
        float colliderWidthLeft;

        /** Player HIT Collider - Enemy detection */
        // Shapes
        PolygonShape polygonAttactR = new PolygonShape();

        /** TODO: Base collider radius on sprite dimensions? */
        colliderWidth = super.bodyRadius * 6;
        colliderHeight = super.bodyRadius * 6;
        colliderWidthRight = super.bodyRadius * 9;

        Vector2 bottomLeftAttactR = new Vector2(-colliderWidth / BeyondManager.PPM, 0);
        Vector2 bottomRightAttactR = new Vector2(colliderWidthRight / BeyondManager.PPM, 0);
        Vector2 topLeftAttactR = new Vector2(-colliderWidth / BeyondManager.PPM, colliderHeight / BeyondManager.PPM);
        Vector2 topRightAttactR = new Vector2(colliderWidthRight / BeyondManager.PPM, colliderHeight / BeyondManager.PPM);

        Vector2[] vertsAttactR = new Vector2[] {
                bottomLeftAttactR, bottomRightAttactR, topRightAttactR, topLeftAttactR
        };

        polygonAttactR.set(vertsAttactR);
        shape = polygonAttactR;

        // Fixture definition
        fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.isSensor = true;
        rightHitCollider = super.body.createFixture(fdef);

        // Shapes
        PolygonShape polygonAttactL = new PolygonShape();

        /** TODO: Base collider radius on sprite dimensions? */
        colliderWidthLeft = super.bodyRadius * 9;

        Vector2 bottomLeftAttactL = new Vector2(-colliderWidthLeft / BeyondManager.PPM, 0);
        Vector2 bottomRightAttactL = new Vector2(colliderWidth / BeyondManager.PPM, 0);
        Vector2 topLeftAttactL = new Vector2(-colliderWidthLeft / BeyondManager.PPM, colliderHeight / BeyondManager.PPM);
        Vector2 topRightAttactL = new Vector2(colliderWidth / BeyondManager.PPM, colliderHeight / BeyondManager.PPM);

        Vector2[] vertsAttactL = new Vector2[] {
                bottomLeftAttactL, bottomRightAttactL, topRightAttactL, topLeftAttactL
        };

        polygonAttactL.set(vertsAttactL);
        shape = polygonAttactL;

        // Fixture definition
        fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.isSensor = true;

        leftHitCollider = super.body.createFixture(fdef);

        /** Player HURT Collider - Enemy detection */
        colliderWidth = super.bodyRadius * 5;
        colliderHeight = super.bodyRadius * 5;

        // Shapes
        PolygonShape polygon = new PolygonShape();

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
        fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.isSensor = true;
        super.body.createFixture(fdef).setUserData("playerHurtCollider");
    }

    @Override
    public void setName(String name) {
    }

    @Override
    public void damageActor() { }

    @Override
    public TextureRegion getFrame(float dt) {
        TextureRegion region;

        switch (stateMachine.getCurrentState().toString()) {
                //stateHandler.returnCurrentStateString()) {
            case "NEUTRAL":
                if (isGroundedNormal()){
                    region = (TextureRegion) wolfIdle.getKeyFrame(stateTimer, true);
                } else {
                    region = (TextureRegion) wolfJumpDescend.getKeyFrame(stateTimer, false);
                }
                break;
            case "SLEEP":
                region = (TextureRegion) wolfSleep.getKeyFrame(stateTimer, true);
                break;
            case "STOPPED":
                region = (TextureRegion) wolfIdle.getKeyFrame(stateTimer, true);
                break;
            case "GROWL":
                region = (TextureRegion) wolfGrowl.getKeyFrame(stateTimer, true);
                break;
            case "RUN":
                // Check - Hitting wall, going slowly or running
                if (pastLocation3.x - pastLocation1.x < 0.05f && pastLocation3.x - pastLocation1.x > -0.05f)  {
                    region = (TextureRegion) wolfIdle.getKeyFrame(stateTimer, true);
                } else if (body.getLinearVelocity().x > -walkThreshold && body.getLinearVelocity().x < walkThreshold) {
                    region = (TextureRegion) wolfIdle.getKeyFrame(stateTimer, true);
                } else {
                    region = (TextureRegion) wolfRun.getKeyFrame(stateTimer, true);
                }
                break;
            case "JUMP_CHARGE":
                region = (TextureRegion) wolfJumpCharge.getKeyFrame(stateTimer, false);
                break;
            case "JUMP_ASCEND":
                region = (TextureRegion) wolfJumpAscend.getKeyFrame(stateTimer, false);
                break;
            case "JUMP_DESCEND":
                region = (TextureRegion) wolfJumpDescend.getKeyFrame(stateTimer, false);
                break;
            case "JUMP_LANDING":
                region = (TextureRegion) wolfJumpLanding.getKeyFrame(stateTimer, false);
                break;
            case "DOUBLE_JUMP_ASCEND":
                region = (TextureRegion) woldDoubleJumpAscend.getKeyFrame(stateTimer, false);
                break;
            case "DOUBLE_JUMP_DESCEND":
                region = (TextureRegion) woldDoubleJumpDescend.getKeyFrame(stateTimer, false);
                break;
            case "ATTACK_DASH":
                region = (TextureRegion) wolfAttackDash.getKeyFrame(stateTimer, true);
                break;
            case "LONGJUMP_ASCEND":
                region = (TextureRegion) wolfLongJumpAccend.getKeyFrame(stateTimer, false);
                break;
            case "ATTACK_JUMP_ASCEND":
                region = (TextureRegion) wolfAttackJumpAccend.getKeyFrame(stateTimer, true);
                break;
            case "ATTACK_JUMP_DESCEND":
                region = (TextureRegion) wolfAttackJumpDecend.getKeyFrame(stateTimer, false);
                break;
            case "ATTACK_JUMP_LANDING":
                region = (TextureRegion) wolfAttackJumpLanding.getKeyFrame(stateTimer, false);
                break;
            case "ATTACK_DIVE":
                region = (TextureRegion) wolfAttackDive.getKeyFrame(stateTimer, false);
                break;
            case "DAMAGE_KNOCKBACK":
                region = (TextureRegion) wolfDamage.getKeyFrame(stateTimer, false);
                break;
            case "DAMAGE_LANDING":
                region = (TextureRegion) wolfDamageLanding.getKeyFrame(stateTimer, false);
                break;
            case "DEATH_LANDING":
                region = (TextureRegion) wolfDeathLanding.getKeyFrame(stateTimer, false);
                break;
            default:
                if (isGroundedNormal()){
                    region = (TextureRegion) wolfIdle.getKeyFrame(stateTimer, true);
                } else {
                    region = (TextureRegion) wolfJumpDescend.getKeyFrame(stateTimer, false);
                }
                break;
        }

        region = setRunningRight(region);
        directHitCollider();

        if (lastState != currentState) { stateTimer = 0; } else { stateTimer += dt; }

        //stateTimer = currentState == previousState ? stateTimer + dt : 0;
        //previousState = stateHandler.returnCurrentStateString();
//        stateTimer += dt;
        return region;
    }
    private void recordSpeed(float dt) {
        trackTime += dt;

        //Gdx.app.log(tag, "yes");

        if (trackTime > trackInterval) {

            // Update past speeds
            pastSpeed5 = pastSpeed4;
            pastSpeed4 = pastSpeed3;
            pastSpeed3 = pastSpeed2;
            pastSpeed2 = pastSpeed1;
            pastSpeed1 = new Vector2(body.getLinearVelocity().x, body.getLinearVelocity().y);

            //pastLocation5 = pastLocation4;
            //pastLocation4 = pastLocation3;
            pastLocation3 = pastLocation2;
            pastLocation2 = pastLocation1;
            pastLocation1 = new Vector2(body.getPosition());

            // Reset tracktime
            trackTime = 0.0f;
        }
    }
    public TextureRegion setRunningRight(TextureRegion region) {
        TextureRegion orientatedRegion = region;

        if (stateMachine.getCurrentState().toString().equals("RUN")) {
                //stateHandler.returnCurrentStateString().equals("RUNNING")
                //|| stateHandler.returnCurrentStateString().equals("SLIDING")) {
            if (body.getLinearVelocity().x < 0 && !region.isFlipX()) {          // move LEFT, facing RIGHT
                region.flip(true, false);
                runningRight = false;
            } else if (!runningRight && !region.isFlipX()) {                    // set to move LEFT, facing RIGHT
                region.flip(true, false);
                runningRight = false;
            } else if (body.getLinearVelocity().x > 0 && region.isFlipX()) {    // move RIGHT, facing LEFT
                region.flip(true, false);
                runningRight = true;
            } else if (runningRight && region.isFlipX()) {                      // set to move RIGHT, facing LEFT
                region.flip(true, false);
                runningRight = true;
            }
        } else {
            if (!runningRight && !region.isFlipX()) {                           // jump started LEFT, facing RIGHT
                region.flip(true, false);
            } else if (runningRight && region.isFlipX()) {                      // jump started RIGHT, facing LEFT
                region.flip(true, false);
            }
        }

        return orientatedRegion;
    }

    @Override
    public boolean getRunningRight() {
        return runningRight;
    }

    @Override
    public void changeDirection() {
        body.setLinearVelocity(0.0f, body.getLinearVelocity().y);
        runningRight = !runningRight;
    }

    /** DirectHitCollider - Use to set which hit collider should be used for attacking enemies */
    public void directHitCollider() {
        if (runningRight) {
            rightHitCollider.setUserData("playerHitCollider");
            leftHitCollider.setUserData("inactive");
        } else {
            rightHitCollider.setUserData("inactive");
            leftHitCollider.setUserData("playerHitCollider");
        }
    }

    /** Movement Methods */
    //public boolean controlsYFlipped() { return controlsYFlipped; }
    public boolean controlsLRInsteadOfCD() { return controlsLRInsteadOfCD; }
    public boolean leftPressed() {
        //if (controlsLRInsteadOfCD) {
            return levelScreen.leftPressed();
        //} else {
            //return false;
        //}
    }
    public boolean rightPressed() {
        //if (controlsLRInsteadOfCD) {
            return levelScreen.rightPressed();
        //} else {
            //return false;
        //}
    }
    public boolean jumpJustPressed() { return levelScreen.jumpJustPressed(); }
    public boolean attackJustPressed() { return levelScreen.attackJustPressed(); }
    public boolean changeDirJustPressed() {
        //if (!controlsLRInsteadOfCD) {
            return levelScreen.changeDirJustPressed();
        //} else {
            //return false;
        //}
    }

    public boolean landingHard() {
        boolean hardLanding = false;

        //Gdx.app.log(tag, "y velocity: " + body.getLinearVelocity().y);

        if (pastSpeed5.y < -2) {
            //hardLanding = true;
            //Gdx.app.log(tag, "hardlanding: " + body.getLinearVelocity().y);
            //Gdx.app.log(tag, "linear damping: " + body.getLinearDamping());
        }
        return hardLanding;
    }

    public void setRunningRight(boolean right, boolean setAutoRunning) {
        halfXVel();
        autoRun = setAutoRunning;

        if (right) {
            runningRight = true;
        } else {
            runningRight = false;
        }

        runPhysics();
    }
    public void triggerFreeze() {
        if (!isFrozen) {
            isFrozen = true;
            stateMachine.changeState(PlayerStateMachine.FROZEN);
        }
    }
    public void triggerDamage(int damage) {
        updateHealth(damage);
        stateMachine.changeState(PlayerStateMachine.DAMAGE_KNOCKBACK);
    }
    public void updateHealth(int i) {
        health += i;
        levelScreen.hud.updateHealth(health);
    }
    public boolean isDead() {
        boolean dead = false;
        if (health <= 0) { dead = true; }

        return dead;
    }

    /** Initiate Physics for New State - Does not initiate state */
    public void idlePhysics() {
        //if (debugging) { Gdx.app.log(tag, message + stateHandler.returnCurrentState() + " -> Idle"); }

        // Velocities
        //zeroVelocities();
        halfXVel();

        // Input Lockout
        //inputTimer = clock + idleTimer;
    }
    public void runPhysics() {
        /**
         * Velocity follows tangent of ground and limits unwanted y velocities
         * Rotating a vector 90 degrees is particularily simple.
         *      (x, y) rotated 90 degrees around (0, 0) is (-y, x).
         *      If you want to rotate clockwise, you simply do it the other way around, getting (y, -x).
         */

        // Set initial velocity and get tangent
        Vector2 tangent;
        Vector2 velocity;
        if (runningRight) {
            velocity = new Vector2(runVMax, body.getLinearVelocity().y);
            tangent = new Vector2(super.getGroundedRayNormal().y, -super.getGroundedRayNormal().x);
        } else {
            velocity = new Vector2(-runVMax, body.getLinearVelocity().y);
            tangent = new Vector2(-super.getGroundedRayNormal().y, super.getGroundedRayNormal().x);
        }

        // Update velocity to angle of tangent
        velocity.setAngle(tangent.angle());
        body.setLinearVelocity(velocity);
    }
    public void initJumpPhysics() {
        //if (debugging) { Gdx.app.log(tag, message + stateHandler.returnCurrentState() + " -> Jump"); }

        // Velocities
        halfVelocities();
        if (runningRight) {
            body.setLinearVelocity(jumpVR);
        } else {
            body.setLinearVelocity(jumpVL);
        }

        // Input lockout
        setLockout(jumpLockoutTimer);
        //elapsedTime = 0;
        //lockoutTimer = jumpLockoutTimer;
    }
    public void initJumpChargeOrLandingPhysics(boolean charge) {
        //if (debugging) { Gdx.app.log(tag, message + stateHandler.returnCurrentState() + " -> Jump"); }

        // Velocities
        //zeroVelocities();
        //body.setLinearVelocity(0.0f, body.getLinearVelocity().y);

        // Input lockout
        //elapsedTime = 0;
        if (charge) {
            halfVelocities();
            setLockout(jumpChargeTimer);
            //lockoutTimer = jumpChargeTimer;
        } else {
            //setLockout(jumpLandingTimer);
            lockoutTimer = jumpLandingTimer;
            runPhysics();
        }
    }
    public void initDoubleJumpPhysics() {
        //if (debugging) { Gdx.app.log(tag, message + stateHandler.returnCurrentState() + " -> DoubleJump"); }

        // Velocities
        halfVelocities();
        if (runningRight) {
            body.setLinearVelocity(jumpVR);
        } else {
            body.setLinearVelocity(jumpVL);
        }

        // Input lockout - TODO: Change to move specific lockout
        //inputTimer = clock + lockoutTimer;
    }
    public void slidePhysics(boolean initiating) {
        // Velocity
        if (initiating) {
            // Input lockout
            setLockout(slideTimer);
        }

        /**
         * Velocity follows tangent of ground and limits unwanted y velocities
         * Rotating a vector 90 degrees is particularily simple.
         *      (x, y) rotated 90 degrees around (0, 0) is (-y, x).
         *      If you want to rotate clockwise, you simply do it the other way around, getting (y, -x).
         */

        // Set initial velocity and get tangent
        Vector2 tangent;
        Vector2 velocity;
        if (runningRight) {
            velocity = new Vector2(slideVR);
            tangent = new Vector2(super.getGroundedRayNormal().y, -super.getGroundedRayNormal().x);
        } else {
            velocity = new Vector2(slideVL);
            tangent = new Vector2(-super.getGroundedRayNormal().y, super.getGroundedRayNormal().x);
        }

        // Update velocity to angle of tangent
        velocity.setAngle(tangent.angle());
        body.setLinearVelocity(velocity);

//        if (debugging) { levelScreen.p2.set(super.getActorBody().getPosition().add(tangent)); }
    }
    public void slideKickPhysics() {
        // Velocities
        halfVelocities();
        if (runningRight) {
            body.setLinearVelocity(slideKickVR);
        } else {
            body.setLinearVelocity(slideKickVL);
        }

        // Input lockout
        setLockout(halfSlideKickTimer);
                //jumpLockoutTimer);
    }
    public void initUpperCutPhysics() {
        /*
        if (debugging) { Gdx.app.log(tag, message + currentState + " -> UpperCut"); }
        // TODO: This!

        // Velocities

        // State

        // Input lockout
        */
    }
    public void initLongJumpPhysics() {
        //if (debugging) { Gdx.app.log(tag, message + stateHandler.returnCurrentState() + " -> LongJump"); }

        // Velocities
        halfVelocities();
        if (runningRight) {
            body.setLinearVelocity(londJumpVR);
        } else {
            body.setLinearVelocity(londJumpVL);
        }

        // Input lockout
        setLockout(jumpLockoutTimer);
        //elapsedTime = 0;
        //lockoutTimer = jumpLockoutTimer;
    }
    public void initDiveKickPhysics() {
        //if (debugging) { Gdx.app.log(tag, message + stateHandler.returnCurrentState() + " -> DiveKick"); }

        // Velocities
        halfVelocities();
        if (runningRight) {
            body.applyLinearImpulse(diveKickImp, body.getWorldCenter(), true);
        } else {
            body.applyLinearImpulse(rDiveKickImp, body.getWorldCenter(), true);
        }

        // Input lockout - None
    }
    public void initFallPhysics() {
//        if (debugging) { Gdx.app.log(tag, message + currentState + " -> Fall"); }

        // Velocities
        Vector2 veloctiy = new Vector2(body.getLinearVelocity());

        if (body.getLinearVelocity().x > fallXMax) {
            veloctiy = new Vector2(fallXMax, body.getLinearVelocity().y);
        } else if (body.getLinearVelocity().x < -fallXMax) {
            veloctiy = new Vector2(-fallXMax, body.getLinearVelocity().y);
        }
        body.setLinearVelocity(veloctiy);

        // State

        // Input lockout - TODO: Change to move specific lockout
    }
    public void initDamagePhysics() {
        // Velocities
        halfVelocities();
        if (enemyRight) {
            body.setLinearVelocity(damageVL);
        } else {
            body.setLinearVelocity(damageVR);
        }
        // Input lockout
        setLockout(damageTimer);
        //elapsedTime = 0;
        //lockoutTimer = damageTimer;
    }
    public void initBounce(boolean bounceUp) {
        float vx = body.getLinearVelocity().x;
        float vy = body.getLinearVelocity().y;

        //Gdx.app.log(tag, "bouncing");

        /** TODO: Fix bouncers */

        // Velocities
        halfVelocities();
        if (bounceUp) {
            body.setLinearVelocity(new Vector2(-vx, jumpVR.y));
        } else {
            body.setLinearVelocity(new Vector2(-vx, vy));
        }

        runningRight = !runningRight;
    }
    public void initDeath() {
        //playScreen.cutsceneManager.setTriggerTimer(998, 3f);
        levelScreen.scriptManager.actionTrigger(-100);
    }

    public void continueJumpPhysics() {
        /** Periodically reset x velocity
         *      - Enables player to jump over obstacles even when the beginning of jump is blocked by obstacle
         */
        if (currentState.equals("JUMP_ASCEND") || currentState.equals("DOUBLE_JUMP_ASCEND")) {
            jumpContinueCount--;

            // Every interval number of updates
            if (jumpContinueCount < 1) {
                if (runningRight) {
                    body.setLinearVelocity(jumpVR.x, body.getLinearVelocity().y);
                } else {
                    body.setLinearVelocity(jumpVL.x, body.getLinearVelocity().y);
                }
                jumpContinueCount = continueInterval;
            }
        }
    }
    public void continueDivePhysics() {
        if (currentState.equals("ATTACK_DIVE")) {
            diveContinueCount--;

            if (diveContinueCount < 0) {
                if (runningRight) {
                    body.applyLinearImpulse(diveKickImp, body.getWorldCenter(), true);
                } else {
                    body.applyLinearImpulse(rDiveKickImp, body.getWorldCenter(), true);
                }
                diveContinueCount = continueInterval;
            }
        }
    }
    public void limitDescendVelocity() {
        if (body.getLinearVelocity().x > jumpVR.x) {
            body.setLinearVelocity(jumpVR.x, body.getLinearVelocity().y);
        }

        if (body.getLinearVelocity().x < -jumpVR.x) {
            body.setLinearVelocity(jumpVL.x, body.getLinearVelocity().y);
        }

        /*
        if (runningRight) {
            if (body.getLinearVelocity().x > jumpVR.x) {
                body.setLinearVelocity(jumpVR.x, body.getLinearVelocity().y);
            }
        } else {
            if (body.getLinearVelocity().x < jumpVL.x) {
                body.setLinearVelocity(jumpVL.x, body.getLinearVelocity().y);
            }
        }
        */
    }

    public void checkUnstuck() {
        if (unstickTimer > unstickTimeToCheck) {
            getUnstuck();
        }
    }
    private void getUnstuck() {
        /*
        if (velocityIsSmall(pastSpeed5)
                && velocityIsSmall(pastSpeed4)
                && velocityIsSmall(pastSpeed3)
                && velocityIsSmall(pastSpeed2)
                && velocityIsSmall(pastSpeed1)
        ) {
         */
            if (runningRight) {
                body.setLinearVelocity(miniJumpR);
            } else {
                body.setLinearVelocity(miniJumpL);
            }
        //}
    }

    private boolean velocityIsSmall(Vector2 vec) {
        boolean isSmall = false;

        if (vec.y < minSpeed && vec.y > -minSpeed && vec.x < minSpeed && vec.x > -minSpeed) {
            isSmall = true;
        }

        return isSmall;
    }

    /** Draw */
    public void draw(Batch batch) {
        attackAnimator.draw(batch);
        super.draw(batch);
    }
}