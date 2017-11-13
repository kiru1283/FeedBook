package androidapp.feedbook.exceptions;

/**
 * Class to capture exceptions while verifying feed url
 * @author Kiruthiga
 *
 */
public class FeedException extends Exception {


	/**
	 * Constructor method to pass the custom exception to super class
	 * @param message
	 */
	public FeedException(String message) {
		super(message);
		
	}


}
