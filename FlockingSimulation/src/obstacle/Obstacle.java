package obstacle;

import java.awt.Color;
import java.awt.Graphics;

import boid.AbstractBoid;
import drawing.Canvas;
import geometry.CartesianCoordinate;

public class Obstacle extends AbstractBoid{
	Canvas myCanvas = new Canvas();

	public Obstacle(Canvas canvas, double xPosition, double yPosition) {
		super(canvas, xPosition, yPosition);
	}
	
	void drawObstacle(){
		moveToPoint(new CartesianCoordinate(250, 150));
		myCanvas.paint(Graphics.);
		
	}

}
