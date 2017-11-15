package androidapp.feedbook.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing each feed url subscribed by user
 * @author Kiruthiga
 *
 */
public class Feed {
	
	private String categoryName;
	private String feedName;


	
	private List<Article> entries = new ArrayList<Article>();
	
	/**
	 * Constructor method to set the feedname and category name
	 * @param categoryName - category of feed
	 * @param feedName - name of feed
	 */
	public Feed(String categoryName,String feedName ){
		
		this.categoryName = categoryName;
		this.feedName = feedName;
		
	}
	
	/**
	 * Method to get the name of category from the feed
	 * @return categoryName - category of feed
	 */
	public String getCategoryName() {
		return categoryName;
	}

	/**
	 * Method to get the name of feed 
	 * @return feedName - name of feed
	 */
	public String getFeedName() {
		return feedName;
	}

	/**
	 * Method to get list of articles present in a feed
	 * @return - list of or articles 
	 */
	public List<Article> getArticles() {
        return entries;
    }

}
