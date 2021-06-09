import boid.Boid;
import boid.Bird;
import boid.Obstacle;
import drawing.Canvas;
import geometry.CartesianCoordinate;
import tools.Utils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Flock {

    public static final int DEFAULT_FLOCK_SIZE = 200;
    public static final int INITIAL_DELTA_TIME = 50;
    public static final int DEFAULT_COHESION_RADIUS = 100;
    public static final int DEFAULT_SEPARATION_RADIUS = 50;
    public static final int DEFAULT_ALIGNMENT_RADIUS = 100;
    public static final int DEFAULT_OBSTACLE_RADIUS = 50;

    public static final double INITIAL_SEPARATION_WEIGHT = 4;
    public static final double INITIAL_ALIGNMENT_WEIGHT = .1;
    public static final double INITIAL_COHESION_WEIGHT = .3;
    public static final double INITIAL_OBSTACLE_WEIGHT = 10;
    public static final double INITIAL_ORIENTATION_WEIGHT = .5;

    public final static int DEFAULT_CANVAS_WIDTH = 1200;
    public final static int DEFAULT_CANVAS_HEIGHT = 900;


    private double cohesionRadius = DEFAULT_COHESION_RADIUS;
    private double separationRadius = DEFAULT_SEPARATION_RADIUS;
    private double alignmentRadius = DEFAULT_ALIGNMENT_RADIUS;
    private double obstacleRadius = DEFAULT_OBSTACLE_RADIUS;
    // Obstacles
    Obstacle obstacle;
    private static double obstacleWeight = INITIAL_OBSTACLE_WEIGHT;

    private int flockSize = DEFAULT_FLOCK_SIZE;
    private double separationWeight = INITIAL_SEPARATION_WEIGHT;
    private double alignmentWeight = INITIAL_ALIGNMENT_WEIGHT;
    private double cohesionWeight = INITIAL_COHESION_WEIGHT;
    private double orientationWeight = INITIAL_ORIENTATION_WEIGHT;

    private double deltaTime = INITIAL_DELTA_TIME;
    private final drawing.Canvas canvas;
	private final List<Boid> flock = Collections.synchronizedList(new ArrayList<Boid>());
    private boolean continueRunning;





    public Flock() {
        canvas = new Canvas( DEFAULT_CANVAS_WIDTH,DEFAULT_CANVAS_HEIGHT);


        createObstacle();
        createBoids();
        setUpGUI();
        simulate();

    }

    public static void main(String[] args) {
        System.out.println("Running Flocking Simulation...");
        new Flock();
    }

    // Average position between boid
    public static CartesianCoordinate averagePosition(List<Boid> boids) {
        CartesianCoordinate[] pos = new CartesianCoordinate[boids.size()];
        for (int i = 0; i < boids.size(); i++) {
            pos[i] = boids.get(i).getPosition();
        }
        return CartesianCoordinate.average(pos);
    }

    // Average velocity between boid
    public static CartesianCoordinate averageVelocity(List<Boid> boids) {
        CartesianCoordinate[] velocity = new CartesianCoordinate[boids.size()];
        for (int i = 0; i < boids.size(); i++) {
            velocity[i] = boids.get(i).getVelocity();
        }
        return CartesianCoordinate.average(velocity);
    }


    public void createObstacle() {
        obstacle = new Obstacle(canvas, 250, 150);

    }

    public void createBoids() {

        flock.clear();
        for (int i = 0; i < flockSize; i++) {
            flock.add(new Bird(canvas));
        }
    }

    private void setUpGUI() {

        canvas.setBackground(Color.WHITE);
        UIFrame frame = new UIFrame(canvas, this);
    }

    public List<Boid> neighbours(Boid b, double distance) {
        List<Boid> neighbours = new ArrayList<>();
        synchronized (flock) {
            for (Boid boid : flock) {
                double neighbourDistance = b.distanceBetween(boid);
                if (boid != b && neighbourDistance <= distance && neighbourDistance >= 0) {
                    neighbours.add(boid);
                }
            }
        }
        return neighbours;
    }

    private void simulate() {

        continueRunning = true;


        while (continueRunning) {
            obstacle.display();

            synchronized (flock) {
                for (Boid boid : flock) {
                    boid.display();
                }
            }


            synchronized (flock) {
                for (Boid boid : flock) {
                    List<Boid> separationNeighbours = neighbours(boid, separationRadius);
                    List<Boid> cohesionNeighbours = neighbours(boid, cohesionRadius);
                    List<Boid> alignmentNeighbours = neighbours(boid, alignmentRadius);

                    List<Boid> obstaclesNeighbours = neighboursObstacles(boid, obstacleRadius);

                    CartesianCoordinate separationForce = calculateSeparationForce(separationNeighbours, boid)
                            .multiply(separationWeight);
                    CartesianCoordinate cohesionForce = calculateCohesionForce(cohesionNeighbours, boid)
                            .multiply(cohesionWeight);
                    CartesianCoordinate alignmentForce = calculateAlignmentForce(alignmentNeighbours, boid)
                            .multiply(alignmentWeight);
                    CartesianCoordinate obstacleForce = calculateSeparationForce(obstaclesNeighbours, boid)
                            .multiply(obstacleWeight);

                    // boid.align(flock,alignmentRadius);

                    // New position
                    CartesianCoordinate acceleration = new CartesianCoordinate();
                    acceleration.set(acceleration.add(obstacleForce));
                    acceleration.set(acceleration.add(cohesionForce));
                    acceleration.set(acceleration.add(separationForce));
                    acceleration.set(acceleration.add(alignmentForce));

                    CartesianCoordinate newVelocity = new CartesianCoordinate();
                    newVelocity.set(boid.getVelocity().add(acceleration));
                    boid.setVelocity(newVelocity);

                    CartesianCoordinate newPos = new CartesianCoordinate();
                    newPos.set(newPos.add(boid.getPosition()));
                    newPos.set(newPos.add(newVelocity));
                    boid.setPosition(newPos);

                    double orientation = calculateOrientation( boid) * orientationWeight;
                    boid.turn(orientation);



                    boid.wrapPosition(canvas.getWidth(), canvas.getHeight());
                }
            }
            synchronized (flock) {
                Utils.pause(deltaTime);
                canvas.clear();
            }

        }
    }

    private double calculateOrientation(Boid boid) {
        double desiredAngle = Math.toDegrees(boid.getVelocity().headingY() + Math.PI) + boid.getCurrentAngle() + 90 ;
        return desiredAngle;
    }

    private List<Boid> neighboursObstacles(Boid boid, double obstacleRadius) {
        List<Boid> obstaclesNeighbours = new ArrayList<>();
        List<CartesianCoordinate> obstacles = this.obstacle.obstaclePoints();
        for (CartesianCoordinate obstaclePoint : obstacles) {
            double distanceNeighbor = boid.getPosition().sub(obstaclePoint).norm();
            if (distanceNeighbor <= obstacleRadius && distanceNeighbor >= 0) {
                //obstaclesNeighbours.add(this.obstacle);
                obstaclesNeighbours.add( new Obstacle(canvas,obstaclePoint.getX(),obstaclePoint.getY()));
            }
        }
        return obstaclesNeighbours;
    }

    protected CartesianCoordinate calculateAlignmentForce(List<Boid> neighbours, Boid boid) {
        CartesianCoordinate force = new CartesianCoordinate();

        if (neighbours.size() > 0) {
            CartesianCoordinate averageVelocity = averageVelocity(neighbours);
            force = averageVelocity.sub(boid.getVelocity());
            force.set(force.normalize());
        }
        return force;
    }

    protected CartesianCoordinate calculateCohesionForce(List<Boid> neighbors, Boid boid) {
        CartesianCoordinate force = new CartesianCoordinate();
        if (neighbors.size() > 0) {
            CartesianCoordinate averagePos = averagePosition(neighbors);
            force.set(averagePos.sub(boid.getPosition()));
            force.set(force.normalize());
        }
        return force;
    }

    protected CartesianCoordinate calculateSeparationForce(List<Boid> neighbours, Boid boid) {
        CartesianCoordinate force = new CartesianCoordinate();
        int n = neighbours.size();
        double[] distance = new double[n];
        for (int i = 0; i < n; i++) {
            distance[i] = boid.distanceBetween(neighbours.get(i));
            if (distance[i] > 0) {
                CartesianCoordinate separation = boid.getPosition().sub(neighbours.get(i).getPosition());
                separation.set(separation.normalize());
                separation.set(separation.multiply(1 / distance[i]));
                force.set(force.add(separation));
            }
        }
        return force;
    }

    public int getFlockSize() {
        return flockSize;
    }

    public void setFlockSize(int flockSize) {
        this.flockSize = flockSize;
    }

    public double getDeltaTime() {
        return deltaTime;
    }

    public void setDeltaTime(double deltaTime) {
        this.deltaTime = deltaTime;
    }

    public void setSeparationWeight(double separationWeight) {
        this.separationWeight = separationWeight;
    }

    public void setAlignmentWeight(double alignmentWeight) {
        this.alignmentWeight = alignmentWeight;
    }

    public void setCohesionWeight(double cohesionWeight) {
        this.cohesionWeight = cohesionWeight;
    }


    public double getOrientationWeight() {
        return orientationWeight;
    }

    public void setOrientationWeight(double orientationWeight) {
        this.orientationWeight = orientationWeight;
    }
}
