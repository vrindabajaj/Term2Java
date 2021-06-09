package geometry;

/**
 * Cartesian Co-ordinate in 2D plane , provides 2D vector operations
 */
public class CartesianCoordinate {
    private double x;
    private double y;

    public CartesianCoordinate(double x, double y) {
        this.x = x;
        this.y = y;

    }

    public CartesianCoordinate() {
        this(0, 0);
    }

    public static void main(String[] args) {
        System.out.println("Heading X :" + Math.toDegrees(new CartesianCoordinate(-2, 2).headingX() + Math.PI / 2));
        System.out.println("Heading Y :" + Math.toDegrees(new CartesianCoordinate(-2, 2).headingY() + Math.PI / 2));


    }

    public final static CartesianCoordinate average(CartesianCoordinate[] coordinates) {
        CartesianCoordinate average = new CartesianCoordinate();
        double sumX = 0;
        double sumY = 0;
        for (CartesianCoordinate coordinate : coordinates) {
            sumX += coordinate.getX();
            sumY += coordinate.getY();
        }
        if (coordinates.length != 0) {
            average.setX(sumX / coordinates.length);
            average.setY(sumY / coordinates.length);
        }
        return average;
    }

    public final static CartesianCoordinate targetCoordinate(CartesianCoordinate startPoint, double size, double angleInDegree) {
        double yDirection = size * Math.sin(Math.toRadians(angleInDegree));
        double xDirection = size * Math.cos(Math.toRadians(angleInDegree));
        double newXLength = startPoint.getX() + xDirection;
        double newYLength = startPoint.getY() + yDirection;

        return new CartesianCoordinate(newXLength, newYLength);
    }


    public double length(CartesianCoordinate otherPoint) {
        double lengthA = this.getX() - otherPoint.getX();
        double lengthB = this.getY() - otherPoint.getY();

        return Math.sqrt(Math.pow(lengthA, 2) + Math.pow(lengthB, 2));

    }

    //Multiply by a constant
    public CartesianCoordinate multiply(double c) {
        return (new CartesianCoordinate(this.x * c, this.y * c));
    }

    //Add another co-ordinate
    public CartesianCoordinate add(CartesianCoordinate coordinate) {
        return (new CartesianCoordinate(this.x + coordinate.getX(), this.y + coordinate.getY()));
    }

    //Subtract another co-ordinate
    public CartesianCoordinate sub(CartesianCoordinate coordinate) {
        return (new CartesianCoordinate(this.x - coordinate.getX(), this.y - coordinate.getY()));
    }

    //Norm
    public double norm() {

        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    //Normalized co-ordinate
    public CartesianCoordinate normalize() {
        if (this.norm() > 0)
            return (this.multiply(1 / this.norm()));
        else
            return new CartesianCoordinate();
    }

    public double headingX() {
        return Math.atan2(y, x);
    }

    public double headingY() {
        return Math.atan2(x, y);
    }

    public void set(CartesianCoordinate coordinate) {
        this.setX(coordinate.getX());
        this.setY(coordinate.getY());
    }

    public void limit(double limitVal) {
        double mag = this.norm();
        if( mag > limitVal) {
            this.setX( this.getX()/mag * limitVal);
            this.setY( this.getY()/mag * limitVal);
        }
    }

    public double getX() {
        return x;
    }

    public void setX(double xPosition) {
        this.x = xPosition;
    }

    public double getY() {
        return y;
    }

    public void setY(double yPosition) {
        this.y = yPosition;
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
