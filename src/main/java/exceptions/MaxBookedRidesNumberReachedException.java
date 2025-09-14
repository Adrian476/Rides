package exceptions;

public class MaxBookedRidesNumberReachedException extends Exception{
	 private static final long serialVersionUID = 1L;

	 public MaxBookedRidesNumberReachedException(){
		 super();
	 }
	 
	 public MaxBookedRidesNumberReachedException(String s) {
		 super(s);
	 }
}
