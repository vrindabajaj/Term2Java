import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import boid.Boid;
import boid.DefaultBoid;
import tools.Utils;

public class FlockingTest {
	private int WINDOW_X_SIZE = 1200;
	private int WINDOW_Y_SIZE = 900;

	private JFrame frame = new JFrame();
	private drawing.Canvas canvas = new drawing.Canvas();
	private List<Boid> flock;
	private boolean continueRunning;

	public FlockingTest() {

		flock = new ArrayList<Boid>();
		for (int i = 0; i < 100; i++) {
			flock.add(new DefaultBoid(canvas, Math.random() * 1200, Math.random() * 900));
		}
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
		for (Boid boid : flock) {
			boid.turn(Math.random() * 360);
//			boid.setSpeed((int) (Math.random()*1000));
		}
		while (continueRunning) {
			for (Boid boid : flock) {
				boid.hide();
			}
			for (Boid boid : flock) {
				
				boid.align(flock);
//				boid.separation(flock);
				boid.cohesion(flock);

//				boid.update(deltaTime);
				boid.wrapPosition(WINDOW_X_SIZE, WINDOW_Y_SIZE);
				
			}
			for (Boid boid : flock) {
				boid.show();
			}
			Utils.pause(deltaTime);

		}
	}

}
