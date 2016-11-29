package service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javafx.scene.image.Image;
import json.JsonReader;

public class Service {

	private String name;
	private URL url;
	
	public Service(String serviceName){
		this.name = serviceName;
		if (name.equals("local")){
			
			url = this.getClass().getResource("/data.json");
		}
	}
	
	public Image getPanoPicture(Integer axisX, Integer axisY) throws IOException{
		JsonReader jsonReader = new JsonReader();
		
		String coordinate = Integer.toString(axisX) + ","+Integer.toString(axisY);
		try{
			String json = jsonReader.readJson(url);
			String imageViewer = jsonReader.parseJson(json, coordinate);
			Image pano = new Image(imageViewer);
			return pano;
		}catch (Exception e){
			return null;
		}
	}
	
	public Image getItemPicture(Integer number) throws IOException{
		JsonReader jsonReader = new JsonReader();
		
		String no = Integer.toString(number);
		try{
			String json = jsonReader.readJson(url);
			String imageViewer = jsonReader.parseJsonItem(json, no);
			
			Image item = new Image(imageViewer);
			return item;
		}catch (Exception e){
			return null;
		}
	}
	
	
	public String getName(){
		return name;
	}
}
