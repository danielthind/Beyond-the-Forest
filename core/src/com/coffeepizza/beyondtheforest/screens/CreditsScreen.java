package com.coffeepizza.beyondtheforest.screens;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.coffeepizza.beyondtheforest.BeyondManager;
import com.coffeepizza.beyondtheforest.util.OSTManager;

public class CreditsScreen implements Screen {

    // System
    private boolean debugging = true;
    private String tag = "CreditsScreen";
    private String message = "";

    // Manager
    private BeyondManager manager;

    public CreditsScreen(BeyondManager parent) {
        this.manager = parent;

        manager.loadNewLevel(17);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
