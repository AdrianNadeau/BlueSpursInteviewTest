package model;

public class SaleProduct {

	   private String name;
	   private String email;
	   private Double price;
	   private Double saleprice;
	   private String location;
	   
	   public SaleProduct(String itemName, String itemEmail, double price,  double saleprice, String location) {
	        this.setPrice(price) ;
	        this.setName(itemName);
	        this.setEmail(itemEmail);
	        this.setSaleprice(saleprice);
	        this.setLocation(location);
	        
	    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getSaleprice() {
		return saleprice;
	}

	public void setSaleprice(Double saleprice) {
		this.saleprice = saleprice;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	   

}