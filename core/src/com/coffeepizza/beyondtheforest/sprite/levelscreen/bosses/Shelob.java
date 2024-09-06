package com.coffeepizza.beyondtheforest.sprite.levelscreen.bosses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.coffeepizza.beyondtheforest.BeyondManager;
import com.coffeepizza.beyondtheforest.levelscreen.LevelScreen;
import com.coffeepizza.beyondtheforest.sprite.Actor;
import com.coffeepizza.beyondtheforest.sprite.levelscreen.bosses.AIDirectors.Sdyne;
import com.coffeepizza.beyondtheforest.sprite.levelscreen.bosses.stateMachines.ShelobAttackingStateMachine;
import com.coffeepizza.beyondtheforest.sprite.levelscreen.bosses.stateMachines.ShelobChargingStateMachine;
import com.coffeepizza.beyondtheforest.sprite.util.UserData;

public class Shelob extends Actor {

    // System
    private boolean debugging = false;
    private String tag = "Shelob";
    private String message = "";

    // Manager
    private Sdyne cyberdyne;

    // Type
    private boolean attacker;

    // Textures
    private TextureRegion idleRegionSpider;
    private TextureRegion walkRegionSpider;
    private TextureRegion retreatingRegionSpider;
    private TextureRegion damageRegionSpider;
    private TextureRegion attackRegionSpider;

    private TextureRegion idleRegion;
    private TextureRegion walkingRegion;
    private TextureRegion jumpingDamageRegion;
    private TextureRegion walkingDamageRegion;
    private TextureRegion attackingRegionOne;
    private TextureRegion attackingRegionTwo;
    private TextureRegion attackingRegionThree;
    private TextureRegion deathRegion;

    // Animations
    /*
    private Animation idleAnimationSpider;
    private Animation walkAnimationSpider;
    private Animation retreatingAnimationSpider;
    private Animation chargeAnimationSpider;
    private Animation preAttackAnimationSpider;
    private Animation attackAnimationSpider;
    private Animation damageAnimationSpider;
    private Animation deathAnimationSpider;
    */

    private Animation idleAnimation;
    private Animation walkingAnimation;
    private Animation chargingAnimation;
    private Animation damageKnockbackAnimation;
    private Animation damagedRetreatAnimation;
    private Animation attackingAnimation;
    private Animation deathAnimation;

    // Location
    private Vector2 onsite;
    private Vector2 offsite;

    // Velocities
    private float walkVelocity = 0.625f;
    private float retreatVelocity = walkVelocity * 1.5f;
    private float chargeVelocity = 1.75f;
    private Vector2 damageVR;
    private Vector2 damageVL;

    // Timers
    public float animationTimer = 0f;
    public float attackTime = 7f;
    public float damageKnockbackTimer = 0.2f;

    public float pauseBetweenPhases = 2f;
    public float damageTime = 1f;
    public float retreatPauseTime = 3f;

    // Attack
    public boolean initAttack = false;

    // Colliders
    private UserData shelobUserData;
    private Fixture shelobHitCollider;

    /* Egg
    public float gapBetweenEggs = 2f;
    private float nextLaunchTime = gapBetweenEggs;
    private float minEggDistance = 25f;
    private float eggRangeIncrement = 7.5f;
    private float eggRange = 60f;
    private float xEggDistance;
    */

