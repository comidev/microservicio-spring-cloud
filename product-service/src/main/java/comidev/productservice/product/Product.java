package comidev.productservice.product;

import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import org.springframework.data.annotation.Id;

import comidev.productservice.category.Category;
import comidev.productservice.util.State;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Document(collection = "product")
public class Product {
    @Id
    private String id;
    @NotEmpty(message = "no debe ser vacio")
    private String name;
    private String description;
    @Positive(message = "debe ser mayor a 0")
    private Integer stock;
    private Float price;
    private State status;
    @Field(name = "create_at")
    private Date createAt;
    private Category category;

    @NotNull(message = "no debe ser vacio")
    @Transient
    @JsonProperty(access = Access.WRITE_ONLY)
    private String categoryName;
}
