package com.blibli.cart.repository;

import com.blibli.cart.entity.CartEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface CartRepository extends MongoRepository<CartEntity,UUID> {


}