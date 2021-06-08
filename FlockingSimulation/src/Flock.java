import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import boid.Boid;
import boid.DefaultBoid;
import boid.Obstacle;
import geometry.CartesianCoordinate;
import tools.Utils;

public class Flock {
	public static final int DEFAULT_COHESION_RADIUS = 100;
	public static final int DEFAULT_SEPARATION_RADIUS = 70;
	public static final int DEFAULT_ALIGNMENT_RADIUS = 100;
	public static final int DEFAULT_OBSTACLE_RADIUS = 50;
	public static final int DEFAULT_FLOCK_SIZE = 200;
	public final static int WINDOW_X_SIZE = 1500;
	public final static int WINDOW_Y_SIZE = 1000;

	protected int flockSize = DEFAULT_FLOCK_SIZE;

	protected double cohesionRadius = DEFAULT_COHESION_RADIUS;
	protected double separationRadius = DEFAULT_SEPARATION_RADIUS;
	protected double alignmentRadius = DEFAULT_ALIGNMENT_RADIUS;
	protected double obstacleRadius = DEFAULT_OBSTACLE_RADIUS;

	public static final double INITIAL_SEPARATION_WEIGHT = 4;
	public static final double INITIAL_ALIGNMENT_WEIGHT = 0.05;
	public static final double INITIAL_COHESION_WEIGHT = 0.2;
	public static final double INITIAL_OBSTACLE_WEIGHT = 3.5;

	public static final int DEFAULT_SLIDER_LENGTH = 10;
	public static final int DEFAULT_SLIDER_START = DEFAULT_SLIDER_LENGTH / 2;

	protected double separationWeight = INITIAL_SEPARATION_WEIGHT;
	protected double alignmentWeight = INITIAL_ALIGNMENT_WEIGHT;
	protected double cohesionWeight = INITIAL_COHESION_WEIGHT;
	protected static double obstacleWeight = INITIAL_OBSTACLE_WEIGHT;

	private JFrame frame = new JFrame();
	private JPanel sidePanel = new JPanel();
	private BoxLayout box = new BoxLayout(sidePanel, BoxLayout.Y_AXIS);
	private JSlider separationWeightSlide = new JSlider(SwingConstants.HORIZONTAL, 0, DEFAULT_SLIDER_LENGTH,
			DEFAULT_SLIDER_START);
	private JSlider cohesionWeightSlide = new JSlider(SwingConstants.HORIZONTAL, 0, DEFAULT_SLIDER_LENGTH,
			DEFAULT_SLIDER_START);
	private JSlider alignmentWeightSlide = new JSlider(SwingConstants.HORIZONTAL, 0, DEFAULT_SLIDER_LENGTH,
			DEFAULT_SLIDER_START);
	private JLabel sepLabel = new JLabel("Separation");
	private JLabel cohLabel = new JLabel("Cohesion");
	private JLabel aliLabel = new JLabel("Alignment");
	private JButton resetSlidersButton = new JButton();
	private JButton addBoidsButton = new JButton();

	private drawing.Canvas canvas = new drawing.Canvas();

	private List<Boid> flock;

	// Obstacles
	Obstacle obstacle;

	private boolean continueRunning;

	public Flock() {

		createObstacle();
		createBoids();
		setUpGUI();
		gameLoop();

	}

	public static void main(String[] args) {
		System.out.println("Running FlockingTest...");
		new Flock();
	}

	private void createObstacle() {
		obstacle = new Obstacle(canvas, 250, 150);

	}

	private void createBoids() {
		flock = Collections.synchronizedList(new ArrayList<Boid>());
		for (int i = 0; i < flockSize; i++) {
			flock.add(new DefaultBoid(canvas));
		}
	}

