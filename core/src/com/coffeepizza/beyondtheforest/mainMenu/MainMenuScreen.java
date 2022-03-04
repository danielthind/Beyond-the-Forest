package com.coffeepizza.beyondtheforest.mainMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.coffeepizza.beyondtheforest.BeyondManager;
import com.coffeepizza.beyondtheforest.util.OSTManager;

import java.util.ArrayList;
import java.util.List;

public class MainMenuScreen implements Screen {

    // System
    private boolean debugging = false;
    private String tag = "Menu";
    private String message = "";

    // Manager
    private BeyondManager manager;
    private SpriteBatch spriteBatch;
    private ShapeRenderer shapeRenderer;
    private OSTManager ostManager;

    // Parameters
    public boolean showContinue;
    public boolean showPromptBox, showPromptText;

    // Controller
    private Stage menuStage;
    private Stage promptStage, promptTextStage;
    //private ConfirmationPrompt prompt;
    public float viewWidth, viewHeight;
    private InputMultiplexer multiplexer;

    // Dimensions
        // Mobile
    private float BTN_TBL_LEFT_PADDING, BTN_TBL_WIDTH;
    public float BTN_MAX_WIDTH, BTN_SIZE_RATIO, BTN_HEIGHT, BTN_BETWEEN_PADDING;
    public float BONE_SELECTOR_HEIGHT, BONE_SELECTOR_PADDING;
    private float TITLE_WIDTH, TITLE_SIZE_RATIO, TITLE_HEIGHT;
    private float COPYRIGHT_WIDTH, COPYRIGHT_SIZE_RATIO, COPYRIGHT_HEIGHT, COPYRIGHT_PADDING;
    private float PROMPT_BOX_WIDTH, PROMPT_BOX_HEIGHT;
    private float PROMPT_TEXT_VERTICAL_PADDING;

    // Actors
        // Menu Buttons
    private Table table, buttonTable, titleTable;
    private MenuButton continueBtn, newGameBtn, settingsBtn, exitBtn;
    private TextureRegionDrawable contineDrawable, newGameDrawable, settingsDrawable, exitDrawable, boneDrawable;
    private ImageButton.ImageButtonStyle contineStyle, newGameStyle, settingsStyle, exitStyle;
    private Image title, copyright;

        // Confirmation Prompt
    private Table promptBoxTable;
    private TextureRegionDrawable promptBoxDrawableP1, promptBoxDrawableP2;
    private MenuConfirmPrompt promptMenuPromptBox;
        // Confirmation Buttons
    private Table promptTextTable;
    private MenuButton yesBtn, noBtn;
    private TextureRegionDrawable yesDrawable, noDrawable;
    private ImageButton.ImageButtonStyle yesStyle, noStyle;
    private Image confirmation;

    // Buttons
    private List<MenuButton> menuButtonList = new ArrayList<MenuButton>();
    private List<MenuButton> confirmationButtonList = new ArrayList<MenuButton>();

    // Assets
    private TextureAtlas textureAtlas;

    public MainMenuScreen(BeyondManager parent) {
        this.manager = parent;
        this.spriteBatch = parent.spriteBatch;
        this.shapeRenderer = parent.shapeRenderer;
        this.ostManager = parent.ostManager;
        textureAtlas = parent.getAtlas();

        // Setting up stage
        disablePromptButtons();
        parent.sendDebugMsg(debugging, tag,"Created menu.");
    }

    @Override
    public void show() {
        // Create new table in stage
        createStage();

        // Tables and Actors
        initTableActors();
        getPortDimensions();        // Uses drawable dimensions so is run after drawables are initiated
        createTable();

        //if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            //desktopTableSetup();
        //} else if (Gdx.app.getType() == Application.ApplicationType.Android
        //        || Gdx.app.getType() == Application.ApplicationType.iOS) {
            mobileTableSetup();
        //}

        // Input handling
//        configureButtonListeners();
        setInputProcessor();

        // Buttons and Prompt - Ensure prompt box and buttons and off
        hidePromptBox();
        disablePromptButtons();
        enableMenuButtons();
    }


