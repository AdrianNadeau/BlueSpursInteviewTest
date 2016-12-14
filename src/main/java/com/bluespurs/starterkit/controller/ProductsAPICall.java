package com.bluespurs.starterkit.controller;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
