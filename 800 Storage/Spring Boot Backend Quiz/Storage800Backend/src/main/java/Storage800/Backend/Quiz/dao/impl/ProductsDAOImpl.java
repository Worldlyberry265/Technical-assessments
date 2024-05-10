package Storage800.Backend.Quiz.dao.impl;

import java.util.List;
import java.sql.Timestamp;
import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import Storage800.Backend.Quiz.dao.ProductsDAO;
import Storage800.Backend.Quiz.exceptions.CustomException;
import Storage800.Backend.Quiz.model.Product;


@Repository
public class ProductsDAOImpl implements ProductsDAO {

	@Autowired
	JdbcTemplate Jtemplate;

//	// All the regexes below reduce significantly the percentage of successful sql
//	// injections by not allowing semicolons and quotes

	private boolean isNameValid(String input) {
		// Perform input validation to disallow special characters
		String regex = "^[A-Za-z ]+$"; // Regular expression that only allows alphabets and spaces
		return (input.matches(regex) && input.length() >= 3);
	}

	private boolean isDescValid(String input) {
		// Perform input validation to disallow special characters
		String regex = "^[A-Za-z ,]+$"; // Regular expression that only allows alphabets, commas, and spaces
		return input.matches(regex);
	}

	private boolean isProductIdValid(int id) {
		String sql = "SELECT COUNT(*) FROM Products WHERE id = ? LIMIT 1";
		int count = Jtemplate.queryForObject(sql, Integer.class, id);
		return count == 1;
	}

	@Override
	public ResponseEntity<Object> createProduct(Product product) {
		try {
			// Validate input for special characters
			if (!isNameValid(product.getName())) {
				CustomException exception = new CustomException(HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST",
						"Invalid Name, Only Letters and spaces are allowed.", "/createProduct", ZonedDateTime.now());
				return exception.toResponseEntity();
			}
			if (!isNameValid(product.getCategory())) {
				CustomException exception = new CustomException(HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST",
						"Invalid Category, Only Letters and spaces are allowed.", "/createProduct",
						ZonedDateTime.now());
				return exception.toResponseEntity();
			}
			if (!isDescValid(product.getDescription()) && !product.getDescription().isEmpty()) {
				CustomException exception = new CustomException(HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST",
						"Invalid Description, Only Letters , commas, and spaces are allowed.", "/createProduct",
						ZonedDateTime.now());
				return exception.toResponseEntity();
			}

			int result = Jtemplate.update(
					"INSERT INTO products (prodname, descrip, category, creation_date) VALUES (?, ?, ?, CURRENT_TIMESTAMP)",
					product.getName(), product.getDescription(), product.getCategory());

			if (result > 0) {
				return ResponseEntity.ok("Product Created");
			}
		} catch (DataAccessException e) {
			CustomException exception = new CustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"INTERNAL_SERVER_ERROR", "An error occurred while accessing the data", "/createProduct",
					ZonedDateTime.now());
			return exception.toResponseEntity();

		}
		CustomException exception = new CustomException(HttpStatus.SERVICE_UNAVAILABLE.value(), "SERVICE_UNAVAILABLE",
				"An error occurred while accessing the data", "/createProduct", ZonedDateTime.now());
		return exception.toResponseEntity();

	}

	@Override
	public ResponseEntity<Object> updateProduct(Product product) {
		try {
			// Validate input for special characters
			if (!isNameValid(product.getName())) {
				CustomException exception = new CustomException(HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST",
						"Invalid Name, Only Letters and spaces are allowed.", "/updateProduct", ZonedDateTime.now());
				return exception.toResponseEntity();
			}
			if (!isNameValid(product.getCategory())) {
				CustomException exception = new CustomException(HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST",
						"Invalid Category, Only Letters and spaces are allowed.", "/updateProduct",
						ZonedDateTime.now());
				return exception.toResponseEntity();
			}
			if (!isDescValid(product.getDescription()) && !product.getDescription().isEmpty()) {
				CustomException exception = new CustomException(HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST",
						"Invalid Description, Only Letters , commas, and spaces are allowed.", "/updateProduct",
						ZonedDateTime.now());
				return exception.toResponseEntity();
			}

			if (!isProductIdValid(product.getId())) {
				CustomException exception = new CustomException(HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST",
						"Invalid product, check its id", "/updateProduct", ZonedDateTime.now());
				return exception.toResponseEntity();
			}

			// Im considering even if the user changes only one attribute, the whole product
			// will be sent back, not only the
			// updated attributes
			String updateSql = "UPDATE products SET prodname = ?, descrip = ?, category = ? WHERE id = ?";

			int result = Jtemplate.update(updateSql, product.getName(), product.getDescription(), product.getCategory(),
					product.getId());

			if (result > 0) {
				return ResponseEntity.ok("Product Updated");
			}
		} catch (DataAccessException e) {
			CustomException exception = new CustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"INTERNAL_SERVER_ERROR", "An error occurred while accessing the data", "/updateProduct",
					ZonedDateTime.now());
			return exception.toResponseEntity();

		}
		CustomException exception = new CustomException(HttpStatus.SERVICE_UNAVAILABLE.value(), "SERVICE_UNAVAILABLE",
				"An error occurred while accessing the data", "/updateProduct", ZonedDateTime.now());
		return exception.toResponseEntity();
	}

	@Override
	public ResponseEntity<Object> getAllProducts() {

		CustomException exception = new CustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"Internal Server Error", "Server Out Of Service", "/getAllProducts", ZonedDateTime.now());

		String sql = "SELECT * from products";

		try {

			List<Product> products = Jtemplate.query(sql, (resultSet, rowNum) -> {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("prodname");
				String desc = resultSet.getString("descrip");
				String categ = resultSet.getString("category");
				Timestamp creation_Date = resultSet.getTimestamp("creation_date");

				Product product = new Product(id, name, desc, categ, creation_Date);
				return product;
			});
			return ResponseEntity.ok(products);

		} catch (EmptyResultDataAccessException e) {
			exception = new CustomException(HttpStatus.NOT_FOUND.value(), "NOT_FOUND", "Empty Database",
					"/getAllProducts", ZonedDateTime.now());
			return exception.toResponseEntity();
		} catch (DataAccessException e) {
			exception = new CustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "INTERNAL_SERVER_ERROR",
					"An error occurred while accessing the data", "/getAllProducts", ZonedDateTime.now());
			return exception.toResponseEntity();
		}

	}

}
