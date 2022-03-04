package com.coffeepizza.beyondtheforest.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.utils.Array;
import com.coffeepizza.beyondtheforest.BeyondManager;
import com.coffeepizza.beyondtheforest.util.OSTManager;

public class LoadingScreen implements Screen {

    // System
    private boolean debugging = true;
    private String tag = "LoadingScreen";
    private String message = "";

    // Manager
    private BeyondManager manager;
    private Preferences prefs;
    private OSTManager ostManager;

    // Loader
    private AssetManager assetManager;
    private float loaded = 0f;
    private FileHandleResolver resolver;

    public LoadingScreen(BeyondManager parent) {
        this.manager = parent;

        // Init
        assetManager = new AssetManager();
        resolver = new InternalFileHandleResolver();
        assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

        // Load preferences
        prefs = manager.prefs;

        // Music
        ostManager = manager.ostManager;
    }

    public void loadNewScreen(int screen) {
        switch (screen) {
            case BeyondManager.SPLASH:
                break;
            case BeyondManager.MENU:
                // Load Assets
                float menuStartLoading = Gdx.graphics.getRawDeltaTime();  //getDeltaTime();

                    // Sprite sheet
                assetManager.load("assets.pack", TextureAtlas.class);

                    // Fonts
                FreetypeFontLoader.FreeTypeFontLoaderParameter goldFontParameter
                        = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
                goldFontParameter.fontFileName = "Gold Box 8x8.ttf";

                Gdx.app.log(tag, "Graphics Res: " + Gdx.graphics.getHeight() + ", " + Gdx.graphics.getWidth());
                manager.menuFontSize = (int) (Gdx.graphics.getHeight() * manager.menuFontSizeRatio);
                goldFontParameter.fontParameters.size = manager.menuFontSize;

                assetManager.load("Gold Box 8x8.ttf", BitmapFont.class, goldFontParameter);

                // Use this as a loading loop later if we need
                //loadingBar();

                /** Load assets */
                assetManager.finishLoading();

                if (debugging) {
                    /**
                     * Force completing of asset loading and check loading time
                     * TODO: Check if loading time is long enough to add loading bar
                     *      Instructions in 'Managing your assets' wiki (use assetManager.update() in render loop)
                     */

                    /** Debug Unlock levels */
                    //clearSavePrefs();
                    //generateNewSave();
                    //prefs.putBoolean("saveFile", true);
                    //prefs.putInteger("currentLevel" , 8);
                    //prefs.putInteger("unlockedLevels" , 17);

                    prefs.flush();

                    manager.printPrefs("save file loading screen");

                    // Message loading time
//                    float menuLoadingTime = Gdx.graphics.getRawDeltaTime() - menuStartLoading;
                            //getDeltaTime() - menuStartLoading;
//                    manager.sendDebugMsg(debugging, tag, "Loading took " + menuLoadingTime + "s");
                }

                // Sprite sheet
                manager.textureAtlas = assetManager.get("assets.pack", TextureAtlas.class);

                // Fonts
                manager.goldFont = assetManager.get("Gold Box 8x8.ttf", BitmapFont.class);

                // Music
                ostManager.updateTrack(OSTManager.tracks.MAIN_THEME,
                        Gdx.audio.newMusic(Gdx.files.internal("audio/OST/BeyondThForest.mp3")),
//                        assetManager.get("audio/OST/BeyondThForest.mp3", Music.class),
                        true);

                // Check save and change screen to menu
                manager.setMenuScreen();

                break;
            case BeyondManager.OVERWORLD:
                // Load Assets
                float overworldStartLoading = Gdx.graphics.getDeltaTime();

                // Music
                ostManager.updateTrack(OSTManager.tracks.MAIN_THEME,
                        Gdx.audio.newMusic(
                                Gdx.files.internal("audio/OST/BeyondThForest.mp3")),
                        false);

                if (debugging) {
                    /**
                     * Force completing of asset loading and check loading time
                     * TODO: Check if loading time is long enough to add loading bar
                     */
                    assetManager.finishLoading();
                    // Message loading time
                    float overworldLoadingTime = Gdx.graphics.getDeltaTime() - overworldStartLoading;
//                    manager.sendDebugMsg(debugging, tag, "Loading took " + overworldLoadingTime + "s");
                }

                manager.setOverworld();
                break;
            case BeyondManager.SETTINGS:
                // Music
                ostManager.updateTrack(OSTManager.tracks.MAIN_THEME,
                        Gdx.audio.newMusic(
                                Gdx.files.internal("audio/OST/BeyondThForest.mp3")),
                        false);

                if (debugging) {
                    /**
                     * Force completing of asset loading and check loading time
                     * TODO: Check if loading time is long enough to add loading bar
                     */
                    assetManager.finishLoading();
                }

                manager.setSettingsScreen();
                break;
            case BeyondManager.CREDITS:
                break;
        }
    }

