package com.coffeepizza.beyondtheforest.sprite.levelscreen.enemies;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.coffeepizza.beyondtheforest.BeyondManager;
import com.coffeepizza.beyondtheforest.levelscreen.LevelScreen;
import com.coffeepizza.beyondtheforest.sprite.Actor;
import com.coffeepizza.beyondtheforest.sprite.levelscreen.bosses.AIDirectors.Cyberdyne;
import com.coffeepizza.beyondtheforest.sprite.levelscreen.bosses.AIDirectors.Sdyne;
import com.coffeepizza.beyondtheforest.sprite.levelscreen.enemies.stateMachines.MaggotEggState;
import com.coffeepizza.beyondtheforest.sprite.util.AttackAnimator;

import static com.coffeepizza.beyondtheforest.sprite.levelscreen.enemies.stateMachines.MaggotEggState.CRACKED;

public class MaggotEgg extends Actor {

    // System
    private boolean debugging = false;
    private String tag = "MaggotEgg";
    private String message = "";

    // Managers
    private Cyberdyne cyberdyne;

    // Textures
    private TextureRegion egg;
    private TextureRegion crackedEgg;

    // Animations
    private Animation flyingEgg;

    // Properties
    private boolean isCracked = false;
    private float yMultiplier = 1.5f;

    // Attack Animator
    private AttackAnimator attackAnimator;

    public MaggotEgg(BeyondManager manager, LevelScreen screen, World world, Sdyne cyberdyne, Vector2 position, float xDistance) {
        super(manager, world, screen, "worm_egg");
        super.x = position.x;
        super.y = position.y + ((1f * 16) / BeyondManager.PPM);
        super.defineDynamicBody(false);
        super.setupStateMachine();
        this.cyberdyne = cyberdyne;
        this.attackAnimator = new AttackAnimator(manager, screen, this);

        setupRegions();

        stateMachine = new DefaultStateMachine<MaggotEgg, MaggotEggState>(this, MaggotEggState.FLYING);

        // Launch egg
        body.setLinearVelocity(resolveJumpVelocity(xDistance, Math.abs(xDistance) * yMultiplier));
    }

    public MaggotEgg(BeyondManager manager, LevelScreen screen, World world, Vector2 position, float xDistance, float yDistance) {
        super(manager, world, screen, "worm_egg");
        super.x = position.x;
        super.y = position.y + ((1f * 16) / BeyondManager.PPM);
        super.defineDynamicBody(false);
        super.setupStateMachine();
        this.attackAnimator = new AttackAnimator(manager, screen, this);

        setupRegions();

        stateMachine = new DefaultStateMachine<MaggotEgg, MaggotEggState>(this, MaggotEggState.FLYING);
        attacking = true;

        // Launch egg
        body.setLinearVelocity(resolveJumpVelocity(xDistance, yDistance));
    }

    private void setupRegions() {
        // Get and set regions
        egg = new TextureRegion(this.manager.getAtlas().findRegion("worm_egg"));
        int n = 1 + manager.random.nextInt(1);
        crackedEgg = new TextureRegion(egg, n * 48, 0, 48, 48);

        super.setBounds(0, 0, 48 / BeyondManager.PPM, 48 / BeyondManager.PPM);
        super.setRegion(egg);

        stateTimer = 0;
        setAnimations();
    }

    @Override
    public void setAnimations() {
        Array<TextureRegion> frames = new Array<TextureRegion>();

        // Flying
        frames.add(new TextureRegion(egg, 0 * 48, 0, 48, 48));
        flyingEgg = new Animation(0.1f, frames);
        frames.clear();
    }

    @Override
    public TextureRegion getFrame(float dt) {
        TextureRegion region;

        switch (stateMachine.getCurrentState().toString()) {
            case "FLYING":
                region = (TextureRegion) flyingEgg.getKeyFrame(stateTimer, true);
                break;
            default: //"CRACKED":
                region = crackedEgg;
                break;
//            default:
//                region = (TextureRegion) flyingEgg.getKeyFrame(stateTimer, true);
//                break;
        }

        if (lastState != currentState) { stateTimer = 0; } else { stateTimer += dt; }
        return region;
    }

    @Override
    public void update(float dt) {
        // Update state machine
        elapsedTime += dt;

        stateMachine.update();
        currentState = stateMachine.getCurrentState().toString();

        Vector2 pos = new Vector2(
                body.getPosition().x - getWidth() / 2,    // 96 / 2 = 48
                body.getPosition().y - getHeight() / 2);
        super.setPosition(pos.x, pos.y);

        super.checkStateTimerReset();

        TextureRegion currentRegion = getFrame(dt);
        super.setRegion(currentRegion);
        attackAnimator.update(dt, new TextureRegion(currentRegion), pos);

        lastState = stateMachine.getCurrentState().toString();
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
        return runningRight;
    }

    @Override
    public void changeDirection() { }

    /** Methods */
        // Physics
    public void landingPhysics() {
        // Crack timer if landed or older than 10seconds
        if (isGroundedNormal() || elapsedTime > 10.0f) {
            stateMachine.changeState(CRACKED);
            body.setLinearDamping(1);
            isCracked = true;

            /** TODO: Spawn maggot */
            Maggot maggot = new Maggot(manager, levelScreen, world, body.getPosition());
            levelScreen.actorList.add(maggot);
            cyberdyne.minionList.add(maggot);
        }
    }

    @Override
    public void setName(String name) {

    }

    /** Draw */
    public void draw(Batch batch) {
        attackAnimator.draw(batch);
        super.draw(batch);
    }
}
