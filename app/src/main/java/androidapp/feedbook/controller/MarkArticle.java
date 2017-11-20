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

public class MarkArticle {

	private Context context;
	private String favfilename = "favourites.json";

	public MarkArticle(Context context){
		this.context = context;
	}
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
