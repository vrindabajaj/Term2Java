package turtle;

import drawing.Canvas;
import geometry.CartesianCoordinate;
import geometry.LineSegment;

public class Turtle {
	private Canvas myCanvas;
	private CartesianCoordinate currentPoint = new CartesianCoordinate(0, 0);
	private double currentAngle = 0;
	private boolean isPenDown;

	
	/**
	 * Turtle constructor.
	 * @param myCanvas The canvas to use
	 * @param startingPoint The starting location
	 */
	public Turtle(Canvas myCanvas) {
		this.myCanvas = myCanvas;
		moveToOrigin();
	}

	/**
	 * The turtle is moved in its current direction for the given number of pixels.
	 * If the pen is down when the robot moves, a line will be drawn on the floor.
	 * 
	 * @param size The number of pixels to move.
	 */
	public void move(double size) {

		CartesianCoordinate start = currentPoint;
		CartesianCoordinate end = giveMeEndPoint(currentPoint, size, currentAngle);
		LineSegment lineSegment = new LineSegment(start, end);
		if (isPenDown) {
			myCanvas.drawLineSegment(lineSegment);
		}
		currentPoint = end;
	}

	private CartesianCoordinate giveMeEndPoint(CartesianCoordinate startPoint, double size, double currentAngle2) {
		
		double yDirection = size * Math.sin(Math.toRadians(currentAngle2));
		double xDirection = size * Math.cos(Math.toRadians(currentAngle2));
		double newXLength = startPoint.getX() + xDirection;
		double newYLength = startPoint.getY() + yDirection;

		return new CartesianCoordinate(newXLength, newYLength);
	}

	/**
	 * Rotates the turtle clockwise by the specified angle in degrees.
	 * 
	 * @param i The number of degrees to turn.
	 */
	public void turn(double angle) {
		this.currentAngle = this.currentAngle + angle;
	}

	/**
	 * Moves the pen off the canvas so that the turtle’s route isn’t drawn for any
	 * subsequent movements.
	 */
	public void putPenUp() {
		this.isPenDown = false;
	}

	public double getPositionX() {
		return currentPoint.getX();
	}
	
	public double getPositionY() {
		return currentPoint.getY();
	}

	/**
	 * Lowers the pen onto the canvas so that the turtle’s route is drawn.
	 */
	public void putPenDown() {
		this.isPenDown = true;
	}

	public void moveToCentre() {
		moveToPoint(new CartesianCoordinate(Canvas.DEFAULT_X / 2, Canvas.DEFAULT_Y / 2));
	}

	public void moveToOrigin() {
		moveToPoint(new CartesianCoordinate(0, 0));
		resetAngleToZero();
	}
	
	public void moveToPoint(CartesianCoordinate point) {
		this.currentPoint = point;
	}

	public void resetAngleToZero() {
		this.currentAngle = 0;
	}

	public void drawTurtle() {
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

	public void undrawTurtle() {
		for (int i = 0; i < 4; i++) {
			myCanvas.removeMostRecentLine();
		}
		myCanvas.repaint();
	}

	
	public void wrapPosition(double maxXPos, double maxYPos) {
		if(currentPoint.getX() > maxXPos) {
			currentPoint.setX(0);
		}
		if(currentPoint.getY() > maxYPos) {
			currentPoint.setY(0);
		}
		if(currentPoint.getX() < 0) {
			currentPoint.setX(maxXPos);
		}
		if (currentPoint.getY() < 0) {
			currentPoint.setY(maxYPos);
		}
	}
}
