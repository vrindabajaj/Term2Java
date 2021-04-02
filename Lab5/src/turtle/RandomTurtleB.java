package turtle;

import drawing.Canvas;

public class RandomTurtleB extends DynamicTurtle {
	private int counter;

	public RandomTurtleB(Canvas canvas) {
		super(canvas);
	}

	public RandomTurtleB(Canvas canvas, double xPosition, double yPosition) {
		super(canvas, xPosition, yPosition);
	}

	@Override
	public void update(int time) {
		if(counter == 0) {
			resetCounter();
			turn((Math.random() * 100)-50);
		}
		super.update(time);
		--counter;
	}

	private void resetCounter() {
		this.counter = (int) (Math.random() * 50);
		
	}
}
