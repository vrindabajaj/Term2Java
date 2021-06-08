import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import boid.Boid;
import boid.DefaultBoid;
import boid.Obstacle;
import geometry.CartesianCoordinate;
import tools.Utils;

public class Flock {

	public static final int DEFAULT_FLOCK_SIZE = 200;
	public final static int WINDOW_X_SIZE = 1500;
	public final static int WINDOW_Y_SIZE = 1000;
	public static final int INITIAL_DELTA_TIME = 50;


	protected int flockSize = DEFAULT_FLOCK_SIZE;

	public static final int DEFAULT_COHESION_RADIUS = 150;
	public static final int DEFAULT_SEPARATION_RADIUS = 50;
	public static final int DEFAULT_ALIGNMENT_RADIUS = 100;
	public static final int DEFAULT_OBSTACLE_RADIUS = 80;
	protected double cohesionRadius = DEFAULT_COHESION_RADIUS;
	protected double separationRadius = DEFAULT_SEPARATION_RADIUS;
	protected double alignmentRadius = DEFAULT_ALIGNMENT_RADIUS;
	protected double obstacleRadius = DEFAULT_OBSTACLE_RADIUS;

	public static final double INITIAL_SEPARATION_WEIGHT = 4;
	public static final double INITIAL_ALIGNMENT_WEIGHT = .06;
	public static final double INITIAL_COHESION_WEIGHT = .3;
	public static final double INITIAL_OBSTACLE_WEIGHT = 4;

	public static final int DEFAULT_SLIDER_LENGTH = 10;
	public static final int SLIDER_STARTING_POSITION = DEFAULT_SLIDER_LENGTH / 2;
	public static final double SLIDER_WEIGHT = SLIDER_STARTING_POSITION*1.0;

	protected double separationWeight = INITIAL_SEPARATION_WEIGHT;
	protected double alignmentWeight = INITIAL_ALIGNMENT_WEIGHT;
	protected double cohesionWeight = INITIAL_COHESION_WEIGHT;
	protected static double obstacleWeight = INITIAL_OBSTACLE_WEIGHT;



	private double deltaTime = INITIAL_DELTA_TIME;

	private JFrame frame = new JFrame();
	private JPanel sidePanel = new JPanel();
	private BoxLayout box = new BoxLayout(sidePanel, BoxLayout.Y_AXIS);
	private JSlider separationWeightSlide = new JSlider(SwingConstants.HORIZONTAL, 0, DEFAULT_SLIDER_LENGTH,
			SLIDER_STARTING_POSITION);
	private JSlider cohesionWeightSlide = new JSlider(SwingConstants.HORIZONTAL, 0, DEFAULT_SLIDER_LENGTH,
			SLIDER_STARTING_POSITION);
	private JSlider alignmentWeightSlide = new JSlider(SwingConstants.HORIZONTAL, 0, DEFAULT_SLIDER_LENGTH,
			SLIDER_STARTING_POSITION);
	private JSlider speedSlide = new JSlider(SwingConstants.HORIZONTAL, 0, DEFAULT_SLIDER_LENGTH,
			SLIDER_STARTING_POSITION);
	private JLabel sepLabel = new JLabel("Separation");
	private JLabel cohLabel = new JLabel("Cohesion");
	private JLabel aliLabel = new JLabel("Alignment");
	private JButton resetSlidersButton = new JButton();


	private drawing.Canvas canvas = new drawing.Canvas();

	private List<Boid> flock = Collections.synchronizedList(new ArrayList<Boid>());;

	// Obstacles
	Obstacle obstacle;

	private boolean continueRunning;

	public Flock() {

		setUpGUI();
		createObstacle();
		createBoids();
		gameLoop();

	}

	public static void main(String[] args) {
		System.out.println("Running Flocking Simulation...");
		new Flock();
	}

	private void createObstacle() {
		obstacle = new Obstacle(canvas, 250, 150);

	}

	protected void createBoids() {

		flock.clear();
		for (int i = 0; i < flockSize; i++) {
			flock.add(new DefaultBoid(canvas));
		}
	}

