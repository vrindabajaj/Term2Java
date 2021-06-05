package boid;

import java.util.ArrayList;
import java.util.List;

import drawing.Canvas;
import geometry.CartesianCoordinate;
import geometry.LineSegment;

public abstract class AbstractBoid implements Boid {
    protected Canvas myCanvas;
    protected CartesianCoordinate currentPoint = new CartesianCoordinate(0, 0);
    private double currentAngle = 0;
    private boolean isPenDown;
    public static final double SECONDS_PER_MILLISECS = 0.001;
    protected static final  int SPEED_FACTOR = 200;




    protected CartesianCoordinate speedV = new CartesianCoordinate();
    protected  double speed;
    protected static double vMin = 1; //Minimum speed for a bird
    protected static double vMax = 4; //Maximum speed for a bird




    /**
     * Turtle constructor.
     *
     * @param myCanvas The canvas to use
     */
    public AbstractBoid(Canvas myCanvas) {
        this.myCanvas = myCanvas;
        initialise();
        this.display();
    }

    protected void initialise() {
        //set initial angle
        this.turn( Math.random()* 360);
        this.setPosition( new CartesianCoordinate( Math.random() * Canvas.DEFAULT_X, Math.random()* Canvas.DEFAULT_Y));
        this.setSpeed(Math.random() * SPEED_FACTOR);
        double speedX = Math.random() * (vMax - vMin) + vMin;
        double speedY = Math.random() * (vMax - vMin) + vMin;
        this.setSpeedV( new CartesianCoordinate(speedX,speedY));



    }

    public CartesianCoordinate getSpeedV() {
        return speedV;
    }

    public void setSpeedV(CartesianCoordinate speedV) {
        this.speedV = speedV;
    }
    /**
     * The turtle is moved in its current direction for the given number of pixels.
     * If the pen is down when the robot moves, a line will be drawn on the floor.
     *
     * @param size The number of pixels to move.
     */
    public void move(double size) {

        CartesianCoordinate start = currentPoint;
        CartesianCoordinate end = CartesianCoordinate.targetCoordinate(currentPoint, size, currentAngle);
        LineSegment lineSegment = new LineSegment(start, end);
        if (isPenDown) {
            myCanvas.drawLineSegment(lineSegment);
        }
        currentPoint = end;
    }

    @Override
    public CartesianCoordinate getPosition() {
        return currentPoint;
    }

    /**
     * Rotates the turtle clockwise by the specified angle in degrees.
     */
    public void turn(double angle) {
        this.currentAngle = this.currentAngle + angle;
    }

    /**
     * Moves the pen off the canvas so that the turtle’s route isn’t drawn for any
     * subsequent movements.
     */
    public void putPenUp() {
        this.isPenDown = false;
    }

    /**
     * Lowers the pen onto the canvas so that the turtle’s route is drawn.
     */
    public void putPenDown() {
        this.isPenDown = true;
    }

     public void setToOrigin() {
        setPosition(new CartesianCoordinate(0, 0));
        resetAngleToZero();
    }

    public void setPosition(CartesianCoordinate point) {
        this.currentPoint = point;
    }

    public void resetAngleToZero() {
        this.currentAngle = 0;
    }

    public double getCurrentAngle() {
        return currentAngle;
    }

    public void setCurrentAngle(double currentAngle) {
        this.currentAngle = currentAngle;
    }

    public void display() {
        putPenUp();
        move(29);
        putPenDown();
        turn(150);
        for (int i = 0; i < 3; i++) {
            move(50);
            turn(120);
        }
        turn(30);
        move(29);
        turn(180);
        putPenUp();
    }

    public void hide() {
        for (int i = 0; i < 4; i++) {
            myCanvas.removeMostRecentLine();
        }
        myCanvas.repaint();
    }

    public void wrapPosition(double maxXPos, double maxYPos) {
        if (currentPoint.getX() > maxXPos + 50) {
            currentPoint.setX(0);
        }
        if (currentPoint.getY() > maxYPos + 50) {
            currentPoint.setY(0);
        }
        if (currentPoint.getX() < 0 - 50) {
            currentPoint.setX(maxXPos);
        }
        if (currentPoint.getY() < 0 - 50) {
            currentPoint.setY(maxYPos);
        }
    }

