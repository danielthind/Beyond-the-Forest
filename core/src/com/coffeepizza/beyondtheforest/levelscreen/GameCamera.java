package com.coffeepizza.beyondtheforest.levelscreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.coffeepizza.beyondtheforest.BeyondManager;
import com.coffeepizza.beyondtheforest.levelscreen.LevelScreen;
import com.coffeepizza.beyondtheforest.levelscreen.util.CameraShakeTimer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.math.MathUtils.random;

public class GameCamera extends OrthographicCamera {

    // System
    private boolean debugging = false;
    private String tag = "GameCamera";
    private String message = "";

    // Screen
    private LevelScreen screen;

    // Camera
    public OrthographicCamera gameCam;
    private Vector2 targetPosition;
    public Vector2 lastPosition;
    private BigDecimal bd;

    // Offset
    public enum POSITION_OFFSET {
        OFFSET_NEGATIVE_NORMAL(-0.05f),
        OFFSET_NONE(0.0f),
        OFFSET_NORMAL(0.1f);

        private final float value;

        POSITION_OFFSET(final float newValue) {
            value = newValue;
        }

        public float getValue() { return value; }
    }
    private float yOffset = POSITION_OFFSET.OFFSET_NORMAL.getValue();

    // Debug
    private float debugFollowSpeed = 1f;
    private float debugZoomRate = 0.1f;

    // States
    private enum camMode {
        FOLLOW,
        SNAP,
        SHAKE_FOLLOW,
        SHAKE_SNAP
    }

    private camMode mode;

    public enum camTargets {
        PLAYER,
        BOSS,
        GOLEM,
        CARROT
    }
    private camTargets target;

    // Shake
    private boolean shake;
    private float cameraShakeMagnitude = 0;
    public enum CAMERA_SHAKE {
        HEAVY(10.0f / BeyondManager.PPM),
        MEDIUM(5.0f / BeyondManager.PPM),
        LIGHT(2.5f / BeyondManager.PPM),
        EXTRA_LIGHT(1.0f / BeyondManager.PPM);

        private final float value;

        CAMERA_SHAKE(final float newValue) { value = newValue; }

        public float getValue() { return value; }
    }

    private static List<CameraShakeTimer> timerList = new ArrayList<CameraShakeTimer>();
    private boolean timerComplete = false;

    // Zoom
        // Distance
    public enum ZOOM_DISTANCE {
        EXTRA_FAR(4f),
        FAR(2f),
        NORMAL(1f),
        NEAR(0.5f),
        CLOSEUP(0.25f);

        private final float value;

        ZOOM_DISTANCE(final float newValue) {
            value = newValue;
        }

        public float getValue() { return value; }
    }
    private float targetZoom;                           // Zoom level
        // Rate
    public enum ZOOM_RATE {
        SLOW(0.001f),
        NORMAL(0.005f),
        FAST(0.01f);

        private final float value;

        ZOOM_RATE(final float newValue) {
            value = newValue;
        }

        public float getValue() { return value; }
    }
    private float zoomRate;

    // Lerp
    public enum LERP_ALPHA {
        LERP_INSTANT(1f),
        LERP_NORMAL(0.5f),
        LERP_SLOW(0.005f),
        LERP_SLOWEST(0.0025f);

        private final float value;

        LERP_ALPHA(final float newValue) { value = newValue; }

        public float getValue() { return value; }

    }
    private float lerpAlpha;

    // Port
    public Viewport gamePort;

