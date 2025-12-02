package com.blibli.cart.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Document(collection = CartEntity.COLLECTION_NAME)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartEntity {

    public static final String COLLECTION_NAME = "cart";

    @Id
    private UUID cartId;   // Same as UserID
    private List<CartItem> items = new ArrayList<>();
}
