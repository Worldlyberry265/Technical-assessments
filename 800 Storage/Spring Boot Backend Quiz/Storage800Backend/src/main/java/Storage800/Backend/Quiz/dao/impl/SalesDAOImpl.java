package Storage800.Backend.Quiz.dao.impl;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import Storage800.Backend.Quiz.dao.SalesDAO;
import Storage800.Backend.Quiz.exceptions.CustomException;
import Storage800.Backend.Quiz.model.Sale;

@Repository
public class SalesDAOImpl implements SalesDAO {

	@Autowired
	JdbcTemplate Jtemplate;

	@Override
	public ResponseEntity<Object> createSale(Sale sale) {
		try {

			if (sale.getClient_id() == sale.getSeller_id()) {
				CustomException exception = new CustomException(HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST",
						"Please check the client or the seller, they can't be the same person", "/createSale",
						ZonedDateTime.now());
				return exception.toResponseEntity();
			}

			int result = Jtemplate.update(
					"INSERT INTO sales (creation_date, client_id, seller_id, total_quantity, price) "
							+ "VALUES (CURRENT_TIMESTAMP , ?, ?, ?, ?)",
					sale.getClient_id(), sale.getSeller_id(), sale.getTotal_quantity(), sale.getPrice());

			if (result > 0) {
				return ResponseEntity.ok("Sale Created");
			}
		} catch (DataAccessException e) {
			// Exception occurred while accessing the data
			CustomException exception = new CustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"INTERNAL_SERVER_ERROR", "An error occurred while accessing the data", "/createSale",
					ZonedDateTime.now());
			return exception.toResponseEntity();

		}
		CustomException exception = new CustomException(HttpStatus.SERVICE_UNAVAILABLE.value(), "SERVICE_UNAVAILABLE",
				"An error occurred while accessing the data", "/createSale", ZonedDateTime.now());
		return exception.toResponseEntity();

	}

	@Override
	public ResponseEntity<Object> updateSale(Sale sale, int updater_ID) {
		try {

			String updateSql = "UPDATE Sales SET total_quantity = ?, price = ? WHERE id = ?";

			//LIMIT 1 for efficiency, so it stops at the first match
			String Oldsql = "SELECT creation_date, client_id, seller_id, total_quantity, price FROM sales WHERE id = "
					+ sale.getId() + " LIMIT 1";

			List<Sale> outdatedSale = Jtemplate.query(Oldsql, (resultSet, rowNum) -> {
				int cid = resultSet.getInt("client_id");
				int sid = resultSet.getInt("seller_id");
				int total = resultSet.getInt("total_quantity");
				float price = resultSet.getFloat("price");
				Timestamp creation_Date = resultSet.getTimestamp("creation_date");
				Sale Outdatedsale = new Sale();
				Outdatedsale.setClient_id(cid);
				Outdatedsale.setSeller_id(sid);
				Outdatedsale.setTotal_quantity(total);
				Outdatedsale.setPrice(price);
				Outdatedsale.setCreation_date(creation_Date);
				return Outdatedsale;
			});

			if (outdatedSale.get(0).getClient_id() != updater_ID && updater_ID != outdatedSale.get(0).getSeller_id()) {
				CustomException exception = new CustomException(HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST",
						"Please check the updater id, it should refer to either the seller or the client",
						"/updateSale", ZonedDateTime.now());
				return exception.toResponseEntity();
			}

			int result = Jtemplate.update(updateSql, sale.getTotal_quantity(), sale.getPrice(), sale.getId());
			
			if (result > 0) {
				int result2 = Jtemplate.update(
						"INSERT INTO saleslog (sale_id, updater_id, creation_date, "
								+ "update_date, old_quantity, new_quantity , old_price, new_price) "
								+ "VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?, ?, ?, ?)",
						sale.getId(), updater_ID, outdatedSale.get(0).getCreation_date(),
						outdatedSale.get(0).getTotal_quantity(), sale.getTotal_quantity(),
						outdatedSale.get(0).getPrice(), sale.getPrice());
				if (result2 > 0) {
					return ResponseEntity.ok("Sale Updated and logged");
				} else
					return ResponseEntity.ok("Sale Updated but not logged");
			}

		} catch (EmptyResultDataAccessException e) {
			CustomException exception = new CustomException(HttpStatus.NOT_FOUND.value(), "NOT_FOUND", "Empty Database",
					"/updateSale", ZonedDateTime.now());
			return exception.toResponseEntity();

		} catch (DataAccessException e) {
			CustomException exception = new CustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"INTERNAL_SERVER_ERROR", "An error occurred while accessing the data", "/updateSale",
					ZonedDateTime.now());
			return exception.toResponseEntity();

		}
		CustomException exception = new CustomException(HttpStatus.SERVICE_UNAVAILABLE.value(), "SERVICE_UNAVAILABLE",
				"An error occurred while accessing the data", "/updateSale", ZonedDateTime.now());
		return exception.toResponseEntity();
	}

	@Override
	public ResponseEntity<Object> getAllSales() {

		CustomException exception = new CustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"Internal Server Error", "Server Out Of Service", "/getAllSales", ZonedDateTime.now());

		String sql = "SELECT * from Sales";

		try {

			List<Sale> sales = Jtemplate.query(sql, (resultSet, rowNum) -> {
				int id = resultSet.getInt("id");
				Timestamp creaation_date = resultSet.getTimestamp("creation_date");
				int client_id = resultSet.getInt("client_id");
				int seller_id = resultSet.getInt("seller_id");
				int total = resultSet.getInt("total_quantity");
				float price = resultSet.getFloat("price");
				Sale sale = new Sale(id, creaation_date, client_id, seller_id, total, price);
				return sale;
			});
			return ResponseEntity.ok(sales);

		} catch (EmptyResultDataAccessException e) {
			exception = new CustomException(HttpStatus.NOT_FOUND.value(), "NOT_FOUND", "Empty Database", "/getAllSales",
					ZonedDateTime.now());
			return exception.toResponseEntity();
		} catch (DataAccessException e) {
			exception = new CustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "INTERNAL_SERVER_ERROR",
					"An error occurred while accessing the data", "/getAllSales", ZonedDateTime.now());
			return exception.toResponseEntity();
		}

	}
}
