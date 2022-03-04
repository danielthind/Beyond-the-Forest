package com.coffeepizza.beyondtheforest.levelscreen.util;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.coffeepizza.beyondtheforest.BeyondManager;
import com.coffeepizza.beyondtheforest.levelscreen.LevelScreen;
import com.coffeepizza.beyondtheforest.sprite.util.UserData;

public class Triggers extends InteractiveTileObject {

    // System
    private boolean debugging = false;
    private String tag = "Trigger";
    private String message = "";

    public float x, y;
    public LevelScreen screen;

    public Triggers(LevelScreen screen, World world, TiledMap map, Object obj, int triggerNum) {
        super(world, map, obj, true);
//        super(world, map, obj, true, false);
        fixture.setUserData(new UserData("trigger", this, triggerNum));
        this.screen = screen;

        // Super accepted RectangleMapObjects which need to be converted to Rectangles
        RectangleMapObject rectangleObject = (RectangleMapObject)obj;
        Rectangle rectangle = rectangleObject.getRectangle();

        this.x = (rectangle.getX() + rectangle.getWidth() / 2) / BeyondManager.PPM;
        this.y = (rectangle.getY() + rectangle.getHeight() / 2) / BeyondManager.PPM;
    }
}
