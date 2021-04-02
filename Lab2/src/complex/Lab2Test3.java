package complex;

public class Lab2Test3 {

	public static void main(String[] args) {
		System.out.println("Test: Complex Number");

		ComplexNumber complexNumber = new ComplexNumber(2, 3);		
		//System.out.println("real = " + complexNumber.getReal() + ", imaginary = " + complexNumber.getImaginary());
		System.out.println(complexNumber);
		complexNumber.conjugate();
		System.out.println(complexNumber);
		ComplexNumber cn = new ComplexNumber(4, 5);
		complexNumber.add(cn);
		System.out.println(complexNumber);
		complexNumber.add(null);
		
	}
	

}
