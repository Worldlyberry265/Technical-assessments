package Storage800.Backend.Quiz.dao;

import org.springframework.http.ResponseEntity;
import Storage800.Backend.Quiz.model.Sale;

public interface SalesDAO {

	ResponseEntity<Object> createSale(Sale sale);

	ResponseEntity<Object> updateSale(Sale sale, int updater_ID);

	ResponseEntity<Object> getAllSales();

}
