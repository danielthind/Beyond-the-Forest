package com.coffeepizza.beyondtheforest.sprite.levelscreen.enemies.stateMachines;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.coffeepizza.beyondtheforest.sprite.levelscreen.enemies.Zombie;

public enum ZombieState implements State<Zombie> {

    HOBBLING_LEFT() {
        @Override
        public void update(Zombie entity) {
            // Check for state change
            if (entity.isPastLeftWaypoint()) {
            /*
            entity.stateMachine.changeState(HOBBLING_RIGHT);
            entity.faceRight();
            entity.zeroVelocities();
            */

                entity.stateMachine.changeState(TURN_AROUND);
            } else {
                // Update this state
                //if (zombie.timeToHobble()) {
                entity.hobble();
                //zombie.resetElapsedTimed();
                //}
            }
        }
    },
    HOBBLING_RIGHT() {
        @Override
        public void update(Zombie entity) {
            // Check for state change
            if (entity.isPastRightWaypoint()) {
            /*
            entity.stateMachine.changeState(HOBBLING_LEFT);
            entity.faceLeft();
            entity.zeroVelocities();
             */
                entity.stateMachine.changeState(TURN_AROUND);
            } else {
                // Update this state
                //if (zombie.timeToHobble()) {
                entity.hobble();
                //zombie.resetElapsedTimed();
                //}
            }
        }
    },
    TURN_AROUND() {
        @Override
        public void enter(Zombie entity) {
            entity.turn();
        }

        @Override
        public void update(Zombie entity) {
            if (entity.freeFromLockout()) {
                if (entity.hobbleRight) {
                    entity.stateMachine.changeState(HOBBLING_RIGHT);
                } else {
                    entity.stateMachine.changeState(HOBBLING_LEFT);
                }
            }
        }
    },
    DYING() {
        @Override
        public void enter(Zombie entity) {
            entity.halfVelocities();
            //entity.getActorBody().setLinearVelocity(new Vector2(0.0f, 100f));
        }
    }
    ;

    @Override
    public void enter(Zombie entity) {

    }

    @Override
    public void update(Zombie entity) {

    }

    @Override
    public void exit(Zombie entity) {    }

    @Override
    public boolean onMessage(Zombie entity, Telegram telegram) {
        return false;
    }
}