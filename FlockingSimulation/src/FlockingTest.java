import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import boid.Boid;
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
		for (int i = 0; i < 30; i++) {
			flock.add(new Boid(canvas, Math.random() * 1200, Math.random() * 900));
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
				boid.undrawTurtle();
			}
			for (Boid boid : flock) {
				align(boid);
				boid.update(deltaTime);
				boid.wrapPosition(WINDOW_X_SIZE, WINDOW_Y_SIZE);
			}
			for (Boid boid : flock) {
				boid.show();
			}
			Utils.pause(deltaTime);

		}
	}

	private void align(Boid boid) {
		int perceptionRadius = 50;
		double desiredAngle;
		double totalAngle = 0;
		int totalBoids = 0;
		Boid boidA = boid;
		for (int j = 0; j < flock.size(); j++) {
			Boid boidB = flock.get(j);
			double distance = boidA.distanceBetween(boidB);
			System.out.println(distance);
			if (boidA != boidB && distance < perceptionRadius) {
				totalBoids++;
				totalAngle = totalAngle + boidB.getCurrentAngle();
			}
		}
		if (totalBoids > 0) {
			desiredAngle = totalAngle / totalBoids;
			boidA.turn(360 + desiredAngle - boidA.getCurrentAngle());
		}
	}
}
