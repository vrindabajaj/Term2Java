package person;

public class Superperson extends Person implements SuperHero {

	public Superperson() {
		super("Clark Kent", 40);
	}

	@Override
	public void activateSuperPower() {
		System.out.println("Power Activated!");
	}

	@Override
	public void deactivateSuperPower() {
		System.out.println("Power Deactivated!");
	}

}
