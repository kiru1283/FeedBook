package androidapp.feedbook.model;

import android.content.Context;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import androidapp.feedbook.R;
import androidapp.feedbook.exceptions.JSONFileException;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import static android.app.PendingIntent.getActivity;

/**
 * Class used for reading user and feed Json files
 * @author Kiruthiga
 *
 */
public class JSONReader {

	private  String filename;
	private Context context;
	/**
	 * Constructor of JSONReader to set the file which will be read
	 * @param filename - name of file to read
	 */
	public JSONReader(String filename, Context context) {
		
		this.filename = filename;
		this.context = context;
	}

	/**
	 * Method to read JSON file
	 * @return - array of elements read from Json file
	 * @throws JSONFileException - when there is an error reading json file
	 */
	public JSONArray jsonReader() throws JSONFileException {

		JSONParser parser = new JSONParser();
		JSONArray jsonArray = null;
		try {
			String jsonFileContent = loadJSON();

			//File file = new File(filename);
			//FileReader filereader = new FileReader(file);

			if (jsonFileContent.length() != 0) {

				jsonArray = (JSONArray) parser.parse(jsonFileContent);
			}else
			{
				jsonArray = new JSONArray();

			}

			//filereader.close();

		} catch ( IOException| ParseException e) {
			
			throw new JSONFileException("Error Reading JSON file "+filename+". Error description :"+e.getMessage());
			
		}
		return jsonArray;
	}

	//method to get the content of JSON file
	private String loadJSON() throws IOException {
		String json = null;
			File fileDir = new File(context.getFilesDir(),"FeedBookDir");
			//during first time app load when Directory for internal storage does not exists
			if(!fileDir.exists()){
				fileDir.mkdir();
			}
			File file = new File(fileDir, filename);
			//during first time app load when JSON file does not exists
			if(!file.exists()){
				FileWriter writer = new FileWriter(file);
				writer.append("");
				writer.flush();
				writer.close();
			}
			FileInputStream fis = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fis);
			int size = in.available();
			byte[] buffer = new byte[size];
			in.read(buffer);
			in.close();

			json = new String(buffer, "UTF-8");

		return json;
	}


}
