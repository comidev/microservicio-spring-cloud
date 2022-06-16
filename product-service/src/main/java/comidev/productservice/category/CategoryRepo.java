package comidev.productservice.category;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepo extends MongoRepository<Category, String> {
    Optional<Category> findByName(String name);

    Optional<Category> findByIdOrName(String id, String name);
}
