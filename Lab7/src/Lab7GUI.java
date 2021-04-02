import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import drawing.Canvas;
import tools.Utils;
import turtle.DynamicTurtle;
import turtle.RandomTurtleB;

public class Lab7GUI {
	private final int WINDOW_X_SIZE = 800;
	private final int WINDOW_Y_SIZE = 600;
	private Canvas canvas = new Canvas();
	JFrame frame = new JFrame();
//	public DynamicTurtle turtle = new RandomTurtleB(canvas, WINDOW_Y_SIZE / 2, WINDOW_X_SIZE / 2);

	public Lab7GUI() {

		setupGUI(frame);
//		drawPictures(frame);
		runTurtleGame(canvas);
	}

	private void drawPictures(JFrame frame2) {
		ImageIcon supermanImage = new ImageIcon("superman.png");
		JLabel supermanJLabel = new JLabel(supermanImage);
		frame.add(supermanJLabel, BorderLayout.WEST);
		frame.revalidate();

		ImageIcon hulkImage = new ImageIcon("hulk.png");
		JLabel hulkJLabel = new JLabel(hulkImage);
		frame.add(hulkJLabel, BorderLayout.EAST);
		frame.revalidate();
	}

	private void setupGUI(JFrame frame) {
		frame.setTitle("Turtle Frame");
		frame.setSize(WINDOW_X_SIZE, WINDOW_Y_SIZE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.add(canvas);
	}

	public static void main(String[] args) {
		System.out.println("Lab 7 GUI Test");
		new Lab7GUI();
	}

	private void runTurtleGame(Canvas canvas) {
		RandomTurtleB turtle = new RandomTurtleB(canvas, WINDOW_Y_SIZE / 2, WINDOW_X_SIZE / 2);
		int deltaTime = 20;
		boolean continueRunning = true;
		
		JLabel label = new JLabel("Hello World!");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		frame.add(label, BorderLayout.SOUTH);
		frame.revalidate();
		
		
		// game loop
		while (continueRunning) {
			turtle.undrawTurtle();
			turtle.update(deltaTime);
			turtle.drawTurtle();
			Utils.pause(deltaTime);

			turtle.wrapPosition(WINDOW_X_SIZE, WINDOW_Y_SIZE);
			
			label.setText("(" + turtle.getPositionX() + ", " + turtle.getPositionY() + ")");
		}
		
	}
}
