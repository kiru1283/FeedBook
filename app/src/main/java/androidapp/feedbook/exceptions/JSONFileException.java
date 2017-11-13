package androidapp.feedbook.exceptions;

/**
 * Class to capture exception while reading or writing Json files
 * @author Kiruthiga
 *
 */
public class JSONFileException extends Exception {


	/**
	 * Constructor method to pass the custom exception to super class
	 * @param message
	 */
	public JSONFileException(String message) {
		super(message);
	
	}

	
}
