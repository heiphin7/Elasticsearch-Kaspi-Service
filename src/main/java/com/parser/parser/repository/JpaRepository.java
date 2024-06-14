package com.parser.parser.repository;

import com.parser.parser.models.ProductJPA;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaRepository extends org.springframework.data.jpa.repository.JpaRepository<ProductJPA, String> {
    List<ProductJPA> findAll();
}
