import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import boid.Boid;
import boid.DefaultBoid;
import geometry.CartesianCoordinate;
import tools.Utils;

public class Flock {
	public static final int DEFAULT_COHESION_RADIUS = 100;
	public static final int DEFAULT_SEPARATION_RADIUS = 80;
	public static final int DEFAULT_ALIGNMENT_RADIUS = 80;
	public static final int DEFAULT_OBSTACLE_RADIUS = 50;
	public static final int DEFAULT_FLOCK_SIZE = 100;
	protected  int flockSize = DEFAULT_FLOCK_SIZE;
	private final static int WINDOW_X_SIZE = 1200;
	private final static int WINDOW_Y_SIZE = 900;

	protected double cohesionRadius = DEFAULT_COHESION_RADIUS;
	protected  double separationRadius = DEFAULT_SEPARATION_RADIUS;
	protected  double alignmentRadius = DEFAULT_ALIGNMENT_RADIUS;
	protected  double obstacleRadius = DEFAULT_OBSTACLE_RADIUS;

	protected static final  double DEFAULT_SEPARATION_WEIGHT = 4;
	protected static final double DEFAULT_ALIGNMENT_WEIGHT = 0.05;
	protected static final double DEFAULT_COHESION_WEIGHT = 0.3;

	protected  double separationWeight = DEFAULT_SEPARATION_WEIGHT;
	protected  double alignmentWeight = DEFAULT_ALIGNMENT_WEIGHT;
	protected  double cohesionWeight = DEFAULT_COHESION_WEIGHT;

	private JFrame frame = new JFrame();
	private drawing.Canvas canvas = new drawing.Canvas();
	private List<Boid> flock;
	private boolean continueRunning;

	public Flock() {

		createBoids();
		setUpGUI();
		gameLoop();

	}

	private void createBoids() {
		flock = new ArrayList<Boid>();
		for (int i = 0; i < flockSize; i++) {
			flock.add(new DefaultBoid(canvas));
		}
	}

	public static void main(String[] args) {
		System.out.println("Running FlockingTest...");
		new Flock();
	}

	private void setUpGUI() {
		frame.setTitle("Flocking Simulation");
		frame.setSize(WINDOW_X_SIZE, WINDOW_Y_SIZE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.add(canvas);
		frame.add(canvas, BorderLayout.CENTER);
		frame.setVisible(true);

	}

	private void gameLoop() {
		continueRunning = true;
		int deltaTime = 10;
		for (Boid boid : flock) {
			boid.turn(Math.random() * 360);
			//boid.setSpeed((int) (Math.random()*1000));
		}
		while (continueRunning) {
			for (Boid boid : flock) {
				boid.hide();
			}
			for (Boid boid : flock) {
				
				boid.align(flock, alignmentRadius);
				CartesianCoordinate separationForce = boid.separation(flock, separationRadius).multiply(separationWeight);
				CartesianCoordinate cohesionForce =boid.cohesion(flock, cohesionRadius).multiply(cohesionWeight);
                //New position
				CartesianCoordinate newPos = new CartesianCoordinate();
				newPos.set(newPos.add(boid.getPosition()));
				newPos.set(newPos.add(cohesionForce));
				newPos.set(newPos.add(separationForce));
				boid.setPosition(newPos);

//				boid.update(deltaTime);
				boid.wrapPosition(WINDOW_X_SIZE, WINDOW_Y_SIZE);
				
			}
			for (Boid boid : flock) {
				boid.display();
			}
			Utils.pause(deltaTime);

		}
	}

}