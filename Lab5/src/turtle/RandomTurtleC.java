package turtle;

import drawing.Canvas;

public class RandomTurtleC extends DynamicTurtle {
	private int counter;

	public RandomTurtleC(Canvas canvas) {
		super(canvas);
	}

	public RandomTurtleC(Canvas canvas, double xPosition, double yPosition) {
		super(canvas, xPosition, yPosition);
	}

	@Override
	public void update(int time) {
		if(counter == 0) {
			resetCounter();

			turn(((Math.random() * 1000))/time);
		}
		super.update(time);
		--counter;
	}

	private void resetCounter() {
		this.counter = (int) (Math.random() * 50);
		
	}
}
