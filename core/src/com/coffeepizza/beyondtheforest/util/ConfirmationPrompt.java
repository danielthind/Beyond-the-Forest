package com.coffeepizza.beyondtheforest.util;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.coffeepizza.beyondtheforest.mainMenu.MenuButton;
import com.coffeepizza.beyondtheforest.mainMenu.MenuConfirmPrompt;

public class ConfirmationPrompt implements Disposable {

    // System
    private boolean debugging = true;
    private String tag = "ConfirmationPrompt";
    private String message = "";

    // Stage
    private Stage promptStage, promptTextStage;

    // Confirmation Propmt
    private Table promptBoxTable;
    private TextureRegionDrawable promptBoxDrawableP1, promptBoxDrawableP2;
    private MenuConfirmPrompt promptMenuButton;
    // Confirmation Buttons
    private Table promptTextTable;
    private MenuButton yesBtn, noBtn;
    private TextureRegionDrawable yesDrawable, noDrawable;
    private ImageButton.ImageButtonStyle yesStyle, noStyle;
    private Image confirmationMessage;

    public ConfirmationPrompt() {
        createStages();
        createTables();
    }

    private void createStages() {
        promptStage = new Stage(new ScreenViewport());
        promptStage.clear();

        // Promt Text
        promptTextStage = new Stage(new ScreenViewport());
        promptTextStage.clear();
    }

    private void createTables() {
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

    @Override
    public void dispose() {

    }
}
