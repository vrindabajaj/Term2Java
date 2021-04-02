package turtle;

import drawing.Canvas;
import geometry.CartesianCoordinate;

public class DynamicTurtle extends Turtle {
	public static final double MILLISECONDS_PER_SECOND = 0.001;
	private int speed = 100;
	
	
	public DynamicTurtle(Canvas canvas) {
		super(canvas);
		show();
	}
	
	public DynamicTurtle(Canvas canvas, double xPosition, double yPosition) {
		super(canvas);
		this.moveToPoint(new CartesianCoordinate(xPosition, yPosition));
		this.show();
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}


	public void update(int time) {
		int distance = (int) (time * MILLISECONDS_PER_SECOND * this.speed);
		move(distance);
	}

}
