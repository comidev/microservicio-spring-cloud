package comidev.shoppingservice.client.customer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;

@FeignClient(name = "customer-service", fallback = CustomerFallback.class)
@RequestMapping("/customers")
public interface CustomerClient {

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Customer getById(@PathVariable Long id);


}
