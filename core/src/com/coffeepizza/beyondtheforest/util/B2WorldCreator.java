package com.coffeepizza.beyondtheforest.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.coffeepizza.beyondtheforest.BeyondManager;
import com.coffeepizza.beyondtheforest.levelscreen.LevelScreen;
import com.coffeepizza.beyondtheforest.levelscreen.util.Triggers;
import com.coffeepizza.beyondtheforest.overworld.OverworldScreen;
import com.coffeepizza.beyondtheforest.sprite.Actor;
import com.coffeepizza.beyondtheforest.sprite.Secret;
import com.coffeepizza.beyondtheforest.sprite.levelscreen.enemies.Ghost;
import com.coffeepizza.beyondtheforest.sprite.levelscreen.enemies.Spider;
import com.coffeepizza.beyondtheforest.sprite.levelscreen.enemies.SpiderComposite;
import com.coffeepizza.beyondtheforest.sprite.levelscreen.enemies.Zombie;
import com.coffeepizza.beyondtheforest.sprite.levelscreen.npcs.Golem;

public class B2WorldCreator {

    // System
    private boolean debugging = false;
    private String tag = "B2WorldCreator";
    private String message = "";

    // Manager
    private BeyondManager manager;
    private LevelScreen screen;

    // Eneimes
    private Actor enemy = null;
    private Actor golem = null;
    private Actor secret = null;

    /** Overworld */
    public B2WorldCreator(OverworldScreen screen, TiledMap map) {
        for (MapObject object : map.getLayers().get("ow_triggers").getObjects()) {
            /** Add position with key to pathPositions HashMap */
            // Get position of map object
            float x = object.getProperties().get("x", Float.class);
            float y = object.getProperties().get("y", Float.class);

            screen.pathPositions.put((int) object.getProperties().get("position"), new Vector2(x, y));
            if (debugging) { Gdx.app.log(tag, "position: " + object.getProperties().get("position")); }

            if (object.getProperties().get("lvl") != null) {
                /** Add level button */
                if (debugging) { Gdx.app.log(tag, "Level: " + object.getProperties().get("lvl")); }
                screen.createLevelButton(x, y, (int) object.getProperties().get("lvl"), (int) object.getProperties().get("position"));
            }
        }
    }

