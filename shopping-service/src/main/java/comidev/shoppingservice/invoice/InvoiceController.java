package comidev.shoppingservice.invoice;

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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import comidev.shoppingservice.exception.badRequest.FieldInvalidException;
import comidev.shoppingservice.util.MessageError;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {
    @Autowired
    private InvoiceService invoiceService;

    @GetMapping
    public ResponseEntity<List<Invoice>> getAll() {
        List<Invoice> invoices = invoiceService.getAll();
        return invoices.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(invoices);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Invoice create(@Valid @RequestBody Invoice invoice, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String message = MessageError.from(bindingResult.getFieldErrors());
            throw new FieldInvalidException(message);
        }
        return invoiceService.create(invoice);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Invoice update(@RequestBody Invoice invoice, @PathVariable Long id) {
        return invoiceService.update(invoice, id);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Invoice getById(@PathVariable Long id) {
        return invoiceService.getById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Invoice delete(@PathVariable Long id) {
        return invoiceService.delete(id);
    }
}
