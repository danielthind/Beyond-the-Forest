package com.coffeepizza.beyondtheforest.sprite.levelscreen.enemies.stateMachines;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.coffeepizza.beyondtheforest.sprite.levelscreen.enemies.Ghost;

public enum GhostState implements State<Ghost> {

    ASCENDING() {
        @Override
        public void update(Ghost entity) {
            // Check for state change
            if (entity.isAboveTopWaypoint()) {
                entity.getStateMachine().changeState(DESCENDING);

                entity.halfVelocities();       // Stops bat from going above upper waypoint
            } else {
                // Update this state
                if (entity.timeToFlap()) {
                    entity.flap();
                    entity.setUpFlap();
                    entity.resetElapsedTimed();
                }
            }
        }
    },
    DESCENDING() {
        @Override
        public void update(Ghost entity) {
            // Check for state change
            if (entity.isBelowBottomWaypoint()) {
                entity.getStateMachine().changeState(ASCENDING);
                entity.flap();               // Stops bat from going below lowest waypoint
            } else {
                // Update this state
                if (entity.timeToFlap()) {
                    entity.flap();
                    entity.setDownFlap();
                    entity.resetElapsedTimed();
                }
            }
        }
    },
    DYING() {
        @Override
        public void enter(Ghost entity) {
            /*
            entity.halfVelocities();
            //entity.getActorBody().setLinearVelocity(new Vector2(0.0f, 100f));

             */
        }
    }
    ;

    @Override
    public void enter(Ghost entity) {

    }

    @Override
    public void update(Ghost entity) {

    }

    @Override
    public void exit(Ghost entity) {    }

    @Override
    public boolean onMessage(Ghost entity, Telegram telegram) {
        return false;
    }
}
