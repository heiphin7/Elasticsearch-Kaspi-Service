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
            System.out.println("Перенос продукта с id=" + jpaProduct.getId());
            elasticsearchProductRepository.save(mapper(jpaProduct));
        }
    }

    private Product mapper(ProductJPA productJPA) {
        Product product = new Product();
        product.setId(productJPA.getId());
        product.setTitle(productJPA.getTitle());
        product.setPrice(productJPA.getPrice());
        product.setLink(productJPA.getLink());
        product.setRating(productJPA.getRating());
        product.setReviewsNumber(productJPA.getReviewsNumber());
        product.setCategory(productJPA.getCategory());

        return product;
    }
}

