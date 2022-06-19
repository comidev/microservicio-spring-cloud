package comidev.shoppingservice.client.product;

import org.springframework.stereotype.Component;

@Component
public class ProductFallback implements ProductClient {

    @Override
    public Product getById(String id) {
        System.out.println("\n\n" + this.getClass().getSimpleName() + "::getById" + "\n\n");
        return null;
    }

    @Override
    public Product updateStock(String id, Integer quantity) {
        System.out.println("\n\n" + this.getClass().getSimpleName() + "::updateStock" + "\n\n");
        return null;
    }
}
