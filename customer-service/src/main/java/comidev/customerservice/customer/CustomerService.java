package comidev.customerservice.customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import comidev.customerservice.exception.conflict.FieldAlreadyExistException;
import comidev.customerservice.exception.notFound.NotFoundException;
import comidev.customerservice.region.Region;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerService {
    @Autowired
    private CustomerRepo customerRepo;
    private final String CREATED = "CREATED";
    private final String DELETED = "DELETED";

    public List<Customer> getAll() {
        return customerRepo.findAll();
    }

    public List<Customer> getAllByRegion(Long id) {
        List<Customer> customers = customerRepo.findByRegion(new Region(id));
        if (customers.isEmpty()) {
            String message = "No hay ningún customer de la region: " + id;
            log.error(message);
            throw new NotFoundException(message);
        }
        return customers;
    }

    public Customer create(Customer customer) {
        String numberID = customer.getNumberID();
        validNumberID(numberID); // Idempotencia
        customer.setState(CREATED);
        log.info("{} creado!", numberID);
        return customerRepo.save(customer);
    }

    private void validNumberID(String numberID) {
        if (customerRepo.existsByNumberID(numberID)) {
            String message = "Customer con numberID: " + numberID + " ya existe!";
            log.error(message);
            throw new FieldAlreadyExistException(message);
        }
    }

    public Customer update(Customer customer, Long id) {
        Customer customerDB = getById(id);
        customerDB.setFirstName(customer.getFirstName());
        customerDB.setLastName(customer.getLastName());
        customerDB.setEmail(customer.getEmail());
        customerDB.setPhotoUrl(customer.getPhotoUrl());
        log.info("{} actualizado!", id);
        return customerRepo.save(customerDB);
    }

    public Customer delete(Long id) {
        Customer customerDB = getById(id);
        customerDB.setState(DELETED);
        log.info("{} eliminado!", id);
        return customerRepo.save(customerDB);
    }

    public Customer getById(Long id) {
        return customerRepo.findById(id)
                .orElseThrow(() -> {
                    String message = "Customer (" + id + ") no existe.";
                    log.error(message);
                    return new NotFoundException(message);
                });
    }
}
