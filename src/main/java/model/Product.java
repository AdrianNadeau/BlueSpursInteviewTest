package model;

public class Product {
	   private String name;
	   private Double price;
	   private String currency;
	   private String location;
	   
	   public Product(double prc, String itemName, String itemCurrency, String itemLocation) {
	        this.price = prc ;
	        this.name = itemName;
	        this.currency = itemCurrency;
	        this.location= itemLocation;
	    }
	   

	   public void setName(String name) {
	      this.name = name;
	   }
	   public String getName() {
	      return name;
	   }

	   public void setPrice(double price) {
	      this.price = price;
	   }
	   public Double getPrice() {
	      return price;
	   }


	public String getCurrency() {
		return currency;
	}


	public void setCurrency(String currency) {
		this.currency = currency;
	}


	public String getLocation() {
		return location;
	}


	public void setLocation(String location) {
		this.location = location;
	}


}