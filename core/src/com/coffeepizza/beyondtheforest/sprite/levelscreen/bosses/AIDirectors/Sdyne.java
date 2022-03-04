package com.coffeepizza.beyondtheforest.sprite.levelscreen.bosses.AIDirectors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.coffeepizza.beyondtheforest.BeyondManager;
import com.coffeepizza.beyondtheforest.levelscreen.LevelScreen;
import com.coffeepizza.beyondtheforest.levelscreen.levels.LevelScript;
import com.coffeepizza.beyondtheforest.sprite.Actor;
import com.coffeepizza.beyondtheforest.sprite.levelscreen.bosses.*;
import com.coffeepizza.beyondtheforest.sprite.levelscreen.bosses.AIDirectors.stateMachines.SdyneStateMachine;
import com.coffeepizza.beyondtheforest.sprite.levelscreen.bosses.stateMachines.ShelobAttackingStateMachine;
import com.coffeepizza.beyondtheforest.sprite.levelscreen.bosses.stateMachines.ShelobChargingStateMachine;
import com.coffeepizza.beyondtheforest.sprite.levelscreen.enemies.Maggot;

import java.util.HashMap;

public class Sdyne extends Cyberdyne {

    // System
    private boolean debugging = false;
    private String tag = "Sdyne";
    private String message = "";

    // Shelobs
    public Shelob shelobAttackerRight;
    public Shelob shelobChargerRight;
    public Shelob shelobChargerLeft;
    public Shelob shelobAttackerLeft;
    private boolean updateShelobs = false;
    private boolean drawTR = false;
    private boolean drawBR = false;
    private boolean drawBL = false;
    private boolean drawTL = false;

    // Maggot
    //private MaggotEgg egg;

    // Explosions
    private int explosionNumber = 1;
    private int[] explosionPositions = new int[]
    {
            31, 41, 59,26, 53, 5, 89, 79,32, 38, 46, 26, 43, 38, 32, 79, 50, 28, 84, 19, 71, 69, 39,93, 75, 10, 58, 20,
            97, 49, 44, 59, 23, 7, 81, 64, 6, 28, 62, 8, 99, 86, 28, 3, 48, 25, 34, 21,17, 6, 79, 82, 14, 80, 86, 51,
            32, 82, 30, 66, 47, 9, 38, 44, 60, 95, 50, 58, 22, 31, 72, 53, 59, 40, 81, 28, 48, 11, 17, 45, 2, 84, 10,
            27, 1, 93, 85, 21, 10, 55, 59, 64, 46, 22, 94, 89, 54, 93, 3, 81, 96, 44, 28, 81, 9, 75, 66, 59, 33, 44, 61,
            28, 47, 56, 48, 23, 37, 86, 78, 31, 65, 27, 12, 1, 90, 91, 45, 64, 85, 66, 92, 34, 60,34, 86, 10, 45, 43, 26,
            64, 82, 13, 39, 36, 7, 26, 2, 49, 14, 12, 73, 72, 45, 87, 66, 6, 31, 55, 88, 17, 48, 81, 52, 9, 20, 96, 28,
            29, 25, 40, 91, 71,53, 64, 36, 78, 92, 59, 3, 60, 1, 13, 30, 53, 5, 48, 82, 4, 66, 52, 13, 84,14, 69, 51,
            94, 15, 11, 60, 94, 33, 5, 72, 70, 36, 57, 59, 59, 19, 53, 9, 21, 86, 11, 73, 81, 93,26, 11, 79, 31, 5, 11,
            85, 48, 7, 44, 62, 37, 99, 62, 74, 95, 67, 35, 18, 85, 75, 27, 24, 89, 12, 27, 93, 81, 83, 1, 19, 49, 12,
            98, 33, 67, 33, 62, 44, 6, 56, 64, 30, 86, 2, 13, 94, 94, 63, 95, 22, 47, 37, 19, 7, 2, 17, 98, 60, 94, 37,
            2, 77, 5, 39, 21, 71, 76, 29, 31, 76, 75, 23, 84, 67, 48, 18, 46, 76, 69, 40, 51, 32, 5, 68, 12, 71, 45, 26,
            35, 60, 82, 77, 85, 77, 13, 42, 75, 77, 89, 60, 91, 73, 63, 71, 78, 72, 14, 68, 44, 9, 1, 22, 49, 53, 43, 1,
            46, 54, 95, 85, 37, 10, 50, 79, 22, 79, 68, 92, 58, 92, 35, 42, 1, 99, 56, 11, 21, 29, 2, 19, 60, 86, 40,
            34, 41, 81, 59, 81, 36, 29, 77, 47, 71, 30, 99, 60, 51, 87, 7, 21, 13, 49, 99, 99, 98, 37, 29, 78, 4, 99,
            51, 5, 97, 31, 73, 28, 16, 9, 63, 18, 59, 50, 24, 45, 94, 55, 34, 69, 8, 30, 26, 42, 52, 23, 8, 25, 33, 44,
            68, 50, 35, 26, 19, 31, 18, 81, 71, 1, 3, 13, 78, 38, 75, 28, 86, 58, 75, 33, 20, 83, 81, 42, 6, 17, 17, 76,
            69, 14, 73, 3, 59, 82, 53, 49, 4, 28, 75, 54, 68, 73, 11, 59, 56, 28, 63, 88, 23, 53, 78, 75, 93, 75, 19,
            57, 78, 18, 57, 78, 5, 32, 17, 12, 26, 80, 66, 13, 19, 27, 87, 66, 11, 19, 59, 9, 21, 64, 20, 19, 89
    };
    private float timeToNextExplosion, explosionTimer;
    private boolean above, left;
    private int countToTwo = 0;
    private int countToSix = 0;
    private int explosionSize = 0;
    private Vector2 offset;

