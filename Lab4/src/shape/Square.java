package shape;

import drawing.Canvas;
import geometry.CartesianCoordinate;

public class Square extends Shape {

	public Square(Canvas canvas, CartesianCoordinate position) {
		super(canvas, position);
	}

	@Override
	public void draw() {
		turtle.putPenDown();
		turtle.turn(angle);
		for(int i = 1; i <= 4; i++) {
			turtle.move(size);
			turtle.turn(90);
		}
		turtle.putPenUp();
		
	}

}
