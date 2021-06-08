package boid;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import boid.AbstractBoid;
import boid.Boid;
import drawing.Canvas;
import geometry.CartesianCoordinate;

public class Obstacle extends AbstractBoid {

    public static final int LENGTH = 200;
    public static final int BREADTH = 100;
    private List<CartesianCoordinate> obstaclePoints;

    public Obstacle(Canvas canvas, double xPosition, double yPosition) {
        super(canvas);
        this.setPosition(new CartesianCoordinate(xPosition, yPosition));
        this.setVelocity(new CartesianCoordinate());
        this.setCurrentAngle(0);
    }

    @Override
    public void display() {
        putPenDown();
        move(LENGTH);
        turn(90);
        move(BREADTH);
        turn(90);
        move(LENGTH);
        turn(90);
        move(BREADTH);
        putPenUp();
        resetAngleToZero();
    }

    public List<CartesianCoordinate> obstaclePoints() {

        synchronized (this) {
            if (obstaclePoints == null) {
                obstaclePoints = new ArrayList<>();
                double xPosition = getPosition().getX();
                double yPosition = getPosition().getY();
                double lengthXPosition = xPosition + LENGTH;
                double breadthYPosition = yPosition + BREADTH;
                // length points
                for (double i = xPosition; i <= lengthXPosition; i++) {
                    obstaclePoints.add(new CartesianCoordinate(i, yPosition));
                    obstaclePoints.add(new CartesianCoordinate(i, breadthYPosition));
                }
                //breadth Points
                for (double i = yPosition; i <= breadthYPosition; i++) {
                    obstaclePoints.add(new CartesianCoordinate(xPosition, i));
                    obstaclePoints.add(new CartesianCoordinate(lengthXPosition, i));
                }
            }
        }
        return obstaclePoints;
    }


}
