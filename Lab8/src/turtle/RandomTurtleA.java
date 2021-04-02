package turtle;

import drawing.Canvas;

public class RandomTurtleA extends DynamicTurtle {

	public RandomTurtleA(Canvas canvas) {
		super(canvas);
	}

	public RandomTurtleA(Canvas canvas, double xPosition, double yPosition) {
		super(canvas, xPosition, yPosition);
	}

	@Override
	public void update(int time) {
		turn((Math.random() * 360)-180);
		int distance = (int) (time * MILLISECONDS_PER_SECOND * getSpeed());
		move(distance);
	}

}
