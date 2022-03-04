package com.coffeepizza.beyondtheforest.sprite.levelscreen.enemies.stateMachines;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.coffeepizza.beyondtheforest.sprite.levelscreen.enemies.MaggotEgg;

public enum MaggotEggState implements State<MaggotEgg> {

    FLYING {
        @Override
        public void update(MaggotEgg entity) {
            entity.landingPhysics();
        }
    },
    CRACKED {
        @Override
        public void enter(MaggotEgg entity) {
            entity.attacking = false;
            entity.damageActor();
        }
    }
    ;

    @Override
    public void enter(MaggotEgg entity) {

    }

    @Override
    public void update(MaggotEgg entity) {

    }

    @Override
    public void exit(MaggotEgg entity) {

    }

    @Override
    public boolean onMessage(MaggotEgg entity, Telegram telegram) {
        return false;
    }
}