    // Properties
    private int health = 1;
    private float topDelta = 20;        // Should be in Shelob class
    private float bottomDelta = 90;    // Should be in Shelob class
    private int shelobActive = 0;   // 0: none, 1: top right, 2: bottom right, 3: bottom left, 4: top left

    // Boss locator
    private BossLocator locator;

    public Sdyne(BeyondManager manager, LevelScreen screen, World world, LevelScript scriptManager, HashMap unsortedTriggers) {
        super(manager, screen, world, scriptManager, unsortedTriggers);

        // Setup State Machine
        stateMachine = new DefaultStateMachine<Sdyne, SdyneStateMachine>(this, SdyneStateMachine.WARMING_UP);
        currentState = stateMachine.getCurrentState().toString();
        lastState = currentState;

        // Explosions
        timeToNextExplosion = 0.05f;
        explosionTimer = 0f;
        above = left = false;
        offset = new Vector2((64 * 0) / BeyondManager.PPM, (64 * 0.25f) / BeyondManager.PPM);

        locator = new BossLocator(manager, screen);
    }

    /** Cutscene Methods */
    public void createShelobs() {
        Vector2 off = bossTriggers.get(-200);
        Vector2 on = new Vector2(
                off.x - ((topDelta * 16) / BeyondManager.PPM),
                off.y
        );
        shelobAttackerRight = new Shelob(manager, world, screen, this, on, off, true);

        off = bossTriggers.get(-201);
        on = new Vector2(
                off.x - ((bottomDelta * 16) / BeyondManager.PPM),
                off.y
        );
        shelobChargerRight = new Shelob(manager, world, screen, this, on, off, false);

        off = bossTriggers.get(-202);
        on = new Vector2(
                off.x + ((bottomDelta * 16) / BeyondManager.PPM),
                off.y
        );
        shelobChargerLeft = new Shelob(manager, world, screen, this, on, off, false);

        off = bossTriggers.get(-203);
        on = new Vector2(
                off.x + ((topDelta * 16) / BeyondManager.PPM),
                off.y
        );
        shelobAttackerLeft = new Shelob(manager, world, screen, this, on, off, true);

        updateShelobs = true;
        drawTR = true;
        drawBR = true;
        drawBL = true;
        drawTL = true;
    }
    public void topRightCutsceneEntery() {
        shelobAttackerRight.stateMachine.changeState(ShelobAttackingStateMachine.CUTSCENE_GOING_ONSITE);
        shelobActive = 1;

        locator.activate();
    }

    /** Normal methods */
    public void initAttack() {
        boolean leftAttacksNext = manager.random.nextBoolean();

        if (leftAttacksNext) {
            shelobAttackerLeft.stateMachine.changeState(ShelobAttackingStateMachine.GOING_ONSITE_TO_ATTACK);
            shelobActive = 4;
        } else {
            shelobAttackerRight.stateMachine.changeState(ShelobAttackingStateMachine.GOING_ONSITE_TO_ATTACK);
            shelobActive = 1;
        }

        locator.activate();
    }
    public void initCharge() {
        boolean leftChargesNext = manager.random.nextBoolean();

        if (leftChargesNext) {
            shelobChargerLeft.stateMachine.changeState(ShelobChargingStateMachine.CHARGING_ONSITE);
            shelobActive = 3;
        } else {
            shelobChargerRight.stateMachine.changeState(ShelobChargingStateMachine.CHARGING_ONSITE);
            shelobActive = 2;
        }

        locator.activate();

        scriptManager.actionTrigger(90);
    }
    public boolean everyoneOffsite() {
        boolean everyonesOffsite = false;

        if (shelobAttackerLeft.stateMachine.getCurrentState() == ShelobAttackingStateMachine.OFFSITE
                && shelobAttackerRight.stateMachine.getCurrentState() == ShelobAttackingStateMachine.OFFSITE
                && shelobChargerLeft.stateMachine.getCurrentState() == ShelobChargingStateMachine.OFFSITE
                && shelobChargerRight.stateMachine.getCurrentState() == ShelobChargingStateMachine.OFFSITE)
        {
            everyonesOffsite = true;
            shelobActive = 0;

            locator.deactivate();
//            Gdx.app.log(tag, "Checking if shelobs are offsite:" + everyonesOffsite);
        }

        return everyonesOffsite;
    }
    public void flameOn() {
        if (shelobActive == 1) {
            screen.flameAttacke(false);
        } else if (shelobActive == 4) {
            screen.flameAttacke(true);
        }
    }

