package person;

public class Hulk extends Person implements SuperHero{

	public Hulk() {
		super("Bruce Banner", 12);
	}

	@Override
	public void activateSuperPower() {
		System.out.println("AMGER");
	}

	@Override
	public void deactivateSuperPower() {
		System.out.println("calmn");		
	}

}
