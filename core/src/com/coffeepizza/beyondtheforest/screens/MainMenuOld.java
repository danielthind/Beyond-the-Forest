package com.coffeepizza.beyondtheforest.screens;
import com.badlogic.gdx.Application;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.coffeepizza.beyondtheforest.BeyondManager;

public class MainMenuOld implements Screen {

    // System
    private boolean debugging = true;
    private String tag = "Menu";
    private String message = "";

    // Game Manager
    private BeyondManager parent;

    // Controller
    private Stage stage;

    // Actors
    private Table table;
    private Skin skin;
    private TextButton newGame, preferences, exit, debug;


    public MainMenuOld(BeyondManager parent) {
        parent.sendDebugMsg(debugging, tag,"Initiating menu.");
        this.parent = parent;

        // Setting up stage
        createStage();
    }

    @Override
    public void show() {
        // Create new table in stage
        createTable();
        initTableActors();
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            desktopTableSetup();
        } else if (Gdx.app.getType() == Application.ApplicationType.Android
                || Gdx.app.getType() == Application.ApplicationType.iOS) {
            mobileTableSetup();
        }

        // Input handling
        configureButtonListeners();
        setInputProcessor();
    }

    /** Stage */
    private void createStage() {
        parent.sendDebugMsg(debugging, tag,"Creating new stage.");

        stage = new Stage(new ScreenViewport());
        stage.clear();
    }

    /** Table */
    private void createTable() {
        parent.sendDebugMsg(debugging, tag,"Creating new table.");

        // Create new table that fills stage
        table = new Table();
        table.setFillParent(true);
        if (debugging) { table.setDebug(true); }

        // Add table to stage
        stage.addActor(table);
    }
    private void initTableActors() {
        parent.sendDebugMsg(debugging, tag,"Initiating table actors.");

        skin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));

        newGame = new TextButton("Resume Game", skin);
        preferences = new TextButton("Preferences", skin);
        exit = new TextButton("EXIT", skin);
        debug = new TextButton("Debug", skin);
    }
    private void desktopTableSetup() {
        parent.sendDebugMsg(debugging, tag,"Making table for desktop.");

        table.add(newGame).fillX().uniformX();
        table.row().pad(10,0,10,0);
        table.add(preferences).fillX().uniformX();
        table.row();
        table.add(debug).fillX().uniformX();
        table.row();
        table.add(exit).fillX().uniformX();
    }
    private void mobileTableSetup() {
        if (debugging) {
            message = "Making table for mobile.";
            Gdx.app.log(tag, message);
        }
        table.add(newGame).fillX().uniformX();
        table.row().pad(10,0,10,0);
        table.add(preferences).fillX().uniformX();
        table.row();
        table.add(debug).fillX().uniformX();
        table.row();
        table.add(exit).fillX().uniformX();

    }

    /** Input handling */
    private void configureButtonListeners() {
        parent.sendDebugMsg(debugging, tag,"Configuring buttons.");

        newGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //parent.changeScreen(GameManager.PLAY);
            }
        });

        preferences.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //parent.changeScreen(GameManager.PREFERENCES);
            }
        });

        debug.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //parent.changeScreen(GameManager.DEBUG);
            }
        });

        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
    }
    private void setInputProcessor() {
        parent.sendDebugMsg(debugging, tag,"Setting stage as the menus input processor.");

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        // Update
        update(delta);

        // Clear screen
        Gdx.gl.glClearColor(1f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw
        stage.draw();
    }

    private void update(float dt) {
        stage.act(Math.min(dt, 1 / 30f));
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
