package com.parser.parser.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Table(name = "products")
@Entity
@Data
public class Product {

    @Id
    private String id;

    @Column(name = "title")
    private String title;

    @Column(name = "price")
    private String price;

    @Column(name = "category")
    private String category;

    @Column(name = "reviews_number")
    private Long reviewsNumber;

    @Column(name = "rating")
    private int rating;

    @Column(name = "link")
    private String link;

    @Column(name = "preview")
    private String preview;

    public Product() {

    }

    @Override
    public String toString() {
        return title + "\n"
                + price + "\n";
    }
}
