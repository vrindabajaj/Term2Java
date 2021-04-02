public class AreaOfCircle {

	public static void main(String[] args) {
	
		double radius = 0.0;
		
		if (args.length > 0) {
				radius = Double.parseDouble(args[0]); 
			}
			else {
				System.out.println("no args");
				return;
			}
	
		
		double area = circleArea(radius);
		
		System.out.println("Area of circle is " + area);
	}
	
	
	
	static double circleArea(double radius) {
		
		final double PI = 3.14159;
		double area = PI*radius*radius;
		
		return area;
	}
}