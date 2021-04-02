package turtle;

import drawing.Canvas;

public class DynamicTurtle extends Turtle {
	private int speed = 100;

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public DynamicTurtle(Canvas canvas) {
		super(canvas);
		drawTurtle();
	}

	public void update(int time) {
		undrawTurtle();
		int distance = (int) (time * 0.001 * this.speed);
		move(distance);
		drawTurtle();
	}

}
