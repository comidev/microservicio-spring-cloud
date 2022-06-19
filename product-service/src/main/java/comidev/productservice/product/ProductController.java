package comidev.productservice.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import comidev.productservice.exception.badRequest.FieldInvalidException;
import comidev.productservice.utils.MessageError;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> getAllOrByCategoryIdOrName(
            @RequestParam(name = "categoryId", required = false) String id,
            @RequestParam(name = "categoryName", required = false) String name) {
        List<Product> products = id != null || name != null
                ? productService.getByCategoryIdOrName(id, name)
                : productService.getAll();
        return products.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Product getById(@PathVariable String id) {
        return productService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public Product create(@RequestBody Product product, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String message = MessageError.from(bindingResult.getFieldErrors());
            throw new FieldInvalidException(message);
        }
        return productService.create(product);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Product update(@RequestBody Product product, @PathVariable String id) {
        return productService.update(product, id);
    }

    @PatchMapping("/{id}/stock")
    @ResponseStatus(HttpStatus.OK)
    public Product updateStock(@PathVariable String id,
            @RequestParam(name = "quantity", required = true) Integer quantity) {
        return productService.updateStock(id, quantity);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Product delete(@PathVariable String id) {
        return productService.delete(id);
    }
}
