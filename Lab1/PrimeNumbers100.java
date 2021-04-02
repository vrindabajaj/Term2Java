public class PrimeNumbers100 {

	public static void main(String[] args) {
		
	printPrime100();
	
	}
	
	
	static void printPrime100() {
		int number = 1;
		while (number <= 100) {
			boolean isPrime = true;
			for(int i=2; i<number; i++){
				if(number%i == 0){
					isPrime = false;
				}				
			}
			if(isPrime) {
				System.out.println("Prime: " + number);
			} else {
				//System.out.println("Not Prime: " + number);				
			}
			++number;
			
		}
	}
}
