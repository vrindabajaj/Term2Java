package boid;

import java.util.List;

import geometry.CartesianCoordinate;

public interface Boid {

	void align(List<Boid> flock);

	double distanceBetween(Boid boidB);

	double getCurrentAngle();

	void turn(double d);

	double getPositionX();

	double getPositionY();

	void hide();

	void update(int deltaTime);

	void wrapPosition(double wINDOW_X_SIZE, double wINDOW_Y_SIZE);

	void show();

	void cohesion(List<Boid> flock);
	
	void separation(List<Boid> flock);
	
	void setToPoint(CartesianCoordinate cartesianCoordinate);

	void move(double desiredDistance);

	void setCurrentAngle(double desiredAngle);
	
	int step();

	void moveToPoint(CartesianCoordinate cartesianCoordinate);	

}
