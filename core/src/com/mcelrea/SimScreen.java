package com.mcelrea;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

public class SimScreen implements Screen {

    //SpriteBatch allows the drawing of sprites (2D images) to the screen
    private SpriteBatch spriteBatch;

    //ShapeRendered allows the drawing of shapes to the screen
    private ShapeRenderer shapeRenderer;

    //default font
    private BitmapFont defaultFont;

    //OrthographicCamera is top-down camera
    private OrthographicCamera camera;

    //Controls the world view through he camera
    private Viewport viewport;

    public static final int WIDTH = 1280, HEIGHT = 720;

    ArrayList<Ant> antz = new ArrayList();
    long antzDelay = 1000;
    long nextAntzMove;
    int currentAntDNA = 0;

    @Override
    public void show() {
        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        camera = new OrthographicCamera(WIDTH, HEIGHT);
        viewport = new FitViewport(WIDTH, HEIGHT, camera);
        defaultFont = new BitmapFont();

        spawnInitialPopulation();
        nextAntzMove = System.currentTimeMillis() + antzDelay;
    }

    public void spawnInitialPopulation() {
        for(int i=0; i < 50; i++) {
            antz.add(new Ant(Color.CYAN, 640, 370, 4, 20+(int)(Math.random()*80)));
        }
    }

    public void renderAntz() {
        for(Ant a: antz) {
            a.draw(shapeRenderer);
        }
    }

    public void actAntz(float delta) {
        if(System.currentTimeMillis() >= nextAntzMove) {
            for (Ant a : antz) {
                a.processDNA(delta, currentAntDNA);
            }
            nextAntzMove = System.currentTimeMillis() + antzDelay;
            currentAntDNA++;
        }

        for(Ant a: antz) {
            a.moveHomie(delta);
        }
    }

    @Override
    public void render(float delta) {
        clearScreen();

        actAntz(delta);

        spriteBatch.begin();
        defaultFont.draw(spriteBatch,"Current DNA: " + currentAntDNA, 10, 700);
        spriteBatch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        renderAntz();
        shapeRenderer.end();
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

    public void clearScreen() {
        Gdx.gl20.glClearColor(0,0,0,1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void dispose() {

    }
}
