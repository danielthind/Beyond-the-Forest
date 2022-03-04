package com.coffeepizza.beyondtheforest.levelscreen.util;

import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.coffeepizza.beyondtheforest.BeyondManager;

public abstract class InteractiveTileObject {

    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    //protected Rectangle bounds;
    protected Body body;
    protected Fixture fixture;

    public InteractiveTileObject(World world, TiledMap map, Object obj, boolean isSensor) {
                                 //Rectangle bounds, boolean isSensor) {
        this.world = world;
        this.map = map;
        //this.bounds = bounds;

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

        if (isSensor) {
            fdef.isSensor = true;
        }

        fixture = body.createFixture(fdef);

//        if (isGround) {
//            fixture.setUserData("ground");
//        } else {
            fixture.setUserData(this);
//        }

        //fixture.setUserData(this);

        /*
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        /**
         * TODO: Needs to be updated to use Polygons, Polylines and Circles Map Objects
         *      Reference B2WorldCreator
         *      @assignee: Ashley
         */
        /*
        // Define body type (3 kinds):
        //      Dynamic (e.g. player) affected by forces gravity etc..
        //      Kinematic (e.g. pendulum, moving platforms) aren't affected by forces, but affected by velocities
        //      Static (e.g. ground tile) don't move and are not affected by forces or velocities
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(
                (bounds.getX() + bounds.getWidth() / 2) / MarioBros.PPM,
                (bounds.getY() + bounds.getHeight() / 2) / MarioBros.PPM);

        // Add to world
        body = world.createBody(bdef);

        // Define fixture
        shape.setAsBox(
                (bounds.getWidth() / 2) / MarioBros.PPM,
                (bounds.getHeight() / 2) / MarioBros.PPM);
        fdef.shape = shape;

        if (isSensor) {
            fdef.isSensor = true;
        }

        fixture = body.createFixture(fdef);
        */
    }

    /**
     * Shape methods
     */
    /*
    protected void createObject(World world, TiledMap map, Object obj, boolean isSensor) {
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

        if (isSensor) {
            fdef.isSensor = true;
        }

        bdef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bdef);
        fdef.shape = shape;
        body.createFixture(fdef);
    }
     */

    protected static PolygonShape getRectangle(RectangleMapObject rectangleObject) {
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
    protected static CircleShape getCircle(CircleMapObject circleObject) {
        Circle circle = circleObject.getCircle();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(circle.radius / BeyondManager.PPM);
        circleShape.setPosition(new Vector2(circle.x / BeyondManager.PPM, circle.y / BeyondManager.PPM));
        return circleShape;
    }
    protected static PolygonShape getPolygon(PolygonMapObject polygonObject) {
        PolygonShape polygon = new PolygonShape();
        float[] vertices = polygonObject.getPolygon().getTransformedVertices();

        float[] worldVertices = new float[vertices.length];

        for (int i = 0; i < vertices.length; ++i) {
            worldVertices[i] = vertices[i] / BeyondManager.PPM;
        }

        polygon.set(worldVertices);
        return polygon;
    }
    protected static ChainShape getPolyline(PolylineMapObject polylineObject) {
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
