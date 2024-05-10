package Storage800.Backend.Quiz.dao;

import org.springframework.http.ResponseEntity;
import Storage800.Backend.Quiz.model.Product;

public interface ProductsDAO { 

	ResponseEntity<Object> createProduct(Product product);

	ResponseEntity<Object> updateProduct(Product product);

	ResponseEntity<Object> getAllProducts();

}