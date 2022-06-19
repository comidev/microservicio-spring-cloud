package comidev.shoppingservice.client.product;

import lombok.Getter;

@Getter
public class Product {
    private String name;
    private String description;
    private Float price;
    private Category category;
}
