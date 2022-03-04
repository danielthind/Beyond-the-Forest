package com.coffeepizza.beyondtheforest.levelscreen.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.coffeepizza.beyondtheforest.BeyondManager;
import com.coffeepizza.beyondtheforest.levelscreen.LevelScreen;
import com.coffeepizza.beyondtheforest.levelscreen.GameCamera;
import com.coffeepizza.beyondtheforest.mainMenu.MenuConfirmPrompt;

public class HUD implements Disposable {

    // System
    private boolean debugging = false;
    private String tag = "GAME HUD";
    private String message = "";

    // Managers
    private BeyondManager manager;
    private LevelScreen screen;
    private SpriteBatch spriteBatch;
    public ShapeRenderer shapeRenderer;

    // Properties
    private boolean isBossLevel;

    // Camera
    private GameCamera camera;

    // Assets
    private TextureRegionDrawable healthThree, healthTwo, healthOne, healthZero;
    private TextureRegionDrawable uiEmptySecret, uiFoundSecret;
    private TextureRegionDrawable uiDarkWolfHealthZero, uiDarkWolfHealthOne, uiDarkWolfHealthTwo,
            uiDarkWolfHealthThree, uiDarkWolfHealthFour, uiDarkWolfHealthFive, uiDarkWolfHealthSix;

    // Widgets
        // Labels
        // Images
    private Image healthBar;
    private int flashHealthBarCount = 0;
    private boolean flashHealthBar = false;
    private boolean flashing = false;
    private Image secret1, secret2, secret3, secret4;
    private Image bossHealth;

    // Stage
    public float viewWidth, viewHeight;
    private Stage uiStage;
    private Table uiTable;

    // Dialogue
        // Confirmation Propmt
    private Stage dialogueNarationStage, dialogueDekuStage, dialogueVillianStage;
    private Table promptNarationTable, promptDekuTable, promptVillianTable;
    private TextureRegionDrawable pBNarationDrawableP1, pBNarationDrawableP2,
            pBDekuDrawableP1, pBDekuDrawableP2, pBVillianDrawableP1, pBVillianDrawableP2;
    private MenuConfirmPrompt dialogueBoxNaration, dialogueBoxDeku, dialogueBoxVillian;
    private float PROMPT_BOX_WIDTH, PROMPT_BOX_HEIGHT;
    private boolean showPromptBox = false;
    private boolean showingNaratorDialogue, showingDekuDialogue, showingVillianDialogue;
        // Text
    private Stage dialogueTextStage;
    private Table dialogueTextTable;
    private Label dialogueLabel;
    private String dialogueText = "test text";
    private float PROMPT_TEXT_VERTICAL_PADDING;
    private boolean showPromptText = false;

    //Cutscene Bars & Fades
    private Stage uIBoxStage;
    private Table uIBoxTable;
    public UIControlsBoxes uIBoxes;
    private boolean cutsceneInProgress = false;

    // Colours
    public enum SPEAKER {
        NARRATOR(Color.GREEN),
        DEKU(Color.PURPLE),
        VILLIAN(Color.RED);

        private final Color value;

        SPEAKER(final Color newValue) {
            value = newValue;
        }

        public Color getValue() { return value; }
    }

    // Dimensions
    private float TOP_PADDING, SIDE_PADDING,
            HEALTHBAR_HEIGHT, HEALTHBAR_WIDTH,
            SECRET_HEIGHT, SECRET_WIDTH,
            BOSSHEALTH_WIDTH;

    public HUD(BeyondManager manager, LevelScreen screen, boolean isBossLevel) {
        this.manager = manager;
        this.screen = screen;
        this.spriteBatch = manager.spriteBatch;
        this.shapeRenderer = manager.shapeRenderer;
        this.isBossLevel = isBossLevel;

        showingNaratorDialogue = showingDekuDialogue = showingVillianDialogue = false;

        createStage();
        loadDrawables();
        portDimensions();
        createActors();
        createTable();

        updateSecrets();
    }

