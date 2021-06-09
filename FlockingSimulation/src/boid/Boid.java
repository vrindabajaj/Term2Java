package boid;

import java.util.List;

import geometry.CartesianCoordinate;

public interface Boid {

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

	void setVelocity(CartesianCoordinate velocity);

	double getCurrentAngle();


	void wrapPosition(double wINDOW_X_SIZE, double wINDOW_Y_SIZE);

	void display(boolean orientation);


	void setPosition(CartesianCoordinate cartesianCoordinate);


	void setCurrentAngle(double desiredAngle);




}
