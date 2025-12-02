package com.blibli.product.entityDTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDTO {

    private String productName;
    private String productDesc;
    private Double productUnitPrice;
    private String category;
    private List<String> images;
}
