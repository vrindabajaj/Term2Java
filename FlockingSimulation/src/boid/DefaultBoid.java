package boid;

import drawing.Canvas;

public class DefaultBoid extends AbstractBoid {

	public DefaultBoid(Canvas canvas) {
		super(canvas);
	}

	@Override
	public void display() {
		putPenDown();
		move(25);
		putPenUp();
		turn(180);
		move(10);
		turn(180);
		turn(150);
		putPenDown();
		for (int i = 0; i < 3; i++) {
			move(20);
			turn(120);
		}
		putPenUp();
 		resetAngle();
	}


}
