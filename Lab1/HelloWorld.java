public class HelloWorld {

	public static void main(String[] args) {
		String text = "Without Args !";
		if(args.length > 0) {
			print(args);
		} else {
			print(text);
		}
		
	}
	
	 static void print(Object something) {
		System.out.println(something);		
	}
}

