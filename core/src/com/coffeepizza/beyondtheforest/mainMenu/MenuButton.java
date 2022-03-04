package com.coffeepizza.beyondtheforest.mainMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.utils.Array;
import com.coffeepizza.beyondtheforest.overworld.OWLevelSelectHUD;

public class MenuButton extends ImageButton {

    // System
    private boolean debugging = true;
    private String tag = "MenuButton";
    private String message = "";

    // Game Manager
    private MainMenuScreen menuHUD;
    private OWLevelSelectHUD owHUD;

    // Selector
    private boolean showSelector = true;
    private TextureRegion selectorRegion, selector;
    private Animation animation;

    // Flashing
    private boolean setToFlash = false;
    private boolean flashing = false;
    private float flashTimer = 0f;
    private float flashDuration = 0.075f;

    // Dimensions
    private float selectorPadding, selectorHeight, buttonHeight;

    // Draw
    private boolean draw = true;

    // Timers
    private float elapsedTime = 0;

    public MenuButton(MainMenuScreen menu, ImageButtonStyle style, TextureRegion selector) {
        super(style);
        this.menuHUD = menu;
        this.selectorRegion = selector;

        setDimensionsWithMenu();

        setAnimations(selectorRegion);
    }

    public MenuButton(MainMenuScreen menu, ImageButtonStyle style) {
        super(style);
        this.menuHUD = menu;
        showSelector = false;

        setDimensionsWithMenu();
    }

    public MenuButton(OWLevelSelectHUD over, ImageButtonStyle style) {
        super(style);
        this.owHUD = over;
        showSelector = false;

        setDimentionsWithOverworld();
    }

    private void setDimensionsWithMenu() {
        selectorPadding = menuHUD.BONE_SELECTOR_PADDING;
        selectorHeight = menuHUD.BONE_SELECTOR_HEIGHT;
        buttonHeight = menuHUD.BTN_HEIGHT;
    }
    private void setDimentionsWithOverworld() {
        selectorPadding =  10f;//menuHUD.BONE_SELECTOR_PADDING;
        selectorHeight = 10f;//menuHUD.BONE_SELECTOR_HEIGHT;
        buttonHeight = 10f;//menuHUD.BTN_HEIGHT;
    }

    public void resetButton() {
        elapsedTime = 0f;
        flashTimer = 0f;

        // Selector
        showSelector = false;

        // Flashing
        setToFlash = false;
        flashing = false;
    }

    /** Flashing */
    public void startFlashing() {
        setToFlash = true;
        flashing = true;
    }
    public void stopFlashing() {
        setToFlash = false;
        flashing = false;
    }

    public void setAnimations(TextureRegion region) {
        Array<TextureRegion> frames = new Array<TextureRegion>();

        // Idle
        for(int i = 0; i < 8; i++) {
            frames.add(new TextureRegion(region, i * 16, 0, 16, 16));

        }
        animation = new Animation(0.15f, frames);
        frames.clear();
    }
    private TextureRegion getFrame(float dt) {
        TextureRegion region;
        region = (TextureRegion) animation.getKeyFrame(elapsedTime, true);
        return region;
    }

    /** Selector */
    public void showSelector() {
        showSelector = true;
    }
    public void hideSelector() {
        showSelector = false;
    }

    public void update(float dt) {
        elapsedTime += dt;
        flashTimer += dt;

        if (showSelector) {
            selector = getFrame(elapsedTime);
        }
    }

    public void draw(Batch batch, float parentAlpha) {
        if (draw) {
            // Text
            if (setToFlash) {
                if (flashTimer > flashDuration) {
                    flashTimer = 0;
                    flashing = !flashing;
                }
            }
            if (!setToFlash || flashing) {
                super.draw(batch, parentAlpha);
            }

            // Selector
//            if (showSelector) {
//                if (selector.isFlipX()) {
//                    selector.flip(true, false);
//                }
//                batch.draw(selector,
//                        this.getX() - selectorPadding - selectorHeight,
//                        this.getY(),
//                        buttonHeight,
//                        buttonHeight);
//
//                if (!selector.isFlipX()) {
//                    selector.flip(true, false);
//                }
//                batch.draw(selector,
//                        this.getX() + this.getWidth() + selectorPadding,
//                        this.getY(),
//                        buttonHeight,
//                        buttonHeight);
//            }
        }
    }
}