    public Shelob(BeyondManager manager, World world, LevelScreen screen, Sdyne cyberdyne, Vector2 onsite, Vector2 offsite, boolean attacker) {
        super(manager, world, screen, "shelob_idle", offsite.x, offsite.y);
        this.cyberdyne = cyberdyne;
        this.onsite = onsite;
        this.offsite = offsite;
        this.attacker = attacker;
        defineKinematicBody(false);
        super.setupStateMachine();
        defineColliders();

        // Textures
        //this.idleRegionSpider = manager.getAtlas().findRegion("shelob_idle");
        //this.walkRegionSpider = manager.getAtlas().findRegion("shelob_walk");
        //this.attackRegionSpider = manager.getAtlas().findRegion("shelob_attack");
        //this.retreatingRegionSpider = manager.getAtlas().findRegion("shelob_walk_damaged");
        //this.damageRegionSpider = manager.getAtlas().findRegion("shelob_damage");

        this.idleRegion = manager.getAtlas().findRegion("dark_wollf_idle");
        this.walkingRegion = manager.getAtlas().findRegion("dark_wollf_walk");
        this.jumpingDamageRegion = manager.getAtlas().findRegion("dark_wollf_jumping_damage");
        this.walkingDamageRegion = manager.getAtlas().findRegion("dark_wollf_walk_damaged");
        this.attackingRegionOne = manager.getAtlas().findRegion("dark_wollf_attack1");
        this.attackingRegionTwo = manager.getAtlas().findRegion("dark_wollf_attack2");
        this.attackingRegionThree = manager.getAtlas().findRegion("dark_wollf_attack3");
        this.deathRegion = manager.getAtlas().findRegion("dark_wollf_death");

        stateTimer = 0;
        setAnimations();

        // Buonds and animation region
        //this.setBounds(0,0, 192 / BeyondManager.PPM, 105 / BeyondManager.PPM); // So it knows how large to draw sprite
        this.setBounds(0,0, 72 / BeyondManager.PPM, 48 / BeyondManager.PPM);
        this.setSize((72 * 2)/ BeyondManager.PPM, (48 * 2)/ BeyondManager.PPM);
        this.setRegion(idleRegion);

        // State machine
        if (attacker) {
            stateMachine = new DefaultStateMachine<Shelob, ShelobAttackingStateMachine>(this, ShelobAttackingStateMachine.OFFSITE);
        } else {
            stateMachine = new DefaultStateMachine<Shelob, ShelobChargingStateMachine>(this,ShelobChargingStateMachine.OFFSITE);
        }
        currentState = stateMachine.getCurrentState().toString();
        lastState = currentState;

        /*
        if (offsite.x > onsite.x) {
            // Left Shelobs
            xEggDistance = -minEggDistance;
        } else {
            // Right Shelobs
            xEggDistance = minEggDistance;
        }
        */

        // Velocities
        damageVR = new Vector2(resolveJumpVelocity(6.0f, 5f));
        damageVL = new Vector2(resolveJumpVelocity(-6.0f, 5f));
    }

