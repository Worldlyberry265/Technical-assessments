package Storage800.Backend.Quiz.dao;

import org.springframework.http.ResponseEntity;
import Storage800.Backend.Quiz.model.Client;

public interface ClientsDAO {

	ResponseEntity<Object> createClient(Client client);

	ResponseEntity<Object> updateClient(Client client);

	ResponseEntity<Object> getAllClients();

}