    private void createStage() {
        uiStage = new Stage(new ScreenViewport());

        // Dialogue box
        dialogueNarationStage = new Stage(new ScreenViewport());
        dialogueDekuStage = new Stage(new ScreenViewport());
        dialogueVillianStage = new Stage(new ScreenViewport());
        dialogueTextStage = new Stage(new ScreenViewport());

        // Cutscene Bars
        uIBoxStage = new Stage(new ScreenViewport());
    }
    private void loadDrawables() {
        // Assets
        healthThree = new TextureRegionDrawable(manager.getAtlas().findRegion("3_health"));
        healthTwo = new TextureRegionDrawable(manager.getAtlas().findRegion("2_health"));
        healthOne = new TextureRegionDrawable(manager.getAtlas().findRegion("1_health"));
        healthZero = new TextureRegionDrawable(manager.getAtlas().findRegion("0_health"));
        uiEmptySecret = new TextureRegionDrawable(manager.getAtlas().findRegion("ui_empty_secret"));
        uiFoundSecret = new TextureRegionDrawable(manager.getAtlas().findRegion("ui_found_secret"));
        uiDarkWolfHealthZero  = new TextureRegionDrawable(manager.getAtlas().findRegion("ui_dark_wolf_health_zero"));
        uiDarkWolfHealthOne = new TextureRegionDrawable(manager.getAtlas().findRegion("ui_dark_wolf_health_one"));
        uiDarkWolfHealthTwo = new TextureRegionDrawable(manager.getAtlas().findRegion("ui_dark_wolf_health_two"));
        uiDarkWolfHealthThree = new TextureRegionDrawable(manager.getAtlas().findRegion("ui_dark_wolf_health_three"));
        uiDarkWolfHealthFour = new TextureRegionDrawable(manager.getAtlas().findRegion("ui_dark_wolf_health_four"));
        uiDarkWolfHealthFive = new TextureRegionDrawable(manager.getAtlas().findRegion("ui_dark_wolf_health_five"));
        uiDarkWolfHealthSix = new TextureRegionDrawable(manager.getAtlas().findRegion("ui_dark_wolf_health_six"));

        // Widgets
        // Labels
        // Images
        healthBar = new Image(healthThree);
        secret1 = new Image(uiEmptySecret);
        secret2 = new Image(uiEmptySecret);
        secret3 = new Image(uiEmptySecret);
        secret4 = new Image(uiEmptySecret);

        bossHealth = new Image(uiDarkWolfHealthSix);

        // Prompt box
        pBNarationDrawableP1 = new TextureRegionDrawable(manager.getAtlas().findRegion("main_menu_text_prompt_box_p1"));
        pBNarationDrawableP2 = new TextureRegionDrawable(manager.getAtlas().findRegion("main_menu_text_prompt_box_p2"));
        pBDekuDrawableP1 = new TextureRegionDrawable(manager.getAtlas().findRegion("prompt_box_deku_p1"));
        pBDekuDrawableP2 = new TextureRegionDrawable(manager.getAtlas().findRegion("prompt_box_deku_p2"));
        pBVillianDrawableP1 = new TextureRegionDrawable(manager.getAtlas().findRegion("prompt_box_enemy_p1"));
        pBVillianDrawableP2 = new TextureRegionDrawable(manager.getAtlas().findRegion("prompt_box_enemy_p2"));
    }
    private void createActors() {
        // Dialogue box
            // Prompt box
        dialogueBoxNaration = new MenuConfirmPrompt(this, pBNarationDrawableP1.getRegion(), pBNarationDrawableP2.getRegion());
        dialogueBoxDeku = new MenuConfirmPrompt(this, pBDekuDrawableP1.getRegion(), pBDekuDrawableP2.getRegion());
        dialogueBoxVillian = new MenuConfirmPrompt(this, pBVillianDrawableP1.getRegion(), pBVillianDrawableP2.getRegion());

            // Text
        dialogueLabel = new Label(dialogueText, new Label.LabelStyle(manager.goldFont, Color.WHITE));
        dialogueLabel.setAlignment(Align.center, Align.center);

        // Letterbox
        uIBoxes = new UIControlsBoxes(manager, viewWidth, viewHeight);
    }
    private void portDimensions() {
        // Screen
        viewWidth = uiStage.getViewport().getWorldWidth();
        viewHeight = uiStage.getViewport().getWorldHeight();

        /** Mobile */
        TOP_PADDING = viewHeight * 0.04f;
        SIDE_PADDING = TOP_PADDING;

        HEALTHBAR_HEIGHT = viewHeight * 0.2f;//0.15f;
        HEALTHBAR_WIDTH = (HEALTHBAR_HEIGHT * healthThree.getRegion().getRegionWidth())
                / healthThree.getRegion().getRegionHeight();

        SECRET_HEIGHT = HEALTHBAR_HEIGHT * 0.75f;
        SECRET_WIDTH = (SECRET_HEIGHT * uiEmptySecret.getRegion().getRegionWidth())
                / uiEmptySecret.getRegion().getRegionHeight();

        BOSSHEALTH_WIDTH = HEALTHBAR_HEIGHT
                * (uiDarkWolfHealthZero.getRegion().getRegionWidth() / uiDarkWolfHealthZero.getRegion().getRegionHeight());

        // Prompt
            // Box
        PROMPT_BOX_HEIGHT = (viewHeight * 0.35f);//;0.25f) ;
        PROMPT_BOX_WIDTH = viewWidth * 0.6f;
            // Text
//        PROMPT_TEXT_VERTICAL_PADDING = ((viewHeight / 3) - PROMPT_BOX_HEIGHT) * 0.5f;
        PROMPT_TEXT_VERTICAL_PADDING = viewHeight - PROMPT_BOX_HEIGHT;
    }
    private void createTable() {
        /** Create Tables */
        // UI Table
        uiTable = new Table();
        uiTable.setFillParent(true);

        // Dialogue Box
        promptNarationTable = new Table();
        promptNarationTable.setFillParent(true);
        promptDekuTable = new Table();
        promptDekuTable.setFillParent(true);
        promptVillianTable = new Table();
        promptVillianTable.setFillParent(true);

        // Dialogue Text
        dialogueTextTable = new Table();
        dialogueTextTable.setFillParent(true);

        // Cutscene letter boxing
        uIBoxTable = new Table();
        uIBoxTable.setFillParent(true);

        if (debugging) {
            uiTable.setDebug(true);
            promptNarationTable.setDebug(true);
            promptDekuTable.setDebug(true);
            promptVillianTable.setDebug(true);
            dialogueTextTable.setDebug(true);
            uIBoxTable.setDebug(true);
        }

        /** HUD */
        uiTable.top();

        uiTable.add().padTop(TOP_PADDING);
        uiTable.row();

        uiTable.add(healthBar).height(HEALTHBAR_HEIGHT).width(HEALTHBAR_WIDTH)
                .align(Align.left).expandX().padLeft(SIDE_PADDING);

        if (!isBossLevel) {
            uiTable.add(secret1).height(SECRET_HEIGHT).width(SECRET_WIDTH)
                    .align(Align.right).expandX();
            uiTable.add(secret2).height(SECRET_HEIGHT).width(SECRET_WIDTH);
            uiTable.add(secret3).height(SECRET_HEIGHT).width(SECRET_WIDTH);
            uiTable.add(secret4).height(SECRET_HEIGHT).width(SECRET_WIDTH)
                    .padRight(SIDE_PADDING);
        } else {
            uiTable.add(bossHealth).height(HEALTHBAR_HEIGHT).width(BOSSHEALTH_WIDTH)
                    .align(Align.right).expandX().padRight(SIDE_PADDING);
        }

        uiStage.addActor(uiTable);

        /** Dialogue Box */
            // Box
        promptNarationTable.top();
        promptNarationTable.add().height(viewHeight - PROMPT_BOX_HEIGHT)
                .expandX();
        promptNarationTable.row();
        promptNarationTable.add(dialogueBoxNaration).width(PROMPT_BOX_WIDTH).height(PROMPT_BOX_HEIGHT)
                .expandX().expandY();
        dialogueBoxNaration.setWidth(PROMPT_BOX_WIDTH);
        dialogueBoxNaration.setHeight(PROMPT_BOX_HEIGHT);
        dialogueNarationStage.addActor(promptNarationTable);

        promptDekuTable.top();
        promptDekuTable.add().height(viewHeight - PROMPT_BOX_HEIGHT)
                .expandX();
        promptDekuTable.row();
        promptDekuTable.add(dialogueBoxDeku).width(PROMPT_BOX_WIDTH).height(PROMPT_BOX_HEIGHT)
                .expandX().expandY();
        dialogueBoxDeku.setWidth(PROMPT_BOX_WIDTH);
        dialogueBoxDeku.setHeight(PROMPT_BOX_HEIGHT);
        dialogueDekuStage.addActor(promptDekuTable);

        promptVillianTable.top();
        promptVillianTable.add().height(viewHeight - PROMPT_BOX_HEIGHT)
                .expandX();
        promptVillianTable.row();
        promptVillianTable.add(dialogueBoxVillian).width(PROMPT_BOX_WIDTH).height(PROMPT_BOX_HEIGHT)
                .expandX().expandY();
        dialogueBoxVillian.setWidth(PROMPT_BOX_WIDTH);
        dialogueBoxVillian.setHeight(PROMPT_BOX_HEIGHT);
        dialogueVillianStage.addActor(promptVillianTable);

        // Text
        dialogueTextTable.top();
        dialogueTextTable.add().height(PROMPT_TEXT_VERTICAL_PADDING)//viewHeight * 2 / 3)
                .expandX();
        dialogueTextTable.row();

        dialogueTextTable.add()
                .expandX();
        dialogueTextTable.row();

        dialogueTextTable.add(dialogueLabel)
                .height(manager.menuFontSize)
                .expandX().expandY();

        dialogueTextStage.addActor(dialogueTextTable);

        // Cutscene letter boxing
        uIBoxTable.add(uIBoxes);
        uIBoxStage.addActor(uIBoxTable);
    }

