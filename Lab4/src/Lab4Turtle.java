import javax.swing.JFrame;
import drawing.Canvas;
import tools.Utils;
import turtle.DynamicTurtle;

public class Lab4Turtle {
	private JFrame frame;
	private Canvas canvas;
	private final int WINDOW_X_SIZE = 800;
	private final int WINDOW_Y_SIZE = 600;
	private DynamicTurtle turtle;
	private boolean continueRunning = true;

	public Lab4Turtle() {
		frame = new JFrame();
		canvas = new Canvas();
		turtle = new DynamicTurtle(canvas);

		frame.setTitle("Turtle Frame");
		frame.setSize(WINDOW_X_SIZE, WINDOW_Y_SIZE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.add(canvas);
	}

	public static void main(String[] args) {
		System.out.println("Lab 4 Turtle test");
		Lab4Turtle lab4Turtle = new Lab4Turtle();
		lab4Turtle.runTurtleGame();

	}

	private void runTurtleGame() {
		turtle.undrawTurtle();
		DynamicTurtle turtle = new DynamicTurtle(canvas);

		int deltaTime = 20;

		turtle.turn(90);
		turtle.move(300);
		turtle.turn(270);
		turtle.move(80);

		while (continueRunning) {
			Utils.pause(deltaTime);
			turtle.update(deltaTime);
		}

	}

	private void testTurtle() {
		turtle.moveToCentre();
		turtle.putPenDown();
		
		//Draw a square
		for (int i = 1; i <= 4; i++) {
			turtle.move(100);
			Utils.pause(1000);
			turtle.drawTurtle();
			Utils.pause(1000);
			turtle.undrawTurtle();
			turtle.turn(90);

		}

	}

}
