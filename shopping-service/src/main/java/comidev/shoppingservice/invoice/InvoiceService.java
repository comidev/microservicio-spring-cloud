package comidev.shoppingservice.invoice;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import comidev.shoppingservice.client.customer.Customer;
import comidev.shoppingservice.client.customer.CustomerClient;
import comidev.shoppingservice.client.product.Product;
import comidev.shoppingservice.client.product.ProductClient;
import comidev.shoppingservice.exception.conflict.FieldAlreadyExistException;
import comidev.shoppingservice.exception.notFound.NotFoundException;
// import comidev.shoppingservice.invoiceItem.InvoiceItemRepo;
import comidev.shoppingservice.invoiceItem.InvoiceItem;

@Service
public class InvoiceService {
    private final String CREATED = "CREATED";
    private final String DELETED = "DELETED";

    @Autowired
    private InvoiceRepo invoiceRepo;
    @Autowired
    private CustomerClient customerRest;
    @Autowired
    private ProductClient productRest;

    public List<Invoice> getAll() {
        return invoiceRepo.findAll();
    }

    public Invoice create(Invoice invoice) {
        validNumberInvoice(invoice.getNumberInvoice());
        invoice.setState(CREATED);
        Invoice invoiceDB = invoiceRepo.save(invoice);
        invoiceDB.getItems().forEach(item -> {
            productRest.updateStock(item.getProductId(), item.getQuantity() * -1);
        });
        return invoiceDB;
    }

    private void validNumberInvoice(String numberInvoice) {
        if (invoiceRepo.existsByNumberInvoice(numberInvoice)) {
            String message = "Number invoice ya existe: " + numberInvoice;
            throw new FieldAlreadyExistException(message);
        }
    }

    public Invoice update(Invoice invoice, Long id) {
        Invoice invoiceDB = findById(id);
        invoiceDB.setCustomerId(invoice.getCustomerId());
        invoiceDB.setDescription(invoice.getDescription());
        invoiceDB.setNumberInvoice(invoice.getNumberInvoice());
        invoiceDB.getItems().clear();
        invoiceDB.setItems(invoice.getItems());
        return invoiceRepo.save(invoiceDB);
    }

    public Invoice getById(Long id) {
        Invoice invoiceDB = findById(id);

        Customer customer = customerRest.getById(invoiceDB.getCustomerId());
        invoiceDB.setCustomer(customer);

        List<InvoiceItem> items = invoiceDB.getItems().stream()
                .map(item -> {
                    Product product = productRest.getById(item.getProductId());
                    item.setProduct(product);
                    return item;
                })
                .collect(Collectors.toList());

        float total = 0f;
        for (InvoiceItem invoiceItem : items) {
            total += invoiceItem.getQuantity() * invoiceItem.getProduct().getPrice();
        }

        invoiceDB.setItems(items);
        invoiceDB.setTotal(total);
        return invoiceDB;
    }

    private Invoice findById(Long id) {
        return invoiceRepo.findById(id).orElseThrow(() -> {
            String message = "El id no se encuentra: " + id;
            return new NotFoundException(message);
        });
    }

    public Invoice delete(Long id) {
        Invoice invoiceDB = findById(id);
        invoiceDB.setState(DELETED);
        return invoiceRepo.save(invoiceDB);
    }
}
