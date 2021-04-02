import java.awt.BorderLayout;
import javax.swing.JFrame;

public class FlockingTest {
	private int WINDOW_X_SIZE = 1200;
	private int WINDOW_Y_SIZE = 900;

	private JFrame frame = new JFrame();
	private drawing.Canvas canvas = new drawing.Canvas();;
	
	public FlockingTest() {
		setUpGUI();
		
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
		
}
	
