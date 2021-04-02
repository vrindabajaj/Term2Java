package turtle;

import drawing.Canvas;
import geometry.CartesianCoordinate;
import geometry.LineSegment;

public class Turtle {
	private Canvas myCanvas;
	private CartesianCoordinate currentPoint = new CartesianCoordinate(0, 0);
	private int currentAngle = 0;
	private boolean isPenDown;

	public Turtle(Canvas myCanvas) {
		this.myCanvas = myCanvas;
		moveToOrigin();
		// TODO Auto-generated constructor stub
	}

	/**
	 * The turtle is moved in its current direction for the given number of pixels.
	 * If the pen is down when the robot moves, a line will be drawn on the floor.
	 * 
	 * @param i The number of pixels to move.
	 */
	public void move(int pixels) {

		CartesianCoordinate start = currentPoint;

		CartesianCoordinate end = giveMeEndPoint(currentPoint, pixels, currentAngle);
		LineSegment lineSegment = new LineSegment(start, end);
		if (isPenDown) {
			myCanvas.drawLineSegment(lineSegment);
		}
		currentPoint = end;

	}

	private CartesianCoordinate giveMeEndPoint(CartesianCoordinate startPoint, int pixels, int angleinDegree) {
		// TODO Auto-generated method stub
		double yDirection = pixels * Math.sin(Math.toRadians(angleinDegree));
		double xDirection = pixels * Math.cos(Math.toRadians(angleinDegree));
		double newXLength = startPoint.getX() + xDirection;
		double newYLength = startPoint.getY() + yDirection;

		return new CartesianCoordinate(newXLength, newYLength);
	}

	/**
	 * Rotates the turtle clockwise by the specified angle in degrees.
	 * 
	 * @param i The number of degrees to turn.
	 */
	public void turn(int degrees) {
		this.currentAngle = this.currentAngle + degrees;
	}

	/**
	 * Moves the pen off the canvas so that the turtle’s route isn’t drawn for any
	 * subsequent movements.
	 */
	public void putPenUp() {
		this.isPenDown = false;
	}

	/**
	 * Lowers the pen onto the canvas so that the turtle’s route is drawn.
	 */
	public void putPenDown() {
		this.isPenDown = true;
	}

	public void moveToCentre() {
		this.currentPoint = new CartesianCoordinate(Canvas.DEFAULT_X/2, Canvas.DEFAULT_Y/2);
		
	}

	public void moveToOrigin() {
		this.currentPoint = new CartesianCoordinate(0, 0);
		this.currentAngle = 0;
		
	}

	public void resetAngleToZero() {
		this.currentAngle = 0;
		
	}

}