    /** Stage */
    private void createStage() {
        manager.sendDebugMsg(debugging, tag,"Creating main menu stage.");

        // Logo and Buttons
        menuStage = new Stage(new ScreenViewport());
        //menuStage.clear();

        // Prompt Box
        promptStage = new Stage(new ScreenViewport());
        //promptStage.clear();

        // Promt Text
        promptTextStage = new Stage(new ScreenViewport());
        //promptTextStage.clear();
    }
    private void getPortDimensions() {
        /** Screen */
        viewWidth = menuStage.getViewport().getWorldWidth();
        viewHeight = menuStage.getViewport().getWorldHeight();

        // Tables

        // Desktop

        /** Mobile */
            // Table
        BTN_TBL_LEFT_PADDING = viewWidth * 0.05f;
        BTN_TBL_WIDTH = viewWidth * 0.4F;
            // Menu buttons
        BTN_MAX_WIDTH = BTN_TBL_WIDTH * 0.65f;  // New game is the widest button;
        BTN_SIZE_RATIO = (BTN_MAX_WIDTH / newGameDrawable.getRegion().getRegionWidth());
        BTN_HEIGHT = BTN_SIZE_RATIO * newGameDrawable.getRegion().getRegionHeight();
        BTN_BETWEEN_PADDING = BTN_HEIGHT * 1.5F;
            // Button selector
        BONE_SELECTOR_HEIGHT = BTN_HEIGHT;
        BONE_SELECTOR_PADDING = BTN_HEIGHT * 0.5f;
            // Copyright
        COPYRIGHT_WIDTH = BTN_TBL_WIDTH;
        COPYRIGHT_SIZE_RATIO = COPYRIGHT_WIDTH / copyright.getWidth();
        COPYRIGHT_HEIGHT = COPYRIGHT_SIZE_RATIO * copyright.getHeight();
        COPYRIGHT_PADDING = BTN_HEIGHT * 0.75F;
            // Title
        TITLE_WIDTH = (viewWidth - BTN_TBL_WIDTH) * 0.9f;
        TITLE_SIZE_RATIO = TITLE_WIDTH / title.getWidth();
        TITLE_HEIGHT = TITLE_SIZE_RATIO * title.getHeight();
            // Prompt box
        PROMPT_BOX_HEIGHT = BTN_HEIGHT * 5.6f;//4f;
        PROMPT_BOX_WIDTH = PROMPT_BOX_HEIGHT * 2.8f;
            // Prompt text
        PROMPT_TEXT_VERTICAL_PADDING =
                (viewHeight - PROMPT_BOX_HEIGHT         // top of original promptbox (which was 140 x 50 pixels)
                + (PROMPT_BOX_HEIGHT / 7))              // new prompt box is 160 x 70 -
                        / 2f;
    }

