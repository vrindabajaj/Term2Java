package boid;

import drawing.Canvas;
import turtle.DynamicTurtle;

public class Boid extends DynamicTurtle{

	public Boid(Canvas canvas, double xPosition, double yPosition) {
		super(canvas, xPosition, yPosition);
	}
	
	public Boid(Canvas myCanvas) {
		super(myCanvas);
	}
	@Override
	public void show() {
		putPenDown();
		int size = 40;
		turn(165);
		move(size);
		turn(105);
		move(21);
		turn(105);
		move(size);
		turn(345);
		putPenUp();
	}
	
	@Override
	public void update(int time) {
		super.update(time);
	}
	
	@Override
	public void undrawTurtle() {
		for (int i = 0; i < 3; i++) {
			myCanvas.removeMostRecentLine();
		}
		myCanvas.repaint();
	}
	
}
