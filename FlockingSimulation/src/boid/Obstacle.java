package boid;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import boid.AbstractBoid;
import boid.Boid;
import drawing.Canvas;
import geometry.CartesianCoordinate;

public class Obstacle extends AbstractBoid {

    private double obstacleCentreX = 350;
    private double obstacleCentreY = 200;

    public Obstacle(Canvas canvas, double xPosition, double yPosition) {
        super(canvas);
        this.setPosition( new CartesianCoordinate(xPosition,yPosition));
        this.setVelocity( new CartesianCoordinate());
        this.display();
    }

    @Override
    public void display() {
        putPenDown();
        move(200);
        turn(90);
        move(100);
        turn(90);
        move(200);
        turn(90);
        move(100);
        putPenUp();
    }


}
