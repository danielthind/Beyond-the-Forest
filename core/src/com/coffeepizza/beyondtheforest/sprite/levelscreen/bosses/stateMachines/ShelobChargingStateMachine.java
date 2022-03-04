package com.coffeepizza.beyondtheforest.sprite.levelscreen.bosses.stateMachines;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.coffeepizza.beyondtheforest.sprite.levelscreen.bosses.Shelob;

public enum ShelobChargingStateMachine implements State<Shelob> {

    /** Main states */
    CHARGING_ONSITE() {
        @Override
        public void enter(Shelob entity) {
            entity.activateCollider();
        }

        @Override
        public void update(Shelob entity) {
            if (entity.gotOnsite()) {
                entity.halfXVel();
                entity.stateMachine.changeState(GOING_OFFSITE);
            } else {
                entity.chargingOnsitePhysics();
            }
        }
    },
    GOING_OFFSITE() {
        @Override
        public void update(Shelob entity) {
            if (entity.gotOffsite()) {
                entity.halfXVel();
                entity.stateMachine.changeState(OFFSITE);

            } else {
                entity.chargingOffsitePhysics();
            }
        }

    },
    OFFSITE() {
        @Override
        public void enter(Shelob entity) {
            entity.zeroXVel();
            entity.setLockout(entity.pauseBetweenPhases);
        }
    },






    DAMAGE_KNOCKBACK() {
        @Override
        public void enter(Shelob entity) {
            entity.setLockout(entity.damageKnockbackTimer);
            entity.damaged();
        }

        @Override
        public void update(Shelob entity) {
            if (entity.freeFromLockout()) {
                entity.stateMachine.changeState(DAMAGE_RETREAT);
            }
        }
    },
    DAMAGE_RETREAT(){
        @Override
        public void update(Shelob entity) {
            if (entity.gotOffsite()) {
                entity.halfXVel();
                entity.stateMachine.changeState(OFFSITE);

            } else {
                entity.goingOffsitePhysics();
            }
        }
    },

    DEAD() {
        @Override
        public void enter(Shelob entity) {
            entity.animationTimer = 0;
            entity.zeroXVel();
        }
    }


    /*
    DAMAGE() {
        @Override
        public void enter(Shelob entity) {
            entity.setLockout(entity.damageTime);
            entity.damaged();
        }

        @Override
        public void update(Shelob entity) {
            if (entity.freeFromLockout()) {
                entity.halfVelocities();
                entity.stateMachine.changeState(RETREAT);
            }
        }
    },
    RETREAT() {
        @Override
        public void enter(Shelob entity) {
            entity.setLockout(entity.retreatPauseTime);
        }

        @Override
        public void update(Shelob entity) {
            if (entity.freeFromLockout()) {
                if (entity.gotOffsite()) {
                    entity.halfXVel();
                    entity.stateMachine.changeState(OFFSITE);

                } else {
                    entity.damaagedRetreatOffsite();
                }
            }
        }
    },
    */
    ;

    @Override
    public void enter(Shelob entity) {

    }

    @Override
    public void update(Shelob entity) {

    }

    @Override
    public void exit(Shelob entity) {

    }

    @Override
    public boolean onMessage(Shelob entity, Telegram telegram) { return false;
    }
}
