package com.blibli.product.entityDTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDTO implements Serializable {

    private String productName;
    private String productDesc;
    private Double productUnitPrice;
    private String category;
    private List<String> images;
}
