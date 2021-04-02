package shape;

import geometry.CartesianCoordinate;
import turtle.Turtle;
import drawing.Canvas;

public abstract class Shape {
	protected Turtle turtle;
	protected double size;
	protected double angle; //0 degrees is along the x-axis
	
	public Shape(Canvas canvas, CartesianCoordinate position) {
		this.turtle = new Turtle(canvas);
		turtle.moveToPoint(position);
		this.size = 100;
		this.angle = 0;
	}
	
	public abstract void draw();
	
	public void paint() {
		
	}

	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		this.size = size;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

}