    public GameCamera(LevelScreen screen) {
        this.screen = screen;

        // Camera and Port
        gameCam = new OrthographicCamera();
        gameCam.setToOrtho(false,
                viewportWidth / BeyondManager.UNITSCALE,
                viewportHeight / BeyondManager.UNITSCALE);
        gamePort = new FitViewport(BeyondManager.V_WIDTH / BeyondManager.PPM,
                BeyondManager.V_HEIGHT / BeyondManager.PPM, gameCam);

        // States
            // Targetting
        mode = camMode.FOLLOW;
        target = camTargets.PLAYER;
            // Zoom
        targetZoom = ZOOM_DISTANCE.NORMAL.getValue();
        gameCam.zoom = targetZoom;
        zoomRate = ZOOM_RATE.NORMAL.getValue();
            // Shake
        shake = false;
        cameraShakeMagnitude = 0;
            // Lerp
        setLerpAlpha(LERP_ALPHA.LERP_NORMAL);

        lastPosition = new Vector2(gameCam.position.x, gameCam.position.y);
    }


    /** Position */
        // Setting
    public void setPosition(Vector2 v) {
        gameCam.position.set(v.x, v.y + (yOffset * gameCam.zoom), 0);
    }
    public void setPositionAndTargeting(Vector2 v) {
        setPosition(v);
        targetPosition = new Vector2(v.x, v.y + (yOffset * gameCam.zoom));
    }
        // Delta (last and current(
    public Vector2 deltaPosition() {
        Vector2 vec = new Vector2(gameCam.position.x - lastPosition.x, gameCam.position.y - lastPosition.y);
        return  vec;
    }
        // Lerp
    private void lerpPosition(Vector2 v) {
        //gameCam.position.lerp(new Vector3(v.x, v.y, 0), lerpAlpha);


        Vector3 beginPosition = new Vector3(gameCam.position.cpy());
        gameCam.position.set(beginPosition.lerp(new Vector3(v.x, v.y, 0), lerpAlpha));

        //Gdx.app.log(tag, "lerpPosition: " + v + "\t\t, after lerp: " + gameCam.position);
    }
    public void setLerpAlpha(LERP_ALPHA alpha) {
        lerpAlpha = alpha.getValue();
    }
    public void setOffset(POSITION_OFFSET offset) {
        yOffset = offset.getValue();
    }

    public void update(float dt) {
        lastPosition = new Vector2(gameCam.position.x, gameCam.position.y);

        // Update
        targetting();
        updateZoom();

        // Timers
        for (CameraShakeTimer timer : timerList) {
            timer.update(dt);
        }
        if (timerComplete) { clearTimerList(); }

//        Gdx.app.log(tag, "camera xpos: " + gameCam.position.x);

        // Push update
        snapCameraToPixels();
        gameCam.update();

        if (debugging) {
            debugControls();
            //logDebugInfo();
        }
    }

    /** Targetting */
    private void targetting() {
        // Find targets position
        Vector2 vec = new Vector2(getTargetPosition(target));
        targetPosition.set(vec.x, vec.y + (yOffset * gameCam.zoom));

        // Lerp to target position
        lerpPosition(targetPosition);

        //Gdx.app.log(tag, "target posiiton: " + targetPosition + ", camera position: " + gameCam.position);

        // If we are then shake
        if (shake) {
            setPosition(shakeVector2(
                    new Vector2(gameCam.position.x, gameCam.position.y)
            ));
        }
    }
    private Vector2 getTargetPosition(camTargets target) {
        Vector2 vec = new Vector2();

        switch (target) {
            case PLAYER:
                vec.set(screen.player.getPosition());
                break;
            case BOSS:
                vec.set(screen.getBossPosition());
                break;
            case GOLEM:
                vec.set(screen.getGolemPoition());
                break;
            case CARROT:
                vec.set(screen.carrot.getPosition());
                break;
        }

        return vec;
    }
    public void setTargetAndPosition(camTargets target) {
        this.target = target;
        Vector2 vec = new Vector2(getTargetPosition(target));
        setPosition(vec);
    }
    public void setTargetting(camTargets target) {
        this.target = target;
        //Gdx.app.log(tag, "new target is: " + this.target);
    }
    private void snapCameraToPixels() {
        bd = new BigDecimal(Float.toString(gameCam.zoom));
        bd = bd.setScale(3, RoundingMode.HALF_UP);
        gameCam.zoom = bd.floatValue();

        bd = new BigDecimal(Float.toString(gameCam.position.x));
        bd = bd.setScale(3, RoundingMode.HALF_UP);
        float xx = bd.floatValue();

        bd = new BigDecimal(Float.toString(gameCam.position.y));
        bd = bd.setScale(3, RoundingMode.HALF_UP);
        float yy = bd.floatValue();

        setPosition(new Vector2(xx, yy));
    }

