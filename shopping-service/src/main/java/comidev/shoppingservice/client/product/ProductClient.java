package comidev.shoppingservice.client.product;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;

@FeignClient(name = "product-service", fallback = ProductFallback.class)
@RequestMapping("/products")
public interface ProductClient {

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Product getById(@PathVariable String id);

    @PatchMapping("/{id}/stock")
    @ResponseStatus(HttpStatus.OK)
    public Product updateStock(@PathVariable String id,
            @RequestParam(name = "quantity", required = true) Integer quantity);
}
