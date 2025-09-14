package exceptions;

public class SameBookedRideDateException extends Exception{
	private static final long serialVersionUID = 1L;

	public SameBookedRideDateException(){
		super();
	}
	 
	public SameBookedRideDateException(String s) {
		super(s);
	}
}
