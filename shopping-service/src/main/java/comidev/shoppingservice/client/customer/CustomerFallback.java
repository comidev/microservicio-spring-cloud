package comidev.shoppingservice.client.customer;

import org.springframework.stereotype.Component;

@Component
public class CustomerFallback implements CustomerClient {

    @Override
    public Customer getById(Long id) {
        System.out.println("\n\n CustomerFallback::getById \n\n");
        return null;
    }

}
