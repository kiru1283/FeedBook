package androidapp.feedbook.exceptions;

/**
 * Class to capture exceptions that happen during RSS read operation
 * @author Kiruthiga
 *
 */
public class RSSException extends Exception {


	/**
	 * Constructor method to pass the custom exception to super class
	 * @param message
	 */
	public RSSException(String message) {
		super(message);

	}


}
