package common;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.simple.JSONObject;

public class ProductsAPICall {
	public static String apiCall(String type, String name, String apiKey) throws IOException {
		URL url = null;
		try {
			//TODO add api keys to app properties
			String urlEncode;
			//check for which store api is being called
			if(type.equalsIgnoreCase("bb")){
				//best buy api call
				urlEncode="https://api.bestbuy.com/v1/products(name="+name+"*&regularPrice>200)?apiKey="+apiKey+"&sort=regularPrice.asc&show=name,regularPrice&pageSize=1&format=json";
				urlEncode = urlEncode.replaceAll(" ", "%20");
			}
			else{
				//walmart call
				urlEncode="http://api.walmartlabs.com/v1/search?query="+name+"&format=json&apiKey="+apiKey+"&numItems=1";
				urlEncode = urlEncode.replaceAll(" ", "%20");
			}
	
			url = new URL(urlEncode);
			
			URLConnection yc = url.openConnection();
	        BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
	        String inputLine;
	        String results = ""; 
	        while ((inputLine = in.readLine()) != null){
	            
	        	results+=inputLine;
	        }
	        in.close();
	        return results;
		} catch (MalformedURLException e) {
			 //TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	@SuppressWarnings("unused")
	public static String apiCallEmail(String type, JSONObject jsonValues, String apiKey) throws IOException {
		URL url = null;
		try {
			//TODO add api keys to app properties
			JSONObject aJson = (JSONObject) jsonValues;
			if(!jsonValues.isEmpty()){
				//values are in jsonValues
				String name = (String) jsonValues.get("name");
				String email = (String)jsonValues.get("email");
				String urlEncode;
				//check for which store api is being called
				if(type.equalsIgnoreCase("bb")){
					//best buy api, look at sale price compared to regular price
					urlEncode="https://api.bestbuy.com/v1/products(name="+name+"*&onSale=true&regularPrice>500.00)?apiKey="+apiKey+"&sort=name.asc&show=name,regularPrice,onSale,salePrice&format=json";
					urlEncode = urlEncode.replaceAll(" ", "%20");
				}
				else{
					//walmart api, look at sale price compared to regular price
					urlEncode="http://api.walmartlabs.com/v1/search?query="+name+"&format=json&apiKey="+apiKey+"&numItems=1";
					urlEncode = urlEncode.replaceAll(" ", "%20");
				}
		
				url = new URL(urlEncode);
				
				URLConnection yc = url.openConnection();
		        BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
		        String inputLine;
		        String results = ""; 
		        while ((inputLine = in.readLine()) != null){
		            
		        	results+=inputLine;
		        }
		        in.close();
		        return results;
			}
			else{
				//TODO: send to error page
			}
		} catch (Exception e) {
			 //TODO: Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
