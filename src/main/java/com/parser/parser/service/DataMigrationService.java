package com.parser.parser.service;

import com.parser.parser.models.Product;
import com.parser.parser.models.ProductJPA;
import com.parser.parser.repository.JpaRepository;
import com.parser.parser.repository.SearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DataMigrationService {

    @Autowired
    private JpaRepository jpaProductRepository;

    @Autowired
    private SearchRepository elasticsearchProductRepository;

    @Transactional(readOnly = true)
    public void migrateData() {
        List<ProductJPA> products = jpaProductRepository.findAll();

        for (ProductJPA jpaProduct: products) {
            elasticsearchProductRepository.save(mapper(jpaProduct));
        }
    }

    private Product mapper(ProductJPA productJPA) {
        Product product = new Product();
        product.setId(product.getId());
        product.setLink(product.getLink());
        product.setReviewsNumber(productJPA.getReviewsNumber());
        product.setPrice(product.getPrice());
        product.setTitle(productJPA.getTitle());
        product.setCategory(product.getCategory());

        return product;
    }
}

