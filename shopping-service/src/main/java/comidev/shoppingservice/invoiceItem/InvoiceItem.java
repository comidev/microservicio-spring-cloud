package comidev.shoppingservice.invoiceItem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Positive;

import comidev.shoppingservice.client.product.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "invoice_item")
public class InvoiceItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Positive(message = "debe ser mayor a 0 (cero)")
    private Integer quantity;
    @Column(name = "product_id")
    private String productId;
    @Transient
    private Product product;

    public InvoiceItem() {
        this.quantity = 0;
    }
}