    @Override
    public void setAnimations() {
        Array<TextureRegion> frames = new Array<TextureRegion>();

        // Idle
        for(int i = 0; i < 6; i++) {
            frames.add(new TextureRegion(idleRegion, i * 72, 48, 72, 48));

        }
        for(int i = 0; i < 6; i++) {
            frames.add(new TextureRegion(idleRegion, i * 72, 0, 72, 48));

        }
        idleAnimation = new Animation(0.15f, frames);
        frames.clear();

        // Walk and Charge
        for(int i = 0; i < 6; i++) {
            frames.add(new TextureRegion(walkingRegion, i * 72, 48, 72, 48));

        }
        for(int i = 0; i < 6; i++) {
            frames.add(new TextureRegion(walkingRegion, i * 72, 0, 72, 48));

        }
        walkingAnimation = new Animation(0.15f, frames);
        chargingAnimation = new Animation(0.075f, frames);
        frames.clear();

        // Damage Knockback
        //for(int i = 0; i < 2; i++) {
        for(int i = 1; i < 2; i++) {
            frames.add(new TextureRegion(jumpingDamageRegion, i * 72, 0, 72, 48));
        }
        damageKnockbackAnimation = new Animation(0.3f, frames);
        frames.clear();

        // Retreating
        for(int i = 0; i < 6; i++) {
            frames.add(new TextureRegion(walkingDamageRegion, i * 72, 48, 72, 48));

        }
        for(int i = 0; i < 6; i++) {
            frames.add(new TextureRegion(walkingDamageRegion, i * 72, 0, 72, 48));

        }
        damagedRetreatAnimation = new Animation(0.15f, frames);
        frames.clear();

        // Attacking
        for(int i = 0; i < 4; i++) {
            frames.add(new TextureRegion(attackingRegionOne, i * 72, 0, 72, 48));

        }
        for(int i = 0; i < 4; i++) {
            frames.add(new TextureRegion(attackingRegionTwo, i * 72, 0, 72, 48));

        }
        for(int i = 0; i < 4; i++) {
            frames.add(new TextureRegion(attackingRegionThree, i * 72, 0, 72, 48));

        }
        /*
        for(int i = 0; i < 3; i++) {
            frames.add(new TextureRegion(attackingRegionFour, i * 72, 0, 72, 48));

        }
        */
        attackingAnimation = new Animation(0.15f, frames);
        frames.clear();

        // Death
        frames.add(new TextureRegion(deathRegion, 0 * 72, 0, 72, 48));
        frames.add(new TextureRegion(deathRegion, 1 * 72, 0, 72, 48));
        frames.add(new TextureRegion(deathRegion, 2 * 72, 0, 72, 48));
        frames.add(new TextureRegion(deathRegion, 3 * 72, 0, 72, 48));
        frames.add(new TextureRegion(deathRegion, 0 * 72, 0, 72, 48));
        frames.add(new TextureRegion(deathRegion, 1 * 72, 0, 72, 48));
        frames.add(new TextureRegion(deathRegion, 2 * 72, 0, 72, 48));
        frames.add(new TextureRegion(deathRegion, 3 * 72, 0, 72, 48));
        frames.add(new TextureRegion(deathRegion, 0 * 72, 0, 72, 48));
        frames.add(new TextureRegion(deathRegion, 1 * 72, 0, 72, 48));
        frames.add(new TextureRegion(deathRegion, 2 * 72, 0, 72, 48));
        frames.add(new TextureRegion(deathRegion, 3 * 72, 0, 72, 48));
        frames.add(new TextureRegion(deathRegion, 0 * 72, 0, 72, 48));
        frames.add(new TextureRegion(deathRegion, 1 * 72, 0, 72, 48));
        frames.add(new TextureRegion(deathRegion, 2 * 72, 0, 72, 48));
        frames.add(new TextureRegion(deathRegion, 3 * 72, 0, 72, 48));

        for(int i = 0; i < 12; i++) {
            frames.add(new TextureRegion(deathRegion, i * 72, 0, 72, 48));

        }

        frames.add(new TextureRegion(deathRegion, 12 * 72, 0, 72, 48));
        frames.add(new TextureRegion(deathRegion, 13 * 72, 0, 72, 48));
        frames.add(new TextureRegion(deathRegion, 12 * 72, 0, 72, 48));
        frames.add(new TextureRegion(deathRegion, 13 * 72, 0, 72, 48));

        frames.add(new TextureRegion(deathRegion, 14 * 72, 0, 72, 48));
        frames.add(new TextureRegion(deathRegion, 13 * 72, 0, 72, 48));
        frames.add(new TextureRegion(deathRegion, 14 * 72, 0, 72, 48));
        frames.add(new TextureRegion(deathRegion, 13 * 72, 0, 72, 48));

        frames.add(new TextureRegion(deathRegion, 14 * 72, 0, 72, 48));
        frames.add(new TextureRegion(deathRegion, 15 * 72, 0, 72, 48));
        frames.add(new TextureRegion(deathRegion, 14 * 72, 0, 72, 48));
        frames.add(new TextureRegion(deathRegion, 15 * 72, 0, 72, 48));

        frames.add(new TextureRegion(deathRegion, 16 * 72, 0, 72, 48));
        frames.add(new TextureRegion(deathRegion, 15 * 72, 0, 72, 48));
        frames.add(new TextureRegion(deathRegion, 16 * 72, 0, 72, 48));
        frames.add(new TextureRegion(deathRegion, 15 * 72, 0, 72, 48));

        frames.add(new TextureRegion(deathRegion, 16 * 72, 0, 72, 48));
        frames.add(new TextureRegion(deathRegion, 17 * 72, 0, 72, 48));
        frames.add(new TextureRegion(deathRegion, 16 * 72, 0, 72, 48));
        frames.add(new TextureRegion(deathRegion, 17 * 72, 0, 72, 48));

        frames.add(new TextureRegion(deathRegion, 18 * 72, 0, 72, 48));
        frames.add(new TextureRegion(deathRegion, 17 * 72, 0, 72, 48));
        frames.add(new TextureRegion(deathRegion, 18 * 72, 0, 72, 48));
        frames.add(new TextureRegion(deathRegion, 17 * 72, 0, 72, 48));

        frames.add(new TextureRegion(deathRegion, 18 * 72, 0, 72, 48));
        frames.add(new TextureRegion(deathRegion, 19 * 72, 0, 72, 48));
        frames.add(new TextureRegion(deathRegion, 18 * 72, 0, 72, 48));
        frames.add(new TextureRegion(deathRegion, 19 * 72, 0, 72, 48));

        frames.add(new TextureRegion(deathRegion, 20 * 72, 0, 72, 48));
        frames.add(new TextureRegion(deathRegion, 19 * 72, 0, 72, 48));
        frames.add(new TextureRegion(deathRegion, 20 * 72, 0, 72, 48));
        frames.add(new TextureRegion(deathRegion, 19 * 72, 0, 72, 48));

        frames.add(new TextureRegion(deathRegion, 20 * 72, 0, 72, 48));
        frames.add(new TextureRegion(deathRegion, 21 * 72, 0, 72, 48));
        frames.add(new TextureRegion(deathRegion, 20 * 72, 0, 72, 48));
        frames.add(new TextureRegion(deathRegion, 21 * 72, 0, 72, 48));

        frames.add(new TextureRegion(deathRegion, 22 * 72, 0, 72, 48));
        frames.add(new TextureRegion(deathRegion, 21 * 72, 0, 72, 48));
        frames.add(new TextureRegion(deathRegion, 22 * 72, 0, 72, 48));
        frames.add(new TextureRegion(deathRegion, 21 * 72, 0, 72, 48));

        frames.add(new TextureRegion(deathRegion, 22 * 72, 0, 72, 48));
        frames.add(new TextureRegion(deathRegion, 23 * 72, 0, 72, 48));
        frames.add(new TextureRegion(deathRegion, 22 * 72, 0, 72, 48));
        frames.add(new TextureRegion(deathRegion, 23 * 72, 0, 72, 48));

        frames.add(new TextureRegion(deathRegion, 24 * 72, 0, 72, 48));
        frames.add(new TextureRegion(deathRegion, 23 * 72, 0, 72, 48));
        frames.add(new TextureRegion(deathRegion, 24 * 72, 0, 72, 48));
        frames.add(new TextureRegion(deathRegion, 23 * 72, 0, 72, 48));

        frames.add(new TextureRegion(deathRegion, 24 * 72, 0, 72, 48));
        frames.add(new TextureRegion(deathRegion, 23 * 72, 0, 72, 48));
        frames.add(new TextureRegion(deathRegion, 24 * 72, 0, 72, 48));

        deathAnimation = new Animation(0.1f, frames);
        frames.clear();












        /** Spider */
        /*
        // Idle
        for(int i = 0; i < 4; i++) {
            frames.add(new TextureRegion(idleRegionSpider, i * 111, 0, 111, 70));

        }
        idleAnimationSpider = new Animation(0.15f, frames);
        frames.clear();

        // Walking
        for(int i = 0; i < 7; i++) {
            frames.add(new TextureRegion(walkRegionSpider, i * 111, 0, 111, 70));

        }
        walkAnimationSpider = new Animation(0.15f, frames);
        chargeAnimationSpider = new Animation(0.05f, frames);
        frames.clear();

        // Retreating
        for(int i = 0; i < 7; i++) {
            frames.add(new TextureRegion(retreatingRegionSpider, i * 111, 0, 111, 70));

        }
        retreatingAnimationSpider = new Animation(0.15f, frames);
        frames.clear();

        // Pre Attack
        for(int i = 0; i < 2; i++) {
            frames.add(new TextureRegion(attackRegionSpider, i * 111, 0, 111, 70));

        }
        preAttackAnimationSpider = new Animation(0.15f, frames);
        frames.clear();

        // Attack
        for(int i = 2; i < 4; i++) {
            frames.add(new TextureRegion(attackRegionSpider, i * 111, 0, 111, 70));

        }
        attackAnimationSpider = new Animation(0.15f, frames);
        frames.clear();

        // Damage
        for(int i = 0; i < 2; i++) {
            frames.add(new TextureRegion(damageRegionSpider, i * 111, 0, 111, 70));

        }
        damageAnimationSpider = new Animation(0.15f, frames);
        frames.clear();

        // Death
        frames.add(new TextureRegion(retreatingRegionSpider, 0 * 111, 0, 111, 70));
        deathAnimationSpider = new Animation(0.15f, frames);
        frames.clear();
        */
    }

