package json;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import org.json.JSONObject;
import org.json.JSONArray;
public class JsonReader {

	
	public String readJson(URL url) throws FileNotFoundException, UnsupportedEncodingException{
		BufferedReader reader = null;
		String laststr = "";
		try{
			InputStream InputStream = url.openStream();
			
			InputStreamReader inputStreamReader = new InputStreamReader(InputStream, "UTF-8");
			reader = new BufferedReader(inputStreamReader);
			
			String tempString = null;
			
			while((tempString = reader.readLine()) != null){
				laststr += tempString;
			}
			
			reader.close();
		}catch (Exception e){
			e.printStackTrace();
		}
		return laststr;
	}
	
	public String parseJson(String json, String coordinate){
		try{
		JSONObject jsonObject = new JSONObject(json);
		JSONArray jsonArray = jsonObject.getJSONArray("pano");
		
		int size = jsonArray.length();
		for(int  i = 0; i < size; i++){
			JSONObject jsonObject2 = jsonArray.getJSONObject(i);
			if (jsonObject2.getString("coordinate").equals(coordinate)){
				
				return jsonObject2.getString("url");
			}
		}
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public String parseJsonItem(String json, String number){
		try{
		JSONObject jsonObject = new JSONObject(json);
		JSONArray jsonArray = jsonObject.getJSONArray("item");
		int size = jsonArray.length();
		for(int  i = 0; i < size; i++){
			JSONObject jsonObject2 = jsonArray.getJSONObject(i);
			if (jsonObject2.getString("number").equals(number)){
				
				return jsonObject2.getString("url");
			}
		}
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
