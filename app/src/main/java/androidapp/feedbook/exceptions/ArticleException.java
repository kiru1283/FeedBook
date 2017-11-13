package androidapp.feedbook.exceptions;

/**
 * Class to catch exceptions that occur when a URL is opened using runtime
 * @author Kiruthiga
 *
 */
public class ArticleException extends Exception {

	/**
	 * Constructor method to set the exception Message
	 * @param message
	 */
	public ArticleException(String message) {
		super(message);
		
	}

	
}
