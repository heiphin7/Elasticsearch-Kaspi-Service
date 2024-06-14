package com.parser.parser.service;

import com.parser.parser.models.Product;
//import com.parser.parser.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    //private final ProductRepository repository;

    public void save(Product product) {
        System.out.println("Начало сохранения");
        System.out.println("Saved new product with id=" + product.getId());
        System.out.println(product);
        //repository.save(product);
    }

}
