package boid;

import java.util.List;

import drawing.Canvas;
import geometry.CartesianCoordinate;
import geometry.LineSegment;

public abstract class AbstractBoid implements Boid{
	protected Canvas myCanvas;
	protected CartesianCoordinate currentPoint = new CartesianCoordinate(0, 0);
	private double currentAngle = 0;
	private boolean isPenDown;
	public static final double MILLISECONDS_PER_SECOND = 0.001;
	private int speed = 100;
	protected int perceptionRadius = 100;
	protected int time = 20;
	
	
	public AbstractBoid(Canvas canvas, double xPosition, double yPosition) {
		this(canvas);
		this.setToPoint(new CartesianCoordinate(xPosition, yPosition));
		this.show();
	}
	
	
	/**
	 * Turtle constructor.
	 * @param myCanvas The canvas to use
	 */
	public AbstractBoid(Canvas myCanvas) {
		this.myCanvas = myCanvas;
		setToOrigin();
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

	public void setToCentre() {
		setToPoint(new CartesianCoordinate(Canvas.DEFAULT_X / 2, Canvas.DEFAULT_Y / 2));
	}

	public void setToOrigin() {
		setToPoint(new CartesianCoordinate(0, 0));
		resetAngleToZero();
	}
	
	public void setToPoint(CartesianCoordinate point) {
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

	public void show() {
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
	
	public double distanceBetween(Boid turtle) {
		
		double lengthA = this.getPositionX() - turtle.getPositionX();
		double lengthB = this.getPositionY() - turtle.getPositionY();

		return Math.sqrt(Math.pow(lengthA, 2) + Math.pow(lengthB, 2));
	}
	
	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public void moveToPoint(CartesianCoordinate point) {
		double length = this.currentPoint.length(point);
		int step = step();
		int numberOfSteps = (int) (length / step);
		System.out.println(numberOfSteps);
		for (int i = 0; i < numberOfSteps; i++) {
			move(step);
		}
	}

	public void update(int time) {
		move(step());
	}

	
	@Override
	public int step() {
		int distance = (int) (time * MILLISECONDS_PER_SECOND * this.speed);
		return distance;
	}
	
	@Override
	public void align(List<Boid> flock) {
		double desiredAngle;
		double totalAngle = 0;
		int totalBoids = 0;
		Boid boidA = this;
		for (int j = 0; j < flock.size(); j++) {
			Boid boidB = flock.get(j);
			double distance = boidA.distanceBetween(boidB);
			if (boidA != boidB && distance < perceptionRadius) {
				totalBoids++;
				totalAngle = totalAngle + boidB.getCurrentAngle();
			}
		}
		if (totalBoids > 0) {
			desiredAngle = totalAngle / totalBoids;
			boidA.turn(360 + desiredAngle - boidA.getCurrentAngle());
		}
	}
	
	@Override
	public void cohesion(List<Boid> flock) {
		double desiredPositionX;
		double desiredPositionY;
		double totalLocationX = 0;
		double totalLocationY = 0;
		int totalBoids = 0;
		Boid boidA = this;
		for (int j = 0; j < flock.size(); j++) {
			Boid boidB = flock.get(j);
			double distance = boidA.distanceBetween(boidB);
			if (boidA != boidB && distance < perceptionRadius) {
				totalBoids++;
				totalLocationX = totalLocationX + boidB.getPositionX();
				totalLocationY = totalLocationY + boidB.getPositionY();
			}
		}
		if (totalBoids > 0) {
			desiredPositionX = totalLocationX / totalBoids;
			desiredPositionY = totalLocationY / totalBoids;
			
			boidA.moveToPoint(new CartesianCoordinate(desiredPositionX, desiredPositionY));
		}
	}
	
	@Override
	public void separation(List<Boid> flock) {
		double boidInterDistance;
		double inverseDistance;
		double totalInverseDistance = 0;
		double desiredDistance = 0;
		double totalAngle = 0;
		double desiredAngle = 0;
		
		int totalBoids = 0;
		Boid boidA = this;
		for (int j = 0; j < flock.size(); j++) {
			Boid boidB = flock.get(j);
			double distance = boidA.distanceBetween(boidB);
			if (boidA != boidB && distance < perceptionRadius) {
				
				totalBoids++;
				
				boidInterDistance = boidA.distanceBetween(boidB);
				inverseDistance = (1/(Math.pow(boidInterDistance, 2)));
				totalInverseDistance = totalInverseDistance + inverseDistance;
				totalAngle = totalAngle + boidB.getCurrentAngle();
			}
		}
		if (totalBoids > 0) {
			desiredDistance = totalInverseDistance / totalBoids;
			desiredAngle = (180 + (totalAngle/totalBoids));
			boidA.setCurrentAngle(desiredAngle);
			boidA.move(desiredDistance);
		}
			
		}
}