    /** Tables */
    private void createTable() {
        manager.sendDebugMsg(debugging, tag,"Creating main menu new table.");

        /** Menu */
        // Create new table that fills stage
        table = new Table();
        table.setFillParent(true);
        buttonTable = new Table();
        //buttonTable.setFillParent(true);
        titleTable = new Table();
        //titleTable.setFillParent(true);

        if (debugging) {
            table.setDebug(true);
            buttonTable.setDebug(true);
            titleTable.setDebug(true);
        }

        // Add table to stage
        menuStage.addActor(table);
        menuStage.addActor(buttonTable);
        menuStage.addActor(titleTable);

        /** Prompt Box */
        promptBoxTable = new Table();
        promptBoxTable.setFillParent(true);

        if (debugging) {
            promptBoxTable.setDebug(true);
        }

        /** Prompt Text */
        promptTextTable = new Table();
        promptTextTable.setFillParent(true);

        if (debugging) {
            promptTextTable.setDebug(true);
        }
    }
    private void initTableActors() {
        manager.sendDebugMsg(debugging, tag,"Initiating main manu table actors.");

        // Load buttons
        loadDrawables();
        createAllButtons();
        configureButtonListeners();
    }
    private void mobileTableSetup() {
        manager.sendDebugMsg(debugging, tag,"Making table for desktop.");

        /** Menu buttons */
            // Button
        if (showContinue) {
            buttonTable.add().width(BONE_SELECTOR_HEIGHT).height(BONE_SELECTOR_HEIGHT);
            buttonTable.add().width(BONE_SELECTOR_PADDING).height(BONE_SELECTOR_PADDING);
            buttonTable.add(continueBtn).width(contineDrawable.getRegion().getRegionWidth() * BTN_SIZE_RATIO).height(BTN_HEIGHT);
            buttonTable.add().width(BONE_SELECTOR_PADDING).height(BONE_SELECTOR_PADDING);
            buttonTable.add().width(BONE_SELECTOR_HEIGHT).height(BONE_SELECTOR_HEIGHT);
            buttonTable.row();

            buttonTable.add().height(BTN_BETWEEN_PADDING).colspan(3);
            buttonTable.row();
        }
        buttonTable.add().width(BONE_SELECTOR_HEIGHT).height(BONE_SELECTOR_HEIGHT);
        buttonTable.add().width(BONE_SELECTOR_PADDING).height(BONE_SELECTOR_PADDING);
        buttonTable.add(newGameBtn).width(newGameDrawable.getRegion().getRegionWidth() * BTN_SIZE_RATIO).height(BTN_HEIGHT);
        buttonTable.add().width(BONE_SELECTOR_PADDING).height(BONE_SELECTOR_PADDING);
        buttonTable.add().width(BONE_SELECTOR_HEIGHT).height(BONE_SELECTOR_HEIGHT);
        buttonTable.row();

        buttonTable.add().height(BTN_BETWEEN_PADDING).colspan(3);
        buttonTable.row();

        buttonTable.add().width(BONE_SELECTOR_HEIGHT).height(BONE_SELECTOR_HEIGHT);
        buttonTable.add().width(BONE_SELECTOR_PADDING).height(BONE_SELECTOR_PADDING);
        buttonTable.add(settingsBtn).width(settingsDrawable.getRegion().getRegionWidth() * BTN_SIZE_RATIO).height(BTN_HEIGHT);
        buttonTable.add().width(BONE_SELECTOR_PADDING).height(BONE_SELECTOR_PADDING);
        buttonTable.add().width(BONE_SELECTOR_HEIGHT).height(BONE_SELECTOR_HEIGHT);
        buttonTable.row();

        buttonTable.add().height(BTN_BETWEEN_PADDING).colspan(3);
        buttonTable.row();

        buttonTable.add().width(BONE_SELECTOR_HEIGHT).height(BONE_SELECTOR_HEIGHT);
        buttonTable.add().width(BONE_SELECTOR_PADDING).height(BONE_SELECTOR_PADDING);
        buttonTable.add(exitBtn).width(exitDrawable.getRegion().getRegionWidth() * BTN_SIZE_RATIO).height(BTN_HEIGHT);
        buttonTable.add().width(BONE_SELECTOR_PADDING).height(BONE_SELECTOR_PADDING);
        buttonTable.add().width(BONE_SELECTOR_HEIGHT).height(BONE_SELECTOR_HEIGHT);
        buttonTable.row();

            // Title Table
        titleTable.add(title).width(TITLE_WIDTH).height(TITLE_HEIGHT);
                //.padBottom(TITLE_HEIGHT)
                //.padRight(BTN_TBL_LEFT_PADDING);
        ;

            // Add tables
        table.add(buttonTable).width(BTN_TBL_WIDTH).expandY()
                .padLeft(BTN_TBL_LEFT_PADDING);
        table.add(titleTable).expandX().expandY()
                .padRight(BTN_TBL_LEFT_PADDING);
        table.row();
        table.add(copyright)
                .width(COPYRIGHT_WIDTH)
                .colspan(2)
                .padBottom(COPYRIGHT_PADDING);

        /** Prompt Box */
        promptBoxTable.add(promptMenuPromptBox).width(PROMPT_BOX_WIDTH).height(PROMPT_BOX_HEIGHT).expandX().expandY();
        promptMenuPromptBox.setWidth(PROMPT_BOX_WIDTH);
        promptMenuPromptBox.setHeight(PROMPT_BOX_HEIGHT);

        promptStage.addActor(promptBoxTable);

        /** Prompt Text */
        promptTextTable.top();
        int cols = 3;

            // Confirmation image
        promptTextTable.add()
                .height(BTN_HEIGHT)// / 2)
                .colspan(cols)
                .padTop(PROMPT_TEXT_VERTICAL_PADDING);
        promptTextTable.row();

        promptTextTable.add(confirmation)
                .width(confirmation.getWidth() * BTN_SIZE_RATIO)
                .height(BTN_HEIGHT)
                .colspan(cols);
        promptTextTable.row();

        promptTextTable.add()
                .height(BTN_HEIGHT)
                .colspan(cols);
        promptTextTable.row();

            // Yes and No button
        promptTextTable.add(yesBtn)
                .width(yesDrawable.getRegion().getRegionWidth() * BTN_SIZE_RATIO)
                .height(BTN_HEIGHT);
        promptTextTable.add().height(BTN_HEIGHT).width(BTN_HEIGHT);
        promptTextTable.add(noBtn)
                .width(noDrawable.getRegion().getRegionWidth() * BTN_SIZE_RATIO)
                .height(BTN_HEIGHT);

        promptTextStage.addActor(promptTextTable);
    }
    private void desktopTableSetup() {
        manager.sendDebugMsg(debugging, tag,"Making table for mobile.");
    }

