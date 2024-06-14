package com.parser.parser.models;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "products")
@Data
public class Product {

    @Id
    private String id;

    @Field(type = FieldType.Text, name = "title")
    private String title;

    @Field(type = FieldType.Text, name = "price")
    private String price;

    @Field(type = FieldType.Text, name = "category")
    private String category;

    @Field(type = FieldType.Integer, name = "reviews_number")
    private Long reviewsNumber;

    @Field(type = FieldType.Text, name = "rating")
    private String rating;

    @Field(type = FieldType.Keyword, name = "link")
    private String link;

    @Field(type = FieldType.Text, name = "preview")
    private String preview;

    public Product() {

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
                "\n";
    }
}
