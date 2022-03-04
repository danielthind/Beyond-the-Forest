package com.coffeepizza.beyondtheforest.levelscreen.util;

import com.coffeepizza.beyondtheforest.levelscreen.GameCamera;

public class CameraShakeTimer {

    // System
    private boolean debugging = false;
    private String tag = "CameraTimer";
    private String message = "";

    // Manager
    private GameCamera camera;

    // Timer
    private float duration;

    public CameraShakeTimer(GameCamera camera, float duration) {
        this.camera = camera;
        this.duration = duration;
    }

    private void checkTimerEnded() {
        if (duration < 0) {
            camera.shakeComplete();
        }
    }

    public void update(float dt) {
        duration -= dt;

        checkTimerEnded();
    }
 }