    @Override
    public void setCyberdyneLive() {
        stateMachine.changeState(SdyneStateMachine.OPENING_MOVE);
    }

    @Override
    public void update(float dt) {
        updateCyberdyne(dt);

        // Shelobs
        if (updateShelobs) {
            shelobAttackerRight.update(dt);
            shelobChargerRight.update(dt);
            shelobChargerLeft.update(dt);
            shelobAttackerLeft.update(dt);
        }

        // Locator
        locator.update(dt);

        // Death
        deathAnimations(dt);
    }

    @Override
    public void damageCyberdyne() {
        health--;
        screen.updateBossHealth();

        if (isDead()) {
            stateMachine.changeState(SdyneStateMachine.DEAD);

            shelobAttackerRight.stateMachine.changeState(ShelobAttackingStateMachine.DEAD);
            shelobAttackerRight.deactivateCollider();

            shelobChargerRight.stateMachine.changeState(ShelobChargingStateMachine.DEAD);
            shelobChargerRight.deactivateCollider();

            shelobChargerLeft.stateMachine.changeState(ShelobChargingStateMachine.DEAD);
            shelobChargerLeft.deactivateCollider();

            shelobAttackerLeft.stateMachine.changeState(ShelobAttackingStateMachine.DEAD);
            shelobAttackerLeft.deactivateCollider();

            // Add first explosion
            Actor firstExplosion;
            Vector2 firstPosition = new Vector2(getBossPosition());
            firstPosition.add(new Vector2(-32 / BeyondManager.PPM, -32 / BeyondManager.PPM));

            firstExplosion = new ExplosionSixtyFour(manager, screen, world, firstPosition, 0.1f);
            screen.bossExplosion.add(firstExplosion);

            // Trigger end sequence
            actionCyberdyneTrigger(200);
        }
    }
    public int getBossHealth() {
        return health;
    }
    public boolean isDead() {
        boolean dead = false;

        if (health < 1) { dead = true; }

        return dead;
    }
    private void deathAnimations(float dt) {
        if (isDead()) {
            if (explosionTimer >= timeToNextExplosion) {

                if(explosionNumber < explosionPositions.length) {

                    Vector2 explosionPosition = getBossPosition();
                    float xDelta = (float) (explosionPositions[explosionNumber] * 2) / BeyondManager.PPM;
                    float yDelta = (float) (explosionPositions[explosionNumber - 1] * 1) / BeyondManager.PPM;
                    float yMultiplier, xMultiplier;

                    // Update booleans and counter
                    if (countToTwo < 2) {
                        left = !left;
                        countToTwo++;
                        countToSix++;
                    } else {
                        above = !above;
                        left = !left;
                        if (countToSix > 5) {
                            left = !left;
                        }
                        //left = manager.random.nextBoolean();
                        countToTwo = 0;
                        countToSix = 0;
                    }

                    // Work out multiplyer based booleans
                    if (above) {
                        yMultiplier = 1;
                    } else {
                        yMultiplier = -1;
                    }

                    if (left) {
                        xMultiplier = -1;
                    } else {
                        xMultiplier = 1;
                    }

                    Actor explosion;
                    explosionSize = manager.random.nextInt(4);
                    if (explosionSize == 0) {
                        explosionPosition.add(new Vector2((xDelta * xMultiplier) - (32 / BeyondManager.PPM),
                                (yDelta * yMultiplier) - (32 / BeyondManager.PPM)
                        ));
                        explosionPosition.add(offset);
                        explosion = new ExplosionSixtyFour(manager, screen, world, explosionPosition, 0.1f);
                    } else if (explosionSize == 1) {
                        explosionPosition.add(new Vector2((xDelta * xMultiplier) - (24 / BeyondManager.PPM),
                                (yDelta * yMultiplier) - (24 / BeyondManager.PPM)
                        ));
                        explosionPosition.add(offset);
                        explosion = new ExplosionFourtyEight(manager, screen, world, explosionPosition, 0.075f);
                    } else if (explosionSize == 2) {
                        explosionPosition.add(new Vector2((xDelta * xMultiplier) - (16 / BeyondManager.PPM),
                                (yDelta * yMultiplier) - (16 / BeyondManager.PPM)
                        ));
                        explosionPosition.add(offset);
                        explosion = new ExplosionThirtyTwo(manager, screen, world, explosionPosition, 0.05f);
                    } else {
                        explosionPosition.add(new Vector2((xDelta * xMultiplier) - (8 / BeyondManager.PPM),
                                (yDelta * yMultiplier) - (8 / BeyondManager.PPM)
                        ));
                        explosionPosition.add(offset);
                        explosion = new ExplosionSixteen(manager, screen, world, explosionPosition, 0.025f);
                    }
                    screen.bossExplosion.add(explosion);
                }

                explosionNumber++;
                explosionTimer = 0;
            } else {
                explosionTimer += dt;
            }
        }
    }
    private void expireMaggots() {
        for (int i = 0; i < minionList.size(); i++) {
            Maggot maggot = (Maggot) minionList.get(i);
            maggot.damageActor();
        }
    }