    /** Zoom */
        // Distance
    private void resetZoom() {
        targetZoom = ZOOM_DISTANCE.NORMAL.getValue();
    }
    public void setZoom(ZOOM_DISTANCE zoom) {
        gameCam.zoom = zoom.getValue();
        targetZoom = zoom.getValue();
    }
    public void setZoomTarget(ZOOM_DISTANCE target) {
        targetZoom = target.getValue();
    }
        // Rate
    public void resetZoomRate() { zoomRate = ZOOM_RATE.NORMAL.getValue(); }
    public void setZoomRate(ZOOM_RATE rate) { zoomRate = rate.getValue(); }
        // Update
    private void updateZoom() {
        // Snap to target zoom
        if (Math.abs(gameCam.zoom - targetZoom) < 1.5 * zoomRate) {
            gameCam.zoom = targetZoom;
        }

        // Update zoom towards target
        if (gameCam.zoom > targetZoom) {
            gameCam.zoom -= zoomRate;
        } else if (gameCam.zoom < targetZoom) {
            gameCam.zoom += zoomRate;
        }
    }

    /** Shake */
    private Vector2 shakeVector2(Vector2 v) {
        Vector2 shake = new Vector2(
                v.x = v.x
                        + ((((random.nextFloat() * 2) - 1) * cameraShakeMagnitude) * gameCam.zoom),
                v.y = v.y
                        + ((((random.nextFloat() * 2) - 1) * cameraShakeMagnitude) * gameCam.zoom)
        );

        return shake;
    }
    private void clearTimerList() {
        timerComplete = false;
        for (CameraShakeTimer timer : timerList) {
            /** TODO: Work out how to properly delete an object */
            timer = null;
        }
        timerList.clear();

    }
    public void shakeCamera(float duration, CAMERA_SHAKE magnitude) {
        shake = true;
        cameraShakeMagnitude = magnitude.getValue();
        timerList.add(new CameraShakeTimer(this, duration));
    }
    public void shakeComplete() {
        shake = false;
        cameraShakeMagnitude = 0;
        timerComplete = true;
    }

    /** Debugging */
        // Controlls
    private void debugControls() {
        Gdx.app.log(tag, "Camera debug controls on");

        // Zoom
        if(Gdx.input.isKeyPressed(Input.Keys.P)) {
            resetZoom();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.U)) {
            gameCam.zoom += debugZoomRate;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.O)) {
            gameCam.zoom -= debugZoomRate;
        }

        // Pan
        if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            Vector2 vec = new Vector2(gameCam.position.x, gameCam.position.y + debugFollowSpeed);
            setPosition(vec);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.Q)) {
            //Gdx.app.log(tag, "debug camera moving left");
            Vector2 vec = new Vector2(gameCam.position.x - debugFollowSpeed, gameCam.position.y);
            setPosition(vec);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.E)) {
            Vector2 vec = new Vector2(gameCam.position.x, gameCam.position.y - debugFollowSpeed);
            setPosition(vec);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.R)) {
            //Gdx.app.log(tag, "debug camera moving right");
            Vector2 vec = new Vector2(gameCam.position.x + debugFollowSpeed, gameCam.position.y);
            setPosition(vec);
        }
    }
        // Logging
    private void logDebugInfo() {
        Gdx.app.log(tag, "Camera zoom: " + gameCam.zoom);
    }
}
