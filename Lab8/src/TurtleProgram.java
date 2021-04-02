import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import tools.Utils;
import turtle.DynamicTurtle;
import turtle.RandomTurtleB;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TurtleProgram {
	private int WINDOW_X_SIZE = 800;
	private int WINDOW_Y_SIZE = 600;

	private JFrame frame = new JFrame();
	private drawing.Canvas canvas = new drawing.Canvas();
	private JPanel lowerPanel = new JPanel();
	private JButton addTurtleButton = new JButton("Add Turtle");
	private JButton removeTurtleButton = new JButton("Remove Turtle");
	private FlowLayout flow = new FlowLayout();
	private List<DynamicTurtle> turtles;
	private boolean continueRunning;
	private JSlider turtleSpeed = new JSlider(SwingConstants.HORIZONTAL, 0, 1000, 100);

	public TurtleProgram() {
		setUpGUI();
		gameLoop();
	}

	private void setUpGUI() {
		frame.setTitle("Buttons");
		frame.setSize(WINDOW_X_SIZE, WINDOW_Y_SIZE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.add(canvas);
		frame.add(canvas, BorderLayout.CENTER);
		frame.add(lowerPanel, BorderLayout.SOUTH);
		
		lowerPanel.setLayout(flow);
		lowerPanel.add(addTurtleButton);
		
		turtleSpeed.setMajorTickSpacing(500);
		turtleSpeed.setMinorTickSpacing(100);
		turtleSpeed.setPaintTicks(true);
		turtleSpeed.setPaintLabels(true);
		
		lowerPanel.add(turtleSpeed);
		lowerPanel.add(removeTurtleButton);
		frame.setVisible(true);
		
		turtleSpeed.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce) {
				synchronized (turtles) {
					for (DynamicTurtle turtle : turtles) {
						turtle.setSpeed(turtleSpeed.getValue());
					}
				}
			}
		});
		
		addTurtleButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				turtles.add(new RandomTurtleB(canvas, 400, 300));
				turtles.get(turtles.size()-1).setSpeed(turtleSpeed.getValue());
			}
		});
		
		removeTurtleButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				if(turtles.size() > 0) {
					turtles.remove(turtles.size()-1).undrawTurtle();
				}
			}
		});

		turtles = Collections.synchronizedList(new ArrayList<DynamicTurtle>());

	}

	public static void main(String[] args) {
		System.out.println("Running TurtleProgram...");
		new TurtleProgram();
	}

	private void gameLoop() {
		int deltaTime = 20;
		continueRunning = true;
		while (continueRunning) {
			synchronized (turtles) {
				for (DynamicTurtle turtle : turtles) {
					turtle.undrawTurtle();
				}
			}
			synchronized (turtles) {
				for (DynamicTurtle turtle : turtles) {
					turtle.update(deltaTime);
					turtle.wrapPosition(canvas.getWidth(), canvas.getHeight());
				}
			}
			synchronized (turtles) {
				for (DynamicTurtle turtle : turtles) {
					turtle.drawTurtle();
				}
			}
			Utils.pause(deltaTime);
		}
	}
}