    @Override
    public Vector2 getBossPosition() {
        Vector2 position = new Vector2();

        if (debugging) {
            Gdx.app.log(tag, stateMachine.getCurrentState().toString() + ": "
                    + shelobActive + ", "
                    + stateMachine.getCurrentState().toString().equals("DEAD")
            );
        }


        if (stateMachine.getCurrentState().toString().equals("WARMING_UP")
                || stateMachine.getCurrentState().toString().equals("OPENING_MOVE")
                || stateMachine.getCurrentState().toString().equals("CUTSCENE_DIALOGUE")
                || stateMachine.getCurrentState().toString().equals("CUTSCENE_ATTACK")) {
            position = new Vector2(shelobAttackerRight.getPosition().x,
                    shelobAttackerRight.getPosition().y);
        } else {
            if (shelobActive == 1) {
                position = new Vector2(shelobAttackerRight.getPosition().x,
                        shelobAttackerRight.getPosition().y);
            } else if (shelobActive == 2) {
                position = new Vector2(shelobChargerRight.getPosition().x,
                        shelobChargerRight.getPosition().y);
            } else if (shelobActive == 3) {
                position = new Vector2(shelobChargerLeft.getPosition().x,
                        shelobChargerLeft.getPosition().y);
            } else if (shelobActive == 4) {
                position = new Vector2(shelobAttackerLeft.getPosition().x,
                        shelobAttackerLeft.getPosition().y);
            }
        }

        return position;
    }

    @Override
    public void actionCyberdyneTrigger(int trigger) {
        /** Init intro dialogue from levelScript */
        if (trigger == 1) {
            // Triggers
            actionScreenTrigger(51);

            // Game Mode
            // HUD
            // Camera
            // Sound
            // Player
            // Boss
        }
        else if (trigger == 2) {
            // Triggers
            // Game Mode
            // HUD
            // Camera
            // Sound
            // Player
            // Boss
            stateMachine.changeState(SdyneStateMachine.ATTACK);
            shelobAttackerRight.stateMachine.changeState(ShelobAttackingStateMachine.GOING_ONSITE_TO_ATTACK);
        }

        /** Death */
        else if (trigger == 200) {
            // Triggers
            // Game Mode
            // HUD
            // Camera
            // Sound
            // Player
            // Boss
            actionScreenTrigger(200);
        }
       /*
        else if (trigger == 20) {
            // Triggers
            // Game Mode
            // HUD
            // Camera
            // Sound
            // Player
            // Boss
            expireMaggots();
            actionScreenTrigger(75);
        }
        if (trigger == 1) {
            // Triggers
            // Game Mode
            // HUD
            // Camera
            // Sound
            // Player
            // Boss
            //float xVel = -40f;
            //float yVel = 70f;
            //egg = new MaggotEgg(manager, screen, world, shelobAttackerRight.getActorBody().getPosition(), xVel, yVel);
            //screen.actorList.add(egg);

            //stateMachine.changeState(SdyneStateMachine.CUTSCENE_EGGLAUNCH);
            //actionScreenTrigger(51);
        }
        */

        /*
        else if (trigger == 198) {
            // Triggers
            // Game Mode
            // HUD
            // Camera
            // Sound
            // Player
            // Boss
            screen.flameAttacke(true);
        }
        else if (trigger == 199) {
            // Triggers
            // Game Mode
            // HUD
            // Camera
            // Sound
            // Player
            // Boss
            screen.flameAttacke(false);
        }
        */
    }

    @Override
    public void draw(Batch batch) {
        if (drawTR) { shelobAttackerRight.draw(batch); }
        if (drawBR) { shelobChargerRight.draw(batch); }
        if (drawBL) { shelobChargerLeft.draw(batch); }
        if (drawTL) { shelobAttackerLeft.draw(batch); }

        // Locator
        locator.draw(batch);
    }
}
