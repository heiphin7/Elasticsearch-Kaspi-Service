package com.parser.parser.repository;

import com.parser.parser.models.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchRepository extends ElasticsearchRepository<Product, String> {
    List<Product> findByTitle(String title);

    @Override
    void delete(Product entity);

    @Override
    <S extends Product> S save(S entity);

    void deleteByTitle(String title);
}
