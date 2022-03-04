package com.coffeepizza.beyondtheforest.sprite.levelscreen.enemies.stateMachines;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.coffeepizza.beyondtheforest.sprite.levelscreen.enemies.Spider;

public enum SpiderState implements State<Spider> {

    CLIMPING() {
        @Override
        public void update(Spider entity) {
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
        public void update(Spider entity) {
            // Check for state change
            if (entity.isBelowBottomWaypoint()) {
                entity.getStateMachine().changeState(CLIMPING);
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
        public void enter(Spider entity) {
            entity.halfVelocities();
            //entity.getActorBody().setLinearVelocity(new Vector2(0.0f, 100f));
        }
    }
    ;

    @Override
    public void enter(Spider entity) {

    }

    @Override
    public void update(Spider entity) {

    }

    @Override
    public void exit(Spider entity) {    }

    @Override
    public boolean onMessage(Spider entity, Telegram telegram) {
        return false;
    }
}