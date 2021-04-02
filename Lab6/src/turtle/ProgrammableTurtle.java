package turtle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import drawing.Canvas;

public class ProgrammableTurtle extends Turtle {
	private Scanner in;

	public ProgrammableTurtle(Canvas myCanvas, String fileName) throws FileNotFoundException {
		super(myCanvas);
		File inputFile = new File(fileName);
		in = new Scanner(inputFile);
	}

}
