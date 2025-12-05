package com.blibli.product.service;

import com.blibli.product.entityDTO.ProductRequestDTO;
import com.blibli.product.entityDTO.ProductResponseDTO;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {


    ProductResponseDTO createProduct(ProductRequestDTO productResponseDTO);

    Page<ProductResponseDTO> searchProducts(String keyword, Pageable pageable) throws InterruptedException;
    ProductResponseDTO viewProductDetail(ObjectId productId);
}
