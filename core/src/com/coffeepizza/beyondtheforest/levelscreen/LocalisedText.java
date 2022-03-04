package com.coffeepizza.beyondtheforest.levelscreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.Vector2;
import com.coffeepizza.beyondtheforest.BeyondManager;

import java.util.HashMap;
import java.util.concurrent.TimeoutException;

public class LocalisedText {

    // System
    private boolean debugging = false;
    private String tag = "LocalisedText";
    private String message = "";

    // Manager and Save
    private BeyondManager manager;
    private LevelScreen screen;
    private Preferences prefs;

    // Language Settings
    private String currentLanguage;
        // 0 - English
        // 1 - Spanish
    private int level;
    private HashMap<Integer, String> text;

    public LocalisedText(BeyondManager manager, LevelScreen screen) {
        this.manager = manager;
        this.screen = screen;
        this.level = level;
        this.prefs = manager.prefs;

        currentLanguage = this.prefs.getString("Language");
        if (debugging) {  Gdx.app.log(tag, "Current language: " + currentLanguage); }
        text =  new HashMap<Integer, String>();
    }

    public void setLevelAndInitTexts(int l) {
        this.level = l;

        // Clear text and populate
        text.clear();
        initGeneralText();

        switch (level) {
            case 1:
                initLevelOneText();
                break;
            case 2:
                initLevelTwoText();
                break;
            case 3:
                initLevelThreeText();
                break;
            case 4:
                initLevelFourText();
                break;
            case 5:
                initLevelFiveText();
                break;
            case 6:
                initLevelSixText();
                break;
            case 7:
                initLevelSevenText();
                break;
            case 8:
                initLevelEightText();
                break;
            case 9:
                initLevelNineText();
                break;
            case 10:
                initLevelTenText();
                break;
            case 11:
                initLevelElevenText();
                break;
            case 12:
                initLevelTwelveText();
                break;
            case 13:
                initLevelThirteenText();
                break;
            case 14:
                initLevelFourteenText();
                break;
            case 15:
                initLevelFifteenText();
                break;
            case 16:
                initLevelSixteenText();
                break;
            case 17:
                initLevelSeventeenText();
        }
    }

    private void initGeneralText() {
        if (currentLanguage.equals("ENG")) {
            if (debugging) { Gdx.app.log(tag, "Adding English Text"); }

            // Narrator
            text.put(-100, "TAP TO START");
        } else if (currentLanguage.equals("SPN")) {
            if (debugging) { Gdx.app.log(tag, "Adding Spanish Text"); }

            // Narrator

        }
    }

