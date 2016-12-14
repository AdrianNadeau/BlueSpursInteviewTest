package model;

public class SaleProduct {

	   private String name;
	   private String email;
	   private Double price;
	   private Double saleprice;
	   
	   public SaleProduct(String itemName, String itemEmail, double price,  double saleprice) {
	        this.setPrice(price) ;
	        this.setName(itemName);
	        this.setEmail(itemEmail);
	        this.setSaleprice(saleprice);
	        
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
	   

}