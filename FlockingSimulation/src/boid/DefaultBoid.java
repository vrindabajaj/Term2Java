package boid;

import drawing.Canvas;

public class DefaultBoid extends AbstractBoid {

	public DefaultBoid(Canvas canvas) {
		super(canvas);
	}

	@Override
	public void display() {

		rotate();
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
		
//		turn(30);
//		move(26);
//		turn(180);
		putPenUp();
 		resetAngleToZero();
	}

	private void rotate() {
		double desiredAngle = Math.toDegrees(getVelocity().headingY() + Math.PI) + 90 ;
		turn(desiredAngle);
	}
}
