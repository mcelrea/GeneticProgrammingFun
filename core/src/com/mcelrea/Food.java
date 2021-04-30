package com.mcelrea;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Food {
    private Color color;
    private int x, y;
    private int radius;
    private int amount;
    private Rectangle hitBox;

    public Food(Color color, int x, int y, int radius, int amount) {
        this.color = color;
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.amount = amount;
        hitBox = new Rectangle(x-radius,y-radius,radius*2,radius*2);
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(color);
        shapeRenderer.circle(x,y, radius);
        renderHitBox(shapeRenderer);
    }

    public void renderHitBox(ShapeRenderer shapeRenderer) {
        shapeRenderer.set(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.MAGENTA);
        shapeRenderer.rect(hitBox.x, hitBox.y,hitBox.width,hitBox.height);
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
    }
}
