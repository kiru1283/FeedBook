package androidapp.feedbook.controller;

import android.content.Context;

import androidapp.feedbook.model.JSONReader;

import java.util.ArrayList;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.util.List;
import java.util.concurrent.ExecutionException;

import androidapp.feedbook.exceptions.FeedException;
import androidapp.feedbook.exceptions.JSONFileException;
import androidapp.feedbook.exceptions.RSSException;
import androidapp.feedbook.model.Article;
import androidapp.feedbook.model.Feed;
import androidapp.feedbook.model.JSONWriter;
import androidapp.feedbook.model.RSSReader;

/**
 * Class for Managing actions performed by user on a RSS Feed 
 * @author Kiruthiga
 *
 */
public class ManageFeed {

	private Context context;
	
	private static final String dbfilename = "DB.json";
	public ManageFeed(Context context){
//       userfilename = Context.getResources().getAssets().open("filename");
		this.context = context;
	}

	/**
	 * Method to add the feed URL to DB.json file
	 * @param category - input category of feed being subscribed
	 * @param url - input url of feed being subscribed
	 * @param username - the current logged in user
	 * @return - returns true if the feed has been added to DB.json
	 * @throws FeedException - when  url is invalid
	 * @throws JSONFileException - when writing to DB.json fails
	 * @throws RSSException - when the url is not a valid RSS feed
	 */
	public boolean subscribeFeed(String category, String url, String username)
			throws FeedException, JSONFileException, RSSException {

		boolean removeFeed = false;
		boolean feedexists = false;

		boolean writeFile = false;

		JSONArray arrFeed = viewFeeds();

		if (arrFeed != null) {

			feedexists = verifyFeedExists(arrFeed, category, url, removeFeed, username);
		}

		if (!feedexists) {
			// read to check and load feed data from RSS
			List<String> articles = readFeedContent(category, url);

			if (articles.size() > 0) {

				// store the feed data
				JSONWriter jsonObj = new JSONWriter(dbfilename,context);
				writeFile = jsonObj.jsonWrite(arrFeed, category, url, removeFeed, username);

			} else {

				throw new FeedException("This feed has no data.");
			}

		} else {

			throw new FeedException("Feed url is already added.");
		}

		return writeFile;

	}

	//method to verify that the feed exists in the json file
	@SuppressWarnings("rawtypes")
	private boolean verifyFeedExists(JSONArray arrFeed, String category, String url, boolean remove, String username) {

		// verify is feed is already added
		boolean feedexists = false;

		// System.out.println(arrFeed.size());
		Iterator itarray = arrFeed.iterator();

		while (!feedexists && itarray.hasNext()) {

			JSONObject listObj = (JSONObject) itarray.next();

			if (listObj.get("category").equals(category) && listObj.get("url").equals(url)
					&& listObj.get("username").equals(username)) {

				if (remove) {
					itarray.remove();
				}
				feedexists = true;
			}
		}

		return feedexists;
	}

	/**
	 * Method to read all feeds added to a DB.json file
	 * @return - array of elements read from json file
	 * @throws JSONFileException - when errors are encountered  while reading file
	 */
	public JSONArray viewFeeds() throws JSONFileException {

		JSONReader readObj = new JSONReader(dbfilename,context);

		JSONArray arrFeed = readObj.jsonReader();
		return arrFeed;

	}

	/**
	 * Method to read content of a feed url
	 * @param category - category of feed entered by user
	 * @param url - url of feed entered by user
	 * @return - list of articles available in the feed
	 * @throws RSSException - when error reading the RSS feed
	 */
	public List<String> readFeedContent(String category, String url) throws RSSException {

		RSSReader parser = new RSSReader(category, url);
		//Feed feed = parser.readFeed();
		Feed feed = null;
		try {
			feed = parser.execute(url).get();
		} catch (InterruptedException | ExecutionException e) {
			throw new RSSException(e.getMessage());
		}

		List<Article> articles = null;
		List<String> articleDetails = new ArrayList<>();

		if (feed != null) {
			articles = feed.getArticles();
		}

		for (Article message : articles) {
			articleDetails.add(message.toString());
		}

		return articleDetails;

	}

	/**
	 * Method to verify that the feed url is valid and read content of feed url
	 * @param category - category of feed entered by user
	 * @param url - url of feed entered by user
	 * @param username - logged in userid
	 * @return - list of articles available in the feed
	 * @throws FeedException - when feed url is incorrect
	 * @throws JSONFileException - when error reading DB.json file
	 * @throws RSSException - when error reading the RSS feed
	 */
	public List<String> readFeed(String category, String url, String username)
			throws FeedException, JSONFileException, RSSException {

		boolean feedexists = false;

		JSONArray arrFeed = viewFeeds();

		if (arrFeed != null) {

			feedexists = verifyFeedExists(arrFeed, category, url, false, username);
		}

		if (feedexists) {
			/*RSSReader parser = new RSSReader(category, url);
			Feed feed = parser.readFeed();
			// System.out.println(feed);

			List<Article> articles = null; */
			List<String> articleDetails = readFeedContent(category, url);
			
			
			/*if (feed != null) {
				articles = feed.getArticles();
			}

			for (Article message : articles) {
				articleDetails.add(message.toString());
			}*/

			return articleDetails;
		} else {
			throw new FeedException(
					"Invalid Feed url or category. Please verify your entry and subscribe if its a new Feed.");
		}

	}

	/**
	 * Method to remove a feed from the list of subscribed feeds for the logged in user
	 * @param category - category of feed entered by user
	 * @param url - url of feed entered by user
	 * @param username - logged in userid
	 * @return - true when feed url removed from json file successfully
	 * @throws FeedException - when feed url is incorrect
	 * @throws JSONFileException - when error reading DB.json file
	 */
	public boolean removeFeed(String category, String url, String username) throws FeedException, JSONFileException {

		boolean removeFeed = true;
		boolean feedexists = false;
		boolean retVal = false;

		JSONArray arrFeed = viewFeeds();

		if (arrFeed != null) {
			feedexists = verifyFeedExists(arrFeed, category, url, removeFeed, username);
		}
		if (feedexists) {
			JSONWriter jsonObj = new JSONWriter(dbfilename,context);

			retVal = jsonObj.jsonWrite(arrFeed, category, url, removeFeed, username);

		} else {
			throw new FeedException("Feed can not be unsubscribed as url or category is incorrect");
		}

		return retVal;

	}
	
	@SuppressWarnings("unchecked")
	public JSONArray userFeeds(JSONArray userarrFeed, String inputUser) {		
		
		Iterator<JSONObject> itarray = userarrFeed.iterator();
		
		while (itarray.hasNext()) {
			JSONObject listObj = (JSONObject) itarray.next();
			if (!listObj.get("username").equals(inputUser)) {
					itarray.remove();
			}
		}
		
		return userarrFeed;
	}

}
