package androidapp.feedbook.model;

import android.os.AsyncTask;
import android.util.Log;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndContentImpl;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.google.code.rome.android.repackaged.com.sun.syndication.fetcher.FeedFetcher;
import com.google.code.rome.android.repackaged.com.sun.syndication.fetcher.impl.HttpURLFeedFetcher;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;

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
			/*
			original code used in console app
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

*/
			FeedFetcher feedFetcher = new HttpURLFeedFetcher();
			feed =  feedFetcher.retrieveFeed( new URL( url) );


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
	protected Feed doInBackground(String... urls)  {

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
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		if(feed !=null) {
			for (SyndEntry entry : (List<SyndEntry>) feed.getEntries()) {

				SyndContentImpl descrip = (SyndContentImpl) entry.getDescription();

				Article message = new Article();
				message.setCreator(entry.getAuthor());
				//replace all special characters in the description
				message.setDescription(descrip.getValue().replaceAll("\\<[^>]*>", ""));

				message.setGuid(entry.getUri());

				message.setLink(entry.getLink());
				message.setTitle(entry.getTitle());
				message.setPubdate(entry.getPublishedDate() == null ? "" : sdf.format(entry.getPublishedDate()));
				message.setArticleid(articleid += 1);

				feedObj.getArticles().add(message);

			}
		}else
		{
			//throw new RSSException("This URL is not a valid RSS Feed.");
		}

		return feedObj;
	}


}
