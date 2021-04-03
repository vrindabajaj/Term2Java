package geometry;

public class CartesianCoordinate {
	private double xPosition;
	private double yPosition;

	public CartesianCoordinate(double x, double y) {
		xPosition = x;
		yPosition = y;

	}

	public double getX() {
		return xPosition;
	}

	public void setX(double xPosition) {
		this.xPosition = xPosition;
	}

	public double getY() {
		return yPosition;
	}

	public void setY(double yPosition) {
		this.yPosition = yPosition;
	}

	public String toString() {
		return "(" + xPosition + ", " + yPosition + ")";
	}
	
	public double length(CartesianCoordinate otherPoint) {
		double lengthA = this.getX() - otherPoint.getX();
		double lengthB = this.getY() - otherPoint.getY();

		return Math.sqrt(Math.pow(lengthA, 2) + Math.pow(lengthB, 2));

	}
}
