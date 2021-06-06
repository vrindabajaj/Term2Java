import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
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
import geometry.CartesianCoordinate;
import tools.Utils;

public class Flock {
	public static final int DEFAULT_COHESION_RADIUS = 100;
	public static final int DEFAULT_SEPARATION_RADIUS = 80;
	public static final int DEFAULT_ALIGNMENT_RADIUS = 50;
	public static final int DEFAULT_OBSTACLE_RADIUS = 50;
	public static final int DEFAULT_FLOCK_SIZE = 200;
	protected int flockSize = DEFAULT_FLOCK_SIZE;
	private final static int WINDOW_X_SIZE = 1200;
	private final static int WINDOW_Y_SIZE = 900;

	protected double cohesionRadius = DEFAULT_COHESION_RADIUS;
	protected double separationRadius = DEFAULT_SEPARATION_RADIUS;
	protected double alignmentRadius = DEFAULT_ALIGNMENT_RADIUS;
	protected double obstacleRadius = DEFAULT_OBSTACLE_RADIUS;

	protected static final double DEFAULT_SEPARATION_WEIGHT = 4;
	protected static final double DEFAULT_ALIGNMENT_WEIGHT = 0.05;
	protected static final double DEFAULT_COHESION_WEIGHT = 0.3;

	protected double separationWeight = DEFAULT_SEPARATION_WEIGHT;
	protected double alignmentWeight = DEFAULT_ALIGNMENT_WEIGHT;
	protected double cohesionWeight = DEFAULT_COHESION_WEIGHT;

	private JFrame frame = new JFrame();
	private JPanel sidePanel = new JPanel();
	private BoxLayout box = new BoxLayout(sidePanel, BoxLayout.Y_AXIS);
	private JSlider separationWeightSlide = new JSlider(SwingConstants.HORIZONTAL, 0, 10, 4);
	private JSlider cohesionWeightSlide = new JSlider(SwingConstants.HORIZONTAL, 0, 10, 3);
	private JSlider alignmentWeightSlide = new JSlider(SwingConstants.HORIZONTAL, 0, 10, 5);
	private JLabel sepLabel = new JLabel("Separation");
	private JLabel cohLabel = new JLabel("Cohesion");
	private JLabel aliLabel = new JLabel("Alignment");
	private JButton resetSlidersButton = new JButton();

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
		frame.add(sidePanel, BorderLayout.EAST);

		sidePanel.setLayout(box);
//      lowerPanel.setBackground(Color.gray);

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
//		cohLabel.setFont(Font.PLAIN);

		alignmentWeightSlide.setMajorTickSpacing(5);
		alignmentWeightSlide.setMinorTickSpacing(1);
		alignmentWeightSlide.setPaintTicks(true);
		alignmentWeightSlide.setPaintLabels(true);

		sidePanel.add(aliLabel);
		sidePanel.add(alignmentWeightSlide);

		separationWeightSlide.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce) {
				separationWeight = separationWeightSlide.getValue();
			}
		});

		cohesionWeightSlide.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce) {
				cohesionWeight = (cohesionWeightSlide.getValue() / 10);
			}
		});

		alignmentWeightSlide.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce) {
				alignmentWeight = (alignmentWeightSlide.getValue() / 100);
			}
		});

		resetSlidersButton.setText("Click to reset sliders!");
		sidePanel.add(resetSlidersButton);
		
		frame.setVisible(true);
	}

	private void gameLoop() {
		continueRunning = true;
		int deltaTime = 10;
		for (Boid boid : flock) {
			boid.turn(Math.random() * 360);
			// boid.setSpeed((int) (Math.random()*1000));
		}
		while (continueRunning) {
			for (Boid boid : flock) {
				boid.hide();
			}
			for (Boid boid : flock) {

				boid.align(flock, alignmentRadius);
				CartesianCoordinate separationForce = boid.separation(flock, separationRadius)
						.multiply(separationWeight);
				CartesianCoordinate cohesionForce = boid.cohesion(flock, cohesionRadius).multiply(cohesionWeight);
				CartesianCoordinate alignmentForce = boid.alignmentForce(flock, alignmentRadius)
						.multiply(alignmentWeight);
				// New position
				CartesianCoordinate newVelocity = new CartesianCoordinate();
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
}
