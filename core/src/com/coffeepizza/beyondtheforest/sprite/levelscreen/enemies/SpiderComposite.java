package com.coffeepizza.beyondtheforest.sprite.levelscreen.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.coffeepizza.beyondtheforest.BeyondManager;
import com.coffeepizza.beyondtheforest.levelscreen.LevelScreen;
import com.coffeepizza.beyondtheforest.sprite.Actor;

public class SpiderComposite extends Actor {

    // System
    private boolean debugging = false;
    private String tag = "ShelobComposite";
    private String message = "";

    // Composite Classes
    private Spider shelob;

    // Textures
    public TextureRegion spiderHanging;

    // Animations
    private Animation web;

    // Web Thread
    private TextureRegion webThread;
    private Vector2 v1;
    private Vector2 v2;

    public SpiderComposite(BeyondManager manager, LevelScreen screen, World world, Rectangle rectangle, Spider spider) {
        super(manager, world, screen, "spider_hanging", rectangle);

        // Shelob
        this.shelob = spider;

        // Web animation region
        spiderHanging = new TextureRegion(manager.getAtlas().findRegion("spider_hanging"));
        super.setBounds(0, 0, 64 / BeyondManager.PPM, 64 / BeyondManager.PPM);
        super.setRegion(spiderHanging);
        super.setPosition(
                spider.getActorBody().getPosition().x - getWidth() / 2,
                super.getPatrolTop()
                        - ((spiderHanging.getRegionHeight() / 2) / BeyondManager.PPM)
        );

        setAnimations();
        elapsedTime = 0;

        // Web
        webThread = new Sprite(new TextureRegion(manager.getAtlas().findRegion("spider_thread")));

        v1 = new Vector2(
                shelob.getActorBody().getPosition().x - getWidth() / 2,
                super.getPatrolTop());
        v2 = new Vector2(
                shelob.getActorBody().getPosition().x - getWidth() / 2,
                shelob.getActorBody().getPosition().y - getHeight() / 2
        );
    }

    @Override
    public void setAnimations() {
        Array<TextureRegion> frames = new Array<TextureRegion>();

        // Climbing
        frames.add(new TextureRegion(spiderHanging, 3 * 64, 0, 64, 64));
        web = new Animation(0.15f, frames);
        frames.clear();
    }

    @Override
    public TextureRegion getFrame(float dt) {
        TextureRegion region;

        region = (TextureRegion) web.getKeyFrame(stateTimer, true);

        return region;
    }

    @Override
    public void update(float dt) {
        // Web
        elapsedTime += dt;
        super.setRegion(getFrame(dt));

        // Update Spider
        shelob.update(dt);

        // Update Web Thread
        v1 = new Vector2(
                shelob.getActorBody().getPosition().x,
                super.getPatrolTop() + shelob.getHeight() / 2
        );
        v2 = new Vector2(
                shelob.getActorBody().getPosition().x,
                shelob.getActorBody().getPosition().y
        );
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

    public void draw(Batch batch) {
        // Draw Web and Spider
        batch.draw(webThread, v2.x, v2.y,       // Web Thread
                1 / BeyondManager.PPM,
                (v1.y - v2.y - (32 / BeyondManager.PPM))
        );
        super.draw(batch);                      // Web
        shelob.draw(batch);                     // Spider
    }
}