	private void setUpGUI() {
		frame.setTitle("Flocking Simulation");
		frame.setSize(WINDOW_X_SIZE, WINDOW_Y_SIZE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.add(canvas);
		frame.add(canvas, BorderLayout.CENTER);
		frame.add(sidePanel, BorderLayout.EAST);

		sidePanel.setLayout(box);

		separationWeightSlide.setMajorTickSpacing(5);
		separationWeightSlide.setMinorTickSpacing(1);
		separationWeightSlide.setPaintTicks(true);
		separationWeightSlide.setPaintLabels(true);

		sidePanel.add(sepLabel);
		sidePanel.add(separationWeightSlide);

		cohesionWeightSlide.setMajorTickSpacing(5);
		cohesionWeightSlide.setMinorTickSpacing(1);
		cohesionWeightSlide.setPaintTicks(true);
		cohesionWeightSlide.setPaintLabels(true);

		sidePanel.add(cohLabel);
		sidePanel.add(cohesionWeightSlide);

		alignmentWeightSlide.setMajorTickSpacing(5);
		alignmentWeightSlide.setMinorTickSpacing(1);
		alignmentWeightSlide.setPaintTicks(true);
		alignmentWeightSlide.setPaintLabels(true);

		sidePanel.add(aliLabel);
		sidePanel.add(alignmentWeightSlide);

		resetSlidersButton.setText("Click to reset sliders!");
		sidePanel.add(resetSlidersButton);

		addBoidsButton.setText("ADD BOID");
		sidePanel.add(addBoidsButton);

		separationWeightSlide.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce) {
				separationWeight = (separationWeightSlide.getValue() / 5) * INITIAL_SEPARATION_WEIGHT;
			}
		});

		cohesionWeightSlide.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce) {
				cohesionWeight = (cohesionWeightSlide.getValue() / 5) * INITIAL_COHESION_WEIGHT;
			}
		});

		alignmentWeightSlide.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce) {
				alignmentWeight = (alignmentWeightSlide.getValue() / 5) * INITIAL_ALIGNMENT_WEIGHT;
			}
		});

		resetSlidersButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				separationWeightSlide.setValue(DEFAULT_SLIDER_START);
				cohesionWeightSlide.setValue(DEFAULT_SLIDER_START);
				alignmentWeightSlide.setValue(DEFAULT_SLIDER_START);
			}
		});

		addBoidsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				flockSize = flockSize + 1;
			}
		});
		frame.setVisible(true);
	}

	public List<Boid> neighbours(Boid b, double distance) {
		List<Boid> neighbours = new ArrayList<>();
		synchronized (flock) {
			for (Boid boid : flock) {
				double neighbourDistance = b.distanceBetween(boid);
				if (boid != b && neighbourDistance <= distance && neighbourDistance >= 0) {
					neighbours.add(boid);
				}
			}
		}
		return neighbours;
	}

	private void gameLoop() {
		
		continueRunning = true;
		int deltaTime = 20;
		
		synchronized (flock) {
			for (Boid boid : flock) {
				boid.display();
			}
		}
		while (continueRunning) {
			synchronized (flock) {
				for (Boid boid : flock) {
					boid.hide();
				}
			}
			synchronized (flock) {
				for (Boid boid : flock) {
					List<Boid> separationNeighbours = neighbours(boid, separationRadius);
					List<Boid> cohesionNeighbours = neighbours(boid, cohesionRadius);
					List<Boid> alignmentNeighbours = neighbours(boid, alignmentRadius);

					List<Boid> obstaclesNeighbours = neighboursObstacles(boid, obstacleRadius);

					CartesianCoordinate separationForce = calculateSeparationForce(separationNeighbours, boid)
							.multiply(separationWeight).multiply(2);
					CartesianCoordinate cohesionForce = calculateCohesionForce(cohesionNeighbours, boid)
							.multiply(cohesionWeight).multiply(2);
					CartesianCoordinate alignmentForce = calculateAlignmentForce(alignmentNeighbours, boid)
							.multiply(alignmentWeight).multiply(2);
					CartesianCoordinate obstacleForce = calculateSeparationForce(obstaclesNeighbours, boid)
							.multiply(obstacleWeight).multiply(3);

					// boid.align(flock,alignmentRadius);

					// New position
					CartesianCoordinate acceleration = new CartesianCoordinate();
					acceleration.set(acceleration.add(obstacleForce));
					acceleration.set(acceleration.add(cohesionForce));
					acceleration.set(acceleration.add(separationForce));
					acceleration.set(acceleration.add(alignmentForce));

					CartesianCoordinate newVelocity = new CartesianCoordinate();
					newVelocity.set(boid.getVelocity().add(acceleration));
					boid.setVelocity(newVelocity);

					CartesianCoordinate newPos = new CartesianCoordinate();
					newPos.set(newPos.add(boid.getPosition()));
					newPos.set(newPos.add(newVelocity));
					boid.setPosition(newPos);

//					boid.update(deltaTime);
					boid.wrapPosition(WINDOW_X_SIZE, WINDOW_Y_SIZE);
				}
			}
			synchronized (flock) {
				for (Boid boid : flock) {
					boid.display();
				}
			}
			Utils.pause(deltaTime);
		}
	}

	private List<Boid> neighboursObstacles(Boid boid, double obstacleRadius) {
		List<Boid> obstaclesNeighbours = new ArrayList<>();
		List<CartesianCoordinate> obstacles = this.obstacle.obstaclePoints();
		for (CartesianCoordinate obstacle : obstacles) {
			double distanceNeighbor = boid.getPosition().add(obstacle.multiply(-1)).norm();
			if (distanceNeighbor <= obstacleRadius && distanceNeighbor >= 0) {
				obstaclesNeighbours.add(this.obstacle);
			}
		}
		return obstaclesNeighbours;
	}

	protected CartesianCoordinate calculateAlignmentForce(List<Boid> neighbours, Boid boid) {
		CartesianCoordinate force = new CartesianCoordinate();

		if (neighbours.size() > 0) {
			CartesianCoordinate averageVelocity = averageVelocity(neighbours);
			force = averageVelocity.sub(boid.getVelocity());
			force.set(force.normalize());
		}
		return force;
	}

	protected CartesianCoordinate calculateCohesionForce(List<Boid> neighbors, Boid boid) {
		CartesianCoordinate force = new CartesianCoordinate();
		if (neighbors.size() > 0) {
			CartesianCoordinate averagePos = averagePosition(neighbors);
			force.set(averagePos.sub(boid.getPosition()));
			force.set(force.normalize());
		}
		return force;
	}

	protected CartesianCoordinate calculateSeparationForce(List<Boid> neighbours, Boid boid) {
		CartesianCoordinate force = new CartesianCoordinate();
		int n = neighbours.size();
		double[] distance = new double[n];
		for (int i = 0; i < n; i++) {
			distance[i] = boid.distanceBetween(neighbours.get(i));
			if (distance[i] > 0) {
				CartesianCoordinate separation = boid.getPosition().sub(neighbours.get(i).getPosition());
				separation.set(separation.normalize());
				separation.set(separation.multiply(1 / distance[i]));
				force.set(force.add(separation));
			}
		}
		return force;
	}

	// Average position between boid
	public static CartesianCoordinate averagePosition(List<Boid> boids) {
		CartesianCoordinate[] pos = new CartesianCoordinate[boids.size()];
		for (int i = 0; i < boids.size(); i++) {
			pos[i] = boids.get(i).getPosition();
		}
		return CartesianCoordinate.average(pos);
	}

	// Average velocity between boid
	public static CartesianCoordinate averageVelocity(List<Boid> boids) {
		CartesianCoordinate[] velocity = new CartesianCoordinate[boids.size()];
		for (int i = 0; i < boids.size(); i++) {
			velocity[i] = boids.get(i).getVelocity();
		}
		return CartesianCoordinate.average(velocity);
	}
}
