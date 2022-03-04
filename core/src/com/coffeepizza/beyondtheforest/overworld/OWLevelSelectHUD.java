package com.coffeepizza.beyondtheforest.overworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.coffeepizza.beyondtheforest.BeyondManager;
import com.coffeepizza.beyondtheforest.mainMenu.MenuButton;
import com.coffeepizza.beyondtheforest.mainMenu.MenuConfirmPrompt;

public class OWLevelSelectHUD implements Disposable {

    // System
    private boolean debugging = false;
    private String tag = "OWMenuHUD";
    private String message = "";

    // Game and Handlers
    private BeyondManager manager;
    private OverworldScreen screen;

    // Stage
    public Stage promptStage, dialogueStage;
    public float viewWidth, viewHeight;
    private Table promptTable, dialogueTable;

    // Assets
    private TextureAtlas textureAtlas;

    // Prompt
    private MenuConfirmPrompt promptBox;
    private TextureRegionDrawable promptBoxDrawableP1, promptBoxDrawableP2;

    // Dialogue
    private Label enterLevelLabel;
    private String enterLevelString, enterLevelStringDefault;
    private MenuButton yesBtn, noBtn;
    private TextureRegionDrawable yesDrawable, noDrawable, newGameDrawable;
    private ImageButton.ImageButtonStyle yesStyle, noStyle;

    // Properties
    private boolean showPrompt = false;
    private boolean showDialogue = false;
    private int levelHolder = 0;

    // Dimensions
    private float SELECTOR_PADDING, SELECTOR_HEIGHT, UNIT_HEIGHT, BTN_HEIGHT, PROMPT_BOX_HEIGHT, PROMPT_BOX_WIDTH,
        BOTTOM_PADDING, TOP_PADDING, PROMPT_TEXT_TOP_PADDING, BTN_SIZE_RATIO;

    public OWLevelSelectHUD(BeyondManager game, OverworldScreen over) {
        this.manager = game;
        this.screen = over;
        this.textureAtlas = screen.textureAtlas;

        createStages();
        loadDrawables();
        createAllButtons();
        configureButtonListeners();
        createPromptBox();
        portDimensions();
        createTable();

        mobileTableSetup();
    }

    private void createStages() {
        promptStage = new Stage(new ScreenViewport());
        dialogueStage = new Stage(new ScreenViewport());
    }

    private void loadDrawables() {
        yesDrawable = new TextureRegionDrawable(textureAtlas.findRegion("main_menu_text_yes"));
        noDrawable = new TextureRegionDrawable(textureAtlas.findRegion("main_menu_text_no"));
        //newGameDrawable = new TextureRegionDrawable(textureAtlas.findRegion("main_menu_text_new_game"));
        promptBoxDrawableP1 = new TextureRegionDrawable(textureAtlas.findRegion("main_menu_text_prompt_box_p1"));
        promptBoxDrawableP2 = new TextureRegionDrawable(textureAtlas.findRegion("main_menu_text_prompt_box_p2"));

        yesStyle = new ImageButton.ImageButtonStyle(yesDrawable, null, null,
                null, null, null);
        noStyle = new ImageButton.ImageButtonStyle(noDrawable, null, null,
                null, null, null);
    }
    private void createPromptBox() {
        promptBox = new MenuConfirmPrompt(this, promptBoxDrawableP1.getRegion(), promptBoxDrawableP2.getRegion());

        enterLevelStringDefault = "ENTER LEVEL ";
        enterLevelString = enterLevelStringDefault;
        enterLevelLabel = new Label(enterLevelString, new Label.LabelStyle(manager.goldFont, Color.GREEN));
    }
    private void portDimensions() {
        /** Screen */
        viewWidth = promptStage.getViewport().getWorldWidth();
        viewHeight = promptStage.getViewport().getWorldHeight();

        /** Mobile */
        UNIT_HEIGHT = viewHeight * 0.075f;
        BTN_HEIGHT =  viewHeight * 0.1f;

        SELECTOR_HEIGHT = UNIT_HEIGHT;
        SELECTOR_PADDING = UNIT_HEIGHT * 0.5f;

        PROMPT_BOX_HEIGHT = UNIT_HEIGHT * 5.6f;//4f;
        PROMPT_BOX_WIDTH = PROMPT_BOX_HEIGHT * 2.8f;

        BOTTOM_PADDING = UNIT_HEIGHT * 1f;
        TOP_PADDING = viewHeight - PROMPT_BOX_HEIGHT            // get y value of top of prompt box
                + (PROMPT_BOX_HEIGHT / 7)                       // old box was 140 x 50, new is 160 x 70 (10 padding all sides)
                - BOTTOM_PADDING;
        PROMPT_TEXT_TOP_PADDING = TOP_PADDING + UNIT_HEIGHT;// + manager.fontSize;

        BTN_SIZE_RATIO = BTN_HEIGHT / yesDrawable.getRegion().getRegionHeight();
    }
    private void createTable() {
        promptTable = new Table();
        promptTable.setFillParent(true);

        dialogueTable = new Table();
        dialogueTable.setFillParent(true);

        if (debugging) {
            promptTable.setDebug(true);
            dialogueTable.setDebug(true);
        }

        // Add to stages
        promptStage.addActor(promptTable);
        dialogueStage.addActor(dialogueTable);
    }
    private void mobileTableSetup() {
        // PromptBox
        promptTable.top();

        promptTable.add()
                .padTop(TOP_PADDING);
        promptTable.row();

        promptTable.add(promptBox).width(PROMPT_BOX_WIDTH).height(PROMPT_BOX_HEIGHT);
        promptBox.setWidth(PROMPT_BOX_WIDTH);
        promptBox.setHeight(PROMPT_BOX_HEIGHT);

        // Dialogue
        int cols = 3;
        dialogueTable.top();

        dialogueTable.add()
                .padTop(TOP_PADDING)
                .colspan(cols);
        dialogueTable.row();

        dialogueTable.add()
                .height(UNIT_HEIGHT * 1.5f)
                .colspan(cols);
        dialogueTable.row();

        dialogueTable.add(enterLevelLabel)
                .height(manager.menuFontSize)
                .colspan(cols);
        dialogueTable.row();

        // Gap between text and buttons
        dialogueTable.add()
                .height(UNIT_HEIGHT * 0.5f)
                .colspan(cols);
        dialogueTable.row();

        // YES and NO buttons
        dialogueTable.add(yesBtn)
                .width(yesDrawable.getRegion().getRegionWidth() * BTN_SIZE_RATIO)
                .height(BTN_HEIGHT);
            // Gap between buttons
        dialogueTable.add()
                .width(BTN_HEIGHT)
                .height(BTN_HEIGHT);
        dialogueTable.add(noBtn)
                .width(noDrawable.getRegion().getRegionWidth() * BTN_SIZE_RATIO)
                .height(BTN_HEIGHT);
    }

