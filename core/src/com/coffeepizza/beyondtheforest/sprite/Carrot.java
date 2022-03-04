package com.coffeepizza.beyondtheforest.sprite;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.coffeepizza.beyondtheforest.BeyondManager;
import com.coffeepizza.beyondtheforest.levelscreen.LevelScreen;

public class Carrot extends Actor {

    // System
    private boolean debugging = false;
    private String tag = "Carrot";
    private String message = "";

    // Manager
    private BeyondManager manager;
    private LevelScreen screen;

    public Carrot(BeyondManager manager, World world, LevelScreen screen, float startX, float startY) {
        super(manager, world, screen, "ui_found_secret", startX, startY);
        this.manager = manager;
        this.screen = screen;
        defineCarrotBody();
    }

    private void defineCarrotBody() {
        // Create Basic Body
        BodyDef bdef = new BodyDef();
        bdef.position.set(x, y);
        bdef.type = BodyDef.BodyType.KinematicBody;

        // Crate body in world
        body = world.createBody(bdef);

        // Shapes
        CircleShape cirShape = new CircleShape();
        cirShape.setRadius(bodyRadius / BeyondManager.PPM);
        shape = cirShape;

        // Fixture definition
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.density *= 5;
        fdef.restitution = 0;
        body.createFixture(fdef).setUserData("carrotPhysicsBody");
    }

    @Override
    public void setAnimations() {

    }

    @Override
    public TextureRegion getFrame(float dt) {
        return null;
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void defineColliders() {

    }

    @Override
    public TextureRegion setRunningRight(TextureRegion region) {
        return null;
    }

    @Override
    public boolean getRunningRight() {
        return runningRight;
    }

    @Override
    public void changeDirection() { }

    @Override
    public void setName(String name) {

    }
}
