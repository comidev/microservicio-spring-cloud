package comidev.shoppingservice.invoice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import comidev.shoppingservice.exception.conflict.FieldAlreadyExistException;
import comidev.shoppingservice.exception.notFound.NotFoundException;
// import comidev.shoppingservice.invoiceItem.InvoiceItemRepo;

@Service
public class InvoiceService {
    @Autowired
    private InvoiceRepo invoiceRepo;
    // @Autowired
    // private InvoiceItemRepo invoiceItemRepo;
    private final String CREATED = "CREATED";
    private final String DELETED = "DELETED";

    public List<Invoice> getAll() {
        return invoiceRepo.findAll();
    }

    public Invoice create(Invoice invoice) {
        validNumberInvoice(invoice.getNumberInvoice());
        invoice.setState(CREATED);
        return invoiceRepo.save(invoice);
    }

    private void validNumberInvoice(String numberInvoice) {
        if (invoiceRepo.existsByNumberInvoice(numberInvoice)) {
            String message = "Number invoice ya existe: " + numberInvoice;
            throw new FieldAlreadyExistException(message);
        }
    }

    public Invoice update(Invoice invoice, Long id) {
        Invoice invoiceDB = getById(id);
        invoiceDB.setCustomerId(invoice.getCustomerId());
        invoiceDB.setDescription(invoice.getDescription());
        invoiceDB.setNumberInvoice(invoice.getNumberInvoice());
        invoiceDB.getItems().clear();
        invoiceDB.setItems(invoice.getItems());
        return invoiceRepo.save(invoiceDB);
    }

    public Invoice getById(Long id) {
        return invoiceRepo.findById(id).orElseThrow(() -> {
            String message = "El id no se encuentra: " + id;
            return new NotFoundException(message);
        });
    }

    public Invoice delete(Long id) {
        Invoice invoiceDB = getById(id);
        invoiceDB.setState(DELETED);
        return invoiceRepo.save(invoiceDB);
    }
}
