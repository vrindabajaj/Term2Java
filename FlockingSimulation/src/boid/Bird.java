package boid;

import drawing.Canvas;
/**
 * This represents an individual bird of type {@link Boid}.
 * @author Y3879165
 *
 */
public class Bird extends AbstractBoid {
	
	public Bird(Canvas canvas) {
		super(canvas);
	}

	/**
	 * draws a triangular shape with a beak representing bird
	 */
	@Override
	public void display() {
		putPenDown();
		move(20);
		putPenUp();
		turn(180);
		move(7);
		turn(180);
		turn(150);
		putPenDown();
		for (int i = 0; i < 3; i++) {
			move(15);
			turn(120);
		}
		putPenUp();
 		resetAngle();
	}


}
