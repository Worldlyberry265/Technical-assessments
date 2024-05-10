package Storage800.Backend.Quiz.model;

import java.sql.Timestamp;

public class Product {
	
	private int id;
	private String name;
	private String description;
	private String category;
	private Timestamp creation_date;
	
	
	public Product() {

	}
	
	public Product(int Id, String name, String description, String category, Timestamp creation_date) {
		this.id = Id;
		this.name = name;
		this.description = description;
		this.category = category;
		this.creation_date = creation_date;
	}
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Timestamp getCreation_Date() {
		return creation_date;
	}

	public void setCreation_Date(Timestamp creation_Date) {
		creation_date = creation_Date;
	}
	
	
	
	
}
