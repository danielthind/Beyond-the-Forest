package com.coffeepizza.beyondtheforest.sprite.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.coffeepizza.beyondtheforest.BeyondManager;
import com.coffeepizza.beyondtheforest.levelscreen.LevelScreen;
import com.coffeepizza.beyondtheforest.sprite.Actor;

import java.util.ArrayList;
import java.util.List;

public class AttackAnimator {

    // System
    private boolean debugging = false;
    private String tag = "Attack Animation";

    // Manager
    private BeyondManager manager;
    private LevelScreen screen;
    private Actor parent;

    // Time
    private float attAnimationInterval = 0.025f;
    private float elapsedTime = attAnimationInterval;

    // Attack Frames
    private static List<AttackFrame> attackAnimationList = new ArrayList<AttackFrame>();
    private float maximumDistance;

    public AttackAnimator(BeyondManager manager, LevelScreen screen, Actor parent) {
        this.manager = manager;
        this.screen = screen;
        this.parent = parent;

        int pixels = 8;
        maximumDistance = (float) (Math.sqrt(((pixels^2)*2))) / BeyondManager.PPM;
    }

    public void update(float dt, TextureRegion region, Vector2 position) {
        // New frame check
        if (parent.attacking) {
            if (elapsedTime < 0) {
                // Create new frame
                attackAnimationList.add(new AttackFrame(parent, new TextureRegion(region), position));

                // Reset elapsedTime
                elapsedTime = attAnimationInterval;
            } else {
                elapsedTime -= dt;
            }
        } else {
            elapsedTime = attAnimationInterval;
        }

        // Clear old frames
        List<AttackFrame> expiredFrames = new ArrayList<AttackFrame>();
        for (AttackFrame frame : attackAnimationList) {
            // Update time on frames
            frame.update(dt);

            // Add expired frames to collection
            if (frame.isExpired()) {
                if (debugging) { Gdx.app.log(tag, "Expired frame found"); }
                expiredFrames.add(frame);
            }
        }
        // Remove collection from list
        attackAnimationList.removeAll(expiredFrames);
        for (AttackFrame frame : expiredFrames) {
            // Null elements
            if (debugging) { Gdx.app.log(tag, "Nulling frame"); }
            frame = null;
        }
    }

    public void draw(Batch batch) {
        Color c = batch.getColor();
        batch.setColor(0.75f, 0.6f, 1, 0.9f);

        for (int i = 0; i < attackAnimationList.size(); i++) {
            attackAnimationList.get(i).draw(batch);
        }

        batch.setColor(c);
    }

    class AttackFrame {

        // System
        private boolean debugging = false;
        private String tag = "Attack Frame";

        // Management
        private Actor actor;

        // Time
        private float expiryTime = 0.1f;

        // Position
        private Vector2 position;

        // Textures
        private TextureRegion region;
        private float width, height;

        public AttackFrame(Actor actor, TextureRegion region, Vector2 position) {
            this.actor = actor;
            this.region = new TextureRegion(region);
            this.position = new Vector2(position);

            width = region.getRegionWidth();
            height = region.getRegionHeight();
        }

        public void update(float dt) {
            expiryTime -= dt;
        }

        public boolean isExpired() {
            boolean expired = false;

            if (expiryTime < 0) { expired = true; }

            return expired;
        }

        public void draw(Batch batch) {
            if (debugging) { Gdx.app.log(tag, "Drawing attack animation frame"); }
            batch.draw(region,
                    position.x,
                    position.y,
                    width / BeyondManager.PPM,
                    height / BeyondManager.PPM
            );
        }
    }
}