    @Override
    public TextureRegion getFrame(float dt) {
        TextureRegion region;

        switch (stateMachine.getCurrentState().toString()) {
            case "CUTSCENE_GOING_ONSITE":
                region = (TextureRegion) walkingAnimation.getKeyFrame(stateTimer, true);
                break;
            case "CUTSCENE_PREATTACKING_STANCE":
                region = (TextureRegion) idleAnimation.getKeyFrame(stateTimer, true);
                break;
            case "GOING_ONSITE_TO_ATTACK":
                region = (TextureRegion) walkingAnimation.getKeyFrame(stateTimer, true);
                break;
            case "FLAME_ATTACK":
                region = (TextureRegion) attackingAnimation.getKeyFrame(stateTimer, false);
                break;
            case "ATTACKING_IDLE":
                region = (TextureRegion) idleAnimation.getKeyFrame(stateTimer, true);
                break;
            case "FLAME_ATTACK_TWO":
                region = (TextureRegion) attackingAnimation.getKeyFrame(stateTimer, false);
                break;
            case "ATTACKING_IDLE_TWO":
                region = (TextureRegion) idleAnimation.getKeyFrame(stateTimer, true);
                break;
            case "CHARGING_ONSITE":
                region = (TextureRegion) chargingAnimation.getKeyFrame(stateTimer, true);
                break;
            case "GOING_OFFSITE":
                region = (TextureRegion) walkingAnimation.getKeyFrame(stateTimer, true);
                break;
            case "OFFSITE":
                region = (TextureRegion) idleAnimation.getKeyFrame(stateTimer, true);
                break;
            case "DAMAGE_KNOCKBACK":
                region = (TextureRegion) damageKnockbackAnimation.getKeyFrame(stateTimer, true);
                break;
            case "DAMAGE_RETREAT":
                region = (TextureRegion) damagedRetreatAnimation.getKeyFrame(stateTimer, true);
                break;
            case "DEAD":
                region = (TextureRegion) deathAnimation.getKeyFrame(stateTimer, false);
                break;
            case "ATTACKING":
                region = (TextureRegion) idleAnimation.getKeyFrame(stateTimer, true);
                break;
            default:
                region = (TextureRegion) idleAnimation.getKeyFrame(stateTimer, true);
                break;
        }

        region = setRunningRight(region);
        if (lastState != currentState) { stateTimer = 0; } else { stateTimer += dt; }

        return region;
    }

