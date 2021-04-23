package com.mcelrea;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Ant {
    private Color color;
    private int x, y;
    private int size;
    private float speed;
    private Vector2 direction;
    private Vector2[] dna;

    public Ant(Color color, int x, int y, int size, float speed) {
        this.color = color;
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.size = size;
        dna = new Vector2[100];
        direction = new Vector2().setToRandomDirection();
        initDNA();
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
        shapeRenderer.circle(x,y,size);
    }

    //DO NOT CHANGE THE +0.55....won't work without it
    //Don't ask me why
    public void moveHomie(float delta) {
        x += direction.x * delta * speed + 0.55;
        y += direction.y * delta * speed + 0.55;
    }

    public void processDNA(float delta, int dnaSpot) {

        if (dnaSpot < dna.length) {
            runDNA(dnaSpot);
        }//end if dna.length

    }//end method act
}
