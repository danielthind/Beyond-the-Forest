package com.coffeepizza.beyondtheforest.levelscreen.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.coffeepizza.beyondtheforest.BeyondManager;

public class UIControlsBoxes extends Actor {

    // System
    private boolean debugging = false;
    private String tag = "Letterbox";
    private String message = "";

    // Managers
    private BeyondManager manager;
    private Preferences prefs;
    private ShapeRenderer shapeRenderer;

    // Dimensions
    private float viewWidth, viewHeight;
    private float maxHeight, currentHeight, deltaHeight;

    // Controls
    private boolean flashBottomLeft, flashBottomRight, flashTopLeft, flashTopRight, flashMenu, flashControls = false;
    private int ticker = 0;
    private int flashCount = 2; // How quickly flashing happens
    private Color controlsFillColour, controlsNoColour;
    public boolean hudFillTopLeft, hudFillTopMIddle, hudFillTopRIght, hudFillBottomLeft, hudFillBottomRight;

    // Fade
    private boolean fadeActive = false;
    private float fadeAlpha, fadeDelta  = 0.0f;
    private float fadeChange = 0.005f;
    private boolean blackFade = true;

    // Letterboxes
    private boolean letterboxing = false;

    // Prefs
    private boolean controlsYFlipped, controlsLRInsteadOfCD, HUDVirtualControls, HUDAnimateControls;

    public UIControlsBoxes(BeyondManager manager, float viewWidth, float viewHeight) {
        this.manager = manager;
        this.prefs = manager.prefs;
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;

        shapeRenderer = BeyondManager.shapeRenderer;
        maxHeight = viewHeight * 0.15f;
        deltaHeight = maxHeight * 0.025f;

        controlsFillColour = new Color(Color.PURPLE.r, Color.PURPLE.g, Color.PURPLE.b, 0.5f);
        controlsNoColour = new Color(Color.PURPLE.r, Color.PURPLE.g, Color.PURPLE.b, 0.0f);
        hudFillTopLeft = false;
        hudFillTopMIddle = false;
        hudFillTopRIght = false;
        hudFillBottomLeft = false;
        hudFillBottomRight = false;

        getPrefs();
    }

    public void getPrefs() {
        controlsYFlipped = prefs.getBoolean("controlsYFlipped");
        controlsLRInsteadOfCD = prefs.getBoolean("controlsLRInsteadOfCD");
        HUDVirtualControls = prefs.getBoolean("HUDVirtualControls");
        HUDAnimateControls = prefs.getBoolean("HUDAnimateControls");
    }

    /** Controls */
    public void stopFlashingControls() {
        flashBottomLeft = false;
        flashBottomRight = false;
        flashTopLeft = false;
        flashTopRight = false;
        flashMenu = false;

        ticker = 0;
        flashControls = false;
    }

    /*
        prefs.putBoolean("controlsYFlipped", false);
        prefs.putBoolean("controlsLRInsteadOfCD", false);
        prefs.putBoolean("HUDVirtualControls", true);
        prefs.putBoolean("HUDAnimateControls", true);
     */

    public void flashJump() {
        if (controlsYFlipped) {
            flashTopLeft = true;
        } else {
            flashBottomLeft = true;
        }
    }
    public void flashAttack() {
        if (controlsYFlipped) {
            flashTopRight = true;
        } else {
            flashBottomRight = true;
        }
    }
    public void flashChangeDir() {
        if (controlsYFlipped) {
            flashBottomLeft = true;
            flashBottomRight = true;
        } else {
            flashTopLeft = true;
            flashTopRight = true;
        }
    }
    public void flashMenu() { flashMenu = true;}

    /** Fade */
    public void fadeOpaqueToVisible() {
        fadeActive = true;
        fadeAlpha = 1.0f;
        fadeDelta = -fadeChange;
    }
    public void fadeVisibleToOpaque() {
        fadeActive = true;
        fadeAlpha = 0.0f;
        fadeDelta = fadeChange;
    }
    public void fadeSetScreenToOpaque() {
        fadeActive = true;
        fadeAlpha = 1.0f;
        fadeDelta = 0;
    }
    public void fadeSetScreenToVisible() {
        fadeActive = true;
        fadeAlpha = 0.0f;
        fadeDelta = 0;
    }

    public void setBlackFade(boolean blackFade) {
        this.blackFade = blackFade;
    }

