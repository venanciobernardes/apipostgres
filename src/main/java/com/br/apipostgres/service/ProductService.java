package com.br.apipostgres.service;

import com.br.apipostgres.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Product save(Product product);
    List<Product> findAll();
    Optional<Product> findById(Integer id);
    void delete(Product product);
}
