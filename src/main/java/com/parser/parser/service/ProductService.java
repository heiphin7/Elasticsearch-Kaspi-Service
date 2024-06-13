package com.parser.parser.service;

import com.parser.parser.models.Product;
import com.parser.parser.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    public void save(Product product) {
        // find by product code
        Product productInDb = repository.findById(product.getId()).orElse(null);

        // Если мы не находим по коду, это означает что такого товара нету, поэтому можно сохранять в бд
        if (productInDb == null) {
            System.out.println("Saved new product with id=" + product.getId());
            repository.save(product);
        }

    }

}
