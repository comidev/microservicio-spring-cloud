package comidev.customerservice.customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import comidev.customerservice.client.user.User;
import comidev.customerservice.client.user.UserClient;
import comidev.customerservice.exception.conflict.ConflictException;
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

    @Autowired
    private UserClient userClient;

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
        String dni = customer.getDni();
        validDni(dni); // Idempotencia
        customer.setState(CREATED);

        User user = customer.getUser();
        if (user != null) {
            Long userId = userClient.createCliente(customer.getUser()).getId();
            customer.setUserId(userId);
        }
        log.info("{} creado!", dni);
        return customerRepo.save(customer);
    }

    public Customer createUserForCustomer(Long idCustomer, User user) {
        Customer customerDB = findById(idCustomer);
        if (customerDB.getUserId() != null) {
            throw new ConflictException("Ya tiene user!");
        }
        Long idUser = userClient.createCliente(user).getId();
        customerDB.setUserId(idUser);

        return customerRepo.save(customerDB);
    }

    private void validDni(String dni) {
        if (customerRepo.existsByDni(dni)) {
            String message = "Customer con numberID: " + dni + " ya existe!";
            log.error(message);
            throw new FieldAlreadyExistException(message);
        }
    }

    public Customer update(Customer customer, Long id) {
        Customer customerDB = findById(id);
        customerDB.setName(customer.getName());
        customerDB.setEmail(customer.getEmail());
        customerDB.setPhotoUrl(customer.getPhotoUrl());
        log.info("{} actualizado!", id);
        return customerRepo.save(customerDB);
    }

    public Customer delete(Long id) {
        Customer customerDB = findById(id);
        customerDB.setState(DELETED);
        log.info("{} eliminado!", id);
        return customerRepo.save(customerDB);
    }

    private Customer findById(Long id) {
        return customerRepo.findById(id)
                .orElseThrow(() -> {
                    String message = "Customer (" + id + ") no existe.";
                    log.error(message);
                    return new NotFoundException(message);
                });
    }

    public Customer getById(Long id) {
        Customer customerDB = findById(id);
        Long idUser = customerDB.getUserId();

        if (idUser != null) {
            User user = userClient.getById(idUser);
            customerDB.setUser(user);
        }
        return customerDB;
    }
}