    @Override
    public void update(float dt) {
        elapsedTime += dt;
        //nextLaunchTime -= dt;

        stateMachine.update();
        currentState = stateMachine.getCurrentState().toString();

        Vector2 pos = new Vector2(
                body.getPosition().x - getWidth() / 2,    // 96 / 2 = 48
                //body.getPosition().y - getHeight() * 0.45f);
                (body.getPosition().y - getHeight() / 2) + (10 / BeyondManager.PPM));
        super.setPosition(pos.x, pos.y);
        super.checkStateTimerReset();

        TextureRegion currentRegion = getFrame(dt);
        super.setRegion(currentRegion);
        animationTimer += dt;

        lastState = stateMachine.getCurrentState().toString();
    }

    @Override
    public void damageActor() {
        // Update Cyberdyne
        cyberdyne.damageCyberdyne();

        if (!cyberdyne.isDead()) {
            deactivateCollider();

            if (attacker) {
                stateMachine.changeState(ShelobAttackingStateMachine.DAMAGE_KNOCKBACK);
            } else {
                stateMachine.changeState(ShelobChargingStateMachine.DAMAGE_KNOCKBACK);
            }
        }
    }

    @Override
    public void defineColliders() {
        /** TODO:  Do this method in actor? */

        // Shapes
        PolygonShape polygon = new PolygonShape();

        /** TODO: Base collider radius on sprite dimensions? */
        float colliderWidth = super.bodyRadius * 10;
        float colliderHeight = 16;// / BeyondManager.PPM;//super.bodyRadius * 10;
        Vector2 bottomLeft = new Vector2(-colliderWidth / BeyondManager.PPM,
                -(colliderHeight * 2.5f) / BeyondManager.PPM);
        Vector2 bottomRight = new Vector2(colliderWidth / BeyondManager.PPM,
                -(colliderHeight * 2.5f) / BeyondManager.PPM);
        Vector2 topLeft = new Vector2(-colliderWidth / BeyondManager.PPM,
                colliderHeight / BeyondManager.PPM);
        Vector2 topRight = new Vector2(colliderWidth / BeyondManager.PPM,
                colliderHeight / BeyondManager.PPM);

        Vector2[] verts = new Vector2[] {
                bottomLeft, bottomRight, topRight, topLeft
        };

        polygon.set(verts);
        shape = polygon;

        // Fixture definition
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.isSensor = true;
        shelobHitCollider = super.body.createFixture(fdef);

        // User data
        shelobUserData = new UserData("enemyCollider", this, 0);
        shelobHitCollider.setUserData(shelobUserData);
    }
    public void activateCollider() {
        shelobHitCollider.setUserData(shelobUserData);
    }
    public void deactivateCollider() {
        shelobHitCollider.setUserData("inactive");
    }