    /** Letterboxes */
    public void openLetterboxes() {
        letterboxing = true;
        currentHeight = 0;
    }
    public void closeLetterBoxes() {
        letterboxing = false;
        currentHeight = maxHeight;
    }

    public void update() {
        // Controls
        if (flashTopLeft || flashTopRight || flashBottomRight || flashBottomLeft || flashMenu) {
            ticker += 1;
            if (ticker >  flashCount) {
                flashControls = !flashControls;
                ticker = 0;
            }
        }

        // LetterBoxes
        if (letterboxing) {
            currentHeight += deltaHeight;
            if (currentHeight > maxHeight) { currentHeight = maxHeight; }
        } else {
            currentHeight -= deltaHeight;
            if (currentHeight < 0) { currentHeight = 0; }
        }

        // Fading
        if (fadeActive) {
            fadeAlpha += fadeDelta;

            if (fadeAlpha > 1f) {
                fadeAlpha = 1f;
                fadeDelta = 0f;
            }
            if (fadeAlpha < 0f) {
                fadeAlpha = 0f;
                fadeDelta = 0f;
                fadeActive = false;
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Enable alpha
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        // Letterboxes
        if (currentHeight != 0) {
            shapeRenderer.setColor(0,0,0,1);
            shapeRenderer.rect(0, viewHeight - currentHeight, viewWidth, currentHeight);
            shapeRenderer.rect(0, 0, viewWidth, currentHeight);
        }

        // Flash Controls
        if (flashControls) {
//            shapeRenderer.setColor(1,0,0,1);
            shapeRenderer.setColor(Color.GRAY);
            if (flashTopLeft) {
                shapeRenderer.rect(0, viewHeight * 0.5f,
                        viewWidth / 3, viewHeight * 0.5f);
            }
            if (flashTopRight) {
                shapeRenderer.rect((viewWidth / 3) * 2f, viewHeight * 0.5f,
                        viewWidth / 3, viewHeight * 0.5f);
            }
            if (flashBottomRight) {
                shapeRenderer.rect(viewWidth * 0.5f, 0,
                        viewWidth * 0.5f, viewHeight * 0.5f);;
            }
            if (flashBottomLeft) {
                shapeRenderer.rect(0, 0,
                        viewWidth * 0.5f, viewHeight * 0.5f);
            }
            if (flashMenu) {
                shapeRenderer.rect(viewWidth / 3, viewHeight * 0.5f,
                        viewWidth / 3, viewHeight * 0.5f);
            }
        }

        // Controls
        if(HUDAnimateControls) {
            //shapeRenderer.setColor(controlsFillColour);
            if (hudFillTopLeft) {
                shapeRenderer.rect(0, viewHeight * 0.5f,
                        viewWidth / 3, viewHeight * 0.5f,
                        controlsFillColour, controlsNoColour, controlsFillColour, controlsFillColour);
            }
            if (hudFillTopMIddle) {
                shapeRenderer.rect(viewWidth / 3, viewHeight * 0.5f,
                        viewWidth / 3, viewHeight * 0.5f,
                        controlsNoColour, controlsNoColour, controlsFillColour, controlsFillColour);
            }
            if (hudFillTopRIght) {
                shapeRenderer.rect((viewWidth / 3) * 2f, viewHeight * 0.5f,
                        viewWidth / 3, viewHeight * 0.5f,
                        controlsNoColour, controlsFillColour, controlsFillColour, controlsFillColour);
            }
            if (hudFillBottomLeft) {
                shapeRenderer.rect(0, 0,
                        viewWidth * 0.5f, viewHeight * 0.5f,
                        controlsFillColour, controlsFillColour, controlsNoColour, controlsFillColour);
            }
            if (hudFillBottomRight) {
                shapeRenderer.rect(viewWidth * 0.5f, 0,
                        viewWidth * 0.5f, viewHeight * 0.5f,
                        controlsFillColour, controlsFillColour, controlsFillColour, controlsNoColour);
                ;
            }
        }

        // Fading
        if (fadeActive) {
            if (blackFade) {
                shapeRenderer.setColor(0, 0, 0, fadeAlpha);
            } else {
                shapeRenderer.setColor(1, 1, 1, fadeAlpha);
            }
            shapeRenderer.rect(0, 0, viewWidth, viewHeight);
        }

        shapeRenderer.end();
        batch.begin();
    }
}