    /** Dialogue Box */
    /**
     * Open prompt
     * @params
     *      text: new text to display
     *          16 characters max line length
     *          4 lines max
     *      colour: new text colour (use hud static colours)
     */
    public void promptBoxOpen(String text, SPEAKER speaker) {
        dialogueLabel.setText(text);
        dialogueLabel.setColor(speaker.getValue());

        dialogueLabel.setFontScale(0.5f);
//        Gdx.app.log(tag, "" + dialogueLabel.getFontScaleX());

        showPromptBox = true;

        if (speaker == SPEAKER.NARRATOR) {
            dialogueBoxNaration.open();
            showingNaratorDialogue = true;
        } else if (speaker == SPEAKER.DEKU) {
            dialogueBoxDeku.open();
            showingDekuDialogue = true;
        } else if (speaker == SPEAKER.VILLIAN) {
            dialogueBoxVillian.open();
            showingVillianDialogue = true;
        }
    }
    public void promptBoxUpdateText(String text, SPEAKER speaker) {
        dialogueLabel.setText(text);
        dialogueLabel.setColor(speaker.getValue());
    }
    public void showPromptText() {
        showPromptText = true;
    }
    // Close prompt
    public void promptBoxClose() {
        if (showingNaratorDialogue) {
            dialogueBoxNaration.close();
        }
        if (showingDekuDialogue) {
            dialogueBoxDeku.close();
        }
        if (showingVillianDialogue) {
            dialogueBoxVillian.close();
        }

        showPromptText = false;
    }
    public void hidePromptBox() {
        showingNaratorDialogue = showingDekuDialogue = showingVillianDialogue = false;
        showPromptBox = false;
    }

