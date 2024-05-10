package Storage800.Backend.Quiz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import Storage800.Backend.Quiz.dao.ClientsDAO;
import Storage800.Backend.Quiz.dao.ProductsDAO;
import Storage800.Backend.Quiz.dao.SalesDAO;
import Storage800.Backend.Quiz.model.Client;
import Storage800.Backend.Quiz.model.Product;
import Storage800.Backend.Quiz.model.Sale;

@RestController
public class MainController {

	@Autowired
	private ProductsDAO pDAO;

	@Autowired
	private ClientsDAO cDAO;

	@Autowired
	private SalesDAO sDAO;

	@PostMapping("/createProduct")
	public ResponseEntity<Object> CreateProduct(@RequestBody Product product) {
		ResponseEntity<Object> result = pDAO.createProduct(product);
		return result;
	}

	@PutMapping("/updateProduct")
	public ResponseEntity<Object> UpdateProduct(@RequestBody Product product) {
		ResponseEntity<Object> result = pDAO.updateProduct(product);
		return result;
	}

	@GetMapping("/getAllProducts")
	public ResponseEntity<Object> GetAllProducts() {
		ResponseEntity<Object> result = pDAO.getAllProducts();
		return result;
	}

	@PostMapping("/createClient")
	public ResponseEntity<Object> CreateClient(@RequestBody Client client) {
		ResponseEntity<Object> result = cDAO.createClient(client);
		return result;
	}

	@PutMapping("/updateClient")
	public ResponseEntity<Object> UpdateClient(@RequestBody Client client) {
		ResponseEntity<Object> result = cDAO.updateClient(client);
		return result;
	}

	@GetMapping("/getAllClients")
	public ResponseEntity<Object> GetAllClients() {
		ResponseEntity<Object> result = cDAO.getAllClients();
		return result;
	}

	@PostMapping("/createSale")
	public ResponseEntity<Object> CreateSale(@RequestBody Sale sale) {
		ResponseEntity<Object> result = sDAO.createSale(sale);
		return result;
	}

	@PutMapping("/updateSale/{updater_ID}")
	public ResponseEntity<Object> UpdateSale(@RequestBody Sale sale, @PathVariable int updater_ID) {
		ResponseEntity<Object> result = sDAO.updateSale(sale, updater_ID);
		return result;
	}

	@GetMapping("/getAllSales")
	public ResponseEntity<Object> GetAllSales() {
		ResponseEntity<Object> result = sDAO.getAllSales();
		return result;
	}
}
