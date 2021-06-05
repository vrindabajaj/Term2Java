import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import boid.AbstractBoid;
import boid.Boid;
import boid.DefaultBoid;
import boid.Obstacle;
import geometry.CartesianCoordinate;
import tools.Utils;

public class Flock {
	public static final int DEFAULT_COHESION_RADIUS = 100;
	public static final int DEFAULT_SEPARATION_RADIUS = 80;
	public static final int DEFAULT_ALIGNMENT_RADIUS = 80;
	public static final int DEFAULT_OBSTACLE_RADIUS = 300;
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
	protected static final double DEFAULT_OBSTACLE_WEIGHT = 3.5;

	protected  double separationWeight = DEFAULT_SEPARATION_WEIGHT;
	protected  double alignmentWeight = DEFAULT_ALIGNMENT_WEIGHT;
	protected  double cohesionWeight = DEFAULT_COHESION_WEIGHT;
	protected static double obstacleWeight = DEFAULT_OBSTACLE_WEIGHT; //Separation coefficient for an obstacle

	private JFrame frame = new JFrame();
	private drawing.Canvas canvas = new drawing.Canvas();


	private List<Boid> flock;

	//Obstacles
	Obstacle obstacle;

	private boolean continueRunning;

	public Flock() {

		createObstacle();
		createBoids();
		setUpGUI();
		gameLoop();

	}

	private void createObstacle() {
		obstacle = new Obstacle(canvas,250, 150);

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


	public List<Boid> neighbours(Boid b, double distance) {
		List<Boid> neighbours = new ArrayList<>();
		for (Boid boid : flock) {
			double neighbourDistance = b.distanceBetween(boid);
			if (boid != b && neighbourDistance <= distance && neighbourDistance >=0 ) {
				neighbours.add(boid);
			}
		}
		return neighbours;
	}



	private void gameLoop() {
		continueRunning = true;
		int deltaTime = 10;
		for (Boid boid : flock) {
			boid.display();
		}
		while (continueRunning) {
			for (Boid boid : flock) {
				boid.hide();
			}
			for (Boid boid : flock) {
				List<Boid> separationNeighbours = neighbours(boid, separationRadius);
				List<Boid> cohesionNeighbours = neighbours(boid, cohesionRadius);
				List<Boid> alignmentNeighbours = neighbours(boid, alignmentRadius);

				List<Boid> obstaclesNeighbours = neighboursObstacles( boid, obstacleRadius);



				CartesianCoordinate separationForce = calculateSeparationForce( separationNeighbours, boid).multiply(separationWeight).multiply(40);
				CartesianCoordinate cohesionForce = calculateCohesionForce( cohesionNeighbours,boid).multiply(cohesionWeight).multiply(50);
				CartesianCoordinate alignmentForce =calculateAlignmentForce( alignmentNeighbours,boid).multiply(alignmentWeight).multiply(40);

				CartesianCoordinate obstacleForce = calculateSeparationForce( obstaclesNeighbours,boid).multiply(obstacleWeight).multiply(100);

				//New position
				CartesianCoordinate newVelocity = new CartesianCoordinate();
				newVelocity.set(newVelocity.add(obstacleForce));
				newVelocity.set(newVelocity.add(boid.getVelocity()));
				newVelocity.set(newVelocity.add(cohesionForce));
				newVelocity.set(newVelocity.add(separationForce));
				newVelocity.set(newVelocity.add(alignmentForce));
				boid.setVelocity(newVelocity);

				CartesianCoordinate newPos = new CartesianCoordinate();
				newPos.set(newPos.add(boid.getPosition()));
				newPos.set(newPos.add(newVelocity));
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

	private List<Boid> neighboursObstacles(Boid boid, double obstacleRadius) {
		List<Boid> obstaclesNeighbours = new ArrayList<>();
		double distanceNeighbor = boid.distanceBetween(this.obstacle);
		if (distanceNeighbor <= obstacleRadius && distanceNeighbor >= 0) {
			obstaclesNeighbours.add(this.obstacle);
		}
		return  obstaclesNeighbours;
	}

	protected CartesianCoordinate calculateAlignmentForce(List<Boid> neighbours, Boid boid) {
		CartesianCoordinate force = new CartesianCoordinate();

		if (neighbours.size() > 0) {
			CartesianCoordinate averageSpeed = averageVelocity(neighbours);
			force = averageSpeed.add(boid.getVelocity().multiply(-1));
			force.set(force.normalize());
		}
		return force;
	}

	protected CartesianCoordinate calculateCohesionForce(List<Boid> neighbors, Boid boid) {
		CartesianCoordinate force = new CartesianCoordinate();
		if (neighbors.size() > 0) {
			CartesianCoordinate averagePos = averagePos(neighbors);
			force.set(averagePos.add(boid.getPosition().multiply(-1)));
			force.set(force.normalize());
		}
		return force;
	}



	protected CartesianCoordinate calculateSeparationForce(List<Boid> neighbours, Boid boid) {
		CartesianCoordinate force = new CartesianCoordinate();
		int n = neighbours.size();
		double[] distance = new double[n];
		for(int i = 0 ; i < n ; i++) {
			distance[i] = boid.distanceBetween(neighbours.get(i));
			if (distance[i] > 0) {
				CartesianCoordinate separation = boid.getPosition().add(neighbours.get(i).getPosition().multiply(-1));
				separation.set(separation.normalize());
				separation.set(separation.multiply(1/distance[i]));
				force.set(force.add(separation));
			}
		}
		return force;
	}

	//Average position between boid
	public static CartesianCoordinate averagePos(List<Boid> boids) {
		CartesianCoordinate[] pos = new CartesianCoordinate[boids.size()];
		for (int i = 0 ; i < boids.size() ; i++) {
			pos[i] = boids.get(i).getPosition();
		}
		return CartesianCoordinate.average(pos);
	}

	//Average position between boid
	public static CartesianCoordinate averageVelocity(List<Boid> boids) {
		CartesianCoordinate[] pos = new CartesianCoordinate[boids.size()];
		for (int i = 0 ; i < boids.size() ; i++) {
			pos[i] = boids.get(i).getVelocity();
		}
		return CartesianCoordinate.average(pos);
	}

}
