package com.blibli.product.controller;


import com.blibli.product.entityDTO.GenericResponse;
import com.blibli.product.entityDTO.ProductRequestDTO;
import com.blibli.product.entityDTO.ProductResponseDTO;
import com.blibli.product.serviceImpl.ProductServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/product")
public class ProductController {

    private final ProductServiceImpl productService;

    public ProductController(ProductServiceImpl productService) {
        this.productService = productService;
    }

    @PostMapping("/createProduct")
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody ProductRequestDTO productDTO) {
        ProductResponseDTO savedProduct = productService.createProduct(productDTO);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @GetMapping("/search")
        public GenericResponse<Page<ProductResponseDTO>> searchProduct(
                @RequestParam String keyword,
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10")int size ){
        log.info("Search request for keyword: {}", keyword);

        Pageable pageable = PageRequest.of(page,size);
        Page<ProductResponseDTO> searchedProducts = productService.searchProducts(keyword,pageable);
        return GenericResponse.<Page<ProductResponseDTO>>builder()
                .status("SUCCESS")
                .message("Products fetched")
                .data(searchedProducts)
                .build();
        }

    @GetMapping("/viewProduct")
    public GenericResponse<ProductResponseDTO> viewProduct(@RequestParam("ProductID") String productID){
        ObjectId id = new ObjectId(productID);
        ProductResponseDTO viewedProduct = productService.viewProductDetail(id);

        return GenericResponse.<ProductResponseDTO>builder()
                .status("SUCCESS")
                .message("Products fetched")
                .data(viewedProduct)
                .build();
    }
}

