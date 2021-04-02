import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import person.Hulk;
import person.Person;
import person.SuperHero;
import person.Superperson;

public class Lab7Test {

	public Lab7Test() {
		List<SuperHero> heroes = new ArrayList<SuperHero>();
		heroes.add(new Superperson());
		heroes.add(new Hulk());

		for (SuperHero superHero : heroes) {
			superHero.activateSuperPower();
		}

		for (SuperHero superHero : heroes) {
			superHero.deactivateSuperPower();
		}

	}

	public static void main(String[] args) {
		System.out.println("Lab 7 superhero Test");
		new Lab7Test();

	}

}
