package person;

//import java.time.LocalDate;
//import java.time.LocalTime;

public class Lab2Test {

	public static void main(String[] args) {
		//System.out.println("Test: date:" + LocalDate.now() + ", time:" + LocalTime.now());
		
		Person alice;
		alice = new Person("Alice", 40);
		Person bob;
		bob = new Person("Bob", 25);
		
		alice.setName("Silly");
		alice.setAge(-30);
		System.out.println(alice.getName() + "'s age is "+ alice.getAge());
		
		bob.sayHello();

	}

}
