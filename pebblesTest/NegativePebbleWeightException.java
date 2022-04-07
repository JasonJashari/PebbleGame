
public class NegativePebbleWeightException extends Exception {
	/**
	 * Constructs an instance of the exception with no message
	 */
	public NegativePebbleWeightException() {
		// do nothing
	}
	
	/**
	 * Constructs an instance of the exception containing the message argument
	 * 
	 * @param message message containing details regarding the exception cause
	 */
	public NegativePebbleWeightException(String message) {
		super(message);
	}

}
