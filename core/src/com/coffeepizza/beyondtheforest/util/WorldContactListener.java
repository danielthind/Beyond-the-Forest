package com.coffeepizza.beyondtheforest.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.coffeepizza.beyondtheforest.levelscreen.GameCamera;
import com.coffeepizza.beyondtheforest.levelscreen.LevelScreen;
import com.coffeepizza.beyondtheforest.sprite.Actor;
import com.coffeepizza.beyondtheforest.sprite.util.UserData;

public class WorldContactListener implements ContactListener {

    // System
    private boolean debugging = false;
    private String tag = "WorldContactListener";
    private String message = "";

    // Manager
    private LevelScreen screen;

    // Camera
    private GameCamera camera;

    public WorldContactListener(LevelScreen screen, GameCamera camera) {
        super();
        this.screen = screen;
        this.camera = camera;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        /**
         * When at least ONE of the fixtures UserData is a string
         *      - playerCutSceneCollider    - cutscerne triggers, freezer tiles
         *
         *      - playerHitCollider
         *      - playerHurtCollider
         */

        if ((fixA.getUserData() instanceof String) || (fixB.getUserData() instanceof String)) {
            Fixture stringFixture;
            Fixture objectFixture;

            /** TODO: What happens when they're both strings? */
            if(fixA.getUserData() instanceof String) {
                stringFixture = fixA;
                objectFixture = fixB;
            } else {
                stringFixture = fixB;
                objectFixture = fixA;
            }

            /**
             * Player cutscene collider - Triggers
             */
            if (stringFixture.getUserData().equals("playerCutSceneCollider")) {
                if (objectFixture.getUserData() instanceof UserData) {
                    UserData userData = (UserData) objectFixture.getUserData();

//                    Gdx.app.log(tag, "trigger: " + userData.trigger);

                    if (userData.type.equals("trigger")) {
                        int t = userData.trigger;
                        screen.scriptManager.recordCheckpoint(t);
                        screen.scriptManager.actionTrigger(t);
                    }
                }
            }

            /**
             * Player Hit
             */
            else if (stringFixture.getUserData().equals("playerHitCollider")) {

                if (objectFixture.getUserData() instanceof UserData) {
                    UserData userData = (UserData) objectFixture.getUserData();

                    /** Enemies */
                    if (userData.type.equals("enemyCollider") && screen.player.attacking) {
                        // Destroy
                        Actor actor = (Actor) userData.obj;
                        actor.damageActor();
                    }

                    /** Tutorial Enemy */
                    if (userData.type.equals("tutorialCollider") && screen.player.attacking) {
                        // Destroy
                        Actor actor = (Actor) userData.obj;
                        actor.damageActor();
                    }

                    /** Secrets */
                    if (userData.type.equals("secret")) {
                        // Find and record secret
                        screen.findSecret(userData.trigger);

                        // Destroy
                        Actor actor = (Actor) userData.obj;
                        actor.damageActor();
                    }
                }
            }

            /**
             * Player Hurt
             */
            else if (stringFixture.getUserData().equals("playerHurtCollider")) {
                if (objectFixture.getUserData() instanceof UserData) {
                    UserData userData = (UserData) objectFixture.getUserData();

                    /** Enemies */
                        // Main collider
                    if (userData.type.equals("enemyCollider")
                            && screen.player.health > 0
                            && !screen.player.invincible
                            && !screen.player.attacking) {
                        if (debugging) { Gdx.app.log(tag, "TAKING DAMAGE"); }

                        // Check if player is to the right or left of enemy
                        if (stringFixture.getBody().getPosition().x <= objectFixture.getBody().getPosition().x) {
                            screen.player.enemyRight = true;
                        } else {
                            screen.player.enemyRight = false;
                        }
                        screen.player.triggerDamage(-1);

                        // Shake camera
                        screen.camera.shakeCamera(1.0f, GameCamera.CAMERA_SHAKE.LIGHT);
                    }


                    /** Tutorial Enemy */
                    if (userData.type.equals("tutorialCollider")
                            && screen.player.health > 0
                            && !screen.player.invincible
                            && !screen.player.attacking) {
                        if (debugging) { Gdx.app.log(tag, "TUTORIAL DAMAGE"); }

                        // Check if player is to the right or left of enemy
                        if (stringFixture.getBody().getPosition().x <= objectFixture.getBody().getPosition().x) {
                            screen.player.enemyRight = true;
                        } else {
                            screen.player.enemyRight = false;
                        }
                        screen.player.triggerDamage(0);
//                        screen.player.tutorialHit = true;

                        // Shake camera
                        screen.camera.shakeCamera(1.0f, GameCamera.CAMERA_SHAKE.LIGHT);
                    }
                }
            }

            /**
             * Enemy Colliders
             */
            else if (objectFixture.getUserData() instanceof UserData) {
                UserData userDataObject = (UserData) objectFixture.getUserData();

                // Actors
                if (userDataObject.obj instanceof Actor) {
                    Actor actor =  (Actor) userDataObject.obj;

                    // Side senors
                    if (userDataObject.type.equals("enemySide")) {

                        // Ground
                        if (stringFixture.getUserData().equals("ground")) {
                            if (!actor.isDecending()) {
                                actor.changeDirection();
                            }
                        }
                    }
                }
            }
        }

//        /**
//         * When BOTH of the fixtures UserData are Strings
//         */
//
//        if ((fixA.getUserData() instanceof String) && (fixB.getUserData() instanceof String)) {
//
//        }



        /**
         * When BOTH of the fixtures UserData are UserData.class
         */
        if ((fixA.getUserData() instanceof UserData) && (fixB.getUserData() instanceof UserData)) {
            UserData userA = (UserData) fixA.getUserData();
            UserData userB = (UserData) fixB.getUserData();

            if ((userA.type.equals("enemyCollider")) || (userB.type.equals("enemyCollider"))) {
                UserData enemyUserData;
                UserData otherUserData;

                if (userA.type.equals("enemyCollider")) {
                    enemyUserData = userA;
                    otherUserData = userB;
                } else {
                    enemyUserData = userB;
                    otherUserData = userA;
                }

                /** Enemy and trigger */
                if (otherUserData.type.equals("trigger")) {
                    int t = otherUserData.trigger;

                    /**
                     * Enemy hit a Trigger
                     */
                    if (t == -100) {
                        // Destroy
                        Actor actor = (Actor) enemyUserData.obj;
                        actor.damageActor();
                    }
                }

                /** Enemy and enemy */
                if (otherUserData.type.equals("enemySide")) {
                    Actor actorA = (Actor) enemyUserData.obj;
                    Actor actorB = (Actor) otherUserData.obj;

                    // Change direction depending on position
                    if (actorA.getActorBody().getPosition().x <= actorB.getActorBody().getPosition().x) {
                        if (actorA.getRunningRight()) { actorA.changeDirection(); }
                        if (!actorB.getRunningRight()) { actorB.changeDirection(); }
//                        Gdx.app.log(tag, "changing enemy direction "
//                                + actorA.getRunningRight() + ", " + actorB.getRunningRight());
                    } else {
                        if (!actorA.getRunningRight()) { actorA.changeDirection(); }
                        if (actorB.getRunningRight()) { actorB.changeDirection(); }
//                        Gdx.app.log(tag, "changing enemy direction "
//                                + actorA.getRunningRight() + ", " + actorB.getRunningRight());
                    }
                }
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if ((fixA.getUserData() instanceof String) && (fixB.getUserData() instanceof String)) {

            /** GeneralPhysicsBody - Affected and collides with one way platforms */
            if ((fixA.getUserData().equals("generalPhysicsBody")) || (fixB.getUserData().equals("generalPhysicsBody"))) {
                Fixture actorFix;
                Fixture otherFix;

                // Work out which is the actorsPhysicsBody
                if (fixA.getUserData().equals("generalPhysicsBody")) {
                    actorFix = fixA;
                    otherFix = fixB;
                } else {
                    otherFix = fixA;
                    actorFix = fixB;
                }
                Vector2 vec = actorFix.getBody().getLinearVelocity();

                /** One way platforms */
                    // Left only
                if (vec.x <= 0
                        && otherFix.getUserData().equals("groundLEFT")) {
                    contact.setEnabled(false);
                }
                    // Right only
                if (vec.x >= 0
                        && otherFix.getUserData().equals("groundRIGHT")) {
                    contact.setEnabled(false);
                }
            }

            /** SpecialPhysicsBody - Does not collide with one way platforms */
            if ((fixA.getUserData().equals("specialPhysicsBody")) || (fixB.getUserData().equals("specialPhysicsBody"))) {
                Fixture actorFix;
                Fixture otherFix;

                // Work out which is the actorsPhysicsBody
                if (fixA.getUserData().equals("specialPhysicsBody")) {
                    actorFix = fixA;
                    otherFix = fixB;
                } else {
                    otherFix = fixA;
                    actorFix = fixB;
                }

                if ((otherFix.getUserData().equals("groundLEFT"))
                        || (otherFix.getUserData().equals("groundRIGHT"))) {
                    contact.setEnabled(false);
                }
            }
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
