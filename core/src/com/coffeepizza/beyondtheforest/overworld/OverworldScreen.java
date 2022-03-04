package com.coffeepizza.beyondtheforest.overworld;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.coffeepizza.beyondtheforest.BeyondManager;
import com.coffeepizza.beyondtheforest.util.OSTManager;
import com.coffeepizza.beyondtheforest.util.controllers.GestureInputHandler;
import com.coffeepizza.beyondtheforest.sprite.overworld.OverWolf;
import com.coffeepizza.beyondtheforest.util.B2WorldCreator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OverworldScreen implements Screen {

    // System
    private boolean debugging = false;
    private String tag = "Overworld";
    private String message = "";

    // Timers
    private float elapsedTime = 0;

    // Game and Handlers
    private BeyondManager manager;
    private OSTManager ostManager;
    private InputMultiplexer multiplexer;
    private GestureDetector gestureHandler;

    // Assets
    public TextureAtlas textureAtlas;

    // Map
    private TiledMap map;                                   // Background map
    private OrthogonalTiledMapRenderer mapRenderer;         // Background map renderer
    private MapProperties mapProperties;
    private int mapWidth, mapHeight, tileLength;
    private TmxMapLoader maploader;
    public HashMap<Integer, Vector2> pathPositions = new HashMap<Integer, Vector2>();

    // Camera
    public OrthographicCamera gameCam;
    private Viewport gamePort;
    private float defaultCameraZoom = 1f;
    private float cLeftLimit, cRightLimit, cTopLimit, cBottomLimit;
    //public float viewWidth, viewHeight;
    private int cameraEdgeBuffer = 10;

    // Buttons
    public Stage owStage;
    public static List<OWLevelButtons> levelButtonsList = new ArrayList<OWLevelButtons>();
    private TextureRegionDrawable lvlUpSelectable, lvlUpNotSelectable, lvlDown;

    // HUDs
    private OWMenuHUD hud;
    private OWLevelSelectHUD levelHUD;

    // Player
    public OverWolf wolf;

    /** Read from Save */
    private Preferences prefs;
    private int currentLevel;
    private int unlockedLevels;

    /* Reload Level */
    private boolean reloadLevel = false;
    private float reloadTimerDefault = 1f;
    private float reloadTimer = reloadTimerDefault;
    private int reload = 0;

    public OverworldScreen(BeyondManager game) {
        this.manager = game;
        this.ostManager = manager.ostManager;

        /** Get save data */
        prefs = manager.prefs;
//        prefs =  Gdx.app.getPreferences("Beyond Data");
        currentLevel = prefs.getInteger("currentLevel");
        unlockedLevels = prefs.getInteger("unlockedLevels");

        /** Texture atlas */
        textureAtlas = new TextureAtlas("assets.pack");

        /** Tiled map */
        loadTileMap();
        getTileMapProperties();

        /** Camera */
        createCamera();
        setCameraBoundaries();
        limitCameraZoom();
        limitCameraPosition();

        /** HUD */
        hud = new OWMenuHUD(manager, this);
        levelHUD = new OWLevelSelectHUD(manager, this);

        /** Stage and Assets */
        loadButtonDrawables();
        owStage = new Stage(gamePort);

        /**
         * World Creator
         *      - Fills pathPosition's HashMap
         *      - Fills levelSelectorList List
         *      - Create lvl buttons
         */
        new B2WorldCreator(this, map);
        if (debugging) { Gdx.app.log(tag, "pathPostions: \n" + pathPositions); }

        /** Button Configurations - Setup which are clickable and which are not */
        lockAndUnlockLevels();

        /** Player Selector */
        wolf = new OverWolf(game,this, currentLevel, getLevelPositionCount(currentLevel), getLevelPoint(currentLevel));

        /** Input handling */
        // Stage (mouse and click)
        setupInputHandler();
    }

    public void resetScreen() {
        if (debugging) { Gdx.app.log(tag, "Resetting..."); }

        /** Get new save data */
//        prefs =  Gdx.app.getPreferences("Beyond Data");
        currentLevel = prefs.getInteger("currentLevel");
        unlockedLevels = prefs.getInteger("unlockedLevels");

        // Unlock levels
        lockAndUnlockLevels();

        // Remove prompts if open
        removeCurrentPrompt();

        // Stage (mouse and click)
        setupInputHandler();

        // Move wolf to level
        wolf.reset(currentLevel, getLevelPositionCount(currentLevel), getLevelPoint(currentLevel));
    }

    public TextureAtlas getAtlas() {
        return textureAtlas;
    }

    /** TileMap Methods */
    private void loadTileMap() {
        // To reduce tearing apparently
        TmxMapLoader.Parameters params = new TmxMapLoader.Parameters();
        params.textureMagFilter = Texture.TextureFilter.Nearest;
        params.textureMinFilter = Texture.TextureFilter.Linear;

        // Load map and rendered
        maploader = new TmxMapLoader();
        map = maploader.load("coffee_pizza_overworld.tmx");

        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / BeyondManager.PPM);
    }
    private void getTileMapProperties() {
        mapProperties = map.getProperties();
        mapWidth = mapProperties.get("width", Integer.class);
        mapHeight = mapProperties.get("height", Integer.class);
        tileLength = mapProperties.get("tilewidth", Integer.class);
    }

    /** Level Methods */
    public int getLevelPositionCount(int level) {
        int count = 0;

        for(OWLevelButtons lvl : levelButtonsList) {    // Iterate through levels list
            if (level == lvl.level) {                   // Find level
                count = lvl.positionCount;              // Record positions
            }
        }

        return count;
    }
    private Vector2 getLevelPoint(int level) {
        Vector2 point = new Vector2();

        for(OWLevelButtons lvl : levelButtonsList) {    // Iterate through levels list
            if (level == lvl.level) {                   // Find level
                int i = lvl.positionCount;              // Record positions
                point.set(getPositionPoint(i, "Overworld"));           // Get positions point (x, y)
            }
        }

        return point;
    }
    public Vector2 getPositionPoint(int position, String s) {
        Vector2 vec = pathPositions.get(position);

        if (debugging) { Gdx.app.log(tag, s + ", requested position: " + position + "\t, its getPositionPoint: " + vec); }

        return vec;
    }
    public int getLevelOfThisPosition(int position) {
        /**
         *  Get positions level
         *      Otherwise return -1
         */
        int level = -1;

        for(OWLevelButtons lvl : levelButtonsList) {    // Iterate through levels list
            if (position == lvl.positionCount) {
                level = lvl.level;
            }
        }

        return level;
    }

    // Level Select Buttons
    private void loadButtonDrawables() {
        TextureRegion buttons = getAtlas().findRegion("ow_level_secetor");
        lvlUpSelectable = new TextureRegionDrawable(new TextureRegion(buttons, 0, 0, 48, 48));
        lvlDown = new TextureRegionDrawable(new TextureRegion(buttons, 48, 0, 48, 48));
        lvlUpNotSelectable = new TextureRegionDrawable(new TextureRegion(buttons, 96, 0, 48, 48));
    }
    public void createLevelButton(float x, float y, int lvlCount, int posCount) {
        OWLevelButtons owButton = new OWLevelButtons(this, x, y,
                lvlUpSelectable, lvlUpNotSelectable, lvlDown,
                lvlCount, posCount
        );

        /** TODO: Check if this should be a HashMap where the level numbers are the keys? */
        levelButtonsList.add(owButton);
        owStage.addActor(owButton);
        if (debugging) Gdx.app.log(tag, "Creating OW level button: " + lvlCount);
    }
    private void lockAndUnlockLevels() {
        for(OWLevelButtons lvl : levelButtonsList) {
            if (lvl.level <= unlockedLevels) {
                //lvl.setChecked(true);
                //lvl.setDisabled(false);
                lvl.setUnlockStyle();
            } else {
                //lvl.setChecked(true);
                //lvl.setDisabled(true);
                lvl.setLockStyle();
            }
        }
    }

    // Reset Level
    public void reloadLevel(int level) {
        reloadLevel = true;
        reload = level;
    }

    /** HUD Methods */
    public void removeCurrentPrompt() {
        levelHUD.clearPrompt();
    }
    public void createNewPrompt(int levelToPrompt) {
        levelHUD.createPrompt(levelToPrompt);

        // Disable level buttons
        for(OWLevelButtons lvl : levelButtonsList) {
            lvl.setDisabled(true);
        }
    }
    public void enableLevelButtons() {
        for(OWLevelButtons lvl : levelButtonsList) {
            lvl.setDisabled(false);
        }
    }

    /** Controller Methods */
    private void setupInputHandler() {
        multiplexer = new InputMultiplexer();

        multiplexer.addProcessor(hud.stage);                // Add HUD stage for overlay
        multiplexer.addProcessor(levelHUD.promptStage);
        multiplexer.addProcessor(levelHUD.dialogueStage);
        multiplexer.addProcessor(owStage);                  // Add overworld stage for in-game
        createGestureHandler();                             // For zoom, pinch and drag
        multiplexer.addProcessor(gestureHandler);

        // Set multiplexer as input processor
        Gdx.input.setInputProcessor(multiplexer);
    }
    private void createGestureHandler() {
        gestureHandler = new GestureDetector(new GestureInputHandler() {
            // Vectors
            private Vector2 oldInitialFirstPointer = null, oldInitialSecondPointer = null;
            private float oldScale;

            @Override
            public boolean touchDown(float x, float y, int pointer, int button) {
                x = Gdx.input.getX();
                y = Gdx.input.getY();
                //Gdx.app.log(tag, "x,y: " + x + ", " + y);
                //Vector3 temp = new Vector3();
                Vector3 touch = new Vector3();
                touch = gameCam.unproject(new Vector3 (x, y, 0));
                //Gdx.app.log(tag, "x, y: " + touch.x + ", " + touch.y);

                //clickedLevel(Gdx.input.getX(), Gdx.input.getX());
                return false;

                /*
                                        initial                 zoom out            zoom in
                touchdown x,y       -   770.0, 96.0             647.0, 250.0        628.0, 298.0            -> sensitive to camera
                unproject           -   2.1566668, 2.2858975    2.16, 2.2842946     2.168334, 2.315918      -> looks absolute
                get.input           -   768.0, 96.0             649.0, 253.0        643.0, 263.0            -> sensitive to camera

                co-ordinates        - 298m 720
                 */

            }


            @Override
            public boolean pan(float x, float y, float deltaX, float deltaY) {
                // Scale and zoom
                float scaleX = gamePort.getWorldWidth() / (float)gamePort.getScreenWidth();
                float scaleY = gamePort.getWorldHeight() / (float)gamePort.getScreenHeight();
                float currentZoom = gameCam.zoom;

                // Pan
                gameCam.translate((-deltaX * scaleX) * currentZoom, (deltaY * scaleY) * currentZoom);

                // Bound within box
                limitCameraPosition();

                gameCam.update();
                return false;
            }

            @Override
            public boolean pinch (Vector2 initialFirstPointer, Vector2 initialSecondPointer, Vector2 firstPointer, Vector2 secondPointer){
                if(!(initialFirstPointer.equals(oldInitialFirstPointer) && initialSecondPointer.equals(oldInitialSecondPointer))){
                    oldInitialFirstPointer = initialFirstPointer.cpy();
                    oldInitialSecondPointer = initialSecondPointer.cpy();
                    oldScale = gameCam.zoom;
                }

                Vector3 center = new Vector3(
                        (firstPointer.x + initialSecondPointer.x) / 2,
                        (firstPointer.y + initialSecondPointer.y) / 2,
                        0
                );
                zoomCamera(center, oldScale * initialFirstPointer.dst(initialSecondPointer) / firstPointer.dst(secondPointer));
                return true;
            }

            private void zoomCamera(Vector3 origin, float scale) {
                gameCam.update();
                Vector3 oldUnprojection = gameCam.unproject(origin.cpy()).cpy();
                gameCam.zoom = scale; //Larger value of zoom = small images, border view

                // Bind between our max (x3.5f) and min (x0.5f)
                limitCameraZoom();

                gameCam.update();
                Vector3 newUnprojection = gameCam.unproject(origin.cpy()).cpy();
                gameCam.position.add(oldUnprojection.cpy().add(newUnprojection.cpy().scl(-1f)));
            }
        });
    }
    public void handleGamePlayInput(float dt) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            //game.changeScreen(GameManager.MENU);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
            gameCam.zoom += 0.025;//0.02;
            limitCameraZoom();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.X)) {
            gameCam.zoom -= 0.025;//0.02;
            limitCameraZoom();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.C)) {
            //cameraShake(1, 2 / GameManager.PPM);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            gameCam.position.y += 0.1f;
            limitCameraPosition();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            gameCam.position.y -= 0.1f;
            limitCameraPosition();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            gameCam.position.x -= 0.1f;
            limitCameraPosition();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            gameCam.position.x += 0.1f;
            limitCameraPosition();
        }
    }

    /** Camera Methods */
    private void createCamera() {
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(BeyondManager.V_WIDTH / BeyondManager.PPM,
                BeyondManager.V_HEIGHT / BeyondManager.PPM, gameCam);
        gameCam.zoom = defaultCameraZoom;
    }
    private void setCameraBoundaries() {
        cLeftLimit = (cameraEdgeBuffer * tileLength) / BeyondManager.PPM;
        cRightLimit = ((mapWidth - cameraEdgeBuffer) * tileLength) / BeyondManager.PPM;
        cBottomLimit = cLeftLimit;
        cTopLimit = ((mapHeight - cameraEdgeBuffer) * tileLength) / BeyondManager.PPM;
    }
    private void limitCameraZoom() {
        gameCam.zoom = Math.min(defaultCameraZoom * 3.5f, Math.max(gameCam.zoom, defaultCameraZoom * 0.5f));
    }
    private void limitCameraPosition() {
        gameCam.position.x = Math.min(cRightLimit, Math.max(cLeftLimit, gameCam.position.x));
        gameCam.position.y = Math.min(cTopLimit, Math.max(cBottomLimit, gameCam.position.y));
    }

    /** Saving */
    public void saveNewCurrentLevel(int level) {
        currentLevel = level;
        prefs.putInteger("currentLevel", currentLevel);
    }

    @Override
    public void show() {
        /** Start Focus on player */
        gameCam.position.x = wolf.wX;
        gameCam.position.y = wolf.wY;
    }

    @Override
    public void render(float delta) {
        // UPDATE
        update(delta);

        /** Draw methods */
        // Clear screen
        Gdx.gl.glClearColor(0f,0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        /** Draw tilemap */
        mapRenderer.setView(gameCam);
        mapRenderer.render();

        /** Draw Level Buttons */
        owStage.draw();

        /** Sprites */
        manager.spriteBatch.setProjectionMatrix(gameCam.combined);
        manager.spriteBatch.begin();

        // Player
        wolf.draw(manager.spriteBatch);

        manager.spriteBatch.end();

        /** Debugging */
        //Draw guidelines
        if (debugging) {
            // Shape Renderer
            manager.shapeRenderer.setProjectionMatrix(gameCam.combined);
            manager.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

            // Points
            Vector2 p1;
            Vector2 p2;

            // Player Position
            /*
            gameManager.shape.setColor(Color.RED);
            p1 = new Vector2(wolf.x, 0);
            p2 = new Vector2(wolf.x, mapHeight * tileLength / GameManager.PPM);
            gameManager.shape.line(p1, p2);
            p1 = new Vector2(0, wolf.y);
            p2 = new Vector2(mapWidth * tileLength / GameManager.PPM, wolf.y);
            gameManager.shape.line(p1, p2);

            p1 = new Vector2(wolf.wX, 0);
            p2 = new Vector2(wolf.wX, mapHeight * tileLength / GameManager.PPM);
            gameManager.shape.line(p1, p2);
            p1 = new Vector2(0, wolf.wY);
            p2 = new Vector2(mapWidth * tileLength / GameManager.PPM, wolf.wY);
            gameManager.shape.line(p1, p2);
            */

            // Camera bounding box
            manager.shapeRenderer.setColor(Color.BLUE);
            // Left
            p1 = new Vector2(cLeftLimit, 0);
            p2 = new Vector2(cLeftLimit, mapHeight * tileLength / BeyondManager.PPM);
            manager.shapeRenderer.line(p1, p2);
            // Right
            p1 = new Vector2(cRightLimit, 0);
            p2 = new Vector2(cRightLimit, mapHeight * tileLength / BeyondManager.PPM);
            manager.shapeRenderer.line(p1, p2);
            // Bottom
            p1 = new Vector2(0, cBottomLimit);
            p2 = new Vector2(mapWidth * tileLength / BeyondManager.PPM, cBottomLimit);
            manager.shapeRenderer.line(p1, p2);
            // Top
            p1 = new Vector2(0, cTopLimit);
            p2 = new Vector2(mapWidth * tileLength / BeyondManager.PPM, cTopLimit);
            manager.shapeRenderer.line(p1, p2);

            manager.shapeRenderer.end();
        }

        // Hud
        hud.stage.draw();
        levelHUD.draw();
    }

    public void update(float dt) {
        elapsedTime += dt;

        // Reload Level
        if (reloadLevel) {
            reloadTimer -= dt;
            if (reloadTimer < 0) {
                reloadTimer = reloadTimerDefault;
                reloadLevel = false;

                manager.loadNewLevel(reload);
            }
        }

        // Controls
        handleGamePlayInput(dt);

        // HUD
        levelHUD.update(dt);

        // Player
        wolf.update(dt);

        // Update camera every time it moves
        gameCam.update();

        // Update Map
        mapRenderer.setView(gameCam);

        // Music
        ostManager.update(dt);
    }

    @Override
    public void resize(int width, int height) {
        /** TODO: Update to take aspect ratio into account? */
        gamePort.update(width, height);
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
        hud.stage.dispose();
        levelHUD.dispose();
        mapRenderer.dispose();
        manager.shapeRenderer.dispose();
        owStage.dispose();
    }
}