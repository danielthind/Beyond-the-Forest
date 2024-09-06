package com.coffeepizza.beyondtheforest.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class OSTManager {

    // System
    private boolean debugging = false;
    private String tag = "OSTManager";
    private String message = "";

    // Music
    private Music music;

    // Tracks
    public enum tracks {
        NONE,
        MAIN_THEME,
        MOUNTAINS,
        CAVE
    }
    private tracks track;

    // Track states
    private float mainThemePosition = 0f;
    private float mountainsPosition = 0f;
    private float cavePosition = 0f;

    // General States
    private static final float volumeLimit = 0.6f;
    private float volumeStep = 0.005f;

    public OSTManager() {
        track = tracks.NONE;
    }

    public void update(float dt) {
        // Return volume to limit
        float vol = music.getVolume();
        if (vol < volumeLimit) {
            music.setVolume(vol + volumeStep);
        }
    }

    public void updateTrack(tracks newTrack, Music musicToStream, boolean resetPosition) {
        if (track != newTrack) {
            // Save state of last music
            if (music != null) { saveMusicState(track); }
            track = newTrack;

            // Dispose and load new music
            if (music != null) {
                music.stop();
                music.dispose();
            }
            music = musicToStream;
            updateTrackPosition(resetPosition);
            music.play();
            music.setLooping(true);
            music.setVolume(0.0f);
        }
    }

    private void saveMusicState(tracks track) {
        if (debugging) {
            printPositions("SAVE");
//            Gdx.app.log(tag, "Saving music state." +
//                    "\ntrack: " + track +
//                    "\nPosition = " + music.getPosition());
            Gdx.app.log(tag, "track saving: " + track);
        }

        float position = music.getPosition();

        switch (track) {
            case MAIN_THEME:
                mainThemePosition = position;
                break;
            case MOUNTAINS:
                mountainsPosition = position;
                break;
            case CAVE:
                cavePosition = position;
                break;
        }
    }

    private void updateTrackPosition(boolean resetPosition) {
        if (debugging) {
            printPositions("UPDATE");
//            Gdx.app.log(tag, "Updating music state." +
//                    "\nresetPosition: " + resetPosition +
//                    "\nPosition = " + music.getPosition());
            Gdx.app.log(tag, "track updating: " + track);
        }

        switch (track) {
            case MAIN_THEME:
                Gdx.app.log(tag, " updating main theme for position: " + mainThemePosition);
                if (resetPosition) { mainThemePosition = 0f; }
                music.setPosition(mainThemePosition);
                break;
            case MOUNTAINS:
                if (resetPosition) { mountainsPosition = 0f; }
                music.setPosition(mountainsPosition);
                break;
            case CAVE:
                if (resetPosition) { cavePosition = 0f; }
                music.setPosition(cavePosition);
                break;
        }
    }

    private void printPositions(String string) {
        Gdx.app.log(tag, "--- " + string + " Music Positions ---" +
                "\nmainThemePosition: " + mainThemePosition +
                "\nmountainsPosition: " + mountainsPosition +
                "\ncavePosition: " + cavePosition +
                "\n--- ---"
        );
    }
}
