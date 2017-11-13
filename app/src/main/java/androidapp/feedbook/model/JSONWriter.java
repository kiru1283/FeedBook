package androidapp.feedbook.model;

import android.content.Context;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import androidapp.feedbook.exceptions.JSONFileException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Class used for writing user and Feed details to Json files. 
 * @author Kiruthiga
 *
 */
public class JSONWriter {

	private String filename;
 private Context mcoContext;
	/**
	 * Constructor to set the name of the file to read
	 * @param filename
	 */
	public JSONWriter(String filename, Context mcoContext) {
		this.filename = filename;
		this.mcoContext = mcoContext;

	}

	/**
	 * Method to write all the Feed urls to DB.json
	 * @param jsonArray - list of feeds to be written to DB.json
	 * @param category -  category of feed subscribed
	 * @param url - link of feed subscribed
	 * @param removeUrl - boolean to indicate if feed needs to be removed(unsubscribed) from DB.json
	 * @param username - userid of logged in user
	 * @return - true if JSON file write operation completed successfully
	 * @throws JSONFileException - when error occurs while writing JSON file
	 */
	@SuppressWarnings("unchecked")
	public boolean jsonWrite(JSONArray jsonArray, String category, String url, boolean removeUrl, String username) throws JSONFileException {
		
		boolean retVal = false;		
		
		if (!removeUrl) {
			// to subscribe a feed
			JSONObject obj = new JSONObject();
			obj.put("username", username);
			obj.put("category", category);
			obj.put("url", url);
			

			if (jsonArray == null) {

				jsonArray = new JSONArray();
			}

			jsonArray.add(obj);
		}
		
		//retVal =fileWriter(jsonArray);
		retVal = wrtieFileOnInternalStorage(jsonArray.toJSONString());


		return retVal;

	}

	/**
	 * Method to write the userid and password to user.json
	 * @param jsonArray - list of users to be written to users.json
	 * @param userid - userid of the logged in user
	 * @param salt - random salt used for encrypting password
	 * @param pwd - encrypted password to be written to users.json
	 * @return - true if user details are written to Json file successfully
	 * @throws JSONFileException - when error occurs while writting to json file 
	 */
	@SuppressWarnings("unchecked")
	public boolean jsonUserWrite(JSONArray jsonArray, String userid, String salt, String pwd) throws JSONFileException {

		boolean retVal = false;
		
		JSONObject obj = new JSONObject();

		obj.put("userid", userid);
		obj.put("salt", salt);
		obj.put("pwd", pwd);

		if (jsonArray == null) {
			jsonArray = new JSONArray();
		}

		jsonArray.add(obj);
		
		//retVal = fileWriter(jsonArray);
		retVal = wrtieFileOnInternalStorage(jsonArray.toJSONString());

		return retVal;
	}


	private boolean wrtieFileOnInternalStorage  ( String sBody) throws JSONFileException{
		boolean retVal = false;
		File fileDir = new File(mcoContext.getFilesDir(),"FeedBookDir");
		//during first time app load when Directory for internal storage does not exists
		if(!fileDir.exists()){
			fileDir.mkdir();
		}

		try{
			File file = new File(fileDir, filename);
			FileWriter writer = new FileWriter(file);
			writer.append(sBody);
			writer.flush();
			writer.close();
			retVal = true;

		}catch (Exception e){
			throw new JSONFileException("Error Writing to file "+filename+". Error description: "+e.getMessage());
		}
		return retVal;
	}


}
