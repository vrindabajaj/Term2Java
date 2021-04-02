package exercise1;

public class FizzBuzz {

	public static void main(String[] args) {
		print100();

	}

	public static void print100() {
		int number = 1;
		while(number <= 100) {
			FizzBuzz(number);
			number++;
		}
	}
	
	public static void FizzBuzz(int n) {
		if (n%3 == 0 && n%5 == 0) {
			System.out.println("FizzBuzz");
		}
		else if (n%5 == 0) {
			System.out.println("Buzz");
		}
		else if (n%3 == 0) {
			System.out.println("Fizz");
		}
		else{
			System.out.println(n);
		}
	}
}
