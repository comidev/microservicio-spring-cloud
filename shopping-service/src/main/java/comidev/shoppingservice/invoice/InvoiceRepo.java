package comidev.shoppingservice.invoice;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepo extends JpaRepository<Invoice, Long> {
    List<Invoice> findByCustomerId(Long customerId);

    boolean existsByNumberInvoice(String numberInvoice);
}
