package shape;

import drawing.Canvas;
import geometry.CartesianCoordinate;

public class Circle extends Shape{

	public Circle(Canvas canvas, CartesianCoordinate position) {
		super(canvas, position);
	}
	
	@Override
	public void draw() {
		double arcLength;
		turtle.putPenUp();
		turtle.move(size/2);
		turtle.putPenDown();
		
		arcLength = (2*(size/2)*Math.sin(Math.toRadians(0.5)));
		
		for (int i = 1; i <= 360; i++) {
			turtle.move(arcLength);
			turtle.turn(1);
		}
	}
	

}
