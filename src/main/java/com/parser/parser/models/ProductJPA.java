package com.parser.parser.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

@Table(name = "products")
@Entity
@Data
@AllArgsConstructor
public class ProductJPA {

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
    private String rating;

    @Column(name = "link")
    private String link;

    @Column(name = "preview")
    private String preview;

    public ProductJPA() {

    }

    @Override
    public String toString() {
        return  "Код продукта: " + id + "\n" +
                "Название: " + title + "\n"
                + "Цена: " + price + "\n" +
                "Ссылка: \n" + link + "\n"
                + "Рейтинг (по отзывам): " + rating + "\n" +
                "Количество отзывов: " + reviewsNumber + "\n" +
                "Категория продукта: " + category  + "\n" +
                "\n\n";
    }
}