/**
 * Beyond the Forest. A game designed for mobile and inspired by the Castlevanias 1986.
 *
 * @author  IdleMouse
 * @version 1.0
 */

package com.coffeepizza.beyondtheforest;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.coffeepizza.beyondtheforest.interfaces.Service;
import com.coffeepizza.beyondtheforest.levelscreen.*;
import com.coffeepizza.beyondtheforest.mainMenu.MainMenuScreen;
import com.coffeepizza.beyondtheforest.overworld.OverworldScreen;
import com.coffeepizza.beyondtheforest.screens.*;
import com.coffeepizza.beyondtheforest.settingsscreen.InGameSettingsScreen;
import com.coffeepizza.beyondtheforest.settingsscreen.SettingsScreen;
import com.coffeepizza.beyondtheforest.util.OSTManager;

import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class BeyondManager extends Game {

	// System
	private boolean debugging = false;
	private String tag = "BeyondManager";
	private String message = "";

	// Virtual Dimensions
	public static final int V_WIDTH = 400;
	public static final int V_HEIGHT = 200;
	public static final float PPM = 100;
	public static final float UNITSCALE = 1 / PPM;

	// Screens
	private SplashScreen splashScreen;
	private LoadingScreen loadingScreen;
	private MainMenuScreen mainMenu;
	private SettingsScreen settingsMenu;
	private InGameSettingsScreen inGameSettings;
	private OverworldScreen overworld;
	private LevelScreen levelScreen;
	private PauseScreen pauseScreen;
	private CreditsScreen creditsScreen;

	// Tools
	public static SpriteBatch spriteBatch;
	public static ShapeRenderer shapeRenderer;

	// Assets
	public TextureAtlas textureAtlas;
	public Array<Texture> parallaxTextures;
	public float menuFontSizeRatio = 0.05f;
	public int menuFontSize;
//	public int gameFontSize = 20;
	public BitmapFont goldFont;

	// Screen ints
	public static final int SPLASH = 0;
	public static final int MENU = 1;
	public static final int OVERWORLD = 2;
	public static final int SETTINGS = 3;
	public static final int PAUSE = 4;
	public static final int CREDITS = 5;

	// Game constants
	public static final int GRAVITY = -10;

	// Load preferences
	public Preferences prefs;

	// Music
	public OSTManager ostManager = new OSTManager();

	// Platform config
	private Service service;
	public Random random = new Random();

	public BeyondManager(Service service) {
		this.service = service;
	}

	public BeyondManager() {}
	
	@Override
	public void create () {
		spriteBatch = new SpriteBatch();
		spriteBatch.enableBlending();
		shapeRenderer = new ShapeRenderer();

		// Get preferences
		prefs =  Gdx.app.getPreferences("Beyond Data");

		printPrefs("Game Starting");

		// Init Loading Screen
		loadingScreen = new LoadingScreen(this);
		loadNewScreen(BeyondManager.MENU);
	}

	public TextureAtlas getAtlas() {
		return textureAtlas;
	}

	/** Load through LoadingScreen */
	 	/** Do or do not clear save */
	public void loadNewScreen(int screen) {
		loadingScreen.loadNewScreen(screen);
	}
	public void setGameSaveExists() {
		prefs.putBoolean("saveFile", true);
		prefs.flush();
	}

	public void clearSave() {
		prefs.clear();
		loadingScreen.generateNewSave();
	}

//	public void loadNewScreenClearSave(int screen) {
//		loadingScreen.clearSavePrefs();
//		loadingScreen.generateNewSave();
//		loadingScreen.loadNewScreen(screen);
//	}

	/** Load a level */
	public void loadNewLevel(int level) {
		sendDebugMsg(debugging, tag, "Loading level: " + level);

		// Load level and assets
		loadingScreen.loadLevelAssets(level);

		// Create new levelscreen
		if (levelScreen != null) {

			Gdx.app.log(tag, "[WARNING] level screen previously created");

			levelScreen.dispose();
			levelScreen = null;
		}

		switch (level) {
			case 1:
				levelScreen = new LevelScreen(this, 1);
				break;
			case 2:
				levelScreen = new LevelScreen(this, 2);
				break;
			case 3:
				levelScreen = new LevelScreen(this, 3);
				break;
			case 4:
				levelScreen = new LevelScreen(this, 4);
				break;
			case 5:
				levelScreen = new LevelScreen(this, 5);
				break;
			case 6:
				levelScreen = new LevelScreen(this, 6);
				break;
			case 7:
				levelScreen = new LevelScreen(this, 7);
				break;
			case 8:
				levelScreen = new LevelScreen(this, 8);
				break;
			case 9:
				levelScreen = new LevelScreen(this, 9);
				break;
			case 10:
				levelScreen = new LevelScreen(this, 10);
				break;
			case 11:
				levelScreen = new LevelScreen(this, 11);
				break;
			case 12:
				levelScreen = new LevelScreen(this, 12);
				break;
			case 13:
				levelScreen = new LevelScreen(this, 13);
				break;
			case 14:
				levelScreen = new LevelScreen(this, 14);
				break;
			case 15:
				levelScreen = new LevelScreen(this, 15);
				break;
			case 16:
				levelScreen = new LevelScreen(this, 16);
				break;
			case 17:
				levelScreen = new LevelScreen(this, 17);
				break;
		}

		setupPlatform();
		setScreen(levelScreen);
	}
	public void restartLevelThroughOverworld(int level) {
		overworld.reloadLevel(level);
		setOverworld();
	}

	/** Change Screen */
	public void setMenuScreen() {
		// Check if we already have a menu
		if(mainMenu == null) mainMenu = new MainMenuScreen(this);

		// Check if there is already a save file
		boolean previousSave = prefs.getBoolean("saveFile", false);
		if (!previousSave) {
			loadingScreen.generateNewSave();
		}
		// Set if there should be a continue button
		if (previousSave) {
			mainMenu.showContinue = true;
		} else {
			mainMenu.showContinue = false;
		}

		// Change to mainMenu
		setScreen(mainMenu);
	}
	public void setOverworld() {
		if (debugging) { Gdx.app.log(tag, "Opening overworld"); }

		if (overworld == null) {
			overworld = new OverworldScreen(this);
		}

		overworld.resetScreen();
		setScreen(overworld);
	}
	public void setSettingsScreen() {
		if (debugging) { Gdx.app.log(tag, "Opening settings menu"); }

		if (settingsMenu == null) {
			settingsMenu = new SettingsScreen(this);
		}

		setScreen(settingsMenu);
	}
	public void setInGameSettingsScreenFromLevel(LevelScreen screen) {
		// Create a new one as the screen may have changed
		inGameSettings = new InGameSettingsScreen(this, screen);
		setScreen(inGameSettings);
	}
	public void setInGameSettingsScreenFromOW() {
		// Create a new one as the screen may have changed
		inGameSettings = new InGameSettingsScreen(this);
		setScreen(inGameSettings);
	}

	public void setPreviouslyLoadedLevel(LevelScreen screen) {
		screen.resume();
		setScreen(screen);
	}

	public void loadCreditsScreen() {
		if (creditsScreen != null) {

			Gdx.app.log(tag, "[WARNING] credits screen previously created");

			creditsScreen = null;
		}

		creditsScreen = new CreditsScreen(this);
		setScreen(creditsScreen);
	}

	/** Platform Methods */
	private void setupPlatform() {
		if (Gdx.app.getType() == Application.ApplicationType.Desktop) {

		} else if (Gdx.app.getType() == Application.ApplicationType.Android) {
			this.service.keepScreenOn(true);
		} else if (Gdx.app.getType() == Application.ApplicationType.iOS) {

		}
	}

	@Override
	public void render () {
		// Delegates render method to whichever screen is active at the time
		super.render();
	}

	@Override
	public void dispose () {
		spriteBatch.dispose();
		shapeRenderer.dispose();
	}

	public void sendDebugMsg(Boolean isDebugging, String classTag, String msg) {
		if (isDebugging) {
			Gdx.app.log(classTag, msg);
		}
	}

	public void setCurrentLevel(int level) {
		prefs.putInteger("currentLevel", level);
	}

	public void printPrefs(String string) {
		HashMap<String, ?> map = (HashMap<String, ?>) prefs.get();
		Set<String> keys = prefs.get().keySet();

		Gdx.app.log("Preferences", "---------- [" + string + "] ----------");

		for (String key : keys) {
			Gdx.app.log("Preferences", key + ": " + map.get(key));
		}
		Gdx.app.log("Preferences", "---------- ----------");
	}
}