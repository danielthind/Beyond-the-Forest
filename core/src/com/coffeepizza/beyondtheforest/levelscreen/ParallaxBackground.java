package com.coffeepizza.beyondtheforest.levelscreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.coffeepizza.beyondtheforest.BeyondManager;

public class ParallaxBackground extends Actor {

    // System
    private boolean debugging = false;
    private String tag = "ParallaxBackground";
    private String message = "";

    // Managers
    private BeyondManager manager;
    private LevelScreen screen;
    private SpriteBatch spriteBatch;
    private ShapeRenderer shapeRenderer;

    public enum theme {
        MOUNTAINS,
        CAVE,
        CREDITS
    }
    private theme mapTheme;

    private float scrollX, scrollY;
    private float speed;

    private Array<Texture> layers;
    private final int LAYER_SPEED_DIFFERENCE = 5;//0;

    private float x, y, screenWidth, screenHeight, scaleX, scaleY;
    private float srcX0, srcY0, srcX1, srcY1, srcX2, srcY2;
    private float cameraDelta;
    private int originX, originY, rotation;
    private boolean flipX,flipY;

    private float maxX;
    private float aspectRatio;

    public ParallaxBackground(Array<Texture> textures, theme mapTheme, float maxX){
        layers = textures;
        this.mapTheme = mapTheme;
        this.maxX = maxX;

        for(int i = 0; i < textures.size; i++){
            layers.get(i).setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.ClampToEdge);
        }

        // Constants
        screenWidth =  Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        srcX0 = srcY0 = srcX1 = srcY1 = srcX2 = srcY2 = 0;
        cameraDelta = 0.08f;

        // Aspect ratio
        aspectRatio = screenWidth / screenHeight;

        if (debugging) {
            Gdx.app.log(tag, "screenWidth, screenHeight:" + screenWidth + ", " + screenHeight);
            Gdx.app.log(tag, "aR: " + aspectRatio);
            Gdx.app.log(tag, "tex width, height:" + layers.get(0).getWidth() + ", " + layers.get(0).getHeight());
            Gdx.app.log(tag, "source X, Y: "
                    + (int) srcX0 + ", "
                    + (int) (screenHeight - (layers.get(0).getWidth() / aspectRatio)));
            Gdx.app.log(tag, "tex draw w, h:"
                    + layers.get(0).getWidth() + ", "
                    + (int) (layers.get(0).getWidth() / aspectRatio));
        }

        if (debugging) {
            for (int i = 0; i < layers.size; i++) {
                Gdx.app.log(tag,"parallax layer " + i + " width, height:" +
                        layers.get(i).getWidth() + ", " + layers.get(i).getHeight()
                );
            }
        }
    }

    public void update(Vector2 vec) {
        /** TODO: Make movement factors variables instead of hardcoding them */
        // X axis update
        switch (mapTheme) {
            case MOUNTAINS: case CAVE:
                // X delta
                srcX0 += vec.x * (0.005f / cameraDelta);
                srcX1 += vec.x * (0.08f / cameraDelta);
                srcX2 += vec.x * (0.8f / cameraDelta);
                break;
        }

        // Y axis update
        switch (mapTheme) {
            case MOUNTAINS:
                // Y delta
                srcY1 -= vec.y * (0.02f / cameraDelta);
                srcY2 -= vec.y * (0.2f / cameraDelta);
                break;
            case CAVE:
                // Y delta
                srcY2 -= vec.y * (0.2f / cameraDelta);
                break;
            case CREDITS:
                // Y delta
                srcY2 -= vec.y * (0.1f / cameraDelta);
                break;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * parentAlpha);

        // First layer
        switch (mapTheme) {
            case MOUNTAINS: case CAVE: case CREDITS:
                batch.draw(layers.get(0), 0, 0, 0, 0, screenWidth, screenHeight, 1, 1, 0,
                        (int) srcX0, 0,
                        layers.get(0).getWidth(), (int) (layers.get(0).getWidth() / aspectRatio),
                        false, false
                );
                break;
        }

        // Second layer
        switch (mapTheme) {
            case MOUNTAINS: case CREDITS:
                batch.draw(layers.get(1), 0, 0, 0, 0, screenWidth, screenHeight, 1, 1, 0,
                        (int) srcX1, (int) srcY1,
                        (int) layers.get(1).getWidth(), (int) (layers.get(1).getWidth() / aspectRatio),
                        false, false
                );
                break;
            case CAVE:
                /** Note - Layer 2 for cave does not have y axis shift */
                batch.draw(layers.get(1), 0, 0, 0, 0, screenWidth, screenHeight, 1, 1, 0,
                        (int) srcX1, 0,
                        (int) layers.get(1).getWidth(), (int) (layers.get(1).getWidth() / aspectRatio),
                        false, false
                );
        }

        // Third layer
        switch (mapTheme) {
            case MOUNTAINS: case CAVE: case CREDITS:
                batch.draw(layers.get(2), 0, 0, 0, 0, screenWidth, screenHeight, 1, 1, 0,
                        (int) srcX2, (int) srcY2,
                        layers.get(2).getWidth(), (int) (layers.get(2).getWidth() / aspectRatio),
                        false, false
                );
                break;
        }

    }

    public void resize(int width, int height) {
        this.screenWidth =  width;
        this.screenHeight = height;
        this.setSize(width, height);
    }
}
