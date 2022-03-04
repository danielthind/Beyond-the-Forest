package com.coffeepizza.beyondtheforest.sprite.levelscreen.bosses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.coffeepizza.beyondtheforest.BeyondManager;
import com.coffeepizza.beyondtheforest.levelscreen.LevelScreen;
import com.coffeepizza.beyondtheforest.sprite.Actor;
import com.coffeepizza.beyondtheforest.sprite.util.UserData;

import java.util.*;

public class FlameAttack extends Actor {

    // System
    private boolean debugging = false;
    private String tag = "FlameAttack";
    private String message = "";

    // Properties
    private boolean leftToRight;
    private int startLocation, endLocation, locationCount;
    private boolean attackComplete;
    private float shortYDistanceToNextFlame;

    // Textures
    private TextureRegion flame;
    private float width, height;

    // Animations
    private Animation burn;

    // Flames
    protected HashMap<Integer, Vector2> flameLocations = new HashMap<>();
    protected HashMap<Integer, Flame> flameList = new HashMap<>();
    protected HashMap<Integer, Flame> flamesToExtinguish = new HashMap<>();
    protected HashMap<Integer, FixtureDef> colliders = new HashMap<>();
    protected HashMap<Integer, Body> bodies = new HashMap<>();

    // Timers
    protected float frameTime = 0.1f;
    protected float timeToNextFlame = 0.15f;
    protected float timeToNextFlameQuick = 0.15f / 4;
    protected float timeToExtinguish = frameTime * 6;

    public FlameAttack(BeyondManager manager, LevelScreen screen, World world, boolean leftToRight) {
        super(manager, world, screen, "flame");
        super.defineDynamicBody(false);
        this.leftToRight = leftToRight;

        // Textures
        flame = new TextureRegion(manager.getAtlas().findRegion("flame"));

        super.setBounds(0, 0, 44 / BeyondManager.PPM, 52 / BeyondManager.PPM);
        super.setRegion(flame);

        width = (flame.getRegionWidth() / 5) / BeyondManager.PPM;
        height = flame.getRegionHeight() / BeyondManager.PPM;

        stateTimer = 0;
        setAnimations();

        flameLocations.putAll(screen.bossLineUnsorted);

        // Setup locations
        if (leftToRight) {
            startLocation = -800;
            endLocation = -875;
        } else {
            startLocation = -975;
            endLocation = -900;
        }
        locationCount = startLocation;

        // Distance to next flame for normal wait vs quick wait
        shortYDistanceToNextFlame = Math.abs(2 * (flameLocations.get(-800).x - flameLocations.get(-801).x));

        /*
        Gdx.app.log(tag, "LocationCount: " + locationCount);
        for (int key : flameLocations.keySet()) {
            Gdx.app.log(tag, "key list: " + key);
        }
        */
    }

    @Override
    public void setAnimations() {
        Array<TextureRegion> frames = new Array<TextureRegion>();

        // Idle
        for(int i = 0; i < 5; i++) {
            frames.add(new TextureRegion(flame, i * 44, 0, 44, 52));

        }
        burn = new Animation(frameTime, frames);
        frames.clear();
    }

    @Override
    public TextureRegion getFrame(float dt) {
        TextureRegion region;

        region = (TextureRegion) burn.getKeyFrame(dt, false);

        return region;
    }

    @Override
    public void update(float dt) {
        // Add new flames or
        if (!attackComplete) {
            stateTimer += dt;

            if (normalWaitTime()) {
                if (stateTimer > timeToNextFlame) {
                    createFlame();

                    // Iterate and complete attack
                    iterateFlames();

                    stateTimer = 0f;
                }
            } else {
                if (stateTimer > timeToNextFlameQuick) {
                    createFlame();

                    // Iterate and complete attack
                    iterateFlames();

                    stateTimer = 0f;
                }
            }
        }

        // Update individual flames
        for (int key : flameList.keySet()) {
            flameList.get(key).update(dt);

            if (flameList.get(key).extinguishFlame) {
                flameList.get(key).extinguish();

                flamesToExtinguish.put(key, flameList.get(key));
                removeFlame(key);
            }
        }


        // Remove flames
        for (int key : flamesToExtinguish.keySet()) {
            flameList.remove(key);

            // Destory fixtures
            Array<Fixture> fList = bodies.get(key).getFixtureList();
            for (Fixture fix : fList) {
                bodies.get(key).destroyFixture(fix);
            }
        }
        flamesToExtinguish.clear();

        // Remove FlameAttack
        if (attackComplete && flameList.size() == 0) {
            levelScreen.enemiesToRemove.add(this);
        }
    }

    @Override
    public void defineColliders() {

    }

