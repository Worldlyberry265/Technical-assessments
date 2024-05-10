package Storage800.Backend.Quiz.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import Storage800.Backend.Quiz.dao.ClientsDAO;
import Storage800.Backend.Quiz.exceptions.CustomException;
import Storage800.Backend.Quiz.model.Client;

@Repository
public class ClientsDAOImpl implements ClientsDAO {

	@Autowired
	JdbcTemplate Jtemplate;

	private boolean isNameValid(String input) {
		// Perform input validation to disallow special characters
		String regex = "^[A-Za-z ]+$"; // Regular expression that only allows alphabets and spaces
		return (input.matches(regex) && input.length() >= 3);
	}

	private boolean isMobileValid(String input) {
		// Perform input validation to disallow special characters
		String regex = "^\\+[0-9]{6,}$"; // Regular expression that only allows at least numbers and starts with +
		return input.matches(regex);
	}

	private boolean isMobileUnique(String mobile) { // To search if the email is already used
		String sql = "SELECT COUNT(*) FROM clients WHERE mobile = ? LIMIT 1";
		int count = Jtemplate.queryForObject(sql, Integer.class, mobile);
		return count == 0; // if not found return true, else if count = 1 return false
	}

	@Override
	public ResponseEntity<Object> createClient(Client client) {
		try {
			// Validate input for special characters
			if (!isNameValid(client.getFirstname()) || !isNameValid(client.getLastname())) {
				CustomException exception = new CustomException(HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST",
						"Invalid Name, Only Letters and spaces are allowed, and should be of minimum 3 length",
						"/createClient", ZonedDateTime.now());
				return exception.toResponseEntity();
			}
			if (!isMobileValid(client.getMobile())) {
				CustomException exception = new CustomException(HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST",
						"Invalid mobile, It should start with a plus sign (+) and continue with numbers",
						"/createProduct", ZonedDateTime.now());
				return exception.toResponseEntity();
			}

			if (!isMobileUnique(client.getMobile())) {
				CustomException exception = new CustomException(HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST",
						"Mobile not unique, please enter a different one.", "/createClient", ZonedDateTime.now());
				return exception.toResponseEntity();
			}

			int result = Jtemplate.update("INSERT INTO clients (firstname, lastname, mobile) VALUES (?, ?, ?)",
					client.getFirstname(), client.getLastname(), client.getMobile());

			if (result > 0) {
				return ResponseEntity.ok("Client Created");
			}
		} catch (DataAccessException e) {
			CustomException exception = new CustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"INTERNAL_SERVER_ERROR", "An error occurred while accessing the data", "/createClient",
					ZonedDateTime.now());
			return exception.toResponseEntity();

		}
		CustomException exception = new CustomException(HttpStatus.SERVICE_UNAVAILABLE.value(), "SERVICE_UNAVAILABLE",
				"An error occurred while accessing the data", "/createClient", ZonedDateTime.now());
		return exception.toResponseEntity();

	}

	@Override
	public ResponseEntity<Object> updateClient(Client client) {
		try {
			// Validate input for special characters
			if (!isNameValid(client.getFirstname()) || !isNameValid(client.getLastname())) {
				CustomException exception = new CustomException(HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST",
						"Invalid Name, Only Letters and spaces are allowed, and should be of minimum 3 length",
						"/updateClient", ZonedDateTime.now());
				return exception.toResponseEntity();
			}
			if (!isMobileValid(client.getMobile())) {
				CustomException exception = new CustomException(HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST",
						"Invalid mobile, It should start with a plus sign (+) and continue with at least 6 numbers",
						"/updateClient", ZonedDateTime.now());
				return exception.toResponseEntity();
			}

			String MobileSQL = "SELECT mobile FROM clients where ID = ? LIMIT 1";
			// depreciated
//			String mobile = Jtemplate.queryForObject(MobileSQL, new Object[] { client.getId() }, String.class);

			NamedParameterJdbcTemplate namedTemplate = new NamedParameterJdbcTemplate(Jtemplate);
			Map<String, Object> params = new HashMap<>();
			params.put("id", client.getId());
			String mobile = namedTemplate.queryForObject(MobileSQL, params, String.class);

			// The + sign is causing an error, that's why I'm comparing the 2 mobiles
			// starting from index 1
			if (!client.getMobile().substring(1).matches(mobile.substring(1))) {
				if (!isMobileUnique(client.getMobile())) {
					CustomException exception = new CustomException(HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST",
							"This mobile already exists, You should use a different mobile.", "/updateClient",
							ZonedDateTime.now());
					return exception.toResponseEntity();
				}
			}
			// Im considering even if the user changes only one attribute, the whole product
			// will be sent back, not only the
			// updated attributes
			String updateSql = "UPDATE clients SET firstname = ?, lastname = ?, mobile = ? WHERE id = ?";

			int result = Jtemplate.update(updateSql, client.getFirstname(), client.getLastname(), client.getMobile(),
					client.getId());

			if (result > 0) {
				return ResponseEntity.ok("Client Updated");
			}
		} catch (DataAccessException e) {
			CustomException exception = new CustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"INTERNAL_SERVER_ERROR", "An error occurred while accessing the data", "/updateClient",
					ZonedDateTime.now());
			return exception.toResponseEntity();

		}
		CustomException exception = new CustomException(HttpStatus.SERVICE_UNAVAILABLE.value(), "SERVICE_UNAVAILABLE",
				"An error occurred while accessing the data", "/updateClient", ZonedDateTime.now());
		return exception.toResponseEntity();
	}

	@Override
	public ResponseEntity<Object> getAllClients() {

		CustomException exception = new CustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"Internal Server Error", "Server Out Of Service", "/getAllClients", ZonedDateTime.now());

		String sql = "SELECT * from Clients";

		try {

			List<Client> clients = Jtemplate.query(sql, (resultSet, rowNum) -> {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("firstname");
				String lastname = resultSet.getString("lastname");
				String mobile = resultSet.getString("mobile");

				Client client = new Client(id, name, lastname, mobile);
				return client;
			});
			return ResponseEntity.ok(clients);

		} catch (EmptyResultDataAccessException e) {
			exception = new CustomException(HttpStatus.NOT_FOUND.value(), "NOT_FOUND", "Empty Database",
					"/getAllClients", ZonedDateTime.now());
			return exception.toResponseEntity();
		} catch (DataAccessException e) {
			exception = new CustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "INTERNAL_SERVER_ERROR",
					"An error occurred while accessing the data", "/getAllClients", ZonedDateTime.now());
			return exception.toResponseEntity();
		}

	}

}