    /** Buttons and Dialogue */
        // Init
    private void loadDrawables() {
        // Drawables
        contineDrawable = new TextureRegionDrawable(textureAtlas.findRegion("main_menu_text_continue"));
        newGameDrawable = new TextureRegionDrawable(textureAtlas.findRegion("main_menu_text_new_game"));
        settingsDrawable = new TextureRegionDrawable(textureAtlas.findRegion("main_menu_text_settings"));
        exitDrawable = new TextureRegionDrawable(textureAtlas.findRegion("main_menu_text_exit"));
        boneDrawable = new TextureRegionDrawable(textureAtlas.findRegion("items_bone"));
        promptBoxDrawableP1 = new TextureRegionDrawable(textureAtlas.findRegion("main_menu_text_prompt_box_p1"));
        promptBoxDrawableP2 = new TextureRegionDrawable(textureAtlas.findRegion("main_menu_text_prompt_box_p2"));
        yesDrawable = new TextureRegionDrawable(textureAtlas.findRegion("main_menu_text_yes"));
        noDrawable = new TextureRegionDrawable(textureAtlas.findRegion("main_menu_text_no"));

        // Style
        contineStyle = new ImageButton.ImageButtonStyle(contineDrawable, null, null,
                null, null, null);
        newGameStyle = new ImageButton.ImageButtonStyle(newGameDrawable, null, null,
                null, null, null);
        settingsStyle = new ImageButton.ImageButtonStyle(settingsDrawable, null, null,
                null, null, null);
        exitStyle = new ImageButton.ImageButtonStyle(exitDrawable, null, null,
                null, null, null);
        yesStyle = new ImageButton.ImageButtonStyle(yesDrawable, null, null,
                null, null, null);
        noStyle = new ImageButton.ImageButtonStyle(noDrawable, null, null,
                null, null, null);

        // Image
        title = new Image(textureAtlas.findRegion("main_menu_title_with_castle"));
        copyright = new Image(textureAtlas.findRegion("main_menu_text_c_2020_coffee_pizza"));
        confirmation = new Image(textureAtlas.findRegion("main_menu_text_confirm"));
    }
    private void createAllButtons() {
        createMenuButtons();
        createConfirmationDialogue();
    }
    private void createMenuButtons() {
        // Create buttons
        continueBtn = new MenuButton(this, contineStyle, boneDrawable.getRegion());
        newGameBtn = new MenuButton(this, newGameStyle, boneDrawable.getRegion());
        settingsBtn = new MenuButton(this, settingsStyle, boneDrawable.getRegion());
        exitBtn = new MenuButton(this, exitStyle, boneDrawable.getRegion());

        // Add to list
        menuButtonList.add(continueBtn);
        menuButtonList.add(newGameBtn);
        menuButtonList.add(settingsBtn);
        menuButtonList.add(exitBtn);
    }
    private void createConfirmationDialogue() {
        // Create buttons
        yesBtn = new MenuButton(this, yesStyle);
        noBtn = new MenuButton(this, noStyle);

        // Add to list
        confirmationButtonList.add(yesBtn);
        confirmationButtonList.add(noBtn);

        // Create prompt
        promptMenuPromptBox = new MenuConfirmPrompt(this, promptBoxDrawableP1.getRegion(), promptBoxDrawableP2.getRegion());
    }
        // Modifiers
    private void disableMenuButtons() {
        for(MenuButton button : menuButtonList) {
            button.resetButton();
            button.setDisabled(true);                           // Set for receives input events but not trigger
            //button.setTouchable(Touchable.disabled);          // Set for receiving input events
        }
    }
    private void enableMenuButtons() {
        for(MenuButton button : menuButtonList) {
            button.resetButton();
            button.setDisabled(false);                          // Set for receives input events but not trigger
            //button.setTouchable(Touchable.enabled);           // Set for receiving input events
        }
    }

    /** Prompt */
        // Enable/Disable
    private void disablePromptButtons() {
        showPromptText = false;

        for(MenuButton button : confirmationButtonList) {
            button.resetButton();
            button.setTouchable(Touchable.disabled);            // Set for receiving input events
        }
    }
    private void enablePromptButtons() {
        showPromptText = true;

        for(MenuButton button : confirmationButtonList) {
            button.setTouchable(Touchable.enabled);             // Set for receives input events
        }
    }
        // Open prompt
    private void triggerPromptBox() {
        //newGameBtn.startFlashing();
        newGameBtn.showSelector();

        showPromptBox = true;
        promptMenuPromptBox.open();
    }
    public void triggerPromptText() {
        showPromptBox = true;
        enablePromptButtons();
    }
        // Close prompt
    private void shutdownPrompt() {
        disablePromptButtons();
        promptMenuPromptBox.close();
    }
    public void hidePromptBox() {
        //newGameBtn.stopFlashing();
        newGameBtn.hideSelector();

        showPromptBox = false;
        enableMenuButtons();
    }

