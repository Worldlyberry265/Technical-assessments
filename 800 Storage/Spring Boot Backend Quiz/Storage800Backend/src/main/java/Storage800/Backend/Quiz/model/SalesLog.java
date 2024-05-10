package Storage800.Backend.Quiz.model;

import java.sql.Timestamp;

public class SalesLog {

	private int sale_id;
	private int updater_id;
	private Timestamp creation_date;
	private Timestamp update_date;
	private int old_quantity;
	private int new_quantity;
	private float old_price;
	private float new_price;
	
	public SalesLog() {
		
	}

	public SalesLog(int sales_id, int updater_id, Timestamp creation_date, Timestamp update_date, int old_quantity,
			int new_quantity, float old_price, float new_price) {
		
		super();
		this.sale_id = sales_id;
		this.updater_id = updater_id;
		this.creation_date = creation_date;
		this.update_date = update_date;
		this.old_quantity = old_quantity;
		this.new_quantity = new_quantity;
		this.old_price = old_price;
		this.new_price = new_price;
	}

	public int getSales_id() {
		return sale_id;
	}

	public void setSales_id(int sales_id) {
		this.sale_id = sales_id;
	}

	public int getUpdater_id() {
		return updater_id;
	}

	public void setUpdater_id(int updater_id) {
		this.updater_id = updater_id;
	}

	public Timestamp getCreation_date() {
		return creation_date;
	}

	public void setCreation_date(Timestamp creation_date) {
		this.creation_date = creation_date;
	}

	public Timestamp getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(Timestamp update_date) {
		this.update_date = update_date;
	}

	public int getOld_quantity() {
		return old_quantity;
	}

	public void setOld_quantity(int old_quantity) {
		this.old_quantity = old_quantity;
	}

	public int getNew_quantity() {
		return new_quantity;
	}

	public void setNew_quantity(int new_quantity) {
		this.new_quantity = new_quantity;
	}

	public float getOld_price() {
		return old_price;
	}

	public void setOld_price(float old_price) {
		this.old_price = old_price;
	}

	public float getNew_price() {
		return new_price;
	}

	public void setNew_price(float new_price) {
		this.new_price = new_price;
	}

}
