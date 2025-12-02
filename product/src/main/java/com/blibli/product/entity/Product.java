package com.blibli.product.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Document(collection = Product.COLLECTION_NAME)
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Product {

    public static final String COLLECTION_NAME = "Product";
    @Id
    private ObjectId productId;

    private String productName;
    private String productDesc;
    private Double productUnitPrice;
    private String category;
    private List<String> images;


}


