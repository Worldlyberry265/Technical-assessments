package Storage800.Backend.Quiz.model;

import java.sql.Timestamp;

public class Sale {
	
	private int id;
	private Timestamp creation_date;
	private int client_id;
	private int seller_id;
	private int total_quantity;
	private float price;
	
	
	public Sale() {
		
	}
	
	public Sale(int id, Timestamp creation_date, int client_id, int seller_id, int total_quantity, float price) {
		super();
		this.id = id;
		this.creation_date = creation_date;
		this.client_id = client_id;
		this.seller_id = seller_id;
		this.total_quantity = total_quantity;
		this.price = price;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public Timestamp getCreation_date() {
		return creation_date;
	}


	public void setCreation_date(Timestamp creation_date) {
		this.creation_date = creation_date;
	}


	public int getClient_id() {
		return client_id;
	}


	public void setClient_id(int client_id) {
		this.client_id = client_id;
	}


	public int getSeller_id() {
		return seller_id;
	}


	public void setSeller_id(int seller_id) {
		this.seller_id = seller_id;
	}


	public int getTotal_quantity() {
		return total_quantity;
	}


	public void setTotal_quantity(int total_quantity) {
		this.total_quantity = total_quantity;
	}


	public float getPrice() {
		return price;
	}


	public void setPrice(float price) {
		this.price = price;
	}
	
	
	
	
}
