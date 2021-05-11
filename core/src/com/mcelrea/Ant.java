package com.mcelrea;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Ant implements Comparable<Ant> {
    private Color color;
    private int x, y;
    private int radius;
    private float speed;
    private Vector2 direction;
    private Vector2[] dna;
    private boolean antHasFood;
    private Rectangle hitBox;
    private int score = 0;
    private int initialX, initialY;
    private Vector2 initialDirection;

    public Ant(Color color, int x, int y, int radius, float speed) {
        this.color = new Color((float)Math.random(), (float)Math.random(), (float)Math.random(), 1);
        this.x = x;
        initialX = x;
        this.y = y;
        initialY = y;
        this.speed = speed;
        this.radius = radius;
        dna = new Vector2[100];
        direction = new Vector2().setToRandomDirection();
        initialDirection = direction;
        hitBox = new Rectangle(x-radius,y-radius,radius*2,radius*2);
        initDNA();
    }

    public Ant(Ant parent1, Ant parent2) {
        this.color = parent1.color;
        this.x = parent1.initialX;
        initialX = x;
        this.y = parent1.initialY;
        initialY = y;
        this.speed = Math.random() < 0.5 ? parent1.speed : parent2.speed;
        this.radius = 4;
        hitBox = new Rectangle(x-radius,y-radius,radius*2,radius*2);
        direction = parent1.initialDirection;
        initialDirection = direction;
        dna = new Vector2[100];

        int randNum = (int)(Math.random()*100);
        for(int i=0; i < randNum; i++) {
            dna[i] = parent1.dna[i];
        }
        for(int i=randNum; i < dna.length; i++) {
            dna[i] = parent2.dna[i];
        }

        mutateDNA();
    }

    private void mutateDNA() {
        int mutationChance = 40; //4% chance

        for(int i=0; i < dna.length; i++) {
            int randNum = (int)(1 + Math.random() * 1000);
            if(randNum <= mutationChance) {
                int chance =(int)(1 + Math.random() * 100);
                if (chance <= 50) {
                    dna[i] = new Vector2().setToRandomDirection();
                }
                else {
                    dna[i] = null;
                }
            }
        }

    }


    public void resetAnt() {
        this.x = initialX;
        this.y = initialY;
        this.direction = initialDirection;
        this.score = 0;
        antHasFood = false; //we added this on Tuesday, May 4th
        this.radius = 4;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public void rewardAnt() {
        int middleX = 640;
        int middleY = 360;

        if(antHasFood) {
            double scoreToAdd = Math.abs(Math.sqrt(Math.pow(middleX-x,2) + Math.pow(middleY-y,2)));
            if(score > 0) //silly check to make sure score doesn't go negative
                score += 5000 - (int)scoreToAdd;
        }
    }

    public void gotFood(boolean value) {
        if(value == true && antHasFood == false) {
            score++;
            SimScreen.totalAntzWithFood++;
        }

        antHasFood = value;
    }

    public boolean hasFood() {
        return antHasFood;
    }

    public void updateHitBox() {
        hitBox.setPosition(x-radius,y-radius);
    }

    private void initDNA() {
        for(int i=0; i < dna.length; i++) {
            int chance =(int)(1 + Math.random() * 100);
            if (chance <= 50) {
                dna[i] = new Vector2().setToRandomDirection();
            }
        }
    }

    public void runDNA(int dnaSpot) {
        if(dna[dnaSpot] != null) {
            direction = dna[dnaSpot];
        }
    }

    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(color);
        shapeRenderer.circle(x,y, radius);
        //drawHitBox(shapeRenderer);
        if(antHasFood) {
            shapeRenderer.set(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.circle(x, y, radius + 5);
            shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        }
    }

    public void drawHitBox(ShapeRenderer shapeRenderer) {
        shapeRenderer.set(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.MAGENTA);
        shapeRenderer.rect(hitBox.x, hitBox.y,hitBox.width,hitBox.height);
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
    }

    //DO NOT CHANGE THE +0.55....won't work without it
    //Don't ask me why
    public void moveHomie(float delta) {
        x += direction.x * delta * speed + 0.55;
        y += direction.y * delta * speed + 0.55;
        updateHitBox();
    }

    public void processDNA(float delta, int dnaSpot) {

        if (dnaSpot < dna.length) {
            runDNA(dnaSpot);
        }//end if dna.length

    }//end method act

    @Override
    public int compareTo(Ant o) {
        return this.score - o.score;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}
