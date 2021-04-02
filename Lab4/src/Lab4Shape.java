import javax.swing.JFrame;

import drawing.Canvas;
import geometry.CartesianCoordinate;
import shape.Circle;
import shape.Square;

public class Lab4Shape {
	private JFrame frame;
	private final int WINDOW_X_SIZE = 800;
	private final int WINDOW_Y_SIZE = 600;
	private Canvas canvas;

	public Lab4Shape() {
		frame = new JFrame();
		canvas = new Canvas();

		frame.setTitle("Shape Frame");
		frame.setSize(WINDOW_X_SIZE, WINDOW_Y_SIZE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.add(canvas);
	}

	public static void main(String[] args) {
		System.out.println("Lab 4 Shape test");
		Lab4Shape lab4shape = new Lab4Shape();
		lab4shape.testSquare();

	}

	private void testCircle() {
		CartesianCoordinate position = new CartesianCoordinate(400, 300);
		Circle circle = new Circle(canvas, position);
		circle.setSize(100);
		circle.draw();

	}

	private void testSquare() {
		CartesianCoordinate position = new CartesianCoordinate(400, 300);
		Square square = new Square(canvas, position);
		for (int i = 0; i <= 20; i++) {
			square.setAngle(i);
			square.setSize(i * 10 + 50);
			square.draw();
		}
	}

}