    @Override
    public TextureRegion setRunningRight(TextureRegion region) {
        TextureRegion orientatedRegion = region;

        /** State based orientation */
        if (stateMachine.getCurrentState().toString().equals("CUTSCENE_GOING_ONSITE")
                || stateMachine.getCurrentState().toString().equals("CUTSCENE_PREATTACKING_STANCE")
                || stateMachine.getCurrentState().toString().equals("GOING_ONSITE_TO_ATTACK")
                || stateMachine.getCurrentState().toString().equals("FLAME_ATTACK")
                || stateMachine.getCurrentState().toString().equals("ATTACKING_IDLE")
                || stateMachine.getCurrentState().toString().equals("FLAME_ATTACK_TWO")
                || stateMachine.getCurrentState().toString().equals("ATTACKING_IDLE_TWO")
                || stateMachine.getCurrentState().toString().equals("CHARGING_ONSITE")
        ) {
            if (onsite.x > offsite.x) {
                // Left Shelobs
                if (region.isFlipX()) { region.flip(true, false); }         // if flipped (left), then flip (end right)
            } else {
                // Right Shelobs
                if (!region.isFlipX()) { region.flip(true, false); }        // if normal (right), then flip (end left)
            }
        }

        if (stateMachine.getCurrentState().toString().equals("GOING_OFFSITE")
                || stateMachine.getCurrentState().toString().equals("DAMAGE_RETREAT")

        ) {
            if (!(onsite.x > offsite.x)) {
                // Left Shelobs
                if (region.isFlipX()) { region.flip(true, false); }         // if flipped (left), then flip (end right)
            } else {
                // Right Shelobs
                if (!region.isFlipX()) { region.flip(true, false); }        // if normal (right), then flip (end left)
            }
        }

        /** Velocity based orientation */
        if (stateMachine.getCurrentState().toString().equals("DAMAGE_KNOCKBACK")
                || stateMachine.getCurrentState().toString().equals("OFFSITE")
                || stateMachine.getCurrentState().toString().equals("DEAD")
        ) {
            if (body.getLinearVelocity().x < 0 && !region.isFlipX()) {
                region.flip(true, false);
                runningRight = false;
            } else if (body.getLinearVelocity().x > 0 && region.isFlipX()) {
                region.flip(true, false);
                runningRight = true;
            }
        }


        /*
        CUTSCENE_GOING_ONSITE
        CUTSCENE_PREATTACKING_STANCE
        GOING_ONSITE_TO_ATTACK
        FLAME_ATTACK
        ATTACKING_IDLE
        FLAME_ATTACK_TWO
        ATTACKING_IDLE_TWO
        CHARGING_ONSITE

        GOING_OFFSITE
        DAMAGE_RETREAT

        DAMAGE_KNOCKBACK
        OFFSITE
        DEAD

         */
        /** Velocity based orientation *
        if (stateMachine.getCurrentState().toString().equals("GOING_ONSITE_TO_ATTACK")
                || stateMachine.getCurrentState().toString().equals("CHARGING_ONSITE")
                || stateMachine.getCurrentState().toString().equals("GOING_OFFSITE")
                || stateMachine.getCurrentState().toString().equals("DAMAGE")
                || stateMachine.getCurrentState().toString().equals("RETREAT")
                || stateMachine.getCurrentState().toString().equals("DEATH")
        ) {
            if (body.getLinearVelocity().x < 0 && !region.isFlipX()) {
                region.flip(true, false);
                runningRight = false;
            } else if (body.getLinearVelocity().x > 0 && region.isFlipX()) {
                region.flip(true, false);
                runningRight = true;
            }
        } else {
            if (!runningRight && !region.isFlipX()) {                           // Going left, facing RIGHT
                region.flip(true, false);
            } else if (runningRight && region.isFlipX()) {                      // Going right, facing LEFT
                region.flip(true, false);
            }
        }

        /** State based orientation *
        if (stateMachine.getCurrentState().toString().equals("CUTSCENE_GOING_ONSITE")
                || stateMachine.getCurrentState().toString().equals("CUTSCENE_PREATTACKING_STANCE")
                || stateMachine.getCurrentState().toString().equals("CUTSCENE_ATTACKING_STANCE")
                || stateMachine.getCurrentState().toString().equals("PREATTACKING")
                || stateMachine.getCurrentState().toString().equals("ATTACKING")
                || stateMachine.getCurrentState().toString().equals("OFFSITE")
        ) {
            if (onsite.x > offsite.x) {
                // Left Shelobs
                if (region.isFlipX()) { region.flip(true, false); }
            } else {
                // Right Shelobs
                if (!region.isFlipX()) { region.flip(true, false); }
            }
        } */

        return orientatedRegion;
    }

