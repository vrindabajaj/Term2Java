package boid;

import drawing.Canvas;

public class DefaultBoid extends AbstractBoid{

	public DefaultBoid(Canvas canvas, double xPosition, double yPosition) {
		super(canvas, xPosition, yPosition);
	}
	
	public DefaultBoid(Canvas myCanvas) {
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
	public void hide() {
		super.hide();
	}
}
