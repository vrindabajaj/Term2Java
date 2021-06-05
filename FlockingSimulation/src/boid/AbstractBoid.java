package boid;

import java.util.ArrayList;
import java.util.List;

import drawing.Canvas;
import geometry.CartesianCoordinate;
import geometry.LineSegment;

public abstract class AbstractBoid implements Boid {
    protected Canvas myCanvas;
    protected CartesianCoordinate currentPoint = new CartesianCoordinate(0, 0);
    private double currentAngle = 0;
    private boolean isPenDown;
    public static final double SECONDS_PER_MILLISECS = 0.001;
    protected static final  int SPEED_FACTOR = 200;




    protected CartesianCoordinate velocity = new CartesianCoordinate();
    protected  double speed;
    protected static double vMin = 1; //Minimum velocity for a bird
    protected static double vMax = 4; //Maximum velocity for a bird




    /**
     * Turtle constructor.
     *
     * @param myCanvas The canvas to use
     */
    public AbstractBoid(Canvas myCanvas) {
        this.myCanvas = myCanvas;
       // this.turn( Math.random()* 360);
        this.setPosition( initialPosition());
        this.setVelocity( initialVelocity());
        this.setSpeed(Math.random() * SPEED_FACTOR);
        //this.display();
    }





    private CartesianCoordinate initialPosition() {
        return new CartesianCoordinate( Math.random() * Canvas.DEFAULT_X, Math.random()* Canvas.DEFAULT_Y);
    }

    protected CartesianCoordinate initialVelocity() {
        double speedX = Math.random() * (vMax - vMin) + vMin;
        double speedY = Math.random() * (vMax - vMin) + vMin;
        return new CartesianCoordinate(speedX, speedY);
    }

    public CartesianCoordinate getVelocity() {
        return velocity;
    }

    public void setVelocity(CartesianCoordinate newVelocity) {
        if (newVelocity.getX() > vMax || newVelocity.getX() < -vMax) {
            this.velocity.setX(vMax);
        }
        else {
            this.velocity.setX(newVelocity.getX());
        }

        if (newVelocity.getY() > vMax || newVelocity.getY() < -vMax) {
            this.velocity.setY(vMax);
        }
        else {
            this.velocity.setY(newVelocity.getY());
        }
    }
    /**
     * The turtle is moved in its current direction for the given number of pixels.
     * If the pen is down when the robot moves, a line will be drawn on the floor.
     *
     * @param size The number of pixels to move.
     */
    public void move(double size) {

        CartesianCoordinate start = currentPoint;
        CartesianCoordinate end = CartesianCoordinate.targetCoordinate(currentPoint, size, currentAngle);
        LineSegment lineSegment = new LineSegment(start, end);
        if (isPenDown) {
            myCanvas.drawLineSegment(lineSegment);
        }
        currentPoint = end;
    }

    @Override
    public CartesianCoordinate getPosition() {
        return currentPoint;
    }

    /**
     * Rotates the turtle clockwise by the specified angle in degrees.
     */
    public void turn(double angle) {
        this.currentAngle = this.currentAngle + angle;
    }

    /**
     * Moves the pen off the canvas so that the turtle�s route isn�t drawn for any
     * subsequent movements.
     */
    public void putPenUp() {
        this.isPenDown = false;
    }

    /**
     * Lowers the pen onto the canvas so that the turtle�s route is drawn.
     */
    public void putPenDown() {
        this.isPenDown = true;
    }

     public void setToOrigin() {
        setPosition(new CartesianCoordinate(0, 0));
        resetAngleToZero();
    }

    public void setPosition(CartesianCoordinate point) {
        this.currentPoint = point;
    }

    public void resetAngleToZero() {
        this.currentAngle = 0;
    }

    public double getCurrentAngle() {
        return currentAngle;
    }

    public void setCurrentAngle(double currentAngle) {
        this.currentAngle = currentAngle;
    }

    public void display() {
        putPenUp();
        move(29);
        putPenDown();
        turn(150);
        for (int i = 0; i < 3; i++) {
            move(50);
            turn(120);
        }
        turn(30);
        move(29);
        turn(180);
        putPenUp();
    }

    public void hide() {
        for (int i = 0; i < 4; i++) {
            myCanvas.removeMostRecentLine();
        }
        myCanvas.repaint();
    }

    public void wrapPosition(double maxXPos, double maxYPos) {
        if (currentPoint.getX() > maxXPos + 50) {
            currentPoint.setX(0);
        }
        if (currentPoint.getY() > maxYPos + 50) {
            currentPoint.setY(0);
        }
        if (currentPoint.getX() < 0 - 50) {
            currentPoint.setX(maxXPos);
        }
        if (currentPoint.getY() < 0 - 50) {
            currentPoint.setY(maxYPos);
        }
    }

