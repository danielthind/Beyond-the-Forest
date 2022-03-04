package com.coffeepizza.beyondtheforest.mainMenu;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.coffeepizza.beyondtheforest.levelscreen.util.HUD;
import com.coffeepizza.beyondtheforest.overworld.OWLevelSelectHUD;

public class MenuConfirmPrompt extends Actor {

    // System
    private boolean debugging = false;
    private String tag = "MenuConfirmPrompt";
    private String message = "";

    // Parent
    private MainMenuScreen menuHUD;
    private OWLevelSelectHUD overworldHUD;
    private HUD gameHUD;
    private int HUDType;

    // Asset Properties
    private TextureRegion animationRegionP1, animationRegionP2, sprite;
    private Animation openAnimation, closeAnimation;
    private boolean opening = false;
    private boolean close = false;
    private float width, height;

    // Timers
    private float elapsedTime = 0f;
    private float anitmation = 0f;

    public MenuConfirmPrompt(MainMenuScreen screen, TextureRegion animationRegionP1, TextureRegion animationRegionP2) {
        super();
        this.menuHUD = screen;
        this.HUDType = 0;
        this.animationRegionP1 = animationRegionP1;
        this.animationRegionP2 = animationRegionP2;

        setAnimations();
        sprite = getFrame(0f);
        //setPosition(menu.viewWidth / 2, menu.viewHeight / 2);
        setOrigin(menuHUD.viewWidth / 2, menuHUD.viewHeight / 2);
        //sprite = getFrame(0f);
    }

    public MenuConfirmPrompt(OWLevelSelectHUD screen, TextureRegion animationRegionP1, TextureRegion animationRegionP2) {
        super();
        this.overworldHUD = screen;
        this.HUDType = 1;
        this.animationRegionP1 = animationRegionP1;
        this.animationRegionP2 = animationRegionP2;

        setAnimations();
        sprite = getFrame(0f);
        //setPosition(menu.viewWidth / 2, menu.viewHeight / 2);
        setOrigin(overworldHUD.viewWidth / 2, overworldHUD.viewHeight / 2);
        //sprite = getFrame(0f);
    }
    public MenuConfirmPrompt(HUD screen, TextureRegion animationRegionP1, TextureRegion animationRegionP2) {
        super();
        this.gameHUD = screen;
        this.HUDType = 2;
        this.animationRegionP1 = animationRegionP1;
        this.animationRegionP2 = animationRegionP2;

        setAnimations();
        sprite = getFrame(0f);
        setOrigin(gameHUD.viewWidth / 2, gameHUD.viewHeight / 2);
    }

    public void setAnimations() {
        Array<TextureRegion> frames = new Array<TextureRegion>();

        // Open
        frames.add(new TextureRegion(animationRegionP1, 0 * 160, 0, 160, 70));
        frames.add(new TextureRegion(animationRegionP1, 1 * 160, 0, 160, 70));
        frames.add(new TextureRegion(animationRegionP1, 4 * 160, 0, 160, 70));
        frames.add(new TextureRegion(animationRegionP1, 5 * 160, 0, 160, 70));
        frames.add(new TextureRegion(animationRegionP2, 0 * 160, 0, 160, 70));
        frames.add(new TextureRegion(animationRegionP2, 1 * 160, 0, 160, 70));
        frames.add(new TextureRegion(animationRegionP2, 2 * 160, 0, 160, 70));
        frames.add(new TextureRegion(animationRegionP2, 3 * 160, 0, 160, 70));
        frames.add(new TextureRegion(animationRegionP2, 4 * 160, 0, 160, 70));

        openAnimation = new Animation(0.05f, frames);
        frames.clear();

        // Close
        frames.add(new TextureRegion(animationRegionP2, 4 * 160, 0, 160, 70));
        frames.add(new TextureRegion(animationRegionP2, 3 * 160, 0, 160, 70));
        frames.add(new TextureRegion(animationRegionP2, 2 * 160, 0, 160, 70));
        frames.add(new TextureRegion(animationRegionP2, 1 * 160, 0, 160, 70));
        frames.add(new TextureRegion(animationRegionP2, 0 * 160, 0, 160, 70));
        frames.add(new TextureRegion(animationRegionP1, 5 * 160, 0, 160, 70));
        frames.add(new TextureRegion(animationRegionP1, 4 * 160, 0, 160, 70));

        closeAnimation = new Animation(0.05f, frames);
        frames.clear();
    }

    public TextureRegion getFrame(float dt) {
        TextureRegion region;

        if (opening) {
            region = (TextureRegion) openAnimation.getKeyFrame(anitmation, false);
            anitmation += dt;
        } else {
            region = (TextureRegion) closeAnimation.getKeyFrame(anitmation, false);
            anitmation += dt;
        }

        return region;
    }

    public void open() {
        anitmation = 0;
        opening = true;
        close = false;

        sprite = getFrame(0f); // Stops first frame being of a close animation because box hasn't updated
    }

    public void close() {
        anitmation = 0;
        opening = false;

        sprite = getFrame(0f); // Stops first frame being of an open animation because box hasn't updated
    }

    public void setWidth(float w) {
        width = w;
    }
    public void setHeight(float h) {
        height = h;
    }

    public void update(float dt) {
        elapsedTime += dt;

        if (opening) {
            if (openAnimation.isAnimationFinished(anitmation)) {
                /** Open and trigger action in correct HUD */
                if (HUDType == 0) {
                    menuHUD.triggerPromptText();
                } else if (HUDType == 1) {
                    overworldHUD.triggerPromptText();
                } else if (HUDType == 2) {
                    gameHUD.showPromptText();
                }
            }
        } else {
            if (closeAnimation.isAnimationFinished(anitmation)) {
                /** Close and trigger action in correct HUD */
                if (HUDType == 0) {
                    menuHUD.hidePromptBox();
                } else if (HUDType == 1) {
                    overworldHUD.hidePromptBox();
                } else if (HUDType == 2) {
                    gameHUD.hidePromptBox();
                }
            }
        }
        sprite = getFrame(dt);
    }

    public void draw(Batch batch, float parentAlpha) {
        batch.draw(sprite, this.getX(), this.getY(), width, height);
        //Gdx.app.log(tag, "something drawing: " + this.getX() + ", " + this.getY());
    }


}