    private void initLevelOneText() {
        if (currentLanguage.equals("ENG")) {
            // Intro Cutscene
                // Deku
            text.put(1, "DEEP IN THE FOREST\n\n" +
                    "LONG HAVE I SERVED\n\n" +
                    "AS THE GUARDIAN SPIRIT");
            text.put(2, "BUT NOW MURKY SHADOWS POISON ME\n\n" +
                    "AND VILE BEASTS\n\n" +
                    "DESCEND UPON OUR WILDS");
            text.put(3, "WITH DARKNESS CLOUDING MY VISION\n\n" +
                    "I AM UNABLE TO PROTECT THESE LANDS");
            text.put(4, "THE TIME HAS COME FOR YOU\n\n" +
                    "MY CHILD TO TEST YOUR RESOLVE\n\n" +
                    "AND TAKE MY PLACE");
            text.put(5, "YOUNG ONE...\n\n" +
                    //"THIS IS NO DREAM\n\n" +
                    "OUR FOREST NEEDS YOU");
            text.put(6, "WAKE UP!");
            text.put(7, "HEAD DOWN TO THE VALLEY\n\n" +
                    "TAKE THE PATH THROUGH THE CAVES\n\n" +
                    "I WAIT FOR YOU THERE\n\n");

            // Tutorial
                // Narrator
            text.put(50, "TO CHANGE DIRECTION ATTACK\n\n" +
                    "TAP EITHER OF THE FLASHING PARTS\n\n" +
                    "OF THE SCREEN");
            text.put(51, "TO JUMP\n\n" +
                    "TAP THIS FLASHING CORNER\n\n" +
                    "OF THE SCREEN");
            text.put(52, "TO DASH ATTACK\n\n" +
                    "TAP THIS FLASHING CORNER\n\n" +
                    "OF THE SCREEN");
            text.put(53, "CONTROLS AND MORE\n\n" +
                    "CAN BE CHANGED\n\n" +
                    "FROM THE SETTINGS MENU");
        } else if (currentLanguage.equals("SPN")) {

        }
    }
    private void initLevelTwoText() {

    }
    private void initLevelThreeText() {

    }
    private void initLevelFourText() {

    }
    private void initLevelFiveText() {
        if (currentLanguage.equals("ENG")) {
            // Intro Cutscene
                // Deku
            text.put(1, "AH MY CHILD YOU\n\n" +
                    "DRAW CLOSER TO ME");
            text.put(2, "BUT STAY ALERT\n\n" +
                    "LONG HAS PAST THE TIME WHEN THESE\n\n" +
                    "CAVES WERE SAFE TO TRAVEL");
            text.put(3, "BE CAUTIOUS AS\n\n" +
                    "NEW DANGERS LAY AHEAD");
            text.put(4, "I WAIT FOR YOU AHEAD");

        } else if (currentLanguage.equals("SPN")) {

        }
    }
    private void initLevelSixText() {

    }
    private void initLevelSevenText() {

    }
    private void initLevelEightText() {
        if (currentLanguage.equals("ENG")) {
            // Intro cutscene
                // Deku
            text.put(1, "AAHHHHH!");
            text.put(2, "ONCE AGAIN I RISE!");
            text.put(3, "...");
            text.put(4, "WELCOME CHILD");
            text.put(5, "YOU FIND ME HERE\n\n" +
                    "FRAIL AND OLD");
            text.put(6, "NO LONGER FIT TO SERVE\n\n" +
                    "AS THE GUARDIAN\n\n" +
                    "OF THESE HERE LANDS");
            text.put(7, "TRUTH BE TOLD\n\n" +
                    "I AM FAILING THE FOREST\n\n" +
                    "FAILING MY DUTIES");
            text.put(8, "THIS FOREST, THESE LANDS\n\n" +
                    "WERE ONCE LUSH AND GREEN");
            text.put(9, "GREAT AND MIGHTY TRESS\n\n" +
                    "COVERED BOTH THE HILLS AND VALLEYS\n\n" +
                    "EVERY CREATURE LIVED IN PEACE");
            text.put(10, "PEACE NO LONGER");
            text.put(11, "THOSE BEASTS YOU ENCOUNTERED\n\n" +
                    "ON YOUR JOURNEY HERE\n\n" +
                    "THEY PILLAGE AND DESTROY");
            text.put(12, "I CAN NO LONGER FEND THEM OFF\n\n" +
                    "MY STRENGTH IS LONG GONE");
            text.put(13, "BUT YOU MY CHILD");
            text.put(14, "YOU MAY JUST BE READY\n\n" +
                    "TO TAKE MY PLACE\n\n" +
                    "AND TAKE THE FIGHT TO THEM");
            text.put(15, "READY TO TAKE THE FIGHT TO HIM...");
            text.put(16, "FOR NOW GO FORWARD\n\n" +
                    "SO I CAN TEACH YOU\n\n" +
                    "NEW SKILLS TO FACE THE PATH AHEAD");

            // Tutorial
            text.put(52, "TO ATTACK DIVE\n\n" +
                    "FIRST JUMP, THEN TAP ATTACK"
            );
            text.put(62, "TO LONG JUMP\n\n" +
                    "FIRST ATTACK DASH, THEN TAP JUMP"
            );

            // Extro cutscene
        } else if (currentLanguage.equals("SPN")) {
            text.put(1, "AAHHHHH!");
        }
    }
    private void initLevelNineText() {

    }
    private void initLevelTenText() {

    }
    private void initLevelElevenText() {

    }
    private void initLevelTwelveText() {

    }
    private void initLevelThirteenText() {

    }
    private void initLevelFourteenText() {
        if (currentLanguage.equals("ENG")) {
            // Intro cutscene
                // Deku
            text.put(1, "AHH MY CHILD\n\n" +
                    "YOU FIND ME ONCE AGAIN");
            text.put(2, "YOUR JOURNEY IS CLOSE\n\n" +
                    "TO ITS CONCLUSION");
            text.put(3, "ABOVE US NOW\n\n" +
                    "IS HIS CORRUPTED AND VILE CASTLE");
            text.put(4, "VENTURE UP AND\n\n" +
                    "BRING THIS TO AN END");
            text.put(5, "BUT BEFORE YOU DO\n\n" +
                    "GO FORTH SO YOU CAN LEARN\n\n" +
                    "THE LAST FEW SKILLS YOU WILL NEED");


            // Tutorial
            text.put(52, "TO ATTACK HOP\n\n" +
                    "FIRST ATTACK DASH,\n\n" +
                    "THEN TAP ATTACK AGAIN"
            );
            text.put(62, "TO DOUBLE JUMP\n\n" +
                    "FIRST JUMP, THEN TAP JUMP AGAIN"
            );

            // Extro cutscene
        } else if (currentLanguage.equals("SPN")) {
            text.put(1, "AAHHHHH!");
        }
    }
    private void initLevelFifteenText() {
        if (currentLanguage.equals("ENG")) {
            // Intro Cutscene
                // VILLIAN
            text.put(1, "WHO ENTERS!");
            text.put(2, "NO ONES HOME!\n\n" +
                    "NOW SCRAM AND DONT COME BACK!");

        } else if (currentLanguage.equals("SPN")) {

        }
    }
    private void initLevelSixteenText() {
        if (currentLanguage.equals("ENG")) {
            // Intro cutscene
                // VILLIAN
            text.put(1, "SO YOURE THE LITTLE RUNT\n\n" +
                    "THATS BEEN CAUSING ALL THIS MESS");
            text.put(2, "YOU THINK YOU CAN COME UP HERE\n\n" +
                    "AND PUT A STOP TO THIS");
            text.put(3, "NO ONE STOPS ME!");
            text.put(4, "A WIMP LIKE YOU\n\n" +
                    "COULD NEVER THROW ME OUT!");
            text.put(5, "BWA HA HA!");

            // Tutorial

            // Extro cutscene
                // VILLIAN
            text.put(50, "AHHHAHHAHHAHAHHHH!!");
                // Deku
            text.put(75, "IT IS DONE");
        } else if (currentLanguage.equals("SPN")) {
            text.put(1, "AHHHAHHAHHAHAHHHH!");
        }
    }

