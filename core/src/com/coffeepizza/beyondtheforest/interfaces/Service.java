package com.coffeepizza.beyondtheforest.interfaces;

/** Platform Specific Methods */
public interface Service {
    // Based on:
    // https://stackoverflow.com/questions/44149979/keep-android-screen-from-going-to-sleep-on-specific-screen-libgdx

    void keepScreenOn(boolean isOn);

}