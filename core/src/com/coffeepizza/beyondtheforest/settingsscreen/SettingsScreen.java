package com.coffeepizza.beyondtheforest.settingsscreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.coffeepizza.beyondtheforest.BeyondManager;
import com.coffeepizza.beyondtheforest.mainMenu.MenuButton;
import com.coffeepizza.beyondtheforest.util.OSTManager;

public class SettingsScreen implements Screen {

    // System
    private boolean debugging = false;
    private String tag = "Settings";
    private String message = "";

    // Game Manager
    private BeyondManager manager;
    private SpriteBatch spriteBatch;
    private ShapeRenderer shapeRenderer;
    private OSTManager ostManager;
    private Preferences prefs;

    // Assets
    private TextureAtlas textureAtlas;

    // Controller
        // Layouts
    private Stage stage;
    private Table mainTable, togglesTable, previewsTable;
        // Dimensions
    public float viewWidth, viewHeight,
                toggleTableHeight, previewTableHeight,
                buttonHeight, rowPadding, edgePadding, previewHeight;
        // Input
    private InputMultiplexer multiplexer;

    // Actors
        // Drawables
    private TextureRegionDrawable directionsDrawableCD, directionsDrawableLR,// HUDDrawableMin, HUDDrawableMax,
            drawableTrue, drawableFalse;
    private TextureRegionDrawable
            previewControlsTopCD, previewControlsTopLR,
            previewControlsBottomCD, previewControlsBottomLR,
            previewHUDTopCD, previewHUDTopLR,
            previewHUDBottomCD, previewHUDBottomLR,
            previewNoHUD, previewHUDAnimation;
    private TextureRegionDrawable backButtonDrawable;
        // Toggles
    private ImageButton.ImageButtonStyle directionsStyle, HUDStyle, YAxisStyle, animateStyle, backStyle;
    private ImageButton YAxisToggle, directionToggle, //HUDToggle,
            animateButtonsToggle, backButton;
        // Images
    private Image controlsPreview, HUDPreview;
        // Labels
    private Label directionsLabel, //HUDLabel,
                YAxisLabel, animateLabel;

    public SettingsScreen(BeyondManager parent) {
        this.manager = parent;
        this.spriteBatch = parent.spriteBatch;
        this.shapeRenderer = parent.shapeRenderer;
        this.ostManager = parent.ostManager;
        textureAtlas = parent.getAtlas();
        prefs = parent.prefs;
    }

    @Override
    public void show() {
        // Create new table in stage
        createStage();
        getPortDimensions();

        // Tables and Actors
        initTableActors();
        createTable();
        mobileTableSetup();
        updateButtonStates();
        updatePreview();
            // Input
        setInputProcessor();
    }

