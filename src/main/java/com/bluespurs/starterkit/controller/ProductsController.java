package com.bluespurs.starterkit.controller;

import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bluespurs.starterkit.service.EmailServiceImpl;

import common.ProductsAPICall;
import model.Product;
import model.SaleProduct;


@Configuration
@PropertySource(value = { "classpath:application.properties" })
@RestController
@RequestMapping(value = "/products")
public class ProductsController {
	// product and sale product objects
	Product bestBuyProduct;
	Product walmartProduct;

	SaleProduct saleProduct;

	// get api keys from application.properties file
	@Value("${bestbuyAPIKey}")
	private String bestbuyAPIKey;

	@Value("${walmartAPIKey}")
	private String walmartAPIKey;

	
	

	@RequestMapping(value = "/search/", method = RequestMethod.GET)
	public ResponseEntity<?> getProducts(@RequestParam(value = "name") String name) throws ParseException {
		// TODO add api keys to app properties
		try {

			// Best buy api call to find lowest priced item
			String result = ProductsAPICall.apiCall("bb", name, bestbuyAPIKey);
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
			result = ProductsAPICall.apiCall("wm", name, walmartAPIKey);
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

			if (bestBuyProduct.getPrice() < walmartProduct.getPrice()) {
				// best buy is cheapest
				return new ResponseEntity<>(bestBuyProduct, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(walmartProduct, HttpStatus.OK);

			}
		} catch (Exception ex) {
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
		// get best buy minimum item on sale
		String result;
		result = ProductsAPICall.apiCallEmail("bb", json1, bestbuyAPIKey);

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

			// String itemName, String itemEmail, double price, double saleprice
			saleProduct = new SaleProduct(productName, (String) json1.get("email"), productPrice, productSalePrice,
					"Best Buy");
			// use emailservice to send email if there is a sale on name of product

			sendEmail(saleProduct.getEmail(), saleProduct.getName(), saleProduct.getPrice(), saleProduct.getSaleprice(),
					saleProduct.getLocation());

			// get walmart minimum item on sale

			result = ProductsAPICall.apiCallEmail("wm", json1, walmartAPIKey);

			parser = new JSONParser();
			obj = parser.parse(result);
			jsonObject = (JSONObject) obj;

			jsonArray = (JSONArray) jsonObject.get("items");

			// get first object since results are sorted lowest to highest
			for (Object objectWalmart : jsonArray) {
				aJson = (JSONObject) objectWalmart;
				productName = (String) aJson.get("name");
				productPrice = (Double) aJson.get("msrp");
				productSalePrice = (Double) aJson.get("salePrice");
				if (productSalePrice < productPrice) {
					// on sale item, break
					saleProduct = new SaleProduct(productName, (String) json1.get("email"), productPrice,
							productSalePrice, "Walmart");
					sendEmail(saleProduct.getEmail(), saleProduct.getName(), saleProduct.getPrice(),
							saleProduct.getSaleprice(), saleProduct.getLocation());
					break;
				}
			}
			return;
		}
	}

	private void sendEmail(String email, String name, Double price, Double saleprice, String location) {
		EmailServiceImpl emailImp = new EmailServiceImpl();
		emailImp.sendEmail(saleProduct.getEmail(), "The price of the " + saleProduct.getName() + " has dropped!",
				"The price of the " + saleProduct.getName() + " has dropped from " + saleProduct.getPrice() + " USD to "
						+ saleProduct.getSaleprice() + " USD! Get it quick at " + saleProduct.getLocation() + "!");
	}
}
// TODO: create appropriate model and return user friendly message
// @ExceptionHandler(Exception.class)
// public ModelAndView handleError(HttpServletRequest req, Exception ex) {
// ModelAndView mav = new ModelAndView();
// mav.addObject("exception", ex);
// mav.addObject("url", req.getRequestURL());
// mav.setViewName("error");
// return mav;
// }
