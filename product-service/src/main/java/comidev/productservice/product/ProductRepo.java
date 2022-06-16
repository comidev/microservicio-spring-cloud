package comidev.productservice.product;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import comidev.productservice.category.Category;

@Repository
public interface ProductRepo extends MongoRepository<Product, String> {
    List<Product> findByCategory(Category category);
}
