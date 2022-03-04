package com.coffeepizza.beyondtheforest.overworld;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.coffeepizza.beyondtheforest.BeyondManager;

public class OWLevelButtons extends ImageButton {

    // System
    private boolean debugging = false;
    private String tag = "OWLvlButton";
    private String message = "";

    // Screen
    private OverworldScreen screen;

    // Counts
    public int level, positionCount;

    //Drawables
    private ImageButtonStyle selectableStyle, notSelectableStyle;
    private TextureRegionDrawable upSelectable, upNotSelectable, down;

    public OWLevelButtons(OverworldScreen screen, float x, float y,
                          TextureRegionDrawable upSel, TextureRegionDrawable upNotSel, TextureRegionDrawable dwn,
                          int level, int positionCount) {
        // Super and drawables
        super(upSel, dwn);
        this.screen = screen;
        this.upSelectable = upSel;
        this.upNotSelectable = upNotSel;
        this.down = dwn;
        this.level = level;
        this.positionCount = positionCount;
        selectableStyle = new ImageButtonStyle(upSelectable, down, null,
                null, null, null);
        notSelectableStyle = new ImageButtonStyle(upNotSelectable, null, null,
                null, null, null);

        // Position and Scale
        super.setPosition(
                (x - (this.getWidth() / 3)) / BeyondManager.PPM,          // Buttons are 3 tiles/ 48 pixels wide
                (y - (this.getWidth() / 3)) / BeyondManager.PPM);
        super.setTransform(true);
        super.setScale(1 / BeyondManager.PPM);


        /** Set Button Logic */
        super.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                menuButtonLogic();
            }
        });
    }

    public void setUnlockStyle() {
        super.setStyle(selectableStyle);
        //super.setChecked(true);
        super.setDisabled(false);
    }
    public void setLockStyle() {
        super.setStyle(notSelectableStyle);
        //super.setChecked(true);
        super.setDisabled(true);
    }

    /** Onclick logic */
    public void menuButtonLogic() {
        /** Prompt start level here */
        screen.removeCurrentPrompt();
        screen.createNewPrompt(level);

        // Save
        saveNewCurrentLevel();

        /** Move OverWolf */
        if (!wolfAlreadyHere()) {
            screen.wolf.move(level);
        }
    }

    public boolean wolfAlreadyHere() {
        boolean here = false;
        if (screen.wolf.getCurrentLevel() == level) {
            here = true;
        }

        return here;
    }

    /** Save Logic */
    private void saveNewCurrentLevel() {
        screen.saveNewCurrentLevel(level);
    }
}