    /** Cutscene Bars */
    public void showLetterboxing() {
        cutsceneInProgress = true;

        uIBoxes.openLetterboxes();
    }
    public void hideLetterboxing() {
        cutsceneInProgress = false;

        uIBoxes.closeLetterBoxes();
    }

    /** Controls UIs */
    public void stopFlashingControls() { uIBoxes.stopFlashingControls(); }
    public void flashJump() { uIBoxes.flashJump(); }
    public void flashAttack() { uIBoxes.flashAttack(); }
    public void flashChangeDir() { uIBoxes.flashChangeDir(); }
    public void flashMenu() { uIBoxes.flashMenu(); }

    /** Fades */
    public void fadeOpaqueToVisible() { uIBoxes.fadeOpaqueToVisible(); }
    public void fadeVisibleToOpaque() { uIBoxes.fadeVisibleToOpaque(); }
    public void fadeSetScreenToOpaque() { uIBoxes.fadeSetScreenToOpaque(); }
    public void fadeSetScreenToVisible() { uIBoxes.fadeSetScreenToVisible(); }
    public void setBlackFade(boolean blackFade) { uIBoxes.setBlackFade(blackFade);}

    /** Health */
    public void updateHealth(int health) {
        switch (health) {
            case 3:
                healthBar.setDrawable(healthThree);
                flashHealthBar = false;
                break;
            case 2:
                healthBar.setDrawable(healthTwo);
                flashHealthBar = false;
                break;
            case 1:
                healthBar.setDrawable(healthOne);
                flashHealthBar = true;
                break;
//            case 0:
//                healthBar.setDrawable(healthZero);
//                flashHealthBar = false;
//                break;
            default:
                healthBar.setDrawable(healthZero);
                flashHealthBar = false;
                break;
        }
    }
    private void flashHealthBar() {
        flashHealthBarCount++;
        if (flashHealthBarCount > 4) {
            flashHealthBarCount = 0;
            flashing = !flashing;
        }

        if (flashing) {
            healthBar.setDrawable(healthOne);
        } else {
            healthBar.setDrawable(healthZero);
        }
    }

