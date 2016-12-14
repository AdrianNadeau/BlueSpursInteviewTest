package com.bluespurs.starterkit.controller;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bluespurs.starterkit.service.EmailServiceImpl;


import common.ProductsAPICall;
import model.Product;
import model.SaleProduct;

//Get all products from BestBuy and Walmart API's to compare
@RestController
@RequestMapping(value = "/products")
public class ProductsController {
	//product and sale product objects
	Product bestBuyProduct;
	Product walmartProduct;
	
	SaleProduct bestBuySaleProduct;
	SaleProduct walmartSaleProduct;
	
	//TODO: get keys from project properties
	String bestBuyApiKey = "pfe9fpy68yg28hvvma49sc89";
	String walmartApiKey = "rm25tyum3p9jm9x9x7zxshfa";
	
	@RequestMapping(value = "/search/", method = RequestMethod.GET)
	public ResponseEntity<?> getProducts(@RequestParam(value = "name") String name) throws ParseException {
		// TODO add api keys to app properties
		try {

			// Best buy api call to find lowest priced item
			String result = ProductsAPICall.apiCall("bb", name,bestBuyApiKey);
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
			result = ProductsAPICall.apiCall("wm", name, walmartApiKey);
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
			return new ResponseEntity<>("Error, please contact Adminstration for assistance.", HttpStatus.BAD_REQUEST);
		}
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/alert/", method = RequestMethod.GET)
	@ResponseBody
	public void findSale() throws IOException, ParseException {
		
		JSONObject json1 = new JSONObject();
	    json1.put("name", "ipad");
	    json1.put("email", "adriannadeau.art@gmail.com");
	    
	    String result = ProductsAPICall.apiCallEmail("bb", json1, bestBuyApiKey);
	    
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(result);
		JSONObject jsonObject = (JSONObject) obj;

		JSONArray jsonArray = (JSONArray) jsonObject.get("products");

		// get first object since results are sorted lowest to highest
		for (Object object : jsonArray) {
			JSONObject aJson = (JSONObject) object;
			String productName = (String) aJson.get("name");
			Double productPrice = (Double) aJson.get("regularPrice");
			Double productSalePrice = (Double) aJson.get("salePrice");
			
			//String itemName, String itemEmail, double price,  double saleprice
			bestBuySaleProduct = new SaleProduct(productName,(String)json1.get("email"), productPrice, productSalePrice);
		    //use emailservice to send email if there is a sale on name of product
		    String name = (String) json1.get("name");
			String email = (String)json1.get("email");
			String price = (String)json1.get("price");
			String saleprice = (String)json1.get("saleprice");
			EmailServiceImpl emailImp=new EmailServiceImpl();
			emailImp.sendEmail(email, "The price of the "+name+" has dropped!", "The price of the ipad has dropped from "+price+" USD to "+saleprice+"CAD! Get it quick!");
		    return;
		}
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
	   
