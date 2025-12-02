package com.blibli.product.entityDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class ProductRequestDTO {

        private String productName;
        private String productDesc;
        private Double productUnitPrice;
        private String category;
    }

