package com.blibli.product.serviceImpl;

import com.blibli.product.entity.Product;
import com.blibli.product.entityDTO.ProductRequestDTO;
import com.blibli.product.entityDTO.ProductResponseDTO;
import com.blibli.product.repository.ProductRepository;
import com.blibli.product.service.ProductService;
import com.blibli.product.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.blibli.product.utils.CommonUtils.getProductRequestFromDTO;
import static com.blibli.product.utils.CommonUtils.getProductResponseDTO;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {
        Product createdProduct = productRepository.save(getProductRequestFromDTO(productRequestDTO));
        return getProductResponseDTO(createdProduct);
    }

    @Override
    public Page<ProductResponseDTO> searchProducts(String keyword, Pageable pageable) {

        log.info("Searching products for keyword: {}", keyword);
        Page<Product> fetchedProduct = productRepository.searchByNameRegex(keyword,pageable);
       /* List<ProductResponseDTO>  returProductList = new ArrayList<>();
        for(Product eachProduct : fetchedProduct){
            returProductList.add(getProductResponseDTO(eachProduct));
        }*/
        return fetchedProduct.map(CommonUtils::getProductResponseDTO);
    }

    @Override
    public ProductResponseDTO viewProductDetail(ObjectId productId) {
        Product viewedProduct=productRepository.findById(productId).orElseThrow();
        return (getProductResponseDTO(viewedProduct));
    }
}
