package com.coffeepizza.beyondtheforest.sprite.levelscreen.bosses.AIDirectors.stateMachines;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.coffeepizza.beyondtheforest.sprite.levelscreen.bosses.AIDirectors.Sdyne;

public enum SdyneStateMachine implements State<Sdyne> {

    WARMING_UP() {},
    OPENING_MOVE() {
        @Override
        public void enter(Sdyne entity) {
            entity.createShelobs();
            entity.topRightCutsceneEntery();
        }

        @Override
        public void update(Sdyne entity) {
            if (entity.shelobAttackerRight.gotOnsite()) {
                //entity.stateMachine.changeState(CUTSCENE_ATTACK);
                entity.stateMachine.changeState(CUTSCENE_DIALOGUE);
            }
        }
    },
    CUTSCENE_DIALOGUE() {
        @Override
        public void enter(Sdyne entity) {
            entity.actionCyberdyneTrigger(1);
        }
    },
    CUTSCENE_ATTACK() {
        @Override
        public void enter(Sdyne entity) {
            entity.setTimer(4f, 1);
        }
    },
    CUTSCENE_EGGLAUNCH() {},
    ATTACK() {
        @Override
        public void update(Sdyne entity) {
            if (entity.everyoneOffsite()) {
                entity.initCharge();
                entity.stateMachine.changeState(CHARGE);
            }
        }
    },
    CHARGE() {
        @Override
        public void update(Sdyne entity) {
            if (entity.everyoneOffsite()) {
                entity.initAttack();
                entity.stateMachine.changeState(ATTACK);
            }
        }
    },
    DEAD() {}
    ;

    @Override
    public void enter(Sdyne entity) { }

    @Override
    public void update(Sdyne entity) { }

    @Override
    public void exit(Sdyne entity) { }

    @Override
    public boolean onMessage(Sdyne entity, Telegram telegram) { return false; }
}