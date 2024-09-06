package com.coffeepizza.beyondtheforest.overworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.coffeepizza.beyondtheforest.BeyondManager;

public class OWMenuHUD implements Disposable {

    // System
    private boolean debugging = false;
    private String tag = "OWMenuHUD";
    private String message = "";

    // Game and Handlers
    private BeyondManager manager;
    private OverworldScreen parentScreen;
    //private DesktopInputHandler desktopInputHandler;
    //private MobileInputHandler mobileInputHandler;

    // Stage
    public Stage stage;
    private Table menuTable, promptTable;
    public float viewWidth, viewHeight;

    // Main menu buttons
    private Label exitCancelButton;

    // Actors
    private ImageButton menuButton, exitButton, exitConfirmButton, lvlConfirm;
    private Label enterLevelPrompt;

    // Assets
    private TextureRegionDrawable menuUp;
    private TextureRegionDrawable menuDown;
    private TextureRegionDrawable menuChecked;

    // Padding
    private float menuButtonWidth;

    // Level holder
    private int levelHolder = -100;

    public OWMenuHUD(BeyondManager game, OverworldScreen screen) {
        this.manager = game;
        this.parentScreen = screen;
        stage = new Stage(new ScreenViewport());

        // Dimensions
        viewWidth = stage.getViewport().getWorldWidth();
        viewHeight = stage.getViewport().getWorldHeight();
        menuButtonWidth = viewWidth / 10;

        // Load assets
        loadDrawables();

        // Initiate
        initMainMenu();
    }

    private void loadDrawables() {
        // Menu Button
        menuUp = new TextureRegionDrawable(parentScreen.textureAtlas.findRegion("overworld_mm_menu_up"));
        menuDown = new TextureRegionDrawable(parentScreen.textureAtlas.findRegion("overworld_mm_menu_down"));
        menuChecked = new TextureRegionDrawable(parentScreen.textureAtlas.findRegion("overworld_mm_menu_checked"));

        // Prompt Menu
    }

    /** Menu Buttons Methods */
    public void initMainMenu() {
        //stage.clear();
        //menuTable.clear();

        // Create Buttons
        menuButton = new ImageButton(menuUp, menuDown, menuChecked);
        menuButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                Gdx.app.log(tag, "menuButtonLogic");
                menuButtonLogic();
            }
        });

        /*
        exitButton = new ImageButton(menuUp, menuDown, menuChecked);
        exitButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                Gdx.app.log(tag, "exitButtonLogic");
                exitButtonLogic();
            }
        });

        exitConfirmButton = new ImageButton(menuUp, menuDown, menuChecked);
        exitButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                Gdx.app.log(tag, "exitConfirmButtonLogic");
                exitConfirmButtonLogic();
            }
        });

        lvlConfirm = new ImageButton(menuUp, menuDown, menuChecked);
        lvlConfirm.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                Gdx.app.log(tag, "enterLevel");
                enterLevel();
            }
        });
        */

        //exitConfirmButton = new Label("Y", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        exitCancelButton = new Label("N", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        // Load just menu button
        loadJustMenu();
    }

    /** Load Buttons */
    private void loadJustMenu() {
        // Clear and create a table
        //clearTableFromStage();
        //stage.clear();
        clearMenu();
        newMenuTable();

        // Add button to table
        menuTable.add(menuButton).width(menuButtonWidth).height(menuButtonWidth);

        // Add to stage
        addTableToStage();
    }
    private void loadExit() {
        // Clear and create a table
        //clearTableFromStage();
        //stage.clear();
        clearMenu();
        newMenuTable();

        // Add button to table
        menuTable.add(menuButton).width(menuButtonWidth).height(menuButtonWidth);
        menuButton.setChecked(true);
        menuTable.row();
        menuTable.add(exitButton).width(menuButtonWidth).height(menuButtonWidth);

        // Add to stage
        addTableToStage();

    }
    private void loadExitConfirmation() {
        // Clear and create a table
        //clearTableFromStage();
        //stage.clear();
        clearMenu();
        newMenuTable();

        // Add button to table
        menuTable.add(menuButton).width(menuButtonWidth).height(menuButtonWidth);
        menuButton.setChecked(true);
        menuTable.row();
        menuTable.add(exitButton).width(menuButtonWidth).height(menuButtonWidth);
        exitButton.setChecked(true);
        menuTable.row();
        menuTable.add(exitConfirmButton).width(menuButtonWidth).height(menuButtonWidth);

        // Add to stage
        addTableToStage();
    }

    /** Menu */
    // Button Logic
    private void menuButtonLogic() {
        if (menuButton.isChecked()) {
            //loadExit();
            menuButton.setChecked(false);
            manager.setInGameSettingsScreenFromOW();
        } else {
            loadJustMenu();
        }
    }
    private void exitButtonLogic() {
        if (exitButton.isChecked()) {
            loadExitConfirmation();
        } else {
            loadExit();
        }
    }
    private void exitConfirmButtonLogic() {

    }

    // Table
    private void clearMenu() {
        if (menuTable != null) {
            menuTable.remove();
        }
    }
    private void newMenuTable() {
        menuTable = new Table();
        menuTable.setFillParent(true);
        menuTable.top().right();
        menuTable.setName("OverworldMenu");
    }
    private void addTableToStage() {
        // Debugging
        if (debugging) { menuTable.debug(); }

        // Add to stage
        //parentStage.addActor(table);
        stage.addActor(menuTable);
    }

    /** Prompt */
    // Button Logic

    // Table
    public void clearPrompt() {
        if (promptTable != null) {
            promptTable.remove();
        }
        levelHolder = -100;
    }
    /*
    public void createPrompt(int level) {
        // Clear and create a table
        clearPrompt();
        levelHolder = level;


        // Create new table
        promptTable = new Table();
        promptTable.setFillParent(true);
        promptTable.bottom().left();

        // Label
        Actor a = new Actor();
        enterLevelPrompt = new Label("Enter level " + level + "?", new Label.LabelStyle(manager.goldFont, Color.WHITE));
        /*
        lvlConfirm = new ImageButton(menuUp, menuDown, menuChecked);
        exitButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                enterLevel(levelHolder);
            }
        });
        */
        /*
        // Add Actors
        promptTable.add(a).width(menuButtonWidth * 2).height(menuButtonWidth);
        promptTable.add(enterLevelPrompt).width(menuButtonWidth * 5).height(menuButtonWidth);
        promptTable.add(lvlConfirm).width(menuButtonWidth).height(menuButtonWidth);

        // Add to stage
        addPromptToStage();
    }
    */

    private void addPromptToStage() {
        // Debugging
        if (debugging) { promptTable.debug(); }

        // Add to stage
        stage.addActor(promptTable);
    }
    private void enterLevel() {
        Gdx.app.log(tag, "Entering level: " + levelHolder);

        //parent.changeScreen(BeyondManager.PLAYSCREEN);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
