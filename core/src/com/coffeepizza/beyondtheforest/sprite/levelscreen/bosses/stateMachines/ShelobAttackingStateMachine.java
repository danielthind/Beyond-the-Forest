package com.coffeepizza.beyondtheforest.sprite.levelscreen.bosses.stateMachines;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.coffeepizza.beyondtheforest.sprite.levelscreen.bosses.Shelob;

public enum ShelobAttackingStateMachine implements State<Shelob> {


    /** Cutscene states */
    CUTSCENE_GOING_ONSITE() {
        @Override
        public void update(Shelob entity) {
            if (entity.gotOnsite()) {
                entity.halfXVel();
                entity.stateMachine.changeState(CUTSCENE_PREATTACKING_STANCE);

            } else {
                entity.goingOnsitePhysics();
            }
        }
    },
    CUTSCENE_PREATTACKING_STANCE() {
        @Override
        public void enter(Shelob entity) {
            entity.animationTimer = 0;
            entity.zeroXVel();
        }
    },
    //CUTSCENE_ATTACKING_STANCE() {},

    /** Main states */
    GOING_ONSITE_TO_ATTACK() {
        @Override
        public void enter(Shelob entity) {
            entity.animationTimer = 0;
            entity.activateCollider();
        }

        @Override
        public void update(Shelob entity) {
            if (entity.gotOnsite()) {
                entity.stateMachine.changeState(FLAME_ATTACK);
            } else {
                entity.goingOnsitePhysics();
            }
        }
    },
    FLAME_ATTACK() {
        @Override
        public void enter(Shelob entity) {
            entity.zeroXVel();
            // Reset animation timer for animation complete check
            entity.initAttack = false;
            entity.animationTimer = 0;
        }
        @Override
        public void update(Shelob entity) {
            if (entity.initAttack()) {
                entity.flameOn();
            }
            if (entity.flameAttackAnimationComplete()) {

                entity.stateMachine.changeState(ATTACKING_IDLE);
            }
        }
    },
    ATTACKING_IDLE() {
        @Override
        public void enter(Shelob entity) {
            entity.setLockout(entity.attackTime);
        }

        @Override
        public void update(Shelob entity) {
            if (entity.freeFromLockout()) {
                entity.stateMachine.changeState(FLAME_ATTACK_TWO);
            } else {
                entity.attacking();
            }
        }
    },
    FLAME_ATTACK_TWO() {
        @Override
        public void enter(Shelob entity) {
            entity.zeroXVel();
            // Reset animation timer for animation complete check
            entity.initAttack = false;
            entity.animationTimer = 0;
        }
        @Override
        public void update(Shelob entity) {
            if (entity.initAttack()) {
                entity.flameOn();
            }
            if (entity.flameAttackAnimationComplete()) {
                entity.stateMachine.changeState(ATTACKING_IDLE_TWO);
            }
        }
    },
    ATTACKING_IDLE_TWO() {
        @Override
        public void enter(Shelob entity) {
            entity.setLockout(entity.attackTime);
        }

        @Override
        public void update(Shelob entity) {
            if (entity.freeFromLockout()) {
                entity.stateMachine.changeState(GOING_OFFSITE);
            } else {
                entity.attacking();
            }
        }
    },

    GOING_OFFSITE() {
        @Override
        public void update(Shelob entity) {
            if (entity.gotOffsite()) {
                entity.zeroXVel();
                entity.stateMachine.changeState(OFFSITE);

            } else {
                entity.goingOffsitePhysics();
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
