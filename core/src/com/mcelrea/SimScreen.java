package com.mcelrea;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.List;

/*
 * We need to lock the FPS so that ant behavior persists between runs
 */

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
    ArrayList<Food> foodz = new ArrayList();
    long antzDelay = 300;
    long nextAntzMove;
    static int currentAntDNA = 0;
    int currentGen = 1;
    static int totalAntzWithFood = 0;

    int lastGenHighScore = 0;
    Ant lastAntScore;
    int lastLastGenHighScore = 0;
    Ant lastLastAntScore;

    int returnFoodCount = 0;

    int antPopulationSize = 500;

    int homeBaseX = WIDTH/2;
    int homeBaseY = HEIGHT/2;
    Color homeBaseColor = Color.MAROON;
    int homeBaseSize = 50;
    Rectangle homeBaseHitBox = new Rectangle(homeBaseX-homeBaseSize,homeBaseY-homeBaseSize,
            homeBaseSize*2,homeBaseSize*2);

    @Override
    public void show() {
        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        camera = new OrthographicCamera(WIDTH, HEIGHT);
        viewport = new FitViewport(WIDTH, HEIGHT, camera);
        defaultFont = new BitmapFont();

        spawnInitialPopulation();
        spawnFoodz();
        nextAntzMove = System.currentTimeMillis() + antzDelay;
    }

    private void spawnFoodz() {
        foodz.add(new Food(Color.CHARTREUSE, 20, 700, 20,100));
        foodz.add(new Food(Color.CHARTREUSE, 1260, 700, 20,100));
        foodz.add(new Food(Color.CHARTREUSE, 1260, 20, 20,100));
    }

    public void spawnInitialPopulation() {
        for(int i=0; i < antPopulationSize; i++) {
            antz.add(new Ant(Color.CYAN, 640, 370, 4, 80+(int)(Math.random()*80)));
        }
    }

    public void renderAntz() {
        for(Ant a: antz) {
            a.draw(shapeRenderer);
        }
    }

    public void renderFoodz() {
        for(Food f: foodz) {
            f.render(shapeRenderer);
        }
    }

    public void rewardThemAntz() {
        for(Ant a: antz) {
            a.rewardAnt();
        }
    }

    public void printAntWithScore() {
        for(int i=0; i < antz.size(); i++) {
            System.out.println(i + ". " + antz.get(i) + " - (" + antz.get(i).getScore() + ")");
        }
    }

    public void killThemAntz() {

        List newList = antz.subList(antz.size()/2, antz.size());

        antz = new ArrayList<>(newList);
    }

    public void spawnNewGenerationOfAntz() {
        lastLastGenHighScore = lastGenHighScore;
        lastLastAntScore = lastAntScore;
        lastGenHighScore = antz.get(antz.size()-1).getScore();
        lastAntScore = antz.get(antz.size()-1);

        for(int i=0; i < antz.size(); i++) {
            antz.get(i).resetAnt();
        }

        antz.get(antz.size()-1).setRadius(8);
        Ant bestAnt = antz.get(antz.size()-1);

        int oldPopSize = antz.size();

        //1/3 population of random ants
        for(int i=0; i <= oldPopSize/3; i++) {
            int p1 = (int)(Math.random() * oldPopSize);
            int p2 = (int)(Math.random() * oldPopSize);
            antz.add(new Ant(antz.get(p1), antz.get(p2)));
        }

        //1/3 population with best ant as daddy
        for(int i=0; i < oldPopSize/3; i++) {
            int p2 = (int)(Math.random() * oldPopSize);
            antz.add(new Ant(bestAnt,antz.get(p2)));
        }

        //1/3 population random new antz
        for(int i=0; i < oldPopSize/3; i++) {
            antz.add(new Ant(Color.CYAN, 640, 370, 4, 80+(int)(Math.random()*80)));
        }

        currentGen++;
        totalAntzWithFood = 0;
        returnFoodCount = 0;
    }

    public void actAntz(float delta) {
        if(currentAntDNA >= 100) {
            rewardThemAntz();

            sortThemAntz();
            System.out.println("Afting Sorting....");
            printAntWithScore();
            System.out.println();

            killThemAntz();
            System.out.println("Afting Killing....");
            printAntWithScore();
            System.out.println();

            currentAntDNA = 0;
            spawnNewGenerationOfAntz();
        }

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

    public void sortThemAntz() {
        for(int i=0; i < antz.size(); i++) {
            for(int j=1; j < antz.size()-i-1; j++) {
                if(antz.get(j).compareTo(antz.get(j+1)) > 0) {
                    Ant temp = antz.get(j);
                    antz.set(j, antz.get(j+1));
                    antz.set(j+1, temp);
                }
            }
        }
    }

    public void checkCollision() {
        for(Ant a: antz) {
            for(Food f: foodz) {
                if(a.getHitBox().overlaps(f.getHitBox())) {
                    a.gotFood(true);
                }
            }

            if(a.getHitBox().overlaps(homeBaseHitBox)) {
                if(a.hasFood()) {
                    a.setScore(a.getScore()+10000+(100-currentAntDNA));
                    a.gotFood(false);
                    returnFoodCount++;
                }
            }
        }
    }

    @Override
    public void render(float delta) {
        clearScreen();

        actAntz(delta);
        checkCollision();

        spriteBatch.begin();
        defaultFont.draw(spriteBatch,"Current DNA: " + currentAntDNA, 10, 700);
        defaultFont.draw(spriteBatch,"Current Gen: " + currentGen, 10, 680);
        defaultFont.draw(spriteBatch,"Current food gotten: " + totalAntzWithFood, 10, 660);
        defaultFont.draw(spriteBatch,"Last Gen High Score: " + lastGenHighScore + ", " + lastAntScore, 10, 640);
        defaultFont.draw(spriteBatch,"Last Last Gen High Score: " + lastLastGenHighScore + ", " + lastLastAntScore, 10, 620);
        defaultFont.draw(spriteBatch,"Food Return Count: " + returnFoodCount, 10, 600);
        defaultFont.draw(spriteBatch,"Total Ants: " + antz.size(), 10, 580);
        spriteBatch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(homeBaseColor);
        shapeRenderer.circle(homeBaseX, homeBaseY, homeBaseSize);
        renderFoodz();
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
