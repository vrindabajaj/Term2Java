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
	 * @return velocity of boid
	 */
	CartesianCoordinate getVelocity();

	void setVelocity(CartesianCoordinate velocity);

	void turn(double angle);

	double getCurrentAngle();


	void wrapPosition(double width, double height);

	void display();


	void setPosition(CartesianCoordinate cartesianCoordinate);


	void setCurrentAngle(double desiredAngle);




}
