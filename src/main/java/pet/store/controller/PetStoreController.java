package pet.store.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreData.PetStoreCustomer;
import pet.store.controller.model.PetStoreData.PetStoreEmployee;
import pet.store.service.PetStoreService;

@RestController
@RequestMapping("/pet_store")
@Slf4j
public class PetStoreController {
	@Autowired
	private PetStoreService petStoreService;
	
	
	// Method for creating new petStore
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreData insertPetStore(@RequestBody PetStoreData petStoreData) {
		log.info("Creating pet store {}", petStoreData);
		return petStoreService.savePetStore(petStoreData);
	}
	
	
	// Method for updating petStore information using the petStoreId to identify it
	@PutMapping("/{petStoreId}")
	public PetStoreData updatePetStore(@PathVariable Long petStoreId, @RequestBody PetStoreData petStoreData) {
		petStoreData.setPetStoreId(petStoreId);
		log.info("Updating store with Id={}", petStoreId);
		return petStoreService.savePetStore(petStoreData);
	}
	
	// Method for adding new employee
	@PostMapping("/{petStoreId}/employee")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreEmployee insertEmployeeInPetStore(@PathVariable Long petStoreId,
			@RequestBody PetStoreEmployee employee) {
		log.info("Adding employee: '{}' to store '{}'", employee, petStoreId);
		return petStoreService.saveEmployee(petStoreId, employee);
	}
	
	// Method for adding new customer
	@PostMapping("/{petStoreId}/customer")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreCustomer insertCustomerInPetStore(@PathVariable Long petStoreId,
			@RequestBody PetStoreCustomer customer) {
		log.info("Adding customer: '{}' to store '{}'", customer, petStoreId);
		return petStoreService.saveCustomer(petStoreId, customer);
	}

	// Method for retrieving list of pet store w/o customer or employee info
	@GetMapping
	public List<PetStoreData> getAllPetStores() {
		log.info("Retrieving all pet store information without customer and employee data.");
		return petStoreService.retrieveAllPetStores();
	}
	
	// Method for pulling all info about one pet store
	@GetMapping("/{petStoreId}")
	public PetStoreData getPetStoreById(@PathVariable Long petStoreId) {
		log.info("Retrieving information for pet store with Id: '{}'", petStoreId);
		return petStoreService.retrievePetStoreById(petStoreId);
	}
	
	// Method for deleting a pet store, the employees associated with it, and also the pet_store/customer table data linking the two
	@DeleteMapping("/{petStoreId}")
	public Map<String, String> deletePetStoreById(@PathVariable Long petStoreId) {
		log.info("Deleting pet store with Id: '{}'", petStoreId);
		petStoreService.deletePetStoreById(petStoreId);

		return Map.of("message", "Deletion of pet store with Id: '" + petStoreId + "' was successful.");
	}

}


