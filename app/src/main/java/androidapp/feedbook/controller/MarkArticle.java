package androidapp.feedbook.controller;

import android.content.Context;

import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import androidapp.feedbook.model.JSONReader;
import androidapp.feedbook.model.JSONWriter;
import androidapp.feedbook.exceptions.JSONFileException;
import androidapp.feedbook.model.JSONReader;
import androidapp.feedbook.model.JSONWriter;

/**
 * @author Kiruthiga
 * @category Controller class for marking articles as favourites
 */
public class MarkArticle {

	private Context context;
	private String favfilename = "favourites.json";

	public MarkArticle(Context context){
		this.context = context;
	}

	/**
	 * Method to save or remove the article from favourites.json
	 * @param category - the category chosen by the user while starring the article
	 * @param url - url link of the article marked as favourite
	 * @param inputUser - username of the logged in user
	 * @param remove - flag to indicate if the article needs to be removed or added
	 * @return - returns array of articles which have been stored in the favourites.json
	 * @throws JSONFileException - when there is an error reading the file favourites.json
	 */
	@SuppressWarnings("rawtypes")
	public JSONArray saveArticle(String category, String url, String inputUser, boolean remove) throws JSONFileException {

		
		boolean feedexists = false;
		JSONReader readObj = new JSONReader(favfilename,context);

		JSONArray arrFeed = readObj.jsonReader();

		if (arrFeed != null) {

			if (remove) {
				Iterator itarray = arrFeed.iterator();

				while (!feedexists && itarray.hasNext()) {

					JSONObject listObj = (JSONObject) itarray.next();

					if (listObj.get("category").equals(category) && listObj.get("url").equals(url)
							&& listObj.get("username").equals(inputUser)) {

						itarray.remove();

						feedexists = true;
					}
				}
			}

			JSONWriter jsonObj = new JSONWriter(favfilename,context);
			 jsonObj.jsonWrite(arrFeed, category, url, remove, inputUser);
		}
		return arrFeed;

	}

	/**
	 * Method to view the favourite articles marked by the user
	 * @param inputUser - username of the logged in user
	 * @return - array of articles which have been added by the user
	 * @throws JSONFileException - when there is an error reading the favourites.json file.
	 */
	@SuppressWarnings("unchecked")
	public JSONArray viewFavourites(String inputUser) throws JSONFileException {
		JSONReader readObj = new JSONReader(favfilename,context);

		JSONArray arrFeed = readObj.jsonReader();

		if (arrFeed != null) {
			Iterator<JSONObject> itarray = arrFeed.iterator();

			while (itarray.hasNext()) {
				JSONObject listObj = (JSONObject) itarray.next();
				if (!listObj.get("username").equals(inputUser)) {
					itarray.remove();
				}
			}
		}
		return arrFeed;
	}

}
