import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import drawing.Canvas;
import turtle.Turtle;

public class GeometryDrawer {
	private Turtle turtle;

	public GeometryDrawer() {
		super();
		// A JFrame is a window for containing graphical elements
		JFrame frame = new JFrame();

		// Set the title of the JFrame
		frame.setTitle("Canvas");

		// Set the size of the JFrame
		frame.setSize(800, 600);

		// Make pressing the ‘X’ close the window
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Make the JFrame visible
		frame.setVisible(true);

		// Add a window adapter that will make sure the program closes
		// after closing the window
		frame.addWindowListener((WindowListener) new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		// The Canvas which is provided for this course
		Canvas canvas = new Canvas();

		// Add the canvas to the window
		frame.getContentPane().add(canvas);

		// canvas.drawLineBetweenPoints(new CartesianCoordinate(20, 40), new
		// CartesianCoordinate(70, 100));
		turtle = new Turtle(canvas);

	}

	public void drawSquareGrid(int sideSize, int rows, int columns) {
		for (int row = 0; row < rows; row++) {
			moveToRowOrigin(turtle, row, sideSize);
			turtle.putPenDown();
			for (int column = 0; column < columns; column++) {
				drawSquare(turtle, sideSize);
				turtle.move(sideSize);
			}
		}
	}

	private void moveToRowOrigin(Turtle turtle, int row, int sideSize) {
		turtle.putPenUp();
		turtle.moveToOrigin();
		turtle.turn(90);
		turtle.move(row * sideSize);
		turtle.resetAngleToZero();
	}

	private void drawSquare(Turtle turtle, int size) {
		// turtle.moveToOrigin();
		turtle.putPenDown();
		for (int i = 1; i <= 4; i++) {
			turtle.move(size);
			turtle.turn(90);
		}
	}

	public void drawCircle(int radius) {
		turtle.moveToCentre();
		double circumference = 2 * Math.PI * radius;
		System.out.println(circumference);
		// turtle.turn(90);
		for (int i = 1; i < circumference; i++) {
			turtle.move(radius);
			turtle.putPenDown();
			turtle.move(1);
			turtle.putPenUp();
			turtle.moveToCentre();
			turtle.turn(1);
		}
	}

	public void drawHexagon(int sideSize) {
		turtle.resetAngleToZero();
		turtle.turn(30);
		turtle.putPenDown();

		for (int i = 0; i < 6; i++) {
			turtle.turn(60);
			turtle.move(sideSize);
		}
	}

	public void drawHexRow(int sideLength, int noOfHex) {
		turtle.moveToOrigin();
		turtle.turn(25);
		turtle.move(150);

		int length = (int) (Math.sqrt(3) * sideLength);
		// System.out.println(length);

		for (int i = 0; i < noOfHex; i++) {
			turtle.putPenUp();
			turtle.move(length);
			turtle.putPenDown();
			drawHexagon(sideLength);
			turtle.resetAngleToZero();
		}
		
		turtle.putPenUp();
		turtle.resetAngleToZero();
		turtle.turn(120);
		turtle.move(length);
		turtle.putPenDown();
		
		for (int i = 0; i < noOfHex-1; i++) {
			drawHexagon(sideLength);
			turtle.resetAngleToZero();
			turtle.turn(180);
			turtle.putPenUp();
			turtle.move(length);
			turtle.putPenDown();
		}
		
		turtle.putPenUp();
		turtle.resetAngleToZero();
		turtle.turn(60);
		turtle.move(length);
		turtle.putPenDown();
		
		for (int i = 0; i < noOfHex; i++) {
			drawHexagon(sideLength);
			turtle.resetAngleToZero();
			turtle.putPenUp();
			turtle.move(length);
			turtle.putPenDown();
		}
	}
}