    public void loadLevelAssets(int level) {
        if(debugging) { Gdx.app.log(tag, "Loading level: " + level); }
        /**
         * Steps
         * - Create new play screen or use existing?      √ using exisiting
         *      - Reset playscrene                        √ reset in loadlevel method
         * - Load level map etc..
         *      - Set in playscreen
         * - Init playscreen?
         * - Change to playscreen
         */

        boolean loaded = false;

        switch (level) {
            case 1: case 2: case 3: case 4:
                // Music
                ostManager.updateTrack(OSTManager.tracks.MOUNTAINS,
                        Gdx.audio.newMusic(Gdx.files.internal("audio/OST/Cascades.mp3")),
//                        assetManager.get("audio/OST/Cascades.mp3", Music.class),
                        false);

                // Parallax
                manager.parallaxTextures = new Array<Texture>();
                for(int i = 1; i <=3;i++) {

                    assetManager.load("maps/parallax/mountain_" + i + ".png", Texture.class);
                    assetManager.finishLoading();

                    manager.parallaxTextures.add(
                            assetManager.get("maps/parallax/mountain_" + i + ".png", Texture.class)
                    );

                    manager.parallaxTextures.get(manager.parallaxTextures.size-1)
                            .setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
                }

                loaded = true;
                if(debugging) { Gdx.app.log(tag, "Loaded level: " + level); }
                break;
            case 5: case 6: case 7: case 8: case 14:
                // Music
                ostManager.updateTrack(OSTManager.tracks.CAVE,
                        Gdx.audio.newMusic(Gdx.files.internal("audio/OST/WaterSong.mp3")),
//                        assetManager.get("audio/OST/WaterSong.mp3", Music.class),
                        false);

                // Paralax
                manager.parallaxTextures = new Array<Texture>();
                for(int i = 1; i <=3;i++) {

                    assetManager.load("maps/parallax/cave_" + i + ".png", Texture.class);
                    assetManager.finishLoading();

                    manager.parallaxTextures.add(
                            assetManager.get("maps/parallax/cave_" + i + ".png", Texture.class)
                    );

                    manager.parallaxTextures.get(manager.parallaxTextures.size-1)
                            .setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
                }
                loaded = true;

                if(debugging) { Gdx.app.log(tag, "Loaded level: " + level); }
                break;
            case 9: case 10: case 11: case 12: case 13: case 15: case 16:
                // Music
                ostManager.updateTrack(OSTManager.tracks.CAVE,
                        Gdx.audio.newMusic(Gdx.files.internal("audio/OST/WaterSong.mp3")),
//                        assetManager.get("audio/OST/WaterSong.mp3", Music.class),
                        false);

                // Paralax
                manager.parallaxTextures = new Array<Texture>();
                for(int i = 1; i <=3;i++) {

                    assetManager.load("maps/parallax/cemetery_" + i + ".png", Texture.class);
                    assetManager.finishLoading();

                    manager.parallaxTextures.add(
                            assetManager.get("maps/parallax/cemetery_" + i + ".png", Texture.class)
                    );

                    manager.parallaxTextures.get(manager.parallaxTextures.size-1)
                            .setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
                }
                loaded = true;

                if(debugging) { Gdx.app.log(tag, "Loaded level: " + level); }
                break;
            case 17:
                // Music
                ostManager.updateTrack(OSTManager.tracks.CAVE,
                        Gdx.audio.newMusic(Gdx.files.internal("audio/OST/WaterSong.mp3")),
//                        assetManager.get("audio/OST/WaterSong.mp3", Music.class),
                        false);

                // Paralax
                manager.parallaxTextures = new Array<Texture>();
                for(int i = 1; i <=3;i++) {

                    assetManager.load("maps/parallax/credits_" + i + ".png", Texture.class);
                    assetManager.finishLoading();

                    manager.parallaxTextures.add(
                            assetManager.get("maps/parallax/credits_" + i + ".png", Texture.class)
                    );

                    manager.parallaxTextures.get(manager.parallaxTextures.size-1)
                            .setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
                }
                loaded = true;

                if(debugging) { Gdx.app.log(tag, "Loaded level: " + level); }
                break;
        }

        if (loaded) {
            //manager.set
        } else {
            // Retry?
            // Go to menu?
        }
    }

    private void loadingBar() {
        /*
        loaded = assetManager.getProgress();
        while (loaded < 1.0) {
            Gdx.app.log(tag, "" + loaded);

            assetManager.update();                      // Loads assets
            loaded = assetManager.getProgress();        // Gets progress (between 0f and 1f)
            // Update progress bar
        }
        */
    }

    public void generateNewSave() {
        manager.sendDebugMsg(debugging, tag, "Generating new default save file.");

        // Save - Used to determine if the Menu should show a continue button or not
//        prefs.putBoolean("saveFile", true);
            // Triggered when clicking new game from main menu

        // Settings
        prefs.putBoolean("controlsYFlipped", false);
        prefs.putBoolean("controlsLRInsteadOfCD", false);
        prefs.putBoolean("HUDVirtualControls", true);
        prefs.putBoolean("HUDAnimateControls", true);
        prefs.putString("Language", "ENG");

        // Progress
        prefs.putInteger("currentLevel" , 1);
        prefs.putInteger("unlockedLevels" , 1);

        // Levels
        prefs.flush();
    }

    public void clearSavePrefs() {
        prefs.clear();
        prefs.flush();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
