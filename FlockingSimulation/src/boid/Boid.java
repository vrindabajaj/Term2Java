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
		putPenUp();
		move(29);
		putPenDown();
		turn(150);
		for (int i = 0; i < 3; i++) {
			move(30);
			turn(120);
		}
		turn(30);
		move(26);
		turn(180);
		putPenUp();
	}
	
	@Override
	public void update(int time) {
		super.update(time);
	}
	
	@Override
	public void undrawTurtle() {
		super.undrawTurtle();
	}
	
}