    @Override
    public boolean getRunningRight() {
        return runningRight;
    }

    @Override
    public void changeDirection() { }

    /** Movement methods */
        // Physics
    public void goingOnsitePhysics() {
        // Walk
        if (onsite.x > offsite.x) {
            // Left Shelobs
            body.setLinearVelocity(walkVelocity, 0);
        } else {
            // Right Shelobs
            body.setLinearVelocity(-walkVelocity, 0);
        }
    }
    public void chargingOnsitePhysics() {
        // Charge
        if (onsite.x > offsite.x) {
            // Left Shelobs
            body.setLinearVelocity(chargeVelocity, 0);
        } else {
            // Right Shelobs
            body.setLinearVelocity(-chargeVelocity, 0);
        }
    }
    public void goingOffsitePhysics() {
        // Walk
        if (offsite.x > onsite.x) {
            // Left Shelobs
            body.setLinearVelocity(retreatVelocity, 0);
        } else {
            // Right Shelobs
            body.setLinearVelocity(-retreatVelocity, 0);
        }
    }
    public void chargingOffsitePhysics() {
        // Charge
        if (offsite.x > onsite.x) {
            // Left Shelobs
            body.setLinearVelocity(chargeVelocity, 0);
        } else {
            // Right Shelobs
            body.setLinearVelocity(-chargeVelocity, 0);
        }
    }
    public void attacking() {
        /*
        // Fire Egg
        if (nextLaunchTime < 0) {
            if (offsite.x > onsite.x) {
                // Left Shelobs
                xEggDistance -= eggRangeIncrement;
                if (xEggDistance < -eggRange) {
                    xEggDistance = -minEggDistance;
                }
            } else {
                // Right Shelobs
                xEggDistance += eggRangeIncrement;
                if (xEggDistance > eggRange) {
                    xEggDistance = minEggDistance;
                }
            }

            MaggotEgg egg = new MaggotEgg(manager, levelScreen, world, cyberdyne, body.getPosition(), xEggDistance);
            levelScreen.actorList.add(egg);

            // Reset launch timer
            nextLaunchTime = gapBetweenEggs;
        }
        */

    }
    public void damaged() {
        if (levelScreen.player.getActorBody().getPosition().x < body.getPosition().x) {
            // Recoil right
            body.setLinearVelocity(damageVR.x, 0);
        } else {
            // Recoil left
            body.setLinearVelocity(damageVL.x, 0);
        }
    }
    public void damaagedRetreatOffsite() {
        // Charge
        if (offsite.x > onsite.x) {
            // Left Shelobs
            body.setLinearVelocity(chargeVelocity, 0);
        } else {
            // Right Shelobs
            body.setLinearVelocity(-chargeVelocity, 0);
        }
    }
        // Checks
    public boolean gotOnsite() {
        boolean isOnsite = false;
        float distance = Math.abs(body.getPosition().x - onsite.x);

        if (distance < 16f / BeyondManager.PPM) {
            isOnsite = true;
        }

        return isOnsite;
    }
    public boolean initAttack() {
        boolean initiate = false;

        if (!initAttack) {
            if ((stateMachine.getCurrentState() == ShelobAttackingStateMachine.FLAME_ATTACK) ||
                    (stateMachine.getCurrentState() == ShelobAttackingStateMachine.FLAME_ATTACK_TWO)
            ) {
                if (animationTimer > (6 * 0.15f)) {
                    initAttack = true;
                    initiate = true;
                }
            }
        }

        return initiate;
    }
    public boolean flameAttackAnimationComplete() {

        if ((stateMachine.getCurrentState() == ShelobAttackingStateMachine.FLAME_ATTACK) ||
                (stateMachine.getCurrentState() == ShelobAttackingStateMachine.FLAME_ATTACK_TWO)
        ) {
            return (attackingAnimation.isAnimationFinished(animationTimer));
        } else {
            return false;
        }
    }
    public boolean gotOffsite() {
        boolean isOffsite = false;
        float distance = Math.abs(body.getPosition().x - offsite.x);

        if (distance < 16f / BeyondManager.PPM) {
            isOffsite = true;
        }

        return isOffsite;
    }
    /** Attack methods */
    public void flameOn() {
        cyberdyne.flameOn();

        //Gdx.app.log(tag, "FLAME ATTACK");
    }

    @Override
    public void setName(String name) {

    }

    public void draw(Batch batch) {
        super.draw(batch);
    }
}
