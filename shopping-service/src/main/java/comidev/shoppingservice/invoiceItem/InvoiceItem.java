package comidev.shoppingservice.invoiceItem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Positive;

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
    private Float price;

    @Column(name = "product_id")
    private Long productId;

    @Transient
    private Float subTotal;

    public InvoiceItem() {
        this.quantity = 0;
        this.price = (float) 0;
    }

    public Float getSubTotal() {
        if (price > 0 && quantity > 0) {
            return price * quantity;
        }
        return (float) 0;
    }
}
