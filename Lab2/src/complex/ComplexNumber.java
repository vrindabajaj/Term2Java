package complex;

public class ComplexNumber {
	private int real;
	private int imaginary;

	public ComplexNumber(int real, int imaginary) {
		this.real = real;
		this.imaginary = imaginary;
	}

	public int getReal() {
		return real;
	}

	public void setReal(int real) {
		this.real = real;
	}

	public int getImaginary() {
		return imaginary;
	}

	public void setImaginary(int imaginary) {
		this.imaginary = imaginary;
	}

	public String toString() {
		String sign;
		if (imaginary >= 0) {
			sign = " + ";
		} else {
			sign = " - ";
		}
		return "(" + real + sign + Math.abs(imaginary) + "j" + ")";
	}

	public void conjugate() {
		this.imaginary = -1 * this.imaginary;
	}

	public void add(ComplexNumber cn) {
		if (cn != null) {
			this.real = this.real + cn.getReal();
			this.imaginary = this.imaginary + cn.getImaginary();
		}
	}
}
