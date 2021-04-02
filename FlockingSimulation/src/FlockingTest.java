import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import boid.Boid;
import tools.Utils;
import turtle.Turtle;

public class FlockingTest {
	private int WINDOW_X_SIZE = 1200;
	private int WINDOW_Y_SIZE = 900;

	private JFrame frame = new JFrame();
	private drawing.Canvas canvas = new drawing.Canvas();
	private List<Boid> flock;
	private boolean continueRunning;

	public FlockingTest() {

		
		flock = new ArrayList<Boid>();
		Boid boid = new Boid(canvas, 600, 450);
		boid.resetAngleToZero();
		flock.add(boid);
		setUpGUI();
		gameLoop();

	}

	public static void main(String[] args) {
		System.out.println("Running FlockingTest...");
		new FlockingTest();
	}

	private void setUpGUI() {
		frame.setTitle("Buttons");
		frame.setSize(WINDOW_X_SIZE, WINDOW_Y_SIZE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.add(canvas);
		frame.add(canvas, BorderLayout.CENTER);
		frame.setVisible(true);

	}
	private void gameLoop() {
		continueRunning = true;
		int deltaTime = 20;
		while(continueRunning) {
			for (Boid boid : flock) {
				boid.show();
			}
			for (Boid boid : flock) {
				boid.update(deltaTime);
			}
//			for (Boid boid : flock) {
//				boid.undrawTurtle();
//			}
			Utils.pause(deltaTime);
		}
	}

}
