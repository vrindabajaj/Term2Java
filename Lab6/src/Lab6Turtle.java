import javax.swing.JFrame;
import drawing.Canvas;
import turtle.ProgrammableTurtle;

public class Lab6Turtle {
	private final int WINDOW_X_SIZE = 800;
	private final int WINDOW_Y_SIZE = 600;
	
	public Lab6Turtle() {
		JFrame frame = new JFrame();
		Canvas canvas= new Canvas();
		frame.setTitle("Turtle Frame");
		frame.setSize(WINDOW_X_SIZE, WINDOW_Y_SIZE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.add(canvas);	
		
		ProgrammableTurtle turtle;
		String programFile= "turtleProgram.txt";
		
	}
	
	public static void main(String[] args) {
		new Lab6Turtle();
	}
}
