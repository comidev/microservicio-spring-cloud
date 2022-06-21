package comidev.productservice.category;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Document(collection = "category")
public class Category {
    @Id
    private String id;
    private String name;

    public Category(String name) {
        this.name = name;
    }

    public Category() {
    }
}
