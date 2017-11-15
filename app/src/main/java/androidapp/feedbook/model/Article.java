package androidapp.feedbook.model;

/**
 * @author Kiruthiga
 * Class for Articles in a Feed
 *
 */
public class Article {
	private int articleid = 0;
	private String description;
	private String link;
	private String title;
	private String creator;
	private String pubdate;
	private String guid;
	

	public Article(){
	//	articleid++;
		//currentarticle = articleid;
		
	}
	
	/**
	 * Method to set value of article number
	 * @param articleid - the serial number of the article in the feed 
	 */
	public void setArticleid(int articleid) {
		this.articleid = articleid;
	}
	
	/**
	 * Method to get value of article number
	 * @return articleid - the serial number of the article in the feed
	 */
	public int getArticleid() {
		return articleid;
	}

	/**
	 * Method to get article description
	 * @return - description of article in the feed
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Method to set value of article description
	 * @param description - description of article in the feed
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Method to get the link of article from the feed
	 * @return - url of the article from the feed
	 */
	public String getLink() {
		return link;
	}

	/**
	 * Method to set link of the article from the feed
	 * @param link - url of the article from the feed
	 */
	public void setLink(String link) {
		this.link = link;
	}

	/**
	 * Method to get title of article from feed
	 * @return - title of the article from feed
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Method to set the article title
	 * @param title - title of the article from the feed
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Method to get the author of the article
	 * @return - name of the creator of the article from the feed
	 */
	public String getCreator() {
		return creator;
	}

	/**
	 * Method to set the value of author of the article
	 * @param creator - name of the creator of the article from the feed
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 * Method to get the publishdate of the article
	 * @return - publishdate of the article
	 */
	public String getPubdate() {
		return pubdate;
	}

	/**
	 * Method to set the publishdate of the article
	 * @param pubdate - publishdate of the article
	 */
	public void setPubdate(String pubdate) {
		this.pubdate = pubdate;
	}
	
	/**
	 * Method to get the Guid of the article
	 * @return - guid of the article from the feed
	 */
	public String getGuid() {
		return guid;
	}
	
	/**
	 * Method to set the Guid of the article
	 * @param guid - guid of the article from the feed
	 */
	public void setGuid(String guid) {
		this.guid = guid;
	}
	
	/**
	 * Overriden toString method to display details of the article
	 */
	 @Override
	    public String toString() {
	       /* return "Article No."+articleid +" [title=" + title + "; description=" + description
	                + "; link=" + link + "; creator=" + creator + "; guid=" + guid
	                + "; pubdate=" + pubdate + "]";*/
	       return "link=" + link  +"| title=" + title + "| description=" + description
				   + "| pubdate=" + pubdate ;
	    }

	
}
