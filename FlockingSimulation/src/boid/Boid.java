package boid;

import turtle.Turtle;

public class Boid extends Turtle {
	
	
	public Boid() {
		super(null);
		drawBoid();
	}

	private void drawBoid() {
		putPenDown();
		int size = 40;
		int pointAngle = 30;
		turn(165);
		move(size + (size/4));
		turn(105);
		move((int) ((Math.sin(pointAngle/2)) * size));
		turn(105);
		move(size + (size/4));
		turn(345);
		putPenUp();
	}
	
	

}