    protected void defineFlameCollider(int locationCount) {
        // Create Basic Body
        BodyDef bdef = new BodyDef();
        bdef.position.set(flameLocations.get(locationCount).x, flameLocations.get(locationCount).y);
        bdef.type = BodyDef.BodyType.StaticBody;

        // Crate body in world
        Body flameBody = world.createBody(bdef);
        bodies.put(locationCount, flameBody);

        // Shapes
        PolygonShape polygon = new PolygonShape();

        /** TODO: Base collider radius on sprite dimensions? */
        float colliderWidth = super.bodyRadius + 4;
        float colliderHeight = 30f;
        Vector2 bottomLeft = new Vector2(-colliderWidth / BeyondManager.PPM, 0);
        Vector2 bottomRight = new Vector2(colliderWidth / BeyondManager.PPM, 0);
        Vector2 topLeft = new Vector2(-colliderWidth / BeyondManager.PPM, colliderHeight / BeyondManager.PPM);
        Vector2 topRight = new Vector2(colliderWidth / BeyondManager.PPM, colliderHeight / BeyondManager.PPM);

        Vector2[] verts = new Vector2[] {
                bottomLeft, bottomRight, topRight, topLeft
        };

        polygon.set(verts);
        shape = polygon;

        // Fixture definition
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.isSensor = true;

        // Add to collider
        colliders.put(locationCount, fdef);

        flameBody.createFixture(fdef).setUserData(new UserData("enemyCollider", this, 0));

    }

    protected void createFlame() {
        //Gdx.app.log(tag, "widht, height: " + width + ", " + height);
        //Gdx.app.log(tag, "flameLocations: " + flameLocations);
        //Gdx.app.log(tag, "locationCount: " + locationCount);


        Flame flame = new Flame(width,
                height,
                new Vector2(flameLocations.get(locationCount).x, flameLocations.get(locationCount).y)
        );

        defineFlameCollider(locationCount);

        flameList.put(locationCount, flame);
    }

    protected boolean normalWaitTime() {
        boolean normalWait = false;
        float yDistance = 0f;

        if (leftToRight && locationCount != endLocation) {
            yDistance = Math.abs(flameLocations.get(locationCount).y - flameLocations.get(locationCount - 1).y);

        } else if (!leftToRight && locationCount != endLocation) {
            yDistance = Math.abs(flameLocations.get(locationCount).y - flameLocations.get(locationCount + 1).y);
        }

        if (yDistance < shortYDistanceToNextFlame) {
            normalWait = true;
        }

        /*
        float distanceToNextFlame;

        if (leftToRight) {
            if (locationCount != endLocation) {
                // Counts from -800 to -875
                float xDistance = (flameLocations.get(locationCount).x - flameLocations.get(locationCount - 1).x);
                float yDistance = (flameLocations.get(locationCount).y - flameLocations.get(locationCount - 1).y);

                // Calculate distance between positions
                distanceToNextFlame = (float) Math.sqrt(
                        (xDistance * xDistance) +
                        (yDistance * yDistance)
                );
            } else {
                distanceToNextFlame = shortDistanceToNextFlame;
            }
        } else {
            if (locationCount != endLocation) {
                // Counts from -975 to -900
                float xDistance = (flameLocations.get(locationCount).x - flameLocations.get(locationCount + 1).x);
                float yDistance = (flameLocations.get(locationCount).y - flameLocations.get(locationCount + 1).y);

                // Calculate distance between positions
                distanceToNextFlame = (float) Math.sqrt(
                        (xDistance * xDistance) +
                                (yDistance * yDistance)
                );
            } else {
                distanceToNextFlame = shortDistanceToNextFlame;
            }
        }

        if (distanceToNextFlame < shortDistanceToNextFlame) {
            normalWait = false;
        }
        */

        return normalWait;
    }

    protected void iterateFlames() {
        if (leftToRight) {
            locationCount--;

            if (locationCount < endLocation) {
                attackComplete = true;
            }
        } else {
            locationCount++;

            if (locationCount > endLocation) {
                attackComplete = true;
            }
        }
    }

    protected void removeFlame(int locationCount) {
        bodies.get(locationCount).setActive(false);
    }

    @Override
    public TextureRegion setRunningRight(TextureRegion region) {
        return null;
    }

    @Override
    public boolean getRunningRight() {
        return false;
    }

    @Override
    public void changeDirection() {

    }

    @Override
    public void setName(String name) {

    }

    /** Draw */
    @Override
    public void draw(Batch batch) {
        // Draw flames
        for (int key : flameList.keySet()) {
            Flame flame = flameList.get(key);

            batch.draw(getFrame(flame.timeAflame),
                    flame.position.x,
                    flame.position.y,
                    width * 2,
                    height * 2
            );
        }
    }

    class Flame {

        // System
        private boolean debugging = false;
        private String tag = "Flame";
        private String message = "";

        // Properties
        private Vector2 position;
        private float width, height;
        private float timeAflame;

        private boolean extinguishFlame;

        protected Flame(float width, float height, Vector2 position) {
            this.width  = width;
            this.height = height;
            this.position = new Vector2(position.x - (width / 2),position.y - (height / 2));
            timeAflame = 0f;

            extinguishFlame = false;
        }

        protected void update(float dt) {
            if (!extinguishFlame) {
                // Update time
                timeAflame += dt;

                // Set to extinguish
                if (timeAflame > timeToExtinguish) {
                    extinguishFlame = true;
                }
            }
        }

        protected void extinguish() {
            // Remove colider
        }
    }

}
