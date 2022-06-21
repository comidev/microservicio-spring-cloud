package comidev.shoppingservice.client.customer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;

@FeignClient(name = "customer-service", fallback = CustomerFallback.class)
public interface CustomerClient {
    @GetMapping("/customers/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Customer getById(@PathVariable Long id);
}
