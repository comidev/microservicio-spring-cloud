package comidev.productservice.product;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import comidev.productservice.category.Category;
import comidev.productservice.category.CategoryRepo;
import comidev.productservice.exception.notFound.NotFoundException;
import comidev.productservice.utils.State;

@Service
public class ProductService {
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private CategoryRepo categoryRepo;

    public List<Product> getAll() {
        return productRepo.findAll();
    }

    public List<Product> getByCategoryIdOrName(String id, String name) {
        Category category = categoryRepo.findByIdOrName(id, name)
                .orElseThrow(() -> {
                    String message = "Categoria no encontrada: " + id + " | " + name;
                    return new NotFoundException(message);
                });
        return productRepo.findByCategory(category);
    }

    public Product getById(String id) {
        return productRepo.findById(id).orElseThrow(() -> {
            String message = "Id no encontrado: " + id;
            return new NotFoundException(message);
        });
    }

    public Product create(Product product) {
        Category category = getCategoryByName(product.getCategoryName());
        product.setCategory(category);
        product.setStatus(State.CREATED.toString());
        product.setCreateAt(new Date());
        return productRepo.save(product);
    }

    public Product update(Product product, String id) {
        Product productDB = getById(id);
        Category category = getCategoryByName(product.getCategoryName());
        productDB.setCategory(category);
        productDB.setName(product.getName());
        productDB.setDescription(product.getDescription());
        productDB.setPrice(product.getPrice());
        return productRepo.save(productDB);
    }

    private Category getCategoryByName(String name) {
        return categoryRepo.findByName(name).orElseThrow(() -> {
            String message = "Categoria no encontrada: " + name;
            return new NotFoundException(message);
        });
    }

    public Product updateStock(String id, Integer quantity) {
        Product productDB = getById(id);
        Integer stock = productDB.getStock() + quantity;
        productDB.setStock(stock);
        return productRepo.save(productDB);
    }

    public Product delete(String id) {
        Product productDB = getById(id);
        productDB.setStatus(State.DELETED.toString());
        return productRepo.save(productDB);
    }
}