    private void initLevelSeventeenText() {
        if (currentLanguage.equals("ENG")) {
            /*
            Created By
            Daniel Thind @thind

            Music By
            Ashley McGuire

            Original Art Created By
            Luis Zuno @ansimuz
            Kenney.nl @KenneyNL
            Nauris Amatnieks @Namatnieks

            Special Thanks To
            Brent Aureli @BrentAureli
            Libgdx @libGDX
             */

            // Intro cutscene
            text.put(1, "Created By\n\n" +
                    "Daniel Thind @thind");
            text.put(2, "Music By\n\n" +
                    "Ashley McGuire");
            text.put(3, "Original Art Created By\n\n" +
                    "Luis Zuno @ansimuz\n\n" +
                    "Kenney.nl @KenneyNL\n\n" +
                    "Nauris Amatnieks @Namatnieks"
            );
            text.put(4, "Special Thanks To\n\n" +
                    "Brent Aureli @BrentAureli\n\n" +
                    "Libgdx @libGDX"
            );
            text.put(5, "Thank you so much\n\n" +
                    "for to playing my game"
            );

            // Tutorial

            // Extro cutscene

        } else if (currentLanguage.equals("SPN")) {

        }
    }

    public String getText(int textLine) {
        String s = "ERROR";

        if (debugging) {
            Gdx.app.log(tag, "Level: " + level + "\t\t, textLine: " + textLine);
        }

        s = text.get(textLine);

        return s;
    }
}