    /** PlayScreen */
    public B2WorldCreator(BeyondManager manager, LevelScreen screen, World world, TiledMap map) {
        this.manager = manager;
        this.screen = screen;

        if (debugging) {
            Gdx.app.log(tag, message + " Loading world map");
        }

        /** Player start location and Trigger */
        for (MapObject object : map.getLayers().get("triggers").getObjects()) {
        //for (MapObject object : map.getLayers().get("Triggers").getObjects()) {

            //Gdx.app.log(tag, "trigger no.: " + object.getProperties().get("trigger"));

            Triggers trigger = new Triggers(screen, world, map, object, (int) object.getProperties().get("trigger"));
                // Get trigger number
            int n = (int) object.getProperties().get("trigger");
                // Get position of map object
            float x = trigger.x;
            float y = trigger.y;


            /** CheckpointLocations HashMap */
            if ((int) object.getProperties().get("trigger") == -1
                    || (int) object.getProperties().get("trigger") == -2
                    || (int) object.getProperties().get("trigger") == -3
                    || (int) object.getProperties().get("trigger") == -4) {

                // Add to HashMap
                if (debugging) { Gdx.app.log(tag, message + n + " Start location: " + x + ", " + y); }
                screen.checkpointLocations.put(n, new Vector2(x, y));
            }

            /** Camera - carrot */
            if ((int) object.getProperties().get("trigger") == -10) {
                screen.createCarrot(trigger.x, trigger.y);
            }

            /** BossLocation Hashmap */
            if ((int) object.getProperties().get("trigger") <= -200
                && (int) object.getProperties().get("trigger") >= -300) {
                screen.bossLocationsUnsorted.put(n, new Vector2(x, y));
            }

            /** BossLine Hashmap */
            if ((int) object.getProperties().get("trigger") <= -800
                    && (int) object.getProperties().get("trigger") >= -1000) {
                screen.bossLineUnsorted.put(n, new Vector2(x, y));
            }
        }

        /**
         * Ground
         *      Takes: Rectangle, Polygon, Polyline and Circle Map Objects
         */
        for (MapObject object : map.getLayers().get("ground").getObjects()) {
            createGroundObject(world, object);
        }

        /**
         * Bouncer Neutral
         *      Takes: Rectangle, Polygon, Polyline and Circle Map Objects
         */
        /*
        for (MapObject object : map.getLayers().get("Bouncers_Neutral").getObjects()) { //.getByType(RectangleMapObject.class)) {
            new BouncerNeutral(world, map, object, false);
        }
        */

        /**
         * Bouncer Up
         *      Takes: Rectangle, Polygon, Polyline and Circle Map Objects
         */
        /*
        for (MapObject object : map.getLayers().get("Bouncers_Up").getObjects()) { //.getByType(RectangleMapObject.class)) {
            new BouncerUp(world, map, object, false);
        }
        */

        /**
         * Freezer
         *      Takes: Rectangle, Polygon, Polyline and Circle Map Objects
         */
        /*
        for (MapObject object : map.getLayers().get("freezers").getObjects()) {
            //new Freezer(world, map, object, false);
            BodyDef bdef = new BodyDef();
            Shape shape = null;
            FixtureDef fdef = new FixtureDef();
            Body body;

            shape = getRectangle((RectangleMapObject)object);

            bdef.type = BodyDef.BodyType.StaticBody;
            body = world.createBody(bdef);
            fdef.shape = shape;
            fdef.restitution = 0;

            body.createFixture(fdef).setUserData("freezer");
        }
        */

        /**
         * Secrets
         *      Takes: Rectangle, Polygon, Polyline and Circle Map Objects
         */
        for (MapObject object : map.getLayers().get("secrets").getObjects()) {
            // Create Secret
            RectangleMapObject rectangleObject = (RectangleMapObject)object;
            Rectangle rectangle = rectangleObject.getRectangle();

            int secretNumber = (int) object.getProperties().get("secret");

            switch (secretNumber) {
                case 1:
                    if (!screen.secretBone1) {
                        secret = new Secret(manager, world, screen, rectangle, secretNumber);
                        LevelScreen.actorList.add(secret);
                    }
                    break;
                case 2:
                    if (!screen.secretBone2) {
                        secret = new Secret(manager, world, screen, rectangle, secretNumber);
                        LevelScreen.actorList.add(secret);
                    }
                    break;
                case 3:
                    if (!screen.secretBone3) {
                        secret = new Secret(manager, world, screen, rectangle, secretNumber);
                        LevelScreen.actorList.add(secret);
                    }
                    break;
                case 4:
                    if (!screen.secretBone4) {
                        secret = new Secret(manager, world, screen, rectangle, secretNumber);
                        LevelScreen.actorList.add(secret);
                    }
                    break;
            }
        }

        /**
         * Teleporters
         *      Takes: Rectangle, Polygon, Polyline and Circle Map Objects
         */
        /*
        int id = 0;
        for (MapObject object : map.getLayers().get("Teleporters").getObjects()) { //.getByType(RectangleMapObject.class)) {
            if (debugging) { Gdx.app.log(tag, "Creating Teleporters"); }
            //Rectangle rect = ((RectangleMapObject) object).getRectangle();
            //Teleporter a = new Teleporter(world, map, rect, true, object.getProperties().get("colour", String.class));
            Teleporter a = new Teleporter(world, map, object, true, object.getProperties().get("colour", String.class));

            a.setId(id);
            id++;

            PlayScreen.teleporterList.add(a);
        }
        */

        /** Enemies & Golem */

        int counter = 0;

        //for (int i = 0; i < map.getLayers().getCount(); i++) {
        //Gdx.app.log(tag, "layer " + map.getLayers().get(i).getName());
        //}

        for (MapObject object : map.getLayers().get("enemies").getObjects()) {

            //Gdx.app.log(tag, "here " + map.getLayers());

            if (debugging) { Gdx.app.log(tag, "Creating enemies"); }
            //"Zombies").getObjects()) {

            enemy = null;

            if (debugging) { Gdx.app.log(tag, "enemy type: " + object.getProperties().get("type", String.class)); }

            //Gdx.app.log(tag, "enemy type: " + object.getProperties().get("type", String.class));
            //Gdx.app.log(tag, "here");

            if (object.getProperties().get("type", String.class).equals("tutorialZombie")) {
                if (debugging) { Gdx.app.log(tag, "creating tutorial zombie"); }

                // Create zombie
                RectangleMapObject rectangleObject = (RectangleMapObject)object;
                Rectangle rectangle = rectangleObject.getRectangle();
                enemy = new Zombie(this.manager, this.screen, world, rectangle, true);
                enemy.setName("tutorialZombie" + counter);
            }

            if (object.getProperties().get("type", String.class).equals("zombie")) {
                if (debugging) { Gdx.app.log(tag, "creating zombie"); }

                // Create zombie
                RectangleMapObject rectangleObject = (RectangleMapObject)object;
                Rectangle rectangle = rectangleObject.getRectangle();
                enemy = new Zombie(this.manager, this.screen, world, rectangle, false);
                enemy.setName("zombie" + counter);
            }

            if (object.getProperties().get("type", String.class).equals("spider")) {
                if (debugging) { Gdx.app.log(tag, "creating spider"); }

                // Create Spider
                RectangleMapObject rectangleObject = (RectangleMapObject)object;
                Rectangle rectangle = rectangleObject.getRectangle();
                enemy = new SpiderComposite(this.manager, this.screen, world, rectangle,
                        new Spider(this.manager, this.screen, world, rectangle));
//                enemy = new Shelob(this.manager, this.screen, world, rectangle);
                enemy.setName("spider" + counter);
            }

            if (object.getProperties().get("type", String.class).equals("ghost")) {
                if (debugging) { Gdx.app.log(tag, "creating ghost"); }

                // Create Ghost
                RectangleMapObject rectangleObject = (RectangleMapObject)object;
                Rectangle rectangle = rectangleObject.getRectangle();
                enemy = new Ghost(this.manager, this.screen, world, rectangle);
                enemy.setName("ghost" + counter);
            }

            if (object.getProperties().get("type", String.class).equals("golem")) {
                if (debugging) { Gdx.app.log(tag, "setting golem rect"); }

                // Create Golem
                RectangleMapObject rectangleObject = (RectangleMapObject)object;
                Rectangle rectangle = rectangleObject.getRectangle();

                golem = new Golem(this.manager, this.screen, world, rectangle);
                golem.setName("golem");

                screen.setGolem(golem);
                screen.actorList.add(golem);
            }

            /*
            else if (object.getProperties().get("type", String.class).equals("bat")) {
                if (debugging) { Gdx.app.log(tag, "creating bat"); }

                // Create Bat
                RectangleMapObject rectangleObject = (RectangleMapObject)object;
                Rectangle rectangle = rectangleObject.getRectangle();
                enemy = new Bat (world, screen, rectangle);
                enemy.setName("bat" + counter);
                //if (debugging) { Gdx.app.log(tag, "enemy: " + enemy); }

            } else if (object.getProperties().get("type", String.class).equals("slime")) {
                if (debugging) { Gdx.app.log(tag, "creating slime"); }
                // Create Bat
                RectangleMapObject rectangleObject = (RectangleMapObject)object;
                Rectangle rectangle = rectangleObject.getRectangle();
                enemy = new Slime (world, screen, rectangle);
                enemy.setName("slime" + counter);
            } else if (object.getProperties().get("type", String.class).equals("boss")) {
                if (debugging) { Gdx.app.log(tag, "creating boss"); }
                // Create Bat
                RectangleMapObject rectangleObject = (RectangleMapObject)object;
                Rectangle rectangle = rectangleObject.getRectangle();
                enemy = new BossTest (world, screen, rectangle);
                enemy.setName("bossTest" + counter);
            }
            */
            //if (debugging) { Gdx.app.log(tag, "enemy: " + enemy); }
            if (enemy != null) {
                screen.actorList.add(enemy);
                counter++;
            }
        }


        if (debugging) {
            String s = "";
            for (MapLayer layer : map.getLayers()) {
                s += layer.getName() + "\n";
            }

            //Gdx.app.log(tag, "List layers: " + s);
        }
    }

