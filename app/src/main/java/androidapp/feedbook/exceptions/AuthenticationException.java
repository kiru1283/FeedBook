package androidapp.feedbook.exceptions;

/**
 * Class to capture exception during password encryption 
 * @author Kiruthiga
 *
 */
public class AuthenticationException extends Exception {

	/**
	 * Constructor method to pass the custom exception to super class
	 * @param message
	 */
	public AuthenticationException(String message) {
		super(message);		
	}


}