	private void setUpGUI() {
		frame.setTitle("Flocking Simulation");
		frame.setSize(WINDOW_X_SIZE, WINDOW_Y_SIZE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		canvas.setBackground(Color.WHITE);

		frame.add(canvas);
		frame.add(canvas, BorderLayout.CENTER);
		frame.add(sidePanel, BorderLayout.EAST);

		sidePanel.setLayout(box);

		sidePanel.add(Box.createVerticalStrut(30));
		separationWeightSlide.setMajorTickSpacing(5);
		separationWeightSlide.setMinorTickSpacing(1);
		separationWeightSlide.setPaintTicks(true);
		separationWeightSlide.setPaintLabels(true);

		sidePanel.add(sepLabel);
		sidePanel.add(separationWeightSlide);

		sidePanel.add(Box.createVerticalStrut(30));
		cohesionWeightSlide.setMajorTickSpacing(5);
		cohesionWeightSlide.setMinorTickSpacing(1);
		cohesionWeightSlide.setPaintTicks(true);
		cohesionWeightSlide.setPaintLabels(true);
		//sidePanel.add( new JSeparator());

		sidePanel.add(cohLabel);
		sidePanel.add(cohesionWeightSlide);

		sidePanel.add(Box.createVerticalStrut(30));
		alignmentWeightSlide.setMajorTickSpacing(5);
		alignmentWeightSlide.setMinorTickSpacing(1);
		alignmentWeightSlide.setPaintTicks(true);
		alignmentWeightSlide.setPaintLabels(true);

		sidePanel.add(aliLabel);
		sidePanel.add(alignmentWeightSlide);


		sidePanel.add(Box.createVerticalStrut(30));
		JLabel speedLabel = new JLabel("Speed");
		speedSlide.setMajorTickSpacing(5);
		speedSlide.setMinorTickSpacing(1);
		speedSlide.setPaintTicks(true);
		speedSlide.setPaintLabels(true);

		sidePanel.add(speedLabel);
		sidePanel.add(speedSlide);

		speedSlide.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce) {
				double newDeltaTime = (DEFAULT_SLIDER_LENGTH - speedSlide.getValue())/ SLIDER_WEIGHT * INITIAL_DELTA_TIME;
				setDeltaTime(newDeltaTime);
			}
		});

		sidePanel.add(Box.createVerticalStrut(20));
		resetSlidersButton.setText("Click to reset sliders!");
		sidePanel.add(resetSlidersButton);

		sidePanel.add(Box.createVerticalStrut(30));



		Box hBox = Box.createHorizontalBox();
		JTextField boidsSizeField = new JTextField();
		boidsSizeField.setText( Integer.toString(flockSize) );
		boidsSizeField.setMinimumSize( new Dimension(50,20));
		boidsSizeField.setMaximumSize( new Dimension(150,40));
		boidsSizeField.setPreferredSize( new Dimension(100,30));
		hBox.add(boidsSizeField);

		JButton flockSizeButton = new JButton();
		flockSizeButton.setText("Set Flock Size");
		flockSizeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String sizeText = boidsSizeField.getText();
				try {
					int size = Integer.parseInt(sizeText);
					setFlockSize(size);
					createBoids();
				} catch(NumberFormatException exception) {
					boidsSizeField.setForeground(Color.RED);
				}
			}
		});

		hBox.add( flockSizeButton);
		sidePanel.add(hBox);


		separationWeightSlide.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce) {
				separationWeight = (separationWeightSlide.getValue() / SLIDER_WEIGHT) * INITIAL_SEPARATION_WEIGHT;
			}
		});

		cohesionWeightSlide.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce) {
				cohesionWeight = (cohesionWeightSlide.getValue() / SLIDER_WEIGHT) * INITIAL_COHESION_WEIGHT;
			}
		});

		alignmentWeightSlide.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce) {
				System.out.println( "Alignment Slide : " + alignmentWeightSlide.getValue());
				alignmentWeight = (alignmentWeightSlide.getValue() / SLIDER_WEIGHT) * INITIAL_ALIGNMENT_WEIGHT;
			}
		});

		resetSlidersButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				separationWeightSlide.setValue(SLIDER_STARTING_POSITION);
				cohesionWeightSlide.setValue(SLIDER_STARTING_POSITION);
				alignmentWeightSlide.setValue(SLIDER_STARTING_POSITION);
				speedSlide.setValue(SLIDER_STARTING_POSITION);
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

		

		while (continueRunning) {
			obstacle.display();

			synchronized (flock) {
				for (Boid boid : flock) {
					boid.display();
				}
			}
			Utils.pause(deltaTime);

			synchronized (flock) {
				for (Boid boid : flock) {
					List<Boid> separationNeighbours = neighbours(boid, separationRadius);
					List<Boid> cohesionNeighbours = neighbours(boid, cohesionRadius);
					List<Boid> alignmentNeighbours = neighbours(boid, alignmentRadius);

					List<Boid> obstaclesNeighbours = neighboursObstacles(boid, obstacleRadius);

					CartesianCoordinate separationForce = calculateSeparationForce(separationNeighbours, boid)
							.multiply(separationWeight);
					CartesianCoordinate cohesionForce = calculateCohesionForce(cohesionNeighbours, boid)
							.multiply(cohesionWeight);
					CartesianCoordinate alignmentForce = calculateAlignmentForce(alignmentNeighbours, boid)
							.multiply(alignmentWeight);
					CartesianCoordinate obstacleForce = calculateSeparationForce(obstaclesNeighbours, boid)
							.multiply(obstacleWeight);

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

					boid.wrapPosition(WINDOW_X_SIZE, WINDOW_Y_SIZE);
				}
			}
			synchronized (flock) {
//				for (Boid boid : flock) {
//					boid.hide();
//				}
				canvas.clear();
			}

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

	public void setFlockSize(int flockSize) {
		this.flockSize = flockSize;
	}

	public double getDeltaTime() {
		return deltaTime;
	}

	public void setDeltaTime(double deltaTime) {
		this.deltaTime = deltaTime;
	}
}
