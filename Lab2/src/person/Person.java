package person;

public class Person {
	private String name;
	private int age;

	public Person(String name, int age) {
		this.name = name;
		this.age = checkAge(age);
	}

	public int getAge() {
		return age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAge(int age) {
		this.age = checkAge(age);
	}

	private int checkAge(int age) {
		if (age < 0) {
			age = Math.abs(age);
		}
		return age;
	}

	public void sayHello() {
		System.out.println("Hello, I'm " + getName() + ", and I'm " + getAge() + " years old.");
	}

}
