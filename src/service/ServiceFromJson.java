package service;

import org.json.JSONObject;

import json.JsonReader;

public class ServiceFromJson {
	
	private String service;

	public Service fromProperties(){
		try{
			JsonReader jsonReader = new JsonReader();
			String json = jsonReader.readJson(this.getClass().getResource("/properties.json"));
			JSONObject jsonObject = new JSONObject(json);
			service = jsonObject.getString("service");
			return new Service(service);
		}catch (Exception e){
			e.printStackTrace();
		}
		return new Service(service);
		
	}
}
