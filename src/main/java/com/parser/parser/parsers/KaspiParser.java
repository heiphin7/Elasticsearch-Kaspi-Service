package com.parser.parser.parsers;

import com.parser.parser.models.Product;
import com.parser.parser.service.ProductService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KaspiParser {

    @Autowired
    private ProductService productService;

    static final String BASIC_URL = "https://kaspi.kz/shop/search/?text=";

    public List<Product> parseByQuery(String query) {
        List<Product> products = new ArrayList<>();
        WebDriver webDriver = new ChromeDriver();
        String url = BASIC_URL + query;


        for (int i = 0; i < 10; i++) {
            try {
                webDriver.get(url);

                Cookie cookie = new Cookie.Builder("kaspi.storefront.cookie.city", "710000000")
                        .domain("kaspi.kz")
                        .path("/")
                        .expiresOn(new Date(System.currentTimeMillis() + 31536000000L))
                        .isSecure(false)
                        .build();
                webDriver.manage().addCookie(cookie);
                webDriver.navigate().refresh();

                WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));

                JavascriptExecutor js = (JavascriptExecutor) webDriver;
                for (int j = 0; j < 3; j++) {
                    js.executeScript("window.scrollBy(0,500)");
                    Thread.sleep(150);
                }

                String html = webDriver.getPageSource();
                Document doc = Jsoup.parse(html);
                Elements videoElements = doc.select("div.item-card");

                if (videoElements.isEmpty()) {
                    System.out.println("No elements found with the given selector.");
                }

                String category = extractCategory(html);

                for (Element videoElement : videoElements) {
                    String title = videoElement.select("div.item-card__name").text();
                    String price = videoElement.select("span.item-card__prices-price").text();
                    String linkToProduct = videoElement.select("a.item-card__image-wrapper").attr("href");
                    String productID = videoElement.attr("data-product-id");
                    String reviewsText = videoElement.select("div.item-card__rating a").text(); // "(1241 отзыв)"
                    String rating = parseRatingFromClassName(videoElement.select("span.rating").attr("class"));
                    long reviewsNumber = parseReviewsNumber(reviewsText);

                    Product product = new Product();
                    product.setTitle(title);
                    product.setPrice(price);
                    product.setLink(linkToProduct);
                    product.setRating(rating);
                    product.setReviewsNumber(reviewsNumber);
                    product.setId(productID);
                    product.setCategory(category);

                    products.add(product);
                }

                // Нажатие на кнопку "Следующая"
                WebElement nextButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"scroll-to\"]/div[3]/div[2]/li[7]")));
                if (!nextButton.getAttribute("class").contains("_disabled")) {
                    nextButton.click();
                    url = webDriver.getCurrentUrl();
                    Thread.sleep(1000);  // Ждем перезагрузку страницы
                } else {
                    return products;
                }
            } catch(Exception e){
                e.printStackTrace();
            }
        }
        return products;
    }

    public String parseByProductId(String productId) {
        String result = "";

        WebDriver webDriver = new ChromeDriver();

        try {
            webDriver.get(BASIC_URL + productId);

            // set city code to cookies
            Cookie cookie = new Cookie.Builder("kaspi.storefront.cookie.city", "710000000")
                    .domain("kaspi.kz")
                    .path("/")
                    .expiresOn(new Date(System.currentTimeMillis() + 31536000000L))
                    .isSecure(false)
                    .build();
            webDriver.manage().addCookie(cookie);
            webDriver.navigate().refresh();

            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));

            JavascriptExecutor js = (JavascriptExecutor) webDriver;
            for (int i = 0; i < 5; i++) {
                js.executeScript("window.scrollBy(0,500)");
                Thread.sleep(200);
            }

            String html = webDriver.getPageSource();
            Document doc = Jsoup.parse(html);
            Elements videoElements = doc.select("div.item-card");

            if (videoElements.isEmpty()) {
                System.out.println("No elements found with the given selector.");
            }

            for (Element videoElement : videoElements) {
                String title = videoElement.select("div.item-card__name").text();
                String price = videoElement.select("span.item-card__prices-price").text();
                String linkToProduct = videoElement.select("a.item-card__image-wrapper").attr("href");
                String productID = videoElement.attr("data-product-id");
                String reviewsText = videoElement.select("div.item-card__rating a").text(); // "(1241 отзыв)"
                String rating = parseRatingFromClassName(videoElement.select("span.rating").attr("class"));

                long reviewsNumber = parseReviewsNumber(reviewsText);

                Product product = new Product();
                product.setTitle(title);
                product.setPrice(price);
                product.setLink(linkToProduct);
                product.setReviewsNumber(reviewsNumber);
                product.setId(productID);

                // тут, если рейтинг 4.9 то мы ставим 5, так как в kaspi она округляется
                if (rating.equals("4.9")) {
                    product.setRating("5.0");
                } else {
                    product.setRating(rating);
                }

                return product.toString();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            webDriver.quit();
        }
        return result;
    }

    private String parseRatingFromClassName(String className) {
        // класс "_49" -> рейтинг "4.9"
        if (className.contains("_")) {
            return className.substring(className.lastIndexOf("_") + 1, className.length() - 1) + "." + className.charAt(className.length() - 1);
        }
        return "0";
    }

    private long parseReviewsNumber(String reviewsText) {
        // Из текста Пример: "(1241 отзыв)" извлекаем только число
        return Long.parseLong(reviewsText.replaceAll("[^0-9]", ""));
    }

    public String extractCategory(String html) {
        Document document = Jsoup.parse(html);
        Elements categoryElements = document.select("ul.tree__items > li.tree__item > span.tree__link");
        StringBuilder categories = new StringBuilder();

        for (Element category : categoryElements) {
            if (!categories.isEmpty()) {
                categories.append(" > ");
            }
            categories.append(category.text().trim());
        }

        return categories.toString();
    }


    public List<Product> parseByURL(String URL) {
        List<Product> products = new ArrayList<>();
        WebDriver webDriver = new ChromeDriver();

        try {
            webDriver.get(URL);

            Cookie cookie = new Cookie.Builder("kaspi.storefront.cookie.city", "710000000")
                    .domain("kaspi.kz")
                    .path("/")
                    .expiresOn(new Date(System.currentTimeMillis() + 31536000000L))
                    .isSecure(false)
                    .build();
            webDriver.manage().addCookie(cookie);
            webDriver.navigate().refresh();

            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));

            JavascriptExecutor js = (JavascriptExecutor) webDriver;
            for (int j = 0; j < 10; j++) {
                js.executeScript("window.scrollBy(0,500)");
                Thread.sleep(100);
            }

            String html = webDriver.getPageSource();
            Document doc = Jsoup.parse(html);
            Elements videoElements = doc.select("div.item-card");

            if (videoElements.isEmpty()) {
                System.out.println("Не найдено!");
                return null;
            }

            String category = extractCategory(html);

            for (Element videoElement : videoElements) {
                String title = videoElement.select("div.item-card__name").text();
                String price = videoElement.select("span.item-card__prices-price").text();
                String linkToProduct = videoElement.select("a.item-card__image-wrapper").attr("href");
                String productID = videoElement.attr("data-product-id");
                String reviewsText = videoElement.select("div.item-card__rating a").text(); // "(1241 отзыв)"
                String rating = parseRatingFromClassName(videoElement.select("span.rating").attr("class"));
                long reviewsNumber = parseReviewsNumber(reviewsText);

                Product product = new Product();
                product.setTitle(title);
                product.setPrice(price);
                product.setLink(linkToProduct);
                product.setRating(rating);
                product.setReviewsNumber(reviewsNumber);
                product.setId(productID);
                product.setCategory(category);

                products.add(product);
            }
        } catch(Exception e){
            System.out.println(e.getMessage());
        } finally{
            webDriver.quit();
        }

        for (Product product: products) {
            productService.save(product);
        }

        return products;
    }
}
