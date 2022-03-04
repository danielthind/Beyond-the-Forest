package com.coffeepizza.beyondtheforest.sprite.levelscreen.enemies.stateMachines;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.coffeepizza.beyondtheforest.sprite.levelscreen.enemies.Maggot;

public enum MaggotState implements State<Maggot> {

    CRAWLING() {
        @Override
        public void update(Maggot entity) {
            entity.crawlPhysics();

            if (!entity.isGroundedNormal()) {
                entity.stateMachine.changeState(FALLING);
            }
        }
    },
    FALLING() {
        @Override
        public void update(Maggot entity) {
            entity.crawlPhysics();

            if (entity.isGroundedNormal()) {
                entity.stateMachine.changeState(CRAWLING);
            }
        }
    },
    DYING() {
        @Override
        public void enter(Maggot entity) {
            entity.halfVelocities();
        }
    }
    ;

    @Override
    public void enter(Maggot entity) {

    }

    @Override
    public void update(Maggot entity) {

    }

    @Override
    public void exit(Maggot entity) {

    }

    @Override
    public boolean onMessage(Maggot entity, Telegram telegram) {
        return false;
    }
}
