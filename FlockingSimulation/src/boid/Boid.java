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

	void turn(double angle);

	double getCurrentAngle();


	void wrapPosition(double wINDOW_X_SIZE, double wINDOW_Y_SIZE);

	void display();


	void setPosition(CartesianCoordinate cartesianCoordinate);


	void setCurrentAngle(double desiredAngle);




}