    // Init
    private void getPortDimensions() {
        /** Screen */
        viewWidth = stage.getViewport().getWorldWidth();
        viewHeight = stage.getViewport().getWorldHeight();

        toggleTableHeight = viewHeight * 0.45f;
        previewTableHeight = viewHeight - toggleTableHeight;
        edgePadding = viewHeight * 0.05f;

        buttonHeight = viewHeight / 14;
        rowPadding = buttonHeight / 6;

        previewHeight = viewHeight * 0.4f;
    }
    private void createStage() {
        if (debugging) { manager.sendDebugMsg(debugging, tag,"Creating setttings menu new stage."); }

        // Stage for buttons
        stage = new Stage(new ScreenViewport());
        stage.clear();
    }
    private void initTableActors() {
        if (debugging) { manager.sendDebugMsg(debugging, tag,"Initiating settings menu table actors."); }

        // Load buttons
        loadAssets();
        creatButtonsAndImages();
        configureButtonListeners();
    }
    private void loadAssets() {
        // Drawables
            // Toggles
        drawableTrue = new TextureRegionDrawable(textureAtlas.findRegion("setting_menu_trueFalse_tOn"));
        drawableFalse = new TextureRegionDrawable(textureAtlas.findRegion("setting_menu_trueFalse_fOn"));
        directionsDrawableCD = new TextureRegionDrawable(textureAtlas.findRegion("setting_menu_cdlr_cdOn"));
        directionsDrawableLR = new TextureRegionDrawable(textureAtlas.findRegion("setting_menu_cdlr_lrOn"));
        //HUDDrawableMin = new TextureRegionDrawable(textureAtlas.findRegion("setting_menu_minmax_minOn"));
        //HUDDrawableMax = new TextureRegionDrawable(textureAtlas.findRegion("setting_menu_minmax_maxOn"));
            // Previews
        previewControlsTopCD = new TextureRegionDrawable(textureAtlas.findRegion("settings_preview_controls_cd_top"));
        previewControlsTopLR = new TextureRegionDrawable(textureAtlas.findRegion("settings_preview_controls_lr_top"));
        previewControlsBottomCD = new TextureRegionDrawable(textureAtlas.findRegion("settings_preview_controls_cd_bottom"));
        previewControlsBottomLR = new TextureRegionDrawable(textureAtlas.findRegion("settings_preview_controls_lr_bottom"));
        previewHUDTopCD = new TextureRegionDrawable(textureAtlas.findRegion("settings_preview_hud_cd_top"));
        previewHUDTopLR = new TextureRegionDrawable(textureAtlas.findRegion("settings_preview_hud_lr_top"));
        previewHUDBottomCD = new TextureRegionDrawable(textureAtlas.findRegion("settings_preview_hud_cd_bottom"));
        previewHUDBottomLR = new TextureRegionDrawable(textureAtlas.findRegion("settings_preview_hud_lr_bottom"));
        previewNoHUD = new TextureRegionDrawable(textureAtlas.findRegion("settings_preview_hud_min"));
        previewHUDAnimation = new TextureRegionDrawable(textureAtlas.findRegion("settings_preview_hud_animation"));
            // Menu Buttons
        backButtonDrawable = new TextureRegionDrawable(textureAtlas.findRegion("text_back"));

        // Style
        YAxisStyle = new ImageButton.ImageButtonStyle(drawableFalse, null, drawableTrue,
                null, null, null);
        directionsStyle = new ImageButton.ImageButtonStyle(directionsDrawableCD, null, directionsDrawableLR,
                null, null, null);
        //HUDStyle = new ImageButton.ImageButtonStyle(HUDDrawableMin, null, HUDDrawableMax,
                //null, null, null);
        animateStyle = new ImageButton.ImageButtonStyle(drawableFalse, null, drawableTrue,
                null, null, null);
        backStyle = new ImageButton.ImageButtonStyle(backButtonDrawable, null, null,
                null, null, null);

        // Lables
        YAxisLabel = new Label("FLIP CONTROLS", new Label.LabelStyle(manager.goldFont, Color.GREEN));
        directionsLabel = new Label("DIRECTION BUTTONS", new Label.LabelStyle(manager.goldFont, Color.GREEN));
        //HUDLabel = new Label("HUD LEVEL", new Label.LabelStyle(manager.goldFont, Color.GREEN));
        animateLabel = new Label("BUTTON ANIMATIONS", new Label.LabelStyle(manager.goldFont, Color.GREEN));

        YAxisLabel.setFontScale(0.75f);
        YAxisLabel.setAlignment(Align.center);
        directionsLabel.setFontScale(0.75f);
        directionsLabel.setAlignment(Align.center);
        //HUDLabel.setFontScale(0.75f);
        //HUDLabel.setAlignment(Align.center);
        animateLabel.setFontScale(0.75f);
        animateLabel.setAlignment(Align.center);
    }
    private void creatButtonsAndImages() {
        // Toggles
        YAxisToggle = new ImageButton(YAxisStyle);
        directionToggle = new ImageButton(directionsStyle);
        //HUDToggle = new ImageButton(HUDStyle);
        animateButtonsToggle = new ImageButton(animateStyle);

        // Images
        controlsPreview = new Image(previewControlsTopCD);
        HUDPreview = new Image(previewHUDBottomCD);

        // Menu buttons
        backButton = new ImageButton(backStyle);
    }
    private void configureButtonListeners() {
        manager.sendDebugMsg(debugging, tag,"Configuring buttons.");

        YAxisToggle.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                if (YAxisToggle.isChecked()) {
                    // Toggle to True (Direction buttons on the bottom)
                    manager.sendDebugMsg(debugging, tag,"Toggle to True (Direction buttons on the bottom)");
                    yAxisToggleToDirectionOnBottom();
                } else {
                    // Toggle to False (Direction buttons on the top)
                    manager.sendDebugMsg(debugging, tag,"Toggle to False (Direction buttons on the top)");
                    yAxisToggleToDirectionOnTop();
                }
            }
        });

        directionToggle.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                if (directionToggle.isChecked()) {
                    // Toggle to Left/Right
                    manager.sendDebugMsg(debugging, tag,"Toggle to Left/Right");
                    directionsToggleToLeftRight();
                } else {
                    // Toggle to ChangeDir
                    manager.sendDebugMsg(debugging, tag,"Toggle to ChangeDir");
                    directionsToggleToChangeDirection();
                }
            }
        });

        /*
        HUDToggle.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                if (HUDToggle.isChecked()) {
                    // Toggle to Max
                    manager.sendDebugMsg(debugging, tag,"Toggle to Max");
                    HUDToggleToMax();
                } else {
                    // Toggle to Min
                    manager.sendDebugMsg(debugging, tag,"Toggle to Min");
                    HUDToggleToMin();
                }
            }
        });
        */

        animateButtonsToggle.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                if (animateButtonsToggle.isChecked()) {
                    // Toggle to True (Direction buttons on the bottom)
                    manager.sendDebugMsg(debugging, tag,"Toggle to True (animate buttons)");
                    animateButtonsEnable();
                } else {
                    // Toggle to False (Direction buttons on the top)
                    manager.sendDebugMsg(debugging, tag,"Toggle to False (dont animate buttons)");
                    animateButtonsDisable();
                }
            }
        });

        backButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                manager.sendDebugMsg(debugging, tag,"Hit back button");
                manager.loadNewScreen(BeyondManager.MENU);
            }
        });
    }
    private void createTable() {
        manager.sendDebugMsg(debugging, tag,"Creating settings menu table.");

        // Main table
        mainTable = new Table();
        mainTable.setFillParent(true);

        // Inner tables
        togglesTable = new Table();
        previewsTable = new Table();

        if (debugging) {
            mainTable.setDebug(true);
            togglesTable.setDebug(true);
            previewsTable.setDebug(true);
        }
    }
    private void mobileTableSetup() {
        /** Toggles Table */
        togglesTable.top();
        togglesTable.padTop(edgePadding);
        togglesTable.add(YAxisLabel);
        togglesTable.add().width(rowPadding * 2);
        togglesTable.add(YAxisToggle).size(
                (drawableTrue.getRegion().getRegionWidth() * buttonHeight) / drawableTrue.getRegion().getRegionHeight(),
                buttonHeight);
        togglesTable.row();

        togglesTable.add().size(10, rowPadding);
        togglesTable.row();

        togglesTable.add(directionsLabel);
        togglesTable.add().width(rowPadding * 2);
        togglesTable.add(directionToggle).size(
                (directionsDrawableCD.getRegion().getRegionWidth() * buttonHeight) / drawableTrue.getRegion().getRegionHeight(),
                buttonHeight);
        togglesTable.row();

        togglesTable.add().size(10, rowPadding);
        togglesTable.row();

        //togglesTable.add(HUDLabel);
        //togglesTable.add().width(rowPadding * 2);
        //togglesTable.add(HUDToggle).size(
                //(HUDDrawableMin.getRegion().getRegionWidth() * buttonHeight) / drawableTrue.getRegion().getRegionHeight(),
                //buttonHeight);
        //togglesTable.row();

        togglesTable.add().size(10, rowPadding);
        togglesTable.row();

        togglesTable.add(animateLabel);
        togglesTable.add().width(rowPadding * 2);
        togglesTable.add(animateButtonsToggle).size(
                (drawableTrue.getRegion().getRegionWidth() * buttonHeight) / drawableTrue.getRegion().getRegionHeight(),
                buttonHeight);

        /** Preview Table */
        previewsTable.top();
        previewsTable.add(controlsPreview).size(
                (previewControlsTopCD.getRegion().getRegionWidth() * previewHeight) / previewControlsTopCD.getRegion().getRegionHeight(),
                previewHeight);
        previewsTable.add().width(rowPadding);
        previewsTable.add(HUDPreview).size(
                (previewHUDBottomCD.getRegion().getRegionWidth() * previewHeight) / previewHUDBottomCD.getRegion().getRegionHeight(),
                previewHeight);

        previewsTable.row();
        previewsTable.add().expandY();

        previewsTable.row();
        previewsTable.add(backButton)
                .size((backButtonDrawable.getRegion().getRegionWidth() * buttonHeight) / backButtonDrawable.getRegion().getRegionHeight(),
                        buttonHeight)
                .colspan(3)
                .padBottom(edgePadding);

        /** Main Table */
        mainTable.add(togglesTable).expandX().height(toggleTableHeight);
        mainTable.row();
        mainTable.add(previewsTable).expandX().height(previewTableHeight);

        /** Stage */
        stage.addActor(mainTable);
    }
    private void updateButtonStates() {
        /** Read saved configurations and update buttons to reflect */
        // Set states
        YAxisToggle.setChecked(prefs.getBoolean("controlsYFlipped"));

        Gdx.app.log(tag, "lr instead of cd: " + prefs.getBoolean("controlsLRInsteadOfCD"));
        directionToggle.setChecked(prefs.getBoolean("controlsLRInsteadOfCD"));

        //Gdx.app.log(tag, "show controls: " + prefs.getBoolean("HUDVirtualControls"));
        //HUDToggle.setChecked(prefs.getBoolean("HUDVirtualControls"));

        animateButtonsToggle.setChecked(prefs.getBoolean("HUDAnimateControls"));
    }
    private void updatePreview() {
        HUDPreview.setDrawable(previewNoHUD);

        if (!prefs.getBoolean("controlsYFlipped")) {
            // Not flipped
            if (prefs.getBoolean("controlsLRInsteadOfCD")) {
                // Left / Right
                controlsPreview.setDrawable(previewControlsTopLR);

                if (prefs.getBoolean("HUDVirtualControls")) { HUDPreview.setDrawable(previewHUDTopLR); }
            } else {
                // Change direction
                controlsPreview.setDrawable(previewControlsTopCD);

                if (prefs.getBoolean("HUDVirtualControls")) { HUDPreview.setDrawable(previewHUDTopCD); }
            }
        } else {
            // Flipped
            if (prefs.getBoolean("controlsLRInsteadOfCD")) {
                // Left / Right
                controlsPreview.setDrawable(previewControlsBottomLR);

                if (prefs.getBoolean("HUDVirtualControls")) { HUDPreview.setDrawable(previewHUDBottomLR); }
            } else {
                // Change direction
                controlsPreview.setDrawable(previewControlsBottomCD);

                if (prefs.getBoolean("HUDVirtualControls")) { HUDPreview.setDrawable(previewHUDBottomCD); }
            }
        }
    }
    private void setInputProcessor() {
        manager.sendDebugMsg(debugging, tag,"Setting stage as the menus input processor.");

        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);

        Gdx.input.setInputProcessor(multiplexer);
    }

    /** Button Logic */
    private void yAxisToggleToDirectionOnTop() {
        prefs.putBoolean("controlsYFlipped", false);
        prefs.flush();

        updatePreview();
    }
    private void yAxisToggleToDirectionOnBottom() {
        prefs.putBoolean("controlsYFlipped", true);
        prefs.flush();

        updatePreview();
    }
    private void directionsToggleToChangeDirection() {
        prefs.putBoolean("controlsLRInsteadOfCD", false);
        prefs.flush();

        updatePreview();
    }
    private void directionsToggleToLeftRight() {
        prefs.putBoolean("controlsLRInsteadOfCD", true);
        prefs.flush();

        updatePreview();
    }
    private void HUDToggleToMin() {
        prefs.putBoolean("HUDVirtualControls", false);
        prefs.flush();

        updatePreview();
    }
    private void HUDToggleToMax() {
        prefs.putBoolean("HUDVirtualControls", true);
        prefs.flush();

        updatePreview();
    }
    private void animateButtonsEnable() {
        prefs.putBoolean("HUDAnimateControls", true);
        prefs.flush();

        updatePreview();
    }
    private void animateButtonsDisable() {
        prefs.putBoolean("HUDAnimateControls", false);
        prefs.flush();

        updatePreview();
    }

    private void update(float dt) {
        stage.act(Math.min(dt, 1 / 60f));

        // Music
        ostManager.update(dt);
    }

    @Override
    public void render(float delta) {
        // Update
        update(delta);

        // Clear screen
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Stage
        stage.draw();
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
