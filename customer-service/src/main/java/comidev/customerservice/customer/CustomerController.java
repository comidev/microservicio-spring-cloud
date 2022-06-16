package comidev.customerservice.customer;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import comidev.customerservice.exception.badRequest.FieldInvalidException;
import comidev.customerservice.utils.MessageError;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<Customer>> getAllOrByRegion(
            @RequestParam(name = "regionId", required = false) Long regionId) {
        if (regionId != null) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(customerService.getAllByRegion(regionId));
        }
        List<Customer> customers = customerService.getAll();
        if (customers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Customer getById(@PathVariable Long id) {
        return customerService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Customer create(@Valid @RequestBody Customer customer,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String message = MessageError.from(bindingResult.getFieldErrors());
            throw new FieldInvalidException(message);
        }
        return customerService.create(customer);

    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Customer update(@RequestBody Customer customer,
            @PathVariable Long id) {
        return customerService.update(customer, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Customer delete(@PathVariable Long id) {
        return customerService.delete(id);
    }
}
