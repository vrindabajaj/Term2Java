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
/**
 * This class simulates the behaviours of animal flocks seen in nature. 
 * At its core it is made up of three main movements  cohesion, alignment, and separation.
 * The program allows users to change and control each of these variables as well as the speed and number
 * of the individuals (known as Boids, a term coined by Craig Reynolds, the creator of this algorithm).
 * 
 * @author Y3879165
 *
 */
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
    
    private Obstacle obstacle;
    private static double obstacleWeight = INITIAL_OBSTACLE_WEIGHT;

    private int flockSize = DEFAULT_FLOCK_SIZE;
    private double separationWeight = INITIAL_SEPARATION_WEIGHT;
    private double alignmentWeight = INITIAL_ALIGNMENT_WEIGHT;
    private double cohesionWeight = INITIAL_COHESION_WEIGHT;
    private double orientationWeight = INITIAL_ORIENTATION_WEIGHT;

    private double deltaTime = INITIAL_DELTA_TIME;
    private final drawing.Canvas canvas;
	private final List<Boid> flock = Collections.synchronizedList(new ArrayList<Boid>());


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

    protected void createObstacle() {
        obstacle = new Obstacle(canvas, 250, 150);
    }

    protected void createBoids() {
        flock.clear();
        for (int i = 0; i < flockSize; i++) {
            flock.add(new Bird(canvas));
        }
    }

    protected void setUpGUI() {
        canvas.setBackground(Color.WHITE);
        new UIFrame(canvas, this);
    }

    
    protected void simulate() {
    	
        while (true) {
            obstacle.display();

            synchronized (flock) {
                for (Boid boid : flock) {
                    boid.display();
                }
            }


            synchronized (flock) {
                for (Boid boid : flock) {
                	
                	//Calculating boid separation steering force
                    List<Boid> separationNeighbours = neighbours(boid, separationRadius);
                    CartesianCoordinate separationForce = calculateSeparationForce(separationNeighbours, boid)
                            .multiply(separationWeight);
                    
                    //Calculating cohesion steering force
                    List<Boid> cohesionNeighbours = neighbours(boid, cohesionRadius);
                    CartesianCoordinate cohesionForce = calculateCohesionForce(cohesionNeighbours, boid)
                            .multiply(cohesionWeight);

                    //Calculating alignment steering force
                    List<Boid> alignmentNeighbours = neighbours(boid, alignmentRadius);
                    CartesianCoordinate alignmentForce = calculateAlignmentForce(alignmentNeighbours, boid)
                            .multiply(alignmentWeight);
                    
                    //Calculating obstacle separation steering force
                    List<Boid> obstaclesNeighbours = neighboursObstacles(boid, obstacleRadius);
                    CartesianCoordinate obstacleForce = calculateSeparationForce(obstaclesNeighbours, boid)
                            .multiply(obstacleWeight);

                    //determine acceleration
                    CartesianCoordinate acceleration = new CartesianCoordinate();
                    acceleration.set(acceleration.add(obstacleForce));
                    acceleration.set(acceleration.add(cohesionForce));
                    acceleration.set(acceleration.add(separationForce));
                    acceleration.set(acceleration.add(alignmentForce));

                    //add acceleration to new velocity
                    CartesianCoordinate newVelocity = new CartesianCoordinate();
                    newVelocity.set(boid.getVelocity().add(acceleration));
                    // set new velocity to boid
                    boid.setVelocity(newVelocity);

                    //Add new velocity to current boid position to determine new position
                    CartesianCoordinate newPos = new CartesianCoordinate();
                    newPos.set(newPos.add(boid.getPosition()));
                    newPos.set(newPos.add(newVelocity));
                    boid.setPosition(newPos);

                    //apply the orientation relative to velocity
                    double orientation = calculateOrientation( boid) * orientationWeight;
                    boid.turn(orientation);

                    //Ensures that the boid remain within the frame
                    boid.wrapPosition(canvas.getWidth(), canvas.getHeight());
                }
            }
            synchronized (flock) {
            	//clears and redraws every deltaTime
                Utils.pause(deltaTime);
                canvas.clear();
            }
        }
    }
    /**
     * Determines the orientation angle so that the bond can be rotated in the direction of velocity.
     * @param boid
     * @return angle in degrees between 0 and 360
     */
    protected double calculateOrientation(Boid boid) {
        double desiredAngle = Math.toDegrees(boid.getVelocity().headingY() + Math.PI) + boid.getCurrentAngle() + 90 ;
        return desiredAngle;
    }


    /**
     * This method calculates the alignment force so that each boid is set to the average velocity of its neighbours
     * (neighbours determined using @alignmentRadius).
     * </b>
     * Force is calculated using this rule :
     * <ol>
     *   <li>Determine the velocities of each of the neighbours of the supplied boid.</li>
     *   <li>Calculate the average velocity</li>
     *   <li>Calculate the unit vector of the alignment force</li>
     * </ol>
     * @param neighbours list of boids that are within the supplied (@param boid) alignment perception radius
     * @param boid
     * @return alignment force, unit vector
     */
    protected CartesianCoordinate calculateAlignmentForce(List<Boid> neighbours, Boid boid) {
        CartesianCoordinate force = new CartesianCoordinate();

        if (neighbours.size() > 0) {
            CartesianCoordinate averageVelocity = averageVelocity(neighbours);
            force = averageVelocity.sub(boid.getVelocity());
            force.set(force.normalize());
        }
        return force;
    }

    /**
     * This method calculates the cohesion force, cohesion force is returned as a unit vector and is calculated by simply
     * averaging the position of neighbours (neighbours determined using @cohesionRadius).
     * </b>
     * Force is calculated using this rule :
     * <ol>
     *   <li>Determine the positions of each of the neighbours of the supplied boid.</li>
     *   <li>Calculate the average position</li>
     *   <li>Calculate the unit vector of the cohesion force</li>
     * </ol>
     * @param neighbors list of boids that are within the supplied (@param boid) cohesion perception radius
     * @param boid
     * @return cohesion force, unit vector
     */
    protected CartesianCoordinate calculateCohesionForce(List<Boid> neighbors, Boid boid) {
        CartesianCoordinate force = new CartesianCoordinate();
        if (neighbors.size() > 0) {
            CartesianCoordinate averagePos = averagePosition(neighbors);
            force.set(averagePos.sub(boid.getPosition()));
            force.set(force.normalize());
        }
        return force;
    }

    /**
     * This method calculates the separation force so that boids keep a small distance away from
     * other objects (including other boids). The purpose of this force is for boids to make sure they don't
     * collide with each other.
     * </b>
     * Force is calculated using this rule :
     * <ol>
     *   <li>Determine the displacement vector away from supplied boid and each of its neighbours</li>
     *   <li>Calculate unit vector of the displacement force</li>
     *   <li>Sum all the displacement forces, weight it by distance</li>
     * </ol>
     *
     * @param neighbours list of boids that are within the supplied (@param boid) separation perception radius
     * @param boid
     * @return separation force, unit vector
     */
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

    /**
     * Utility method to determine boids that are within the supplied distance.
     * @param b
     * @param distance
     * @return neighbours list
     */
    protected List<Boid> neighbours(Boid b, double distance) {
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

    /**
     * Each point on the perimeter of the @obstacle is treated as an obstacle.
     * @param boid
     * @param obstacleRadius
     * @return list of obstacles representing each point on the perimeter.
     */
    protected List<Boid> neighboursObstacles(Boid boid, double obstacleRadius) {
        List<Boid> obstaclesNeighbours = new ArrayList<>();
        List<CartesianCoordinate> obstacles = this.obstacle.obstaclePerimeter();
        for (CartesianCoordinate obstaclePoint : obstacles) {
            double distanceNeighbor = boid.getPosition().sub(obstaclePoint).norm();
            if (distanceNeighbor <= obstacleRadius && distanceNeighbor >= 0) {
                obstaclesNeighbours.add( new Obstacle(canvas,obstaclePoint.getX(),obstaclePoint.getY()));
            }
        }
        return obstaclesNeighbours;
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
}