    /**
     * Shape methods - repeated in interactive tile objects
     */
    private void createGroundObject(World world, MapObject obj) {
        BodyDef bdef = new BodyDef();
        Shape shape = null;
        FixtureDef fdef = new FixtureDef();
        Body body;
        
        if (obj instanceof RectangleMapObject) {
            shape = getRectangle((RectangleMapObject)obj);
        }
        else if (obj instanceof PolygonMapObject) {
            shape = getPolygon((PolygonMapObject)obj);
        }
        else if (obj instanceof PolylineMapObject) {
            shape = getPolyline((PolylineMapObject)obj);
        }
        else if (obj instanceof CircleMapObject) {
            shape = getCircle((CircleMapObject)obj);
        }
        
        bdef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bdef);
        fdef.shape = shape;
        fdef.restitution = 0;

        /**
         * Platform types
         *      - Left/Right one way platforms
         *      - Normal ground platform
         */
        if (obj.getProperties().get("direction", String.class) != null) {
            if (obj.getProperties().get("direction", String.class).equals("left")) {
                body.createFixture(fdef).setUserData("groundLEFT");
            } else if (obj.getProperties().get("direction", String.class).equals("right")) {
                body.createFixture(fdef).setUserData("groundRIGHT");
            }
        } else {
            body.createFixture(fdef).setUserData("ground");
        }
    }
    private static PolygonShape getRectangle(RectangleMapObject rectangleObject) {
        Rectangle rectangle = rectangleObject.getRectangle();
        PolygonShape polygon = new PolygonShape();
        Vector2 size = new Vector2((rectangle.x + rectangle.width * 0.5f) / BeyondManager.PPM,
                (rectangle.y + rectangle.height * 0.5f ) / BeyondManager.PPM);
        polygon.setAsBox(rectangle.width * 0.5f / BeyondManager.PPM,
                rectangle.height * 0.5f / BeyondManager.PPM,
                size,
                0.0f);
        return polygon;
    }
    private static CircleShape getCircle(CircleMapObject circleObject) {
        Circle circle = circleObject.getCircle();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(circle.radius / BeyondManager.PPM);
        circleShape.setPosition(new Vector2(circle.x / BeyondManager.PPM, circle.y / BeyondManager.PPM));
        return circleShape;
    }
    private static PolygonShape getPolygon(PolygonMapObject polygonObject) {
        PolygonShape polygon = new PolygonShape();
        float[] vertices = polygonObject.getPolygon().getTransformedVertices();

        float[] worldVertices = new float[vertices.length];

        for (int i = 0; i < vertices.length; ++i) {
            worldVertices[i] = vertices[i] / BeyondManager.PPM;
        }

        polygon.set(worldVertices);
        return polygon;
    }
    private static ChainShape getPolyline(PolylineMapObject polylineObject) {
        float[] vertices = polylineObject.getPolyline().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];

        for (int i = 0; i < vertices.length / 2; ++i) {
            worldVertices[i] = new Vector2();
            worldVertices[i].x = vertices[i * 2] / BeyondManager.PPM;
            worldVertices[i].y = vertices[i * 2 + 1] / BeyondManager.PPM;
        }

        ChainShape chain = new ChainShape();
        chain.createChain(worldVertices);
        return chain;
    }
}