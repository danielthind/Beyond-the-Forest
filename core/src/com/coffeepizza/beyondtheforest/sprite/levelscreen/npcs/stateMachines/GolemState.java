package com.coffeepizza.beyondtheforest.sprite.levelscreen.npcs.stateMachines;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.coffeepizza.beyondtheforest.sprite.levelscreen.npcs.Golem;

public enum GolemState implements State<Golem> {

    HIDDEN() {
    },
    RISE() {
    },
    IDLE() {
    }
    ;

    @Override
    public void enter(Golem entity) {

    }

    @Override
    public void update(Golem entity) {

    }

    @Override
    public void exit(Golem entity) {    }

    @Override
    public boolean onMessage(Golem entity, Telegram telegram) {
        return false;
    }

}
