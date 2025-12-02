package com.blibli.product.utils;

import com.blibli.product.entity.Product;
import com.blibli.product.entityDTO.ProductRequestDTO;
import com.blibli.product.entityDTO.ProductResponseDTO;
import org.springframework.beans.BeanUtils;

public class CommonUtils {
    // Entity will be converted to DTO for Product
    public static ProductRequestDTO getProductRequestDTO(Product product) {
        ProductRequestDTO targetDTO = new ProductRequestDTO();
        BeanUtils.copyProperties(product, targetDTO);
        return targetDTO;
    }
    // DTO will be converted to Entity for Product
    public static Product getProductRequestFromDTO(ProductRequestDTO productDTO) {
        Product target = new Product();
        BeanUtils.copyProperties(productDTO, target);
        return target;
    }

    // Entity will be converted to DTO for Product
    public static ProductResponseDTO getProductResponseDTO(Product product) {
        ProductResponseDTO targetDTO = new ProductResponseDTO();
        BeanUtils.copyProperties(product, targetDTO);
        return targetDTO;
    }
    // DTO will be converted to Entity for Product
    public static Product getProductResponseFromDTO(ProductResponseDTO productDTO) {
        Product target = new Product();
        BeanUtils.copyProperties(productDTO, target);
        return target;
    }
}