    /** Buttons */
    private void createAllButtons() {
        yesBtn = new MenuButton( this, yesStyle);
        noBtn = new MenuButton(this, noStyle);
    }

    private void configureButtonListeners() {
        /** Yes and No buttons */
        yesBtn.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                disablePromptButtons();
                loadNewLevel();
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

    private void disablePromptButtons() {
        yesBtn.resetButton();
        yesBtn.setDisabled(true);
        yesBtn.setTouchable(Touchable.disabled);

        noBtn.resetButton();
        noBtn.setDisabled(true);
        noBtn.setTouchable(Touchable.disabled);
    }
    private void enablePromptButtons() {
        //yesBtn.resetButton();
        yesBtn.setDisabled(false);
        yesBtn.setTouchable(Touchable.enabled);

        //noBtn.resetButton();
        noBtn.setDisabled(false);
        noBtn.setTouchable(Touchable.enabled);
    }

    /** Load level */
    private void loadNewLevel() {
        if (levelHolder != 0) {
            manager.setCurrentLevel(levelHolder);
            manager.loadNewLevel(levelHolder);
        } else {
            Gdx.app.log(tag, "Error: levelHolder contains null value");
        }
    }

    /** Prompt Triggers */
    public void clearPrompt() {
        levelHolder = 0;

        showPrompt = false;
        showDialogue = false;

        disablePromptButtons();
    }

    public void createPrompt(int levelToPrompt) {
        levelHolder = levelToPrompt;
        enterLevelString = enterLevelStringDefault + levelHolder + "?";
        enterLevelLabel.setText(enterLevelString);

        showPrompt = true;
        promptBox.open();
    }

    public void hidePromptBox() {
        showPrompt = false;
        levelHolder = 0;
    }

    public void triggerPromptText() {
        enablePromptButtons();
        showDialogue = true;
    }

    private void shutdownPrompt() {
        disablePromptButtons();
        showDialogue = false;
        promptBox.close();
        screen.enableLevelButtons();
    }

    public void update(float dt) {
        promptStage.act(Math.min(dt, 1 / 60f));
        dialogueStage.act(Math.min(dt, 1 / 60f));

        promptBox.update(dt);
        yesBtn.update(dt);
        noBtn.update(dt);
    }

    public void draw() {
        if (showPrompt) {
            promptStage.draw();
            if (showDialogue) {
                dialogueStage.draw();
            }
        }
    }

        @Override
    public void dispose() {
        promptStage.dispose();
        dialogueStage.dispose();
    }
}
