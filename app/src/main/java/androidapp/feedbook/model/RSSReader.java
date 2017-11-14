package androidapp.feedbook.model;

import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.xml.sax.InputSource;

import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;


import androidapp.feedbook.exceptions.RSSException;

/**
 * Class used to read RSS feed based on url input
 * @author Kiruthiga
 *
 */
public class RSSReader extends AsyncTask<String, Void, Feed> {

	private final String url;
	private final String category;

	/**
	 * Constructor to set the category and feed url from user
	 * @param category
	 * @param feedUrl
	 */
	public RSSReader(String category, String feedUrl) {
		this.category = category;
		this.url = feedUrl;
	}

	/**
	 * Method used to read feed url and convert to a Syndfeed object of ROME api
	 * @return Syndfeed object of ROME api with feed url elements
	 * @throws RSSException - when error occurs while reading the feed url
	 */
	public SyndFeed getSyndFeedForUrl() throws RSSException {

		SyndFeed feed = null;
		InputStream inpstr = null;
		try {
			URLConnection openConnection = new URL(url).openConnection();
			
			//some websites have gzip encoding
			if ("gzip".equals(openConnection.getContentEncoding())) {
				inpstr = new GZIPInputStream(inpstr);
			}

			InputSource source = new InputSource(openConnection.getInputStream());

			SyndFeedInput input = new SyndFeedInput();

			feed = input.build(source);

			if (inpstr != null)
				inpstr.close();

		} catch (Exception e) {
			throw new RSSException(
					"Exception occured when building the feed from the url. Error Msg: " + e.getMessage());
		}

		return feed;
	}

	/**
	 * Method used to read the feed url and create the Feed object with the articles
	 * @return feed object with details from rss feed
	 * @throws RSSException - when error occurs while reading the feed url
	 */
	@SuppressWarnings("unchecked")
	//public Feed readFeed() throws  RSSException {
	protected Feed doInBackground(String... urls) {

		Feed feedObj = null;
		SyndFeed feed = null;
		try {
			feed = getSyndFeedForUrl();
		}catch (RSSException e)
		{
			Log.e("RSS Error",e.getMessage().toString());
		}

		feedObj = new Feed(category, "");
		int articleid = 0;

		for (SyndEntry entry : (List<SyndEntry>) feed.getEntries()) {

			SyndContentImpl descrip = (SyndContentImpl) entry.getDescription();

			Article message = new Article();
			message.setCreator(entry.getAuthor());
			//replace all special characters in the description
			message.setDescription(descrip.getValue().replaceAll("\\<[^>]*>", ""));

			message.setGuid(entry.getUri());

			message.setLink(entry.getLink());
			message.setTitle(entry.getTitle());
			message.setPubdate(entry.getPublishedDate().toString());
			message.setArticleid(articleid += 1);

			feedObj.getArticles().add(message);

		}

		return feedObj;
	}

	protected void onPostExecute(Feed feed) {
		// TODO: check this.exception
		// TODO: do something with the feed
	}

}
