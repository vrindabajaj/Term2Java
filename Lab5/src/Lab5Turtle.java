import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import drawing.Canvas;
import tools.Utils;
import turtle.DynamicTurtle;
import turtle.RandomTurtleC;

public class Lab5Turtle {
	private List<DynamicTurtle> turtles;

	private Canvas canvas;
	private final int WINDOW_X_SIZE = 800;
	private final int WINDOW_Y_SIZE = 600;

	public Lab5Turtle() {
		JFrame frame = new JFrame();
		canvas = new Canvas();
		frame.setTitle("Turtle Frame");
		frame.setSize(WINDOW_X_SIZE, WINDOW_Y_SIZE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.add(canvas);
		
		turtles = new ArrayList<DynamicTurtle>();
		
		for (int i = 0; i < 10; i++) {
			turtles.add(new RandomTurtleC(canvas, (Math.random()*800), (Math.random()*600)));
		}
		
		runTurtleGame();
	}

	private void runTurtleGame() {
		int deltaTime = 20;
		boolean continueRunning = true;
		// game loop
		while (continueRunning) {
			for (DynamicTurtle dynamicTurtle : turtles) {
				dynamicTurtle.undrawTurtle();
			}
			for (DynamicTurtle dynamicTurtle : turtles) {
				dynamicTurtle.update(deltaTime);
			}
			for (DynamicTurtle dynamicTurtle : turtles) {
				dynamicTurtle.drawTurtle();
			}
			Utils.pause(deltaTime);
		}
	}

	public static void main(String[] args) {
		new Lab5Turtle();
	}

}