    public double distanceBetween(Boid turtle) {
        return this.getPosition().add(turtle.getPosition().multiply(-1)).norm();

        //		double lengthA = this.getPositionX() - turtle.getPositionX();
        //		double lengthB = this.getPositionY() - turtle.getPositionY();
        //
        //		return Math.sqrt(Math.pow(lengthA, 2) + Math.pow(lengthB, 2));
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

     public void update(int time) {
        move(step(time));
    }


    public int step(int time) {
        int distance = (int) (time * SECONDS_PER_MILLISECS * this.speed);
        return distance;
    }

    @Override
    public void align(List<Boid> flock, double alignmentRadius) {
        double desiredAngle;
        double totalAngle = 0;
        int totalBoids = 0;
        int angleCoefficient = 10;
        Boid boidA = this;
        for (int j = 0; j < flock.size(); j++) {
            Boid boidB = flock.get(j);
            double distance = boidA.distanceBetween(boidB);
            if (boidA != boidB && distance < alignmentRadius) {
                totalBoids++;
                totalAngle = totalAngle + boidB.getCurrentAngle();
            }
        }
        if (totalBoids > 0) {
            desiredAngle = totalAngle / totalBoids;

            //			if (desiredAngle > this.currentAngle) {

            if (desiredAngle < this.currentAngle) {
                for (int i = 0; i < angleCoefficient; i++) {
                    boidA.turn(-1 * ((boidA.getCurrentAngle() - desiredAngle) / angleCoefficient));
                }
            } else {
                for (int i = 0; i < angleCoefficient; i++) {
                    boidA.turn((desiredAngle - boidA.getCurrentAngle()) / angleCoefficient);
                }
            }
        }
    }

    @Override
    public CartesianCoordinate cohesion(List<Boid> flock, double cohesionRadius) {
        CartesianCoordinate force = new CartesianCoordinate();
        List<Boid> neighbors = this.neighbours(flock,cohesionRadius);
        if (neighbors.size() > 0) {
            CartesianCoordinate averagePos = this.averagePos(neighbors);
            force.set(averagePos.add(this.getPosition().multiply(-1)));
            force.set(force.normalize());
        }
        return force;
    }

    //Average position between boid
    public static CartesianCoordinate averagePos(List<Boid> boids) {
        CartesianCoordinate[] pos = new CartesianCoordinate[boids.size()];
          for (int i = 0 ; i < boids.size() ; i++) {
            pos[i] = boids.get(i).getPosition();
        }
        return CartesianCoordinate.average(pos);
    }

    //Average position between boid
    public static CartesianCoordinate averageSpeed(List<Boid> boids) {
        CartesianCoordinate[] pos = new CartesianCoordinate[boids.size()];
        for (int i = 0 ; i < boids.size() ; i++) {
            pos[i] = boids.get(i).getSpeedV();
        }
        return CartesianCoordinate.average(pos);
    }

    @Override
    public CartesianCoordinate separation(List<Boid> flock, double separationRadius) {
        CartesianCoordinate force = new CartesianCoordinate();
        List<Boid> neighbours = this.neighbours(flock, separationRadius);
        int n = neighbours.size();
        double[] distance = new double[n];
        for(int i = 0 ; i < n ; i++) {
            distance[i] = this.distanceBetween(neighbours.get(i));
            if (distance[i] > 0) {
                CartesianCoordinate separation = this.getPosition().add(neighbours.get(i).getPosition().multiply(-1));
                separation.set(separation.normalize());
                separation.set(separation.multiply(1/distance[i]));
                force.set(force.add(separation));
            }
        }
        return force;

    }

    public List<Boid> neighbours(List<Boid> flock, double distance) {
        List<Boid> neighbours = new ArrayList<>();
        for (Boid boid : flock) {
            if (boid != this && this.distanceBetween(boid) < distance) {
                neighbours.add(boid);
            }
        }
        return neighbours;
    }

    @Override
    public void angleOnlySeparation(List<Boid> flock) {
        double totalAngle = 0;
        double desiredAngle = 0;
        int totalBoids = 0;
        Boid boidA = this;

        for (int j = 0; j < flock.size(); j++) {
            Boid boidB = flock.get(j);
            double distance = boidA.distanceBetween(boidB);
            if (boidA != boidB && distance < 50) {
                totalBoids++;

                totalAngle = totalAngle + relativeAngle(boidB);
            }
        }
        if (totalBoids > 0) {
            desiredAngle = (totalAngle / totalBoids);
            // System.out.println(desiredAngle);
            boidA.setCurrentAngle(desiredAngle);
        }
    }

    public int whichQuadrant(Boid otherBoid) {
        if (otherBoid.getPosition().getX() > this.getPosition().getX() && otherBoid.getPosition().getY() > this.getPosition().getY()) {
            return 1;
        }
        if (otherBoid.getPosition().getX() > this.getPosition().getX() && otherBoid.getPosition().getY() < this.getPosition().getY()) {
            return 2;
        }
        if (otherBoid.getPosition().getX() < this.getPosition().getX() && otherBoid.getPosition().getY() < this.getPosition().getY()) {
            return 3;
        }
        if (otherBoid.getPosition().getX() < this.getPosition().getX() && otherBoid.getPosition().getY() > this.getPosition().getY()) {
            return 4;
        }
        return 0;
    }

    public double relativeAngle(Boid otherBoid) {
        int quadrant = whichQuadrant(otherBoid);
        double relativeAngle = 0;
        double diffX = this.getPosition().getX() - otherBoid.getPosition().getX();
        double diffY = this.getPosition().getY() - otherBoid.getPosition().getY();
        double baseAngle = Math.atan2(diffY, diffX);

        if (quadrant == 1) {
            relativeAngle = 360 - baseAngle;
        }
        if (quadrant == 2) {
            relativeAngle = baseAngle;
        }
        if (quadrant == 3) {
            relativeAngle = 180 - baseAngle;
        }
        if (quadrant == 1) {
            relativeAngle = 180 + baseAngle;
        }

        return relativeAngle;

    }

    public boolean isInView(Boid otherBoid) {
        double boidRelAngle;
        if (this.currentAngle < 270 && this.currentAngle > 90) {
            boidRelAngle = relativeAngle(otherBoid) + (270 - this.currentAngle);
        } else {
            boidRelAngle = relativeAngle(otherBoid) - (270 - this.currentAngle);
        }

        if (boidRelAngle > 130 && boidRelAngle < 50) {
            return true;
        }
        return false;
    }
}
