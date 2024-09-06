package com.coffeepizza.beyondtheforest.levelscreen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;//Box2DDebugRenderer;
//import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.coffeepizza.beyondtheforest.BeyondManager;
import com.coffeepizza.beyondtheforest.levelscreen.util.HUD;
import com.coffeepizza.beyondtheforest.sprite.levelscreen.bosses.AIDirectors.Cyberdyne;
import com.coffeepizza.beyondtheforest.sprite.levelscreen.bosses.AIDirectors.Sdyne;
import com.coffeepizza.beyondtheforest.sprite.levelscreen.bosses.FlameAttack;
import com.coffeepizza.beyondtheforest.sprite.levelscreen.npcs.Golem;
import com.coffeepizza.beyondtheforest.util.OSTManager;
import com.coffeepizza.beyondtheforest.util.controllers.GestureInputHandler;
import com.coffeepizza.beyondtheforest.levelscreen.levels.*;
import com.coffeepizza.beyondtheforest.sprite.Actor;
import com.coffeepizza.beyondtheforest.sprite.Carrot;
import com.coffeepizza.beyondtheforest.sprite.levelscreen.player.Player;
import com.coffeepizza.beyondtheforest.util.B2WorldCreator;
import com.coffeepizza.beyondtheforest.util.WorldContactListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LevelScreen implements Screen {

    // System
    private boolean debugging = false;
    private String tag = "Level Screen";
    private String message = "";

    // Manager
    private BeyondManager manager;
    private Preferences prefs;
    public LevelScript scriptManager;
    private SpriteBatch spriteBatch;
    public ShapeRenderer shapeRenderer;
    private OSTManager ostManager;
    public Vector2 p1 = new Vector2(), p2 = new Vector2(), p3 = new Vector2(), p4 = new Vector2();
    public Vector2 p5 = new Vector2(), p6 = new Vector2();

    // Input
    private InputMultiplexer multiplexer;
    private GestureDetector gestureHandler;
    private float screenTouchX, screenTouchY;

    // Tilemap and Atlas
    private TmxMapLoader maploader;
    private TiledMap map;                                   // Background map
    private TiledMap fore;                                  // Foreground map
    private MapProperties mapProperties;
    private int tileWidth, tileHeight, mapWidth, mapHeight;
    private OrthogonalTiledMapRenderer mapRenderer;         // Background map renderer
    private OrthogonalTiledMapRenderer foreRenderer;        // Foreground map renderer

    // Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;                        // Debug renderer

    // Camera
    public GameCamera camera;
    public Carrot carrot;

    // Dimensions
    private float touchscreenInputBuffer;

    // Game Details
    private int level;
    private String levelString;
    private Vector2 startCoordinates;
    public enum GameMode {
        CUTSCENE,
        PLAY
    }
    public GameMode gameMode;

    // Actors
    public static Player player;
    private int inputLevel;
    public static Cyberdyne cyberdyne;
    private boolean bossPresent = false;
    public Rectangle golemRect;
    public static Golem golem;
    private boolean golemPresent = false;
        // General enemies
    public static List<Actor> actorList = new ArrayList<Actor>();
    public static List<Actor> bossExplosion = new ArrayList<Actor>();
    public static List<Actor> enemiesToRemove = new ArrayList<Actor>();
    public static List<Body> bodiesToRemove = new ArrayList<Body>();

    // HUD
    public HUD hud;

    // Parallax Background
    private Stage stage;
    private ParallaxBackground parallax;
    private ParallaxBackground.theme levelTheme;

    // Triggers
    public HashMap<Integer, Vector2> checkpointLocations = new HashMap<Integer, Vector2>();
    public boolean secretBone1, secretBone2, secretBone3, secretBone4;
        // Boss
    public HashMap<Integer, Vector2> bossLocationsUnsorted = new HashMap<Integer, Vector2>();
    public HashMap<Integer, Vector2> bossLineUnsorted = new HashMap<Integer, Vector2>();

    // FPS
    private FPSLogger fps = new FPSLogger();

    // Prefs
    private boolean controlsYFlipped, controlsLRInsteadOfCD, HUDVirtualControls, HUDAnimateControls;

    public LevelScreen(BeyondManager manager, int level) {
        this.manager = manager;
        this.prefs = manager.prefs;
        this.spriteBatch = manager.spriteBatch;
        this.shapeRenderer = manager.shapeRenderer;
        this.ostManager = manager.ostManager;

        /** Set level script */
        this.level = level;
        switch (this.level) {
            case 1:
                levelString = "maps/lvl_01.tmx";
                scriptManager = new Level_Zero_One(manager,this);
                levelTheme = ParallaxBackground.theme.MOUNTAINS;
                inputLevel = 0;
                break;
            case 2:
                levelString = "maps/lvl_02.tmx";
                scriptManager = new Level_Zero_Two(manager, this);
                levelTheme = ParallaxBackground.theme.MOUNTAINS;
                inputLevel = 0;
                break;
            case 3:
                levelString = "maps/lvl_03.tmx";
                scriptManager = new Level_Zero_Three(manager, this);
                levelTheme = ParallaxBackground.theme.MOUNTAINS;
                inputLevel = 0;
                break;
            case 4:
                levelString = "maps/lvl_04.tmx";
                scriptManager = new Level_Zero_Four(manager, this);
                levelTheme = ParallaxBackground.theme.MOUNTAINS;
                inputLevel = 0;
                break;
            case 5:
                levelString = "maps/lvl_05.tmx";
                scriptManager = new Level_Zero_Five(manager, this);
                levelTheme = ParallaxBackground.theme.CAVE;
                inputLevel = 0;
                break;
            case 6:
                levelString = "maps/lvl_06.tmx";
                scriptManager = new Level_Zero_Six(manager, this);
                levelTheme = ParallaxBackground.theme.CAVE;
                inputLevel = 0;
                break;
            case 7:
                levelString = "maps/lvl_07.tmx";
                scriptManager = new Level_Zero_Seven(manager, this);
                levelTheme = ParallaxBackground.theme.CAVE;
                inputLevel = 0;
                break;
            case 8:
                levelString = "maps/lvl_08.tmx";
                scriptManager = new Level_Zero_Eight(manager, this);
                levelTheme = ParallaxBackground.theme.CAVE;
                inputLevel = 1;
                break;
            case 9:
                levelString = "maps/lvl_09.tmx";
                scriptManager = new Level_Zero_Nine(manager, this);
                levelTheme = ParallaxBackground.theme.CAVE;
                inputLevel = 1;
                break;
            case 10:
                levelString = "maps/lvl_10.tmx";
                scriptManager = new Level_One_Zero(manager, this);
                levelTheme = ParallaxBackground.theme.CAVE;
                inputLevel = 1;
                break;
            case 11:
                levelString = "maps/lvl_11.tmx";
                scriptManager = new Level_One_One(manager, this);
                levelTheme = ParallaxBackground.theme.CAVE;
                inputLevel = 1;
                break;
            case 12:
                levelString = "maps/lvl_12.tmx";
                scriptManager = new Level_One_Two(manager, this);
                levelTheme = ParallaxBackground.theme.CAVE;
                inputLevel = 1;
                break;
            case 13:
                levelString = "maps/lvl_13.tmx";
                scriptManager = new Level_One_Three(manager, this);
                levelTheme = ParallaxBackground.theme.CAVE;
                inputLevel = 1;
                break;
            case 14:
                levelString = "maps/lvl_14.tmx";
                scriptManager = new Level_One_Four(manager, this);
                levelTheme = ParallaxBackground.theme.CAVE;
                inputLevel = 2;
                break;
            case 15:
                levelString = "maps/lvl_15.tmx";
                scriptManager = new Level_One_Five(manager, this);
                levelTheme = ParallaxBackground.theme.CAVE;
                inputLevel = 2;
                break;
            case 16:
                levelString = "maps/lvl_06_spider_boss.tmx";
                scriptManager = new Level_One_Six(manager, this);
                levelTheme = ParallaxBackground.theme.CAVE;
                inputLevel = 2;
                break;
            case 17:
                levelString = "maps/lvl_17.tmx";
                scriptManager = new Level_One_Seven(manager, this);
                levelTheme = ParallaxBackground.theme.CAVE;
                inputLevel = 2;
                break;
        }

        /** Tiled map */
            // Loader
        maploader = new TmxMapLoader();
        TmxMapLoader.Parameters params = new TmxMapLoader.Parameters();
        params.textureMagFilter = Texture.TextureFilter.Nearest;
        params.textureMinFilter = Texture.TextureFilter.Linear;
            // Map and Rendered
//        String s = String.valueOf(levelString);
//        Gdx.app.log(tag, levelString);
        map = maploader.load(levelString, params);

        map.getLayers().get("level_map").setVisible(true);
        map.getLayers().get("background_front").setVisible(true);
        map.getLayers().get("background_back").setVisible(true);
        map.getLayers().get("background_sky").setVisible(true);

        map.getLayers().get("foreground_front").setVisible(false);
        map.getLayers().get("foreground_back").setVisible(false);
        map.getLayers().get("enemies_pathing").setVisible(false);
        mapRenderer = new OrthogonalTiledMapRenderer(map, BeyondManager.UNITSCALE);
            // ForeGround and Renderer
        fore = maploader.load(levelString, params);

        fore.getLayers().get("foreground_front").setVisible(true);
        fore.getLayers().get("foreground_back").setVisible(true);

        fore.getLayers().get("enemies_pathing").setVisible(false);
        fore.getLayers().get("level_map").setVisible(false);
        fore.getLayers().get("background_front").setVisible(false);
        fore.getLayers().get("background_back").setVisible(false);
        fore.getLayers().get("background_sky").setVisible(false);
        foreRenderer = new OrthogonalTiledMapRenderer(fore, BeyondManager.UNITSCALE);
            // Properties
        mapProperties = map.getProperties();
        mapWidth = mapProperties.get("width", Integer.class);
        mapHeight = mapProperties.get("height", Integer.class);
        tileWidth = mapProperties.get("tilewidth", Integer.class);
        tileHeight = mapProperties.get("tileheight", Integer.class);

        if (debugging) {
            Gdx.app.log(tag, "" + mapWidth + ", " + mapHeight + ", " + tileWidth + ", " + tileHeight);
        }

        /** Dimensions */
        updateDimensions();

        /** Input */
        setupInputHandler();

        /** Box2D World */
        world = new World(new Vector2(0, BeyondManager.GRAVITY), true);
        world.setContactListener(new WorldContactListener(this, camera));
        b2dr = new Box2DDebugRenderer();

        /** Load .tmx level and create level */
        new B2WorldCreator(manager, this, world, map);       // Player start location is set here with Trigger -{checkpointNum}

        /** Actors */
            // Player
        scriptManager.setStartLocation();
        player = new Player(this.manager, world, this, startCoordinates.x, startCoordinates.y, inputLevel);

            // Boss
        if (this.level == 16) {
            cyberdyne = new Sdyne(manager, this, world, scriptManager, bossLocationsUnsorted);
            bossPresent = true;
        }

        /** HUD */
        if (this.level == 16) {
            hud = new HUD(manager, this, true);
        } else {
            hud = new HUD(manager, this, false);
        }

        /** Parallax Background */
        stage = new Stage();
        parallax = new ParallaxBackground(manager.parallaxTextures,
                levelTheme,
                (tileWidth * mapWidth) / BeyondManager.PPM);
        parallax.setSize(
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight());
        stage.addActor(parallax);

        /** Camera */
        /** TODO: Edit height to respect screen AR */
        camera = new GameCamera(this);
        camera.setPositionAndTargeting(player.getPosition());

        /** Start Conditions */
        scriptManager.checkpointSetup();

        /** Debugging */
//        if (debugging) { manager.printPrefs(tag); }

        getPrefs();
    }

    public void getPrefs() {
        controlsYFlipped = prefs.getBoolean("controlsYFlipped");
        controlsLRInsteadOfCD = prefs.getBoolean("controlsLRInsteadOfCD");
        HUDVirtualControls = prefs.getBoolean("HUDVirtualControls");
        HUDAnimateControls = prefs.getBoolean("HUDAnimateControls");

        //Gdx.app.log(tag, "Regetting prefs");
        player.getPrefs();
        hud.uIBoxes.getPrefs();
    }

    /** Initilisation */
    public void setStartCoordinates(float x, float y) {
        startCoordinates = new Vector2(x, y);
        //Gdx.app.log(tag, "start: " + startCoordinates);
    }
    public void createCarrot(float x, float y) {
        carrot = new Carrot(manager, world,this, x, y);
    }
    private void setupInputHandler() {
        multiplexer = new InputMultiplexer();

        createGestureHandler();                             // For zoom, pinch and drag
        multiplexer.addProcessor(gestureHandler);

        // Set multiplexer as input processor
        Gdx.input.setInputProcessor(multiplexer);
    }
    private void createGestureHandler() {
        gestureHandler = new GestureDetector(new GestureInputHandler() {

            @Override
            public boolean touchDown(float x, float y, int pointer, int button) {
                screenTouchX = x;
                screenTouchY = y;

                //Gdx.app.log(tag, screenTouchX + ", " + screenTouchY);

                return false;
            }

        });
    }

    private void updateDimensions() {
        // Controls
        touchscreenInputBuffer = Gdx.graphics.getHeight() / 20;
    }

    /** Controls */
    public boolean leftPressed() {
        boolean pressed = false;

        if (gameMode == GameMode.PLAY) {
            // Touch
            if (controlsLRInsteadOfCD) {
                if (controlsYFlipped) {
                    if (isBottomLeftPressed()) {
                        pressed = true;
                    }
                } else {
                    if (isTopLeftPressed()) {
                        pressed = true;
                    }
                }
            }
        }

        return pressed;
    }

    public boolean rightPressed() {
        boolean pressed = false;

        if (gameMode == GameMode.PLAY) {
            // Touch
            if (controlsLRInsteadOfCD) {
                if (controlsYFlipped) {
                    if (isBottomRightPressed()) {
                        pressed = true;
                    }
                } else {
                    if (isTopRightPressed()) {
                        pressed = true;
                    }
                }
            }
        }

        return pressed;
    }

    public boolean jumpJustPressed() {
        boolean pressed = false;

        if (gameMode == GameMode.PLAY) {
            // Touch
            if (controlsYFlipped) {
                if (isTopLeftPressed()) {
                    pressed = true;
                }
            } else {
                if (isBottomLeftPressed()) {
                    pressed = true;
                }
            }

            // Keyboard
            if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.J)) {
                    pressed = true;
                }
            }
        }

        return pressed;
    }

    public boolean attackJustPressed() {
        boolean pressed = false;

        if (gameMode == GameMode.PLAY) {
            // Touch
            if (controlsYFlipped) {
                if (isTopRightPressed()) {
                    pressed = true;
                }
            } else {
                if (isBottomRightPressed()) {
                    pressed = true;
                }
            }

            // Keyboard
            if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.K)) {
                    pressed = true;
                }
            }
        }

        return pressed;
    }

    public boolean changeDirJustPressed() {
        boolean pressed = false;

        if (gameMode == GameMode.PLAY) {
            // Touch
            if (!controlsLRInsteadOfCD) {
                if (controlsYFlipped) {
                    if (isBottomLeftPressed() || isBottomRightPressed()) {
                        pressed = true;
                    }
                } else {
                    if (isTopLeftPressed() || isTopRightPressed()) {
                        pressed = true;
                    }
                }
            }

            // Keyboard
            if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
                    pressed = true;
                }
            }
        }

        return pressed;
    }
    public boolean dialogueBoxPressed() {
        boolean pressed = false;

        // Touch
        if (isBottomLeftPressed() || isBottomRightPressed() || isTopLeftPressed() || isTopRightPressed()) {
            pressed = true;
        }

        // Keyboard
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                pressed = true;
            }
        }

        return pressed;
    }
    public boolean menuPressed() {
        boolean pressed = false;

        // Touch
        if (isTopMiddlePressed()) {
            pressed = true;
        }

        // Keyboard
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
                pressed = true;
            }
        }

        return pressed;
    }

    private boolean isTopLeftPressed() {
        boolean pressed = false;

        if (Gdx.input.justTouched() && screenTouchY > touchscreenInputBuffer && screenTouchY < (Gdx.graphics.getHeight() / 2)) {       // Top
            if (screenTouchX > touchscreenInputBuffer && screenTouchX < Gdx.graphics.getWidth() / 3) {
                //Gdx.app.log(tag, "TL, TM, TR, BL, BR: " + "true , " + isTopMiddlePressed() + ", " + isTopRightPressed() + ", " + isBottomLeftPressed() + ", " + isBottomRightPressed());
                pressed = true;
            } else {
                pressed = false;
            }
        } else {
            pressed = false;
        }

        return pressed;
    }
    private boolean isTopMiddlePressed() {
        boolean pressed = false;

        if (Gdx.input.justTouched()) {
            if (screenTouchY > touchscreenInputBuffer && screenTouchY < (Gdx.graphics.getHeight() / 2)) {       // Top
                if (screenTouchX > (Gdx.graphics.getWidth() / 3) && screenTouchX < (2 * Gdx.graphics.getWidth()) / 3) {     // Middle third
                    //Gdx.app.log(tag, "TL, TM, TR, BL, BR: " + isTopLeftPressed() + ", true, " + isTopRightPressed() + ", " + isBottomLeftPressed() + ", " + isBottomRightPressed());
                    pressed = true;
                } else {
                    pressed = false;
                }
            } else {
                pressed = false;
            }
        }

        return pressed;
    }
    private boolean isTopRightPressed() {
        boolean pressed = false;

        if (Gdx.input.justTouched() && screenTouchY > touchscreenInputBuffer && screenTouchY < (Gdx.graphics.getHeight() / 2)) {        // Top
            if (screenTouchX < Gdx.graphics.getWidth() - touchscreenInputBuffer && screenTouchX > (2 * Gdx.graphics.getWidth()) / 3) {
                //Gdx.app.log(tag, "TL, TM, TR, BL, BR: " + isTopLeftPressed() + ", " + isTopMiddlePressed() + ", true, " + isBottomLeftPressed() + ", " + isBottomRightPressed());
                pressed = true;
            } else {
                pressed = false;
            }
        } else {
            pressed = false;
        }

        return pressed;
    }
    private boolean isBottomLeftPressed() {
        boolean pressed = false;

        if (Gdx.input.justTouched() && screenTouchY > (Gdx.graphics.getHeight() / 2) && screenTouchY < Gdx.graphics.getHeight() - touchscreenInputBuffer) {     // Bottom
            if (screenTouchX < Gdx.graphics.getWidth() / 2 && screenTouchX > touchscreenInputBuffer) {        // Left
                //Gdx.app.log(tag, "TL, TM, TR, BL, BR: " + isTopLeftPressed() + ", " + isTopMiddlePressed() + ", " + isTopRightPressed() + ", true, " + isBottomRightPressed());
                pressed = true;
            } else {
                pressed = false;
            }
        } else {
            pressed = false;
        }

        return pressed;
    }
    private boolean isBottomRightPressed() {
        boolean pressed = false;

        if (Gdx.input.justTouched() && screenTouchY > (Gdx.graphics.getHeight() / 2) && screenTouchY < Gdx.graphics.getHeight() - touchscreenInputBuffer) {     // Bottom
            if (screenTouchX > Gdx.graphics.getWidth() / 2 && screenTouchX < Gdx.graphics.getWidth() - touchscreenInputBuffer) {        // Right
                //Gdx.app.log(tag, "TL, TM, TR, BL, BR: " + isTopLeftPressed() + ", " + isTopMiddlePressed() + ", " + isTopRightPressed() + ", " + isBottomLeftPressed() + ", true");
                pressed = true;
            } else {
                pressed = false;
            }
        } else {
            pressed = false;
        }

        return pressed;
    }

    /** Game Updates */
    // Secrets
    public void findSecret(int secretNumber) {
        hud.findSecret(secretNumber);
        scriptManager.saveSecret(secretNumber);
    }

    // Boss
    public Vector2 getBossPosition() {
        Vector2 vec = new Vector2(0, 0);
        if (level == 6) {
            vec = new Vector2(cyberdyne.getBossPosition());
        }
        if (level == 16) {
            vec = new Vector2(cyberdyne.getBossPosition());
        }
        return vec;
    }
    public void flameAttacke(boolean leftToRight) {
        if (bossPresent) {
            Actor flame = new FlameAttack(manager, this, world, leftToRight);
            actorList.add(flame);
        }
    }
    public void updateBossHealth() {
        hud.updateBossHealht(cyberdyne.getBossHealth());
    }

    // Golem
    public void setGolem(Actor golem) {
        this.golem = (Golem) golem;
    }
    public Vector2 getGolemPoition() {
        Vector2 vec = new Vector2(0, 0);
        if (level == 8 || level == 14) {
            vec = new Vector2(golem.getPosition());
        }
        return vec;
    }
    public void setGolemRise() {
        this.golem.golemRise();
    }

    // Player
    public Vector2 getPlayerPosition() {
        return player.getPosition();
    }

    @Override
    public void show() {

    }

    public void update (float dt) {
        // Menu
        if (menuPressed()) {
            //Gdx.app.log(tag, "PAUSING");
            this.pause();
            manager.setInGameSettingsScreenFromLevel(this);
        }

        // Camera
        camera.update(dt);

        // Remove dead enemies
        for (int i = 0; i < enemiesToRemove.size(); i++) {
            enemiesToRemove.get(i).getActorBody().setActive(false);
        }
        enemiesToRemove.clear();
        for (int i = 0; i < bodiesToRemove.size(); i++) {
            //bodiesToRemove.get(i).fi
        }
        bodiesToRemove.clear();

        // Box2D
        world.step(1/60f, 6, 2);

        // Actors
        player.update(dt);

        // Bosses
        if (bossPresent) {
            // Boss Explosions
            for (int i = 0; i < bossExplosion.size(); i++) {
                bossExplosion.get(i).update(dt);
            }

            // Boss
            cyberdyne.update(dt);
        }

        // Enemies
        for (int i = 0; i < actorList.size(); i++) {
            actorList.get(i).update(dt);
        }

        // Level
        parallax.update(camera.deltaPosition());
        hud.update(dt);
        scriptManager.update(dt);

        // Flash controls
        if (HUDAnimateControls) {
            if (isTopLeftPressed()) { hud.uIBoxes.hudFillTopLeft = true; } else { hud.uIBoxes.hudFillTopLeft = false; }
            //if (isTopMiddlePressed()) { hud.uIBoxes.hudFillTopMIddle = true; } else { hud.uIBoxes.hudFillTopMIddle = false; }
            if (isTopRightPressed()) { hud.uIBoxes.hudFillTopRIght = true; } else { hud.uIBoxes.hudFillTopRIght = false; }
            if (isBottomLeftPressed()) { hud.uIBoxes.hudFillBottomLeft = true; } else { hud.uIBoxes.hudFillBottomLeft = false; }
            if (isBottomRightPressed()) { hud.uIBoxes.hudFillBottomRight = true; } else { hud.uIBoxes.hudFillBottomRight = false; }
        }

        // Music
        ostManager.update(dt);
    }

    @Override
    public void render(float delta) {
        // Update
        update(delta);

        // FPS
        //fps.log();

        /** Move clear to level so the colour is level specific */
        Gdx.gl.glClearColor(1f / 255,1f / 255f, 1f / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Game Objects
            // Start SpriteBatch
        spriteBatch.setProjectionMatrix(camera.gameCam.combined);
        spriteBatch.begin();
            // Start ShapeRenderer
        shapeRenderer.setProjectionMatrix(camera.gameCam.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Enable alpha
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

            // Background - Parallax
        stage.draw();
        spriteBatch.end();

            // Background - TileMap
        mapRenderer.setView(camera.gameCam);
        mapRenderer.render();

        // Actors
        spriteBatch.begin();
            // Enemies
        for (int i = 0; i < actorList.size(); i++) {
            actorList.get(i).draw(spriteBatch);
        }
            // Bosses
        if (bossPresent) {
            // Fade to white

            // Boss Explosions
            for (int i = 0; i < bossExplosion.size(); i++) {
                bossExplosion.get(i).draw(spriteBatch);
            }

            // Boss
            cyberdyne.draw(spriteBatch);
        }

            // Golem
        //if (golemPresent) {
          //  golem.draw(spriteBatch);
        //}

            // Player
        player.draw(spriteBatch);

        // End SpriteBatch
        spriteBatch.end();
        // End ShapeRenderer
        shapeRenderer.end();

        // TileMap - Foreground
        foreRenderer.setView(camera.gameCam);
        foreRenderer.render();

        // HUD
        hud.draw();

        if (debugging) {
            // Shape Renderer
            shapeRenderer.setProjectionMatrix(camera.gameCam.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

            if (p1 != null && p2 != null) {
                shapeRenderer.setColor(Color.RED);
                shapeRenderer.line(p1, p2);
            }

            p1.set(player.getActorBody().getPosition());
            shapeRenderer.setColor(Color.MAGENTA);
            p3.set(p1).add(player.getActorBody().getLinearVelocity());
            shapeRenderer.line(p1, p3);


            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.line(p1, p6);

            shapeRenderer.end();

            // Box2d
            b2dr.render(world, camera.gameCam.combined);
        }
    }

    @Override
    public void resize(int width, int height) {
        // Camera
        /** TODO: Update to take aspect ratio into account? */
        camera.gamePort.update(width, height);

        parallax.resize(width, height);
        hud.resize(width, height);

        // Dimensions
        updateDimensions();
    }

    @Override
    public void pause() { }

    @Override
    public void resume() {
        Gdx.app.log(tag, "RESUMING SCREEN");

        /** Dimensions */
        updateDimensions();

        /** Input */
        screenTouchX = -1;
        screenTouchY = -1;
        setupInputHandler();

        /** Reget prefs */
        getPrefs();
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
//        manager.dispose();
        scriptManager = null;
//        spriteBatch.dispose();
//        shapeRenderer.dispose();
        p1 = null;
        p2 = null;

        multiplexer.clear();
        multiplexer = null;
        gestureHandler = null;

        maploader = null;
        map.dispose();
        fore.dispose();
        mapProperties.clear();
        mapRenderer.dispose();
        foreRenderer.dispose();

        world.dispose();
        b2dr.dispose();

        camera = null;
        carrot = null;

//        // Dimensions
//        private float touchscreenInputBuffer;
//
//        // Game Details
//        private int level;
//        private String levelString;
//        private Vector2 startCoordinates;
//        public enum GameMode {
//            CUTSCENE,
//            PLAY
//        }
//        public LevelScreen.GameMode gameMode;

        player = null;

        actorList.clear();
        bossExplosion.clear();
        enemiesToRemove.clear();
        bodiesToRemove.clear();

        hud.dispose();

        stage.dispose();
        parallax.clear();

        checkpointLocations.clear();
    }
}