    public double distanceBetween(Boid turtle) {
        return this.getPosition().add(turtle.getPosition().multiply(-1)).norm();

        //		double lengthA = this.getPositionX() - turtle.getPositionX();
        //		double lengthB = this.getPositionY() - turtle.getPositionY();
        //
        //		return Math.sqrt(Math.pow(lengthA, 2) + Math.pow(lengthB, 2));
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

     public void update(int time) {
        move(step(time));
    }


    public int step(int time) {
        int distance = (int) (time * SECONDS_PER_MILLISECS * this.speed);
        return distance;
    }

    @Override
    public void align(List<Boid> flock, double alignmentRadius) {
        double desiredAngle;
        double totalAngle = 0;
        int totalBoids = 0;
        int angleCoefficient = 10;
        Boid boidA = this;
        for (int j = 0; j < flock.size(); j++) {
            Boid boidB = flock.get(j);
            double distance = boidA.distanceBetween(boidB);
            if (boidA != boidB && distance < alignmentRadius) {
                totalBoids++;
                totalAngle = totalAngle + boidB.getCurrentAngle();
            }
        }
        if (totalBoids > 0) {
            desiredAngle = totalAngle / totalBoids;

            //			if (desiredAngle > this.currentAngle) {

            if (desiredAngle < this.currentAngle) {
                for (int i = 0; i < angleCoefficient; i++) {
                    boidA.turn(-1 * ((boidA.getCurrentAngle() - desiredAngle) / angleCoefficient));
                }
            } else {
                for (int i = 0; i < angleCoefficient; i++) {
                    boidA.turn((desiredAngle - boidA.getCurrentAngle()) / angleCoefficient);
                }
            }
        }
    }





    @Override
    public void angleOnlySeparation(List<Boid> flock) {
        double totalAngle = 0;
        double desiredAngle = 0;
        int totalBoids = 0;
        Boid boidA = this;

        for (int j = 0; j < flock.size(); j++) {
            Boid boidB = flock.get(j);
            double distance = boidA.distanceBetween(boidB);
            if (boidA != boidB && distance < 50) {
                totalBoids++;

                totalAngle = totalAngle + relativeAngle(boidB);
            }
        }
        if (totalBoids > 0) {
            desiredAngle = (totalAngle / totalBoids);
            // System.out.println(desiredAngle);
            boidA.setCurrentAngle(desiredAngle);
        }
    }

    public int whichQuadrant(Boid otherBoid) {
        if (otherBoid.getPosition().getX() > this.getPosition().getX() && otherBoid.getPosition().getY() > this.getPosition().getY()) {
            return 1;
        }
        if (otherBoid.getPosition().getX() > this.getPosition().getX() && otherBoid.getPosition().getY() < this.getPosition().getY()) {
            return 2;
        }
        if (otherBoid.getPosition().getX() < this.getPosition().getX() && otherBoid.getPosition().getY() < this.getPosition().getY()) {
            return 3;
        }
        if (otherBoid.getPosition().getX() < this.getPosition().getX() && otherBoid.getPosition().getY() > this.getPosition().getY()) {
            return 4;
        }
        return 0;
    }

    public double relativeAngle(Boid otherBoid) {
        int quadrant = whichQuadrant(otherBoid);
        double relativeAngle = 0;
        double diffX = this.getPosition().getX() - otherBoid.getPosition().getX();
        double diffY = this.getPosition().getY() - otherBoid.getPosition().getY();
        double baseAngle = Math.atan2(diffY, diffX);

        if (quadrant == 1) {
            relativeAngle = 360 - baseAngle;
        }
        if (quadrant == 2) {
            relativeAngle = baseAngle;
        }
        if (quadrant == 3) {
            relativeAngle = 180 - baseAngle;
        }
        if (quadrant == 1) {
            relativeAngle = 180 + baseAngle;
        }

        return relativeAngle;

    }

    public boolean isInView(Boid otherBoid) {
        double boidRelAngle;
        if (this.currentAngle < 270 && this.currentAngle > 90) {
            boidRelAngle = relativeAngle(otherBoid) + (270 - this.currentAngle);
        } else {
            boidRelAngle = relativeAngle(otherBoid) - (270 - this.currentAngle);
        }

        if (boidRelAngle > 130 && boidRelAngle < 50) {
            return true;
        }
        return false;
    }
}
