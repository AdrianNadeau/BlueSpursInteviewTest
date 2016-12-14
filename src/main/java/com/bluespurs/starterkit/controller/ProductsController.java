package com.bluespurs.starterkit.controller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//Get all products from BestBuy and Walmart API's to compare
@RestController
public class ProductsController {

	Product bestBuyProduct;
	Product walmartProduct;

	@RequestMapping(value = "/products/", method = RequestMethod.GET)
	public ResponseEntity<?> getProducts(@RequestParam(value = "name") String name) throws ParseException {
		// TODO add api keys to app properties
		try {

			// Best buy api call to find lowest priced item
			String result = ProductsAPICall.apiCall("bb", name, "pfe9fpy68yg28hvvma49sc89");
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(result);
			JSONObject jsonObject = (JSONObject) obj;

			JSONArray jsonArray = (JSONArray) jsonObject.get("products");

			// get first object since results are sorted lowest to highest
			for (Object object : jsonArray) {
				JSONObject aJson = (JSONObject) object;
				String productName = (String) aJson.get("name");
				Double productPrice = (Double) aJson.get("regularPrice");
				String productCurrency = "USD";
				String productLocation = "Best Buy";

				bestBuyProduct = new Product(productPrice, productName, productCurrency, productLocation);
			}
			
			// get walmart lowest price record
			result = ProductsAPICall.apiCall("wm", name, "rm25tyum3p9jm9x9x7zxshfa");
			parser = new JSONParser();
			obj = parser.parse(result);
			jsonObject = (JSONObject) obj;

			jsonArray = (JSONArray) jsonObject.get("items");
			for (Object object : jsonArray) {
				JSONObject aJson = (JSONObject) object;
				String productName = (String) aJson.get("name");
				Double productPrice = (Double) aJson.get("msrp");
				String productCurrency = "USD";
				String productLocation = "Walmart";

				walmartProduct = new Product(productPrice, productName, productCurrency, productLocation);
			}
			
			if(bestBuyProduct.getPrice()<walmartProduct.getPrice()){
				//best buy is cheapest
				return new ResponseEntity<>(bestBuyProduct, HttpStatus.OK);
			}
			else{
				return new ResponseEntity<>(walmartProduct, HttpStatus.OK);
				
			}
			
			
		}

		catch (Exception ex) {
			// return "error: "+ex;
			return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
		}
		//TODO: add method to return 

	}
}
//TODO: create appropriate model and return user friendly message
//	@ExceptionHandler(Exception.class)
//	public ModelAndView handleError(HttpServletRequest req, Exception ex) {
//	    ModelAndView mav = new ModelAndView();
//	    mav.addObject("exception", ex);
//	    mav.addObject("url", req.getRequestURL());
//	    mav.setViewName("error");
//	    return mav;
//	}
	   
