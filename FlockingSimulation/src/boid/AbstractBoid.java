package boid;

import drawing.Canvas;
import geometry.CartesianCoordinate;
import geometry.LineSegment;

public abstract class AbstractBoid implements Boid {
	public static final int INITIAL_ANGLE = 270;
	public static double maxVelocity = 4;
    protected static double minVelocity = 1;
    protected Canvas myCanvas;
    protected CartesianCoordinate currentPoint = new CartesianCoordinate(0, 0);
    protected CartesianCoordinate velocity = new CartesianCoordinate();
    private double currentAngle = INITIAL_ANGLE;
    private boolean isPenDown;

    /**
     * Turtle constructor.
     *
     * @param myCanvas The canvas to use
     */
    public AbstractBoid(Canvas myCanvas) {
        this.myCanvas = myCanvas;
        // this.turn( Math.random()* 360);
        this.setPosition(initialPosition());
        this.setVelocity(initialVelocity());
    }

    protected CartesianCoordinate initialPosition() {
        return new CartesianCoordinate(Math.random() * Canvas.DEFAULT_X, Math.random() * Canvas.DEFAULT_Y);
    }

    protected CartesianCoordinate initialVelocity() {
        double randomAngle = Math.toRadians(Math.random() * 360);
        return new CartesianCoordinate(Math.cos(randomAngle), Math.sin(randomAngle)).normalize();
    }

    public CartesianCoordinate getVelocity() {
        return velocity;
    }

    public void setVelocity(CartesianCoordinate newVelocity) {
        if (newVelocity.getX() > maxVelocity || newVelocity.getX() < -maxVelocity) {
            this.velocity.setX(maxVelocity);
        } else {
            this.velocity.setX(newVelocity.getX());
        }

        if (newVelocity.getY() > maxVelocity || newVelocity.getY() < -maxVelocity) {
            this.velocity.setY(maxVelocity);
        } else {
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

    public void setPosition(CartesianCoordinate point) {
        this.currentPoint = point;
    }

    /**
     * Rotates the turtle clockwise by the specified angle in degrees.
     */
    public void turn(double angle) {
        this.currentAngle = this.currentAngle + angle;
    }

    /**
     * Moves the pen off the canvas so that the turtle's route isn't drawn for
     * any subsequent movements.
     */
    public void putPenUp() {
        this.isPenDown = false;
    }

    /**
     * Lowers the pen onto the canvas so that the turtle's route is drawn.
     */
    public void putPenDown() {
        this.isPenDown = true;
    }

    public void resetAngle() {

		this.currentAngle = INITIAL_ANGLE;
	}

    public double getCurrentAngle() {
        return currentAngle;
    }

    public void setCurrentAngle(double currentAngle) {
        this.currentAngle = currentAngle;
    }

    public void display(boolean orientation) {
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

    public double distanceBetween(Boid boid) {
        return this.getPosition().sub(boid.getPosition()).norm();
    }


}