    /** Input handling */
    private void configureButtonListeners() {
        manager.sendDebugMsg(debugging, tag,"Configuring buttons.");

        /** Menu buttons */
        continueBtn.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                disableMenuButtons();
                manager.loadNewScreen(BeyondManager.OVERWORLD);
            }
        });
        continueBtn.addListener(new ClickListener() {
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (!continueBtn.isDisabled()) {
                    continueBtn.startFlashing();
                    continueBtn.showSelector();
                }
            }
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if (!continueBtn.isDisabled()) {
                    continueBtn.stopFlashing();
                    continueBtn.hideSelector();
                }
            }
        });

        newGameBtn.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                // Logic
                disableMenuButtons();
                if (showContinue) {
                    triggerPromptBox();
                } else {
                    disableMenuButtons();
                    manager.setGameSaveExists();
                    manager.loadNewLevel(1);
//                    manager.loadNewScreenClearSave(BeyondManager.OVERWORLD);
                }
            }
        });
        newGameBtn.addListener(new ClickListener() {
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (!newGameBtn.isDisabled()) {
                    newGameBtn.startFlashing();
                    newGameBtn.showSelector();
                }
            }
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if (!newGameBtn.isDisabled()) {
                    newGameBtn.stopFlashing();
                    newGameBtn.hideSelector();
                }
            }
        });

        settingsBtn.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                disableMenuButtons();
                manager.loadNewScreen(BeyondManager.SETTINGS);
            }
        });
        settingsBtn.addListener(new ClickListener() {
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (!settingsBtn.isDisabled()) {
                    settingsBtn.startFlashing();
                    settingsBtn.showSelector();
                }
            }
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if (!settingsBtn.isDisabled()) {
                    settingsBtn.stopFlashing();
                    settingsBtn.hideSelector();
                }
            }
        });

        exitBtn.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                // Exit
                disableMenuButtons();
                Gdx.app.exit();
            }
        });
        exitBtn.addListener(new ClickListener() {
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (!exitBtn.isDisabled()) {
                    exitBtn.startFlashing();
                    exitBtn.showSelector();
                }
            }
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if (!exitBtn.isDisabled()) {
                    exitBtn.stopFlashing();
                    exitBtn.hideSelector();
                }
            }
        });

        /** Yes and No buttons */
        yesBtn.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                disableMenuButtons();
                manager.loadNewLevel(1);
//                manager.loadNewScreenClearSave(BeyondManager.OVERWORLD);
            }
        });
        yesBtn.addListener(new ClickListener() {
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                yesBtn.startFlashing();
            }
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                yesBtn.stopFlashing();
            }
        });

        noBtn.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                shutdownPrompt();
            }
        });
        noBtn.addListener(new ClickListener() {
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                noBtn.startFlashing();
            }
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                noBtn.stopFlashing();
            }
        });
    }
    private void setInputProcessor() {
        manager.sendDebugMsg(debugging, tag,"Setting stage as the menus input processor.");

        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(menuStage);
        multiplexer.addProcessor(promptTextStage);

        Gdx.input.setInputProcessor(multiplexer);
    }

    private void update(float dt) {
        // Update Stages
        menuStage.act(Math.min(dt, 1 / 60f));
        //promptStage.act(Math.min(dt, 1 / 60f));
        promptTextStage.act(Math.min(dt, 1 / 60f));

        /** Buttons */
        for(MenuButton button : menuButtonList) {
            button.update(dt);
        }

        for (MenuButton button : confirmationButtonList) {
            button.update(dt);
        }

        /** Prompt */
        if (showPromptBox) { promptMenuPromptBox.update(dt); }

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

        // Draw
        menuStage.draw();

        if (showPromptBox) {
            promptStage.draw();
            spriteBatch.begin();
            promptMenuPromptBox.draw(spriteBatch, 1);
            spriteBatch.end();
        }

        if (showPromptText) {
            promptTextStage.draw();
        }
    }

    @Override
    public void resize(int width, int height) {
        menuStage.getViewport().update(width, height, true);
        promptStage.getViewport().update(width, height, true);
        promptTextStage.getViewport().update(width, height, true);
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