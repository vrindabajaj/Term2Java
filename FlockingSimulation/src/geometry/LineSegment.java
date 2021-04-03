package geometry;

public class LineSegment {
	private CartesianCoordinate startPoint;
	private CartesianCoordinate endPoint;

	public LineSegment(CartesianCoordinate start, CartesianCoordinate end) {
		startPoint = start;
		endPoint = end;
	}

	public CartesianCoordinate getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(CartesianCoordinate startPoint) {
		this.startPoint = startPoint;
	}

	public CartesianCoordinate getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(CartesianCoordinate endPoint) {
		this.endPoint = endPoint;
	}

	public String toString() {
		return "Line:" + startPoint + "-" + endPoint;
	}

}
