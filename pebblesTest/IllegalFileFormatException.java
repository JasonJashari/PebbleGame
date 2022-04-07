
public class IllegalFileFormatException extends Exception {
	
	/**
	 * Constructs an instance of the exception with no message
	 */
	public IllegalFileFormatException() {
		// do nothing
	}
	
	/**
	 * Constructs an instance of the exception containing the message argument
	 * 
	 * @param message message containing details regarding the exception cause
	 */
	public IllegalFileFormatException(String message) {
		super(message);
	}

}
