package boid;

import geometry.CartesianCoordinate;
/**
 * The Boid interface contains methods which pertain to each individual boid's properties.
 * @author Y3879165
 * 
 */
public interface Boid {

	/**
	 * 
	 * @return x and y coordinate of boid
	 */
	CartesianCoordinate getPosition();

	/**
	 *
	 * @param boid
	 * @return distance between boids
	 */
	double distanceBetween(Boid boid);

	/**
	 *
	 * @return velocity of Boid
	 */
	CartesianCoordinate getVelocity();

	/**
	 * method to apply new velocity
	 * @param velocity
	 */
	void setVelocity(CartesianCoordinate velocity);

	/**
	 * method to to change orientation
	 * @param angle
	 */
	void turn(double angle);

	/**
	 * return current direction
	 * @return
	 */
	double getCurrentAngle();

	/**
	 * to keep boid in bounds
	 * @param width
	 * @param height
	 */
	void wrapPosition(double width, double height);

	/**
	 * to draw a shape of boid
	 */
	void display();

	/**
	 * to set the new position of boid
	 * @param cartesianCoordinate
	 */
	void setPosition(CartesianCoordinate cartesianCoordinate);






}