    /** Secrets */
    private void updateSecrets() {
        if (screen.secretBone1) {
            findSecret(1);
        }
        if (screen.secretBone2) {
            findSecret(2);
        }
        if (screen.secretBone3) {
            findSecret(3);
        }
        if (screen.secretBone4) {
            findSecret(4);
        }
    }
    public void findSecret(int secretNumber) {
        switch (secretNumber) {
            case 1:
                secret1.setDrawable(uiFoundSecret);
                break;
            case 2:
                secret2.setDrawable(uiFoundSecret);
                break;
            case 3:
                secret3.setDrawable(uiFoundSecret);
                break;
            case 4:
                secret4.setDrawable(uiFoundSecret);
                break;
        }
    }

    /** BossHealth */
    public void updateBossHealht(int health) {
        switch (health) {
            case 0:
                bossHealth.setDrawable(uiDarkWolfHealthZero);
                break;
            case 1:
                bossHealth.setDrawable(uiDarkWolfHealthOne);
                break;
            case 2:
                bossHealth.setDrawable(uiDarkWolfHealthTwo);
                break;
            case 3:
                bossHealth.setDrawable(uiDarkWolfHealthThree);
                break;
            case 4:
                bossHealth.setDrawable(uiDarkWolfHealthFour);
                break;
            case 5:
                bossHealth.setDrawable(uiDarkWolfHealthFive);
                break;
            case 6:
                bossHealth.setDrawable(uiDarkWolfHealthSix);
                break;
        }
    }

    public void update(float dt) {
        uiStage.act(Math.min(dt, 1 / 60f));
        dialogueTextStage.act(Math.min(dt, 1 / 60f));

        // Update prompt box is its active
        if (showingNaratorDialogue) { dialogueBoxNaration.update(dt); }
        if (showingDekuDialogue) { dialogueBoxDeku.update(dt); }
        if (showingVillianDialogue) { dialogueBoxVillian.update(dt); }

        // Letterboxes
//        updateHealthBar();
        if(flashHealthBar) { flashHealthBar(); }
        uIBoxes.update();
    }

    public void draw() {
        if (!cutsceneInProgress) {
            uiStage.draw();
        }

        /** UI - Healthbar and secrets */
        uIBoxStage.draw();

        /** Dialogue */
        if (showPromptBox) {
            // Select which box to draw
            if (showingNaratorDialogue) {
                dialogueNarationStage.draw();
                spriteBatch.begin();
                dialogueBoxNaration.draw(spriteBatch, 1);
                spriteBatch.end();
            } else if (showingDekuDialogue) {
                dialogueDekuStage.draw();
                spriteBatch.begin();
                dialogueBoxDeku.draw(spriteBatch, 1);
                spriteBatch.end();
            } else if (showingVillianDialogue) {
                dialogueVillianStage.draw();
                spriteBatch.begin();
                dialogueBoxVillian.draw(spriteBatch, 1);
                spriteBatch.end();
            }
            if (showPromptText) {
                dialogueTextStage.draw();
            }
        }
    }

    @Override
    public void dispose() {
        uiStage.dispose();
        dialogueNarationStage.dispose();
        dialogueDekuStage.dispose();
        dialogueVillianStage.dispose();
        dialogueTextStage.dispose();
        uIBoxStage.dispose();
    }

    public void resize(int width, int height) {
        uiStage.getViewport().update(width, height, true);
        dialogueNarationStage.getViewport().update(width, height, true);
        dialogueDekuStage.getViewport().update(width, height, true);
        dialogueVillianStage.getViewport().update(width, height, true);
        dialogueTextStage.getViewport().update(width, height, true);
    }
}
