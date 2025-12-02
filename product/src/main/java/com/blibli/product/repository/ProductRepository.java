package com.blibli.product.repository;

import com.blibli.product.entity.Product;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, ObjectId> {

    @Query("{ 'productName': { $regex: ?0, $options: 'i' } }")
    Page<Product> searchByNameRegex(String regex, Pageable pageable);
}
