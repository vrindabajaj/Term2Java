package boid;

import java.util.List;

import geometry.CartesianCoordinate;

public interface Boid {

	CartesianCoordinate getPosition();


	void align(List<Boid> flock, double alignmentRadius);

	CartesianCoordinate alignmentForce(List<Boid> flock, double alignmentRadius);

	double distanceBetween(Boid boidB);

	CartesianCoordinate getVelocity();
	void setVelocity(CartesianCoordinate velocity);

	double getCurrentAngle();

	void turn(double d);

	void hide();

	void update(int deltaTime);

	void wrapPosition(double wINDOW_X_SIZE, double wINDOW_Y_SIZE);

	void display();

	CartesianCoordinate cohesion(List<Boid> flock, double cohesionRadius);

	CartesianCoordinate separation(List<Boid> flock, double separationRadius);
	
	void setPosition(CartesianCoordinate cartesianCoordinate);


	void setCurrentAngle(double desiredAngle);

	void angleOnlySeparation(List<Boid> flock);	
	

    public boolean isInView(Boid otherBoid